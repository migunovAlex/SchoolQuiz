package com.schoolquiz.entity.admin.response;

import java.util.List;

import com.schoolquiz.entity.ErrorData;

public class GetUserResultsResponse {
	
	private ErrorData errorData;
	private List<UserResultsEntity> userResults;
	
	public GetUserResultsResponse(){
		errorData = new ErrorData();
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	public List<UserResultsEntity> getUserResults() {
		return userResults;
	}

	public void setUserResults(List<UserResultsEntity> userResults) {
		this.userResults = userResults;
	}
	
	

}
