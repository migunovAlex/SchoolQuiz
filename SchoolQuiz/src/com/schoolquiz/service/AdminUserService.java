package com.schoolquiz.service;

import com.schoolquiz.entity.admin.CheckSessionSummary;
import com.schoolquiz.entity.admin.DeleteGroupResponse;
import com.schoolquiz.entity.admin.FinishSessionSummary;
import com.schoolquiz.entity.admin.OperationGroupResponse;
import com.schoolquiz.entity.admin.decorated.AdminUserSessionSummary;
import com.schoolquiz.entity.admin.decorated.CustomQuestionGroupResponse;
import com.schoolquiz.entity.admin.request.AddGroupRequest;
import com.schoolquiz.entity.admin.request.DeleteGroupRequest;
import com.schoolquiz.entity.admin.request.EditGroupRequest;
import com.schoolquiz.entity.admin.request.GetQuestionsForGroup;
import com.schoolquiz.entity.admin.request.UserSession;
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

}
