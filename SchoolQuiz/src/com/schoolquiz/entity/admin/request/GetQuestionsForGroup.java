package com.schoolquiz.entity.admin.request;

public class GetQuestionsForGroup {
	
	private String userSession;
	private Long groupId;
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	

}
