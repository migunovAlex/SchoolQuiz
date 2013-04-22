package com.schoolquiz.entity.admin.request;

public class DeleteGroupRequest {
	
	public String userSession;
	public long groupId;
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
	@Override
	public String toString(){
		return "userSession - "+userSession+"; groupId - "+groupId;
	}

}
