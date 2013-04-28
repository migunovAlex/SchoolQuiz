package com.schoolquiz.entity.admin.response;

import com.schoolquiz.entity.ErrorData;

public class GetQuestionGroupResponse {
	
	private ErrorData errorData;
	private Long id;
	private String groupName;
	private boolean enabled;
	
	public GetQuestionGroupResponse(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	

}
