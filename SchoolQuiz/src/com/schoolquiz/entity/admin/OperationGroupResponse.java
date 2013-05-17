package com.schoolquiz.entity.admin;

import com.schoolquiz.entity.ErrorData;

public class OperationGroupResponse {
	
	private ErrorData errorData;
	private Boolean result;
	
	public OperationGroupResponse(){
		errorData = new ErrorData();
		result = false;
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
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; added - "+result;
	}
	

}
