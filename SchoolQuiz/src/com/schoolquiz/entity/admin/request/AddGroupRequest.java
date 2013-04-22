package com.schoolquiz.entity.admin.request;

public class AddGroupRequest {
	
	private String userSession;
	private String groupName;
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Override
	public String toString(){
		return "usersession - "+userSession+"; groupName - "+groupName;
	}

}
