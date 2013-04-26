package com.schoolquiz.entity.admin;

import com.schoolquiz.entity.ErrorData;

public class DeleteGroupResponse {
	
	private ErrorData errorData;
	private Boolean deleted;
	
	public DeleteGroupResponse(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; added - "+getDeleted();
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	

}
