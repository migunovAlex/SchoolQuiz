package com.schoolquiz.entity.admin.response;

import com.schoolquiz.entity.ErrorData;

public class AddAnswerResponse {
	
	private ErrorData errorData;
	private Boolean result;
	
	public AddAnswerResponse(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	
	

}
