package com.schoolquiz.entity.admin;

import com.schoolquiz.entity.ErrorData;

public class FinishSessionSummary {
	
	private ErrorData errorData;
	private Boolean finishSession;
	
	public FinishSessionSummary(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public Boolean getFinishSession() {
		return finishSession;
	}
	public void setFinishSession(Boolean finishSession) {
		this.finishSession = finishSession;
	}
	
	

}
