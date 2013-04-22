package com.schoolquiz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.QuestionGroup;
import com.schoolquiz.entity.admin.AdminUser;
import com.schoolquiz.entity.admin.AdminUserSession;
import com.schoolquiz.entity.admin.CheckSessionSummary;
import com.schoolquiz.entity.admin.FinishSessionSummary;
import com.schoolquiz.entity.admin.GroupForAdmin;
import com.schoolquiz.entity.admin.OperationGroupResponse;
import com.schoolquiz.entity.admin.decorated.AdminUserSessionSummary;
import com.schoolquiz.entity.admin.decorated.CustomQuestionGroupResponse;
import com.schoolquiz.entity.admin.request.UserSession;
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
	public OperationGroupResponse addGroup(String userSession, String groupName) {
		OperationGroupResponse operationResponse = new OperationGroupResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(userSession);
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		QuestionGroup groupToAdd = new QuestionGroup();
		groupToAdd.setGroupName(groupName);
		groupToAdd.setEnabled(true);
		QuestionGroup resultGroup = adminDao.addQuestionGroup(groupToAdd);
		if(resultGroup==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		operationResponse.setAdded(true);
		
		return operationResponse;
	}

	@Override
	public OperationGroupResponse editGroup(String userSession, long groupId, String groupName, boolean enabled) {
		OperationGroupResponse operationResponse = new OperationGroupResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(userSession);
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		QuestionGroup groupToEdit = new QuestionGroup();
		groupToEdit.setGroupName(groupName);
		groupToEdit.setId(groupId);
		groupToEdit.setEnabled(enabled);
		QuestionGroup resultGroup = adminDao.updateQuestionGroup(groupToEdit);
		if(resultGroup==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		operationResponse.setAdded(true);
		
		return operationResponse;
	}

	@Override
	public OperationGroupResponse deleteGroup(String userSession, long groupId) {
		OperationGroupResponse operationResponse = new OperationGroupResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(userSession);
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		QuestionGroup groupToDelete = quizDao.getQuestionGroup(groupId);
		
		boolean deleted = adminDao.deleteQuestionGroup(groupToDelete);
		if(deleted==false){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		operationResponse.setAdded(true);
		
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



}
