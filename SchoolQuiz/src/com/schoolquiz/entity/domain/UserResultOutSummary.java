package com.schoolquiz.entity.domain;

import javax.xml.bind.annotation.XmlRootElement;

import com.schoolquiz.entity.ErrorData;

@XmlRootElement(name="userResultSummary")
public class UserResultOutSummary {
	
	private ErrorData errorData;
	private UserResultOut userResult;
	
	
	public UserResultOutSummary(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public UserResultOut getUserResult() {
		return userResult;
	}
	public void setUserResult(UserResultOut userResult) {
		this.userResult = userResult;
	}
	
	
	

}
