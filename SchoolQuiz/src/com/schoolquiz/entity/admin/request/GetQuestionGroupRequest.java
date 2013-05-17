package com.schoolquiz.entity.admin.request;

public class GetQuestionGroupRequest {
	
	private String userSession;
	private Long questionGroupId;
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public Long getQuestionGroupId() {
		return questionGroupId;
	}
	public void setQuestionGroupId(Long questionGroupId) {
		this.questionGroupId = questionGroupId;
	}
	
	
	

}
