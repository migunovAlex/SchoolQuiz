package com.schoolquiz.entity.admin;

import com.schoolquiz.entity.ErrorData;

public class CheckSessionSummary {
	
	private ErrorData errorData;
	private String userSession;
	
	public CheckSessionSummary(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	
	

}
