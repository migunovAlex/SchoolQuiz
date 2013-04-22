package com.schoolquiz.entity.admin.request;

public class EditGroupRequest {

	private String userSession;
	private long groupId;
	private String groupName;
	private boolean enabled;
	
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public String toString(){
		return "userSession - "+userSession+"; groipId - "+groupId+"; groupName - "+groupName+"; enabled - "+enabled;
	}
	
}
