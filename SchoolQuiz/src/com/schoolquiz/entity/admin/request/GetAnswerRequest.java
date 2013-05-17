package com.schoolquiz.entity.admin.request;

public class GetAnswerRequest {
	
	private String userSession;
	private Long answerId;
	
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public Long getAnswerId() {
		return answerId;
	}
	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}
	
	

}
