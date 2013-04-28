package com.schoolquiz.service;

import com.schoolquiz.entity.admin.CheckSessionSummary;
import com.schoolquiz.entity.admin.DeleteGroupResponse;
import com.schoolquiz.entity.admin.FinishSessionSummary;
import com.schoolquiz.entity.admin.OperationGroupResponse;
import com.schoolquiz.entity.admin.decorated.AdminUserSessionSummary;
import com.schoolquiz.entity.admin.decorated.CustomQuestionGroupResponse;
import com.schoolquiz.entity.admin.request.AddAnswerRequest;
import com.schoolquiz.entity.admin.request.AddGroupRequest;
import com.schoolquiz.entity.admin.request.AddQuestionRequest;
import com.schoolquiz.entity.admin.request.DeleteAnswerRequest;
import com.schoolquiz.entity.admin.request.DeleteGroupRequest;
import com.schoolquiz.entity.admin.request.DeleteQuestionRequest;
import com.schoolquiz.entity.admin.request.EditAnswerRequest;
import com.schoolquiz.entity.admin.request.EditGroupRequest;
import com.schoolquiz.entity.admin.request.EditQuestionRequest;
import com.schoolquiz.entity.admin.request.GetAnswerRequest;
import com.schoolquiz.entity.admin.request.GetAnswersForQuestionRequest;
import com.schoolquiz.entity.admin.request.GetQuestionGroupRequest;
import com.schoolquiz.entity.admin.request.GetQuestionRequest;
import com.schoolquiz.entity.admin.request.GetQuestionsForGroup;
import com.schoolquiz.entity.admin.response.AddAnswerResponse;
import com.schoolquiz.entity.admin.response.DeleteAnswerResponse;
import com.schoolquiz.entity.admin.response.EditAnswerResponse;
import com.schoolquiz.entity.admin.response.GetAnswerResponse;
import com.schoolquiz.entity.admin.response.GetAnswersForQuestionResponse;
import com.schoolquiz.entity.admin.response.GetQuestionGroupResponse;
import com.schoolquiz.entity.admin.response.GetQuestionResponse;
import com.schoolquiz.entity.admin.response.GetQuestionsForGroupResponse;

public interface AdminUserService {
	
	public AdminUserSessionSummary checkUser(String userName, String password);
	
	public CheckSessionSummary checkAdminSession(String userSession);
	
	public void updateSessionActivity(String userSession);
	
	public FinishSessionSummary finishSession(String session);
	
	public CustomQuestionGroupResponse getGroupsForAdmin(String session, int numberFrom, int numberElements);
	
	public OperationGroupResponse addGroup(AddGroupRequest addGroupRequest);
	
	public OperationGroupResponse editGroup(EditGroupRequest editGroupRequest);
	
	public DeleteGroupResponse deleteGroup(DeleteGroupRequest deleteGroupRequest);

	public GetQuestionsForGroupResponse getQuestionsForGroup(GetQuestionsForGroup getQuestionsForGroupRequest);

	public OperationGroupResponse addQuestion(AddQuestionRequest addQuestionRequest);

	public OperationGroupResponse editQuestion(EditQuestionRequest editQuestionRequest);

	public OperationGroupResponse deleteQuestion(DeleteQuestionRequest deleteQuestionRequest);

	public GetQuestionGroupResponse getQuestionGroup(GetQuestionGroupRequest getQuestionGroupRequest);

	public GetQuestionResponse getQuestion(GetQuestionRequest getQuestionRequest);

	public GetAnswersForQuestionResponse getAnswersForQuestion(GetAnswersForQuestionRequest getAnswerRequest);

	public GetAnswerResponse getAnswer(GetAnswerRequest getAnswerRequest);

	public AddAnswerResponse addAnswer(AddAnswerRequest addAnswerRequest);

	public EditAnswerResponse editAnswer(EditAnswerRequest editAnswerRequest);

	public DeleteAnswerResponse deleteAnswer(DeleteAnswerRequest deleteAnswerRequest);


}
