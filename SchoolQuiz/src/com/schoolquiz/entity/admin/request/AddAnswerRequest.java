package com.schoolquiz.entity.admin.request;

public class AddAnswerRequest {
	
	private String userSession;
	private String answerText;
	private Boolean enabled;
	
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	

}
