package com.schoolquiz.service;

import com.schoolquiz.entity.admin.CheckSessionSummary;
import com.schoolquiz.entity.admin.FinishSessionSummary;
import com.schoolquiz.entity.admin.OperationGroupResponse;
import com.schoolquiz.entity.admin.decorated.AdminUserSessionSummary;
import com.schoolquiz.entity.admin.decorated.CustomQuestionGroupResponse;
import com.schoolquiz.entity.admin.request.UserSession;

public interface AdminUserService {
	
	public AdminUserSessionSummary checkUser(String userName, String password);
	
	public CheckSessionSummary checkAdminSession(String userSession);
	
	public void updateSessionActivity(String userSession);
	
	public FinishSessionSummary finishSession(String session);
	
	public CustomQuestionGroupResponse getGroupsForAdmin(String session, int numberFrom, int numberElements);
	
	public OperationGroupResponse addGroup(String userSession, String groupName);
	
	public OperationGroupResponse editGroup(String userSession, long groupId, String groupName, boolean enabled);
	
	public OperationGroupResponse deleteGroup(String userSession, long groupId);

}
