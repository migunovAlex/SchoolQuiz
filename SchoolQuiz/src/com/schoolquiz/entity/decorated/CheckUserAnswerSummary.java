package com.schoolquiz.entity.decorated;

import com.schoolquiz.entity.ErrorData;

public class CheckUserAnswerSummary {
	
	private ErrorData errorData;
	private boolean result;
	
	public CheckUserAnswerSummary(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; result - "+result;
	}

}
