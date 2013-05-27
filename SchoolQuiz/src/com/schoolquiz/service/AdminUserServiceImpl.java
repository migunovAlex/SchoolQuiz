package com.schoolquiz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.schoolquiz.entity.Answer;
import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.Question;
import com.schoolquiz.entity.QuestionAnswer;
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
import com.schoolquiz.entity.admin.request.AddAnswerRequest;
import com.schoolquiz.entity.admin.request.AddAnswersToQuestionRequest;
import com.schoolquiz.entity.admin.request.AddGroupRequest;
import com.schoolquiz.entity.admin.request.AddQuestionRequest;
import com.schoolquiz.entity.admin.request.DeleteAnswerRequest;
import com.schoolquiz.entity.admin.request.DeleteGroupRequest;
import com.schoolquiz.entity.admin.request.DeleteQuestionRequest;
import com.schoolquiz.entity.admin.request.EditAnswerRequest;
import com.schoolquiz.entity.admin.request.EditGroupRequest;
import com.schoolquiz.entity.admin.request.EditQuestionRequest;
import com.schoolquiz.entity.admin.request.GetAnswerRequest;
import com.schoolquiz.entity.admin.request.GetAnswerSearchRequest;
import com.schoolquiz.entity.admin.request.GetAnswersForQuestionRequest;
import com.schoolquiz.entity.admin.request.GetGroupsDictRequest;
import com.schoolquiz.entity.admin.request.GetQuestionGroupRequest;
import com.schoolquiz.entity.admin.request.GetQuestionRequest;
import com.schoolquiz.entity.admin.request.GetQuestionsForGroup;
import com.schoolquiz.entity.admin.request.RemoveAnswersFromQuestionRequest;
import com.schoolquiz.entity.admin.request.UserSession;
import com.schoolquiz.entity.admin.response.AddAnswerResponse;
import com.schoolquiz.entity.admin.response.AddAnswersToQuestionResponse;
import com.schoolquiz.entity.admin.response.AnswerEntity;
import com.schoolquiz.entity.admin.response.AnswerItem;
import com.schoolquiz.entity.admin.response.DeleteAnswerResponse;
import com.schoolquiz.entity.admin.response.DictItem;
import com.schoolquiz.entity.admin.response.EditAnswerResponse;
import com.schoolquiz.entity.admin.response.GetAnswerResponse;
import com.schoolquiz.entity.admin.response.GetAnswerSearchResponse;
import com.schoolquiz.entity.admin.response.GetAnswersForQuestionResponse;
import com.schoolquiz.entity.admin.response.GetGroupsDictResponse;
import com.schoolquiz.entity.admin.response.GetQuestionGroupResponse;
import com.schoolquiz.entity.admin.response.GetQuestionResponse;
import com.schoolquiz.entity.admin.response.GetQuestionsForGroupResponse;
import com.schoolquiz.entity.admin.response.QuestionForAdmin;
import com.schoolquiz.entity.admin.response.RemoveAnswersFromQuestionResponse;
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
		
		operationResponse.setResult(true);
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
		if(groupToEdit==null || groupToEdit.getDeleted()==true){
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
		
		operationResponse.setResult(true);
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
		if(groupToDelete==null || groupToDelete.getDeleted()==true){
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
	
	@Override
	public GetQuestionGroupResponse getQuestionGroup(GetQuestionGroupRequest getQuestionGroupRequest) {
		GetQuestionGroupResponse operationResponse = new GetQuestionGroupResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(getQuestionGroupRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		QuestionGroup questionGroup = quizDao.getQuestionGroup(getQuestionGroupRequest.getQuestionGroupId());
		if(questionGroup==null || questionGroup.getDeleted()){
			operationResponse.getErrorData().setErrorCode(ErrorData.NO_SUCH_QUESTION_GROUP);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_QUESTION_GROUP);
			return operationResponse;
		}
		
		operationResponse.setGroupName(questionGroup.getGroupName());
		operationResponse.setId(questionGroup.getId());
		operationResponse.setEnabled(questionGroup.getEnabled());
		
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		
		return operationResponse;
	}

	@Override
	public GetQuestionResponse getQuestion(GetQuestionRequest getQuestionRequest) {
		GetQuestionResponse operationResponse = new GetQuestionResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(getQuestionRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		Question question = quizDao.getQuestion(getQuestionRequest.getQuestionId());
		if(question == null || question.isDeleted()){
			operationResponse.getErrorData().setErrorCode(ErrorData.NO_SUCH_QUESTION);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_QUESTION);
			return operationResponse;
		}
		
		operationResponse.setId(getQuestionRequest.getQuestionId());
		operationResponse.setParentQuestionId(question.getParentId());
		operationResponse.setQuestionGroupId(question.getQuestionGroup().getId());
		operationResponse.setQuestionText(question.getQuestionText());
		operationResponse.setResponseType(question.getResponseType());
		operationResponse.setEnabled(question.getEnabled());
		
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		
		return operationResponse;
	}

	@Override
	public OperationGroupResponse addQuestion(AddQuestionRequest addQuestionRequest) {
		OperationGroupResponse operationResponse = new OperationGroupResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(addQuestionRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		QuestionGroup questionGroup = quizDao.getQuestionGroup(addQuestionRequest.getQuestionGroup());
		
		Question addedQuestion = new Question();
		addedQuestion.setDeleted(false);
		addedQuestion.setEnabled(addQuestionRequest.getEnabled());
		addedQuestion.setParentId(addQuestionRequest.getQuestionParentId());
		addedQuestion.setQuestionGroup(questionGroup);
		addedQuestion.setQuestionText(addQuestionRequest.getQuestionText());
		addedQuestion.setResponseType(addQuestionRequest.getResponseType());
		
		addedQuestion = quizDao.addQuestion(addedQuestion);
		if(addedQuestion==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		operationResponse.setResult(true);
		updateSessionActivity(checkAdminSessionRes.getUserSession());
	
		return operationResponse;
	}

	@Override
	public OperationGroupResponse editQuestion(EditQuestionRequest editQuestionRequest) {
		OperationGroupResponse operationResponse = new OperationGroupResponse(); 
		operationResponse.setResult(false);
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(editQuestionRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		Question questionToEdit = quizDao.getQuestion(editQuestionRequest.getQuestionId());
		if(questionToEdit==null || questionToEdit.isDeleted()){
			operationResponse.getErrorData().setErrorCode(ErrorData.NO_SUCH_QUESTION);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_QUESTION);
			return operationResponse;
		}
		
		
		questionToEdit.setEnabled(editQuestionRequest.getEnabled());
		questionToEdit.setParentId(editQuestionRequest.getQuestionParentId());
		QuestionGroup questionGroup = quizDao.getQuestionGroup(editQuestionRequest.getQuestionGroup());
		questionToEdit.setQuestionGroup(questionGroup);
		questionToEdit.setQuestionText(editQuestionRequest.getQuestionText());
		questionToEdit.setResponseType(Integer.parseInt(editQuestionRequest.getResponseType()+""));
		
		questionToEdit = quizDao.updateQuestion(questionToEdit);
		
		if(questionToEdit==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		operationResponse.setResult(true);
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		
		return operationResponse;
	}

	@Override
	public OperationGroupResponse deleteQuestion(DeleteQuestionRequest deleteQuestionRequest) {
		OperationGroupResponse operationResponse = new OperationGroupResponse(); 
		operationResponse.setResult(false);
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(deleteQuestionRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		Question deletedQuestion = quizDao.getQuestion(deleteQuestionRequest.getQuestionId());
		if(deletedQuestion==null || deletedQuestion.isDeleted()){
			operationResponse.getErrorData().setErrorCode(ErrorData.NO_SUCH_QUESTION);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_QUESTION);
			return operationResponse;
		}
		
		deletedQuestion = quizDao.deleteQuestion(deletedQuestion);
		
		if(deletedQuestion==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		operationResponse.setResult(true);
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		return operationResponse;
	}

	

	@Override
	public GetAnswersForQuestionResponse getAnswersForQuestion(GetAnswersForQuestionRequest getAnswerRequest) {
		GetAnswersForQuestionResponse operationResponse = new GetAnswersForQuestionResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(getAnswerRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		Question foundQuestion = quizDao.getQuestion(getAnswerRequest.getQuestionId());
		if(foundQuestion==null || foundQuestion.isDeleted()){
			operationResponse.getErrorData().setErrorCode(ErrorData.NO_SUCH_QUESTION);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_QUESTION);
			return operationResponse;
		}
		
		List<AnswerEntity> answersForQuestion = quizDao.getAnswersForQuestion(foundQuestion);
		
		operationResponse.setAnswerList(answersForQuestion);
		
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		return operationResponse;
	}

	@Override
	public GetAnswerResponse getAnswer(GetAnswerRequest getAnswerRequest) {
		GetAnswerResponse operationResponse = new GetAnswerResponse(); 
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(getAnswerRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		Answer answer = quizDao.getAnswer(getAnswerRequest.getAnswerId());
		if(answer==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.NO_SUCH_ANSWER);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_ANSWER);
			return operationResponse;
		}
		
		operationResponse.setId(answer.getId());
		operationResponse.setAnswerText(answer.getAnswerText());
		operationResponse.setEnabled(answer.getEnabled());
		
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		return operationResponse;
	}

	@Override
	public AddAnswerResponse addAnswer(AddAnswerRequest addAnswerRequest) {
		AddAnswerResponse operationResponse = new AddAnswerResponse(); 
		operationResponse.setResult(false);
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(addAnswerRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		Answer answerToAdd = new Answer();
		answerToAdd.setAnswerText(addAnswerRequest.getAnswerText());
		answerToAdd.setDeleted(false);
		answerToAdd.setEnabled(addAnswerRequest.getEnabled());
		answerToAdd = quizDao.saveAnswer(answerToAdd);
		
		if(answerToAdd==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		operationResponse.setResult(true);
		
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		return operationResponse;
	}

	@Override
	public EditAnswerResponse editAnswer(EditAnswerRequest editAnswerRequest) {
		EditAnswerResponse operationResponse = new EditAnswerResponse(); 
		operationResponse.setResult(false);
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(editAnswerRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		Answer answerToEdit = quizDao.getAnswer(editAnswerRequest.getId());
		if(answerToEdit==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.NO_SUCH_ANSWER);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_ANSWER);
			return operationResponse;
		}
		
		
		answerToEdit.setAnswerText(editAnswerRequest.getAnswerText());
		answerToEdit.setEnabled(editAnswerRequest.getEnabled());
		answerToEdit = quizDao.updateAnswer(answerToEdit);
		
		if(answerToEdit==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		operationResponse.setResult(true);
		
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		return operationResponse;
	}

	@Override
	public DeleteAnswerResponse deleteAnswer(DeleteAnswerRequest deleteAnswerRequest) {
		DeleteAnswerResponse operationResponse = new DeleteAnswerResponse(); 
		operationResponse.setResult(false);
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(deleteAnswerRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			operationResponse.setErrorData(checkAdminSessionRes.getErrorData());
			return operationResponse;
		}
		
		Answer answerToEdit = quizDao.getAnswer(deleteAnswerRequest.getAnswerId());
		if(answerToEdit==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.NO_SUCH_ANSWER);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_ANSWER);
			return operationResponse;
		}
		
		
		answerToEdit.setDeleted(true);
		answerToEdit = quizDao.updateAnswer(answerToEdit);
		
		if(answerToEdit==null){
			operationResponse.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			operationResponse.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return operationResponse;
		}
		
		operationResponse.setResult(true);
		
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		return operationResponse;
	}

	@Override
	public GetGroupsDictResponse getGroupsDict(GetGroupsDictRequest getGroupsDictRequest) {
		GetGroupsDictResponse response = new GetGroupsDictResponse();
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(getGroupsDictRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			response.setErrorData(checkAdminSessionRes.getErrorData());
			return response;
		}
		
		List<QuestionGroup> questionGroups = quizDao.getQuestionGroups(0, Integer.MAX_VALUE);
		List<DictItem> dictItems = new ArrayList<>();
		
		for(QuestionGroup questionGroup:questionGroups){
			if(questionGroup.getDeleted()) continue;
			DictItem dictItem = new DictItem();
			dictItem.setId(questionGroup.getId());
			dictItem.setText(questionGroup.getGroupName());
			dictItems.add(dictItem);
		}
		
		response.setDictItems(dictItems);
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		
		return response;
	}

	@Override
	public GetAnswerSearchResponse getAnswerSearch(GetAnswerSearchRequest getAnswerSearchRequest) {
		GetAnswerSearchResponse response = new GetAnswerSearchResponse();
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(getAnswerSearchRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			response.setErrorData(checkAdminSessionRes.getErrorData());
			return response;
		}
		
		List<AnswerItem> answerItems = new ArrayList<>();
		List<Answer> foundAnswers = quizDao.searchAnswers(getAnswerSearchRequest.getKeyWord());
		for(Answer answer:foundAnswers){
			if(answer.isDeleted()) continue;
			AnswerItem answerItem = new AnswerItem();
			answerItem.setAnswerText(answer.getAnswerText());
			answerItem.setId(answer.getId());
			answerItem.setEnabled(answer.getEnabled());
			answerItems.add(answerItem);
		}
		
		response.setAnswers(answerItems);
		
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		
		return response;
	}

	@Override
	public AddAnswersToQuestionResponse addAnswersToQuestion(AddAnswersToQuestionRequest addAnswersToQuestionRequest) {
		AddAnswersToQuestionResponse response = new AddAnswersToQuestionResponse();
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(addAnswersToQuestionRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			response.setErrorData(checkAdminSessionRes.getErrorData());
			return response;
		}
		
		if(addAnswersToQuestionRequest.getAnswerIds()==null|| addAnswersToQuestionRequest.getAnswerIds().size()==0 ||
				addAnswersToQuestionRequest.getAnswerIds().size()!=addAnswersToQuestionRequest.getRightAnswers().size()){
			response.getErrorData().setErrorCode(ErrorData.WRONG_PARAMS);
			response.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_WRONG_PARAMS);
			return response;
		}
		
		Question question = quizDao.getQuestionWithAnswers(addAnswersToQuestionRequest.getQuestionId());
		
		if(question==null){
			response.getErrorData().setErrorCode(ErrorData.NO_SUCH_QUESTION);
			response.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_QUESTION);
			return response;
		}
		
		
		for(int i=0; i<addAnswersToQuestionRequest.getAnswerIds().size();i++){
			Long answerId = addAnswersToQuestionRequest.getAnswerIds().get(i);
			List<QuestionAnswer> questionAnswerList = question.getQuestionAnswerList();
			Boolean answerExists = false;
			if(questionAnswerList==null) questionAnswerList = new ArrayList<>();
			
			Iterator<QuestionAnswer> iter = questionAnswerList.iterator();
			QuestionAnswer qAnswer = null;
			while(iter.hasNext()){
				qAnswer = iter.next();
				if(answerId==qAnswer.getAnswer().getId()){
					answerExists = true;
					break;
				}
			}
			
			
			/*for(QuestionAnswer questionAnswer : questionAnswerList){
				if(answerId == questionAnswer.getAnswer().getId()){
					answerExists = true;
					break;
				}
			}*/
			
			
			if(!answerExists){
				Answer answerToAdd = quizDao.getAnswer(answerId);
				if(answerToAdd==null) continue;
				QuestionAnswer questionAnswer = new QuestionAnswer();
				questionAnswer.setQuestion(question);
				questionAnswer.setAnswer(answerToAdd);
				questionAnswer.setRight(addAnswersToQuestionRequest.getRightAnswers().get(i));
//				questionAnswer = quizDao.addQuestionAnswer(questionAnswer);
//				if(questionAnswer==null){
//					response.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
//					response.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
//					return response;
//				}
				question.getQuestionAnswerList().add(questionAnswer);
			}
		}
		
		question = quizDao.updateQuestion(question);
		
		response.setOperationResult(true);
		
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		return response;
	}

	@Override
	public RemoveAnswersFromQuestionResponse removeAnswersFromQuestion(RemoveAnswersFromQuestionRequest removeAnswersFromQuestionRequest) {
		RemoveAnswersFromQuestionResponse response = new RemoveAnswersFromQuestionResponse();
		CheckSessionSummary checkAdminSessionRes = checkAdminSession(removeAnswersFromQuestionRequest.getUserSession());
		if(checkAdminSessionRes.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			response.setErrorData(checkAdminSessionRes.getErrorData());
			return response;
		}
		
		if(removeAnswersFromQuestionRequest.getAnswerIds()==null|| removeAnswersFromQuestionRequest.getAnswerIds().size()==0){
			response.getErrorData().setErrorCode(ErrorData.WRONG_PARAMS);
			response.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_WRONG_PARAMS);
			return response;
		}
		
		Question question = quizDao.getQuestionWithAnswers(removeAnswersFromQuestionRequest.getQuestionId());
		
		if(question==null){
			response.getErrorData().setErrorCode(ErrorData.NO_SUCH_QUESTION);
			response.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NO_SUCH_QUESTION);
			return response;
		}
		
		Iterator<QuestionAnswer> answerIterator = question.getQuestionAnswerList().iterator();
		
		for(Long deleteId: removeAnswersFromQuestionRequest.getAnswerIds()){
			while(answerIterator.hasNext()){
				QuestionAnswer questionAnswer = answerIterator.next();
				if(deleteId == questionAnswer.getAnswer().getId()){
					questionAnswer = quizDao.deleteQuestionAnswer(questionAnswer);
					if(question==null){
						response.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
						response.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
						return response;
					}
				}
			}
		}
		
		
		response.setOperationResult(true);
		updateSessionActivity(checkAdminSessionRes.getUserSession());
		return response;
	}



}
