package com.schoolquiz.entity.domain;

import com.schoolquiz.entity.ErrorData;

public class UserResultJsonOutSummary {
	
	private ErrorData errorData;
	private UserResultJsonOut userResult;
	
	public UserResultJsonOutSummary(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public UserResultJsonOut getUserResult() {
		return userResult;
	}
	public void setUserResult(UserResultJsonOut userResult) {
		this.userResult = userResult;
	}
	
	

}
