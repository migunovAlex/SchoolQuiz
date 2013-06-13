package com.schoolquiz.entity.admin.response;

import com.schoolquiz.entity.ErrorData;

public class EditAnswersFromQuestionResponse {
	
	private ErrorData errorData;
	private Boolean operationResult;
	
	public EditAnswersFromQuestionResponse(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public Boolean getOperationResult() {
		return operationResult;
	}
	public void setOperationResult(Boolean operationResult) {
		this.operationResult = operationResult;
	}
	
	

}
