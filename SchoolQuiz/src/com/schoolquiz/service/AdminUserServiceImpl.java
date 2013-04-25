package com.schoolquiz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.Question;
import com.schoolquiz.entity.QuestionGroup;
import com.schoolquiz.entity.admin.AdminUser;
import com.schoolquiz.entity.admin.AdminUserSession;
import com.schoolquiz.entity.admin.CheckSessionSummary;
import com.schoolquiz.entity.admin.DeleteGroupResponse;
import com.schoolquiz.entity.admin.FinishSessionSummary;
import com.schoolquiz.entity.admin.GroupForAdmin;
import com.schoolquiz.entity.admin.OperationGroupResponse;
import com.schoolquiz.entity.admin.decorated.AdminUserSessionSummary;
import com.schoolquiz.entity.admin.decorated.CustomQuestionGroupResponse;
import com.schoolquiz.entity.admin.request.AddGroupRequest;
import com.schoolquiz.entity.admin.request.DeleteGroupRequest;
import com.schoolquiz.entity.admin.request.EditGroupRequest;
import com.schoolquiz.entity.admin.request.GetQuestionsForGroup;
import com.schoolquiz.entity.admin.request.UserSession;
import com.schoolquiz.entity.admin.response.GetQuestionsForGroupResponse;
import com.schoolquiz.entity.admin.response.QuestionForAdmin;
import com.schoolquiz.entity.controllerparams.QuestionForGroup;
import com.schoolquiz.persistence.AdminDAO;
import com.schoolquiz.persistence.QuizDAO;

public class AdminUserServiceImpl implements AdminUserService {
	
	private static long sleepTime =1000*60;
	private static long sessionLostActivity = 1000*60*20;
	
	private AdminSessionThread sessionRunnable;
	private Thread sessionThread;
	
	@Autowired
	private AdminDAO adminDao;
	
	@Autowired
	private QuizDAO quizDao;
	
	public AdminUserServiceImpl(){
		sessionRunnable = new AdminSessionThread();
		sessionThread = new Thread(sessionRunnable);
		sessionThread.start();
	}

	@Override
	public AdminUserSessionSummary checkUser(String userName, String password) {
		AdminUserSessionSummary adminUserSessionSummary = new AdminUserSessionSummary();
		AdminUser adminUser = adminDao.checkUserCredentials(userName, password);
		if(adminUser==null){
			adminUserSessionSummary.getErrorData().setErrorCode(ErrorData.WRONG_PARAMS);
			adminUserSessionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_WRONG_PARAMS);
			return adminUserSessionSummary;
		}
		
		adminDao.closeActiveSessions(adminUser);
		
		AdminUserSession adminUserSession = adminDao.createSession(adminUser);
		if(adminUserSession==null){
			adminUserSessionSummary.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			adminUserSessionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return adminUserSessionSummary;
		}
		
		adminUserSessionSummary.setAdminUserSession(adminUserSession);
		
		return adminUserSessionSummary;
	}

	@Override
	public CheckSessionSummary checkAdminSession(String userSession) {
		CheckSessionSummary sessionSummary = new CheckSessionSummary();
		if(userSession==null){
			sessionSummary.getErrorData().setErrorCode(ErrorData.WRONG_PARAMS);
			sessionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_WRONG_PARAMS);
			return sessionSummary;
		}
		AdminUserSession adminSession = adminDao.getAdminSession(userSession);
		if(adminSession==null){
			sessionSummary.getErrorData().setErrorCode(ErrorData.WRONG_SESSION);
			sessionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_WRONG_SESSION);
			return sessionSummary;
		}
		if(adminSession.getActive().equals(false)){
			sessionSummary.getErrorData().setErrorCode(ErrorData.INACTIVE_SESSION);
			sessionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_INACTIVE_SESSION);
			return sessionSummary;
		}
		sessionSummary.setUserSession(adminSession.getSession());
		 
		return sessionSummary;
	}
	

	@Override
	public void updateSessionActivity(String userSession) {
		AdminUserSession adminSession = adminDao.getAdminSession(userSession);
		if(adminSession==null) return;
		adminSession.setLastActivity(new Date().getTime());
		adminDao.updateSession(adminSession);
	}
	
	@Override
	public FinishSessionSummary finishSession(String session) {
		FinishSessionSummary finishSessionSummary = new FinishSessionSummary();
		
		AdminUserSession adminSession = adminDao.getAdminSession(session);
		if(adminSession==null){
			finishSessionSummary.getErrorData().setErrorCode(ErrorData.WRONG_PARAMS);
			finishSessionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_OK);
			return finishSessionSummary;
		}
		
		if(adminSession.getActive()==false){
			finishSessionSummary.getErrorData().setErrorCode(ErrorData.INACTIVE_SESSION);
			finishSessionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_INACTIVE_SESSION);
			return finishSessionSummary;
		}
		
		updateSessionActivity(session);
		adminSession.setActive(false);
		AdminUserSession finishResult = adminDao.updateSession(adminSession);
		if(finishResult==null){
			finishSessionSummary.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			finishSessionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return finishSessionSummary;
		}
		finishSessionSummary.setFinishSession(true);
		return finishSessionSummary;
	}
	

	@Override
	public CustomQuestionGroupResponse getGroupsForAdmin(String session, int numberFrom, int numberElements) {
		CustomQuestionGroupResponse custQuestionGroupResponse = new CustomQuestionGroupResponse();
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(session);
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			custQuestionGroupResponse.setRecords("0");
			custQuestionGroupResponse.setPage("0");
			custQuestionGroupResponse.setTotal("0");
			custQuestionGroupResponse.setGroupList(new ArrayList());
			custQuestionGroupResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return custQuestionGroupResponse;
		}
		
		updateSessionActivity(session);
		
		List<QuestionGroup> questionGroups = quizDao.getQuestionGroups(numberFrom, numberElements);
//		System.out.println("QuestionGroups - "+questionGroups);
		
		long numberOfGroups = quizDao.getGroupNumbers();
//		System.out.println("questionGroups number - "+numberOfGroups);
		custQuestionGroupResponse.setTotal(numberOfGroups+"");
		custQuestionGroupResponse.setRecords(questionGroups.size()+"");
		custQuestionGroupResponse.setPage(Integer.parseInt((numberOfGroups/numberElements+1)+"")+"");
		List<GroupForAdmin> groupList = null;
		if(questionGroups.size()>0){
			groupList = new LinkedList<GroupForAdmin>();
			for(QuestionGroup questionGroup:questionGroups){
				if(questionGroup.getDeleted()==true) continue;
				GroupForAdmin group = new GroupForAdmin();
				group.setId(questionGroup.getId());
				group.setGroupName(questionGroup.getGroupName());
				group.setEnabled(questionGroup.getEnabled());
				groupList.add(group);
			}
		}
		custQuestionGroupResponse.setGroupList(groupList);
		
		
		return custQuestionGroupResponse;
	}
	
	@Override
	public OperationGroupResponse addGroup(AddGroupRequest addGroupRequest) {
		OperationGroupResponse operationResponse = new OperationGroupResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(addGroupRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		QuestionGroup groupToAdd = new QuestionGroup();
		groupToAdd.setGroupName(addGroupRequest.getGroupName());
		groupToAdd.setEnabled(true);
		groupToAdd.setDeleted(false);
		QuestionGroup resultGroup = adminDao.addQuestionGroup(groupToAdd);
		if(resultGroup==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		operationResponse.setAdded(true);
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		
		return operationResponse;
	}

	@Override
	public OperationGroupResponse editGroup(EditGroupRequest editGroupRequest) {
		OperationGroupResponse operationResponse = new OperationGroupResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(editGroupRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		QuestionGroup groupToEdit = quizDao.getQuestionGroup(editGroupRequest.getGroupId());
		if(groupToEdit==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		groupToEdit.setGroupName(editGroupRequest.getGroupName());
		groupToEdit.setEnabled(editGroupRequest.isEnabled());
		QuestionGroup resultGroup = adminDao.updateQuestionGroup(groupToEdit);
		if(resultGroup==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		operationResponse.setAdded(true);
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		
		return operationResponse;
	}

	@Override
	public DeleteGroupResponse deleteGroup(DeleteGroupRequest deleteGroupRequest) {
		DeleteGroupResponse operationResponse = new DeleteGroupResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(deleteGroupRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		QuestionGroup groupToDelete = quizDao.getQuestionGroup(deleteGroupRequest.getGroupId());
		groupToDelete.setDeleted(true);
		groupToDelete = adminDao.updateQuestionGroup(groupToDelete);
		if(groupToDelete==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		List<Question> relatedQuestions = adminDao.getQuestionsForGroup(groupToDelete);
		
		for(Question question:relatedQuestions){
			question.setDeleted(true);
			question.setQuestionGroup(null);
			adminDao.updateQuestion(question);
		}
		
		operationResponse.setDeleted(true);
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		
		return operationResponse;
	}


	
	private List<AdminUserSession> getActiveSessionList(){
		return adminDao.getActiveSessions();
	}
	
	private void updateAdminSession(AdminUserSession userSession){
		adminDao.updateSession(userSession);
	}
	
	
	
	
	private class AdminSessionThread implements Runnable{

		@Override
		public void run() {
			for(;;){
				
				try {
					Thread.currentThread().sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				List<AdminUserSession> activeSessionsList = getActiveSessionList();
				if(activeSessionsList==null) continue;
				long currentTime = new Date().getTime();
				for(AdminUserSession adminUserSession:activeSessionsList){
					long currentSessionDifference = currentTime - adminUserSession.getLastActivity();
					if(currentSessionDifference>sessionLostActivity){
						adminUserSession.setActive(false);
						updateAdminSession(adminUserSession);
					}
				}
			}
			
		}
		
	}




	@Override
	public GetQuestionsForGroupResponse getQuestionsForGroup(GetQuestionsForGroup getQuestionsForGroupRequest) {
		GetQuestionsForGroupResponse operationResponse = new GetQuestionsForGroupResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(getQuestionsForGroupRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		QuestionGroup questionGroup = quizDao.getQuestionGroup(getQuestionsForGroupRequest.getGroupId());
		if(questionGroup == null){
			operationResponse.getErrorData().setErrorCode(ErrorData.NO_SUCH_QUESTION_GROUP);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_QUESTION_GROUP);
			return operationResponse;
		}
		
		List<Question> questionsForGroup = adminDao.getQuestionsForGroup(questionGroup);
		if(questionsForGroup.size()>0){
			List<QuestionForAdmin> questions = new ArrayList<>();
			for(Question question:questionsForGroup){
				if(question.isDeleted())continue;
				QuestionForAdmin questionForAdmin = new QuestionForAdmin();
				questionForAdmin.setId(question.getId());
				questionForAdmin.setQuestionText(question.getQuestionText());
				questionForAdmin.setParentId(question.getParentId());
				questionForAdmin.setEnabled(question.getEnabled());
				questions.add(questionForAdmin);
			}
			operationResponse.setQuestionGroups(questions);
		}
		
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		
		
		return operationResponse;
	}



}
