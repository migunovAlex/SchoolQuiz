package com.schoolquiz.entity.admin;

import com.schoolquiz.entity.ErrorData;

public class OperationGroupResponse {
	
	private ErrorData errorData;
	private Boolean added;
	
	public OperationGroupResponse(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public Boolean getAdded() {
		return added;
	}
	public void setAdded(Boolean added) {
		this.added = added;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; added - "+added;
	}
	

}
