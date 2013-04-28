package com.schoolquiz.entity.admin.response;

import com.schoolquiz.entity.ErrorData;

public class EditAnswerResponse {
	
	private ErrorData errorData;
	private Boolean result;
	
	public EditAnswerResponse(){
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
