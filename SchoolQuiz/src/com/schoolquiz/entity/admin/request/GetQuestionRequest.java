package com.schoolquiz.entity.admin.request;

public class GetQuestionRequest {
	
	private String userSession;
	private Long questionId;
	
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	
	

}
