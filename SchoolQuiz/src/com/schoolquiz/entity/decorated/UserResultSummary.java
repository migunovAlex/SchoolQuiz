package com.schoolquiz.entity.decorated;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.UserResult;

public class UserResultSummary {
	
	private ErrorData errorData;
	private UserResult userResult;
	
	public UserResultSummary(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public UserResult getUserResult() {
		return userResult;
	}
	public void setUserResult(UserResult userResult) {
		this.userResult = userResult;
	}
	
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; userResult - "+userResult;
	}

}
