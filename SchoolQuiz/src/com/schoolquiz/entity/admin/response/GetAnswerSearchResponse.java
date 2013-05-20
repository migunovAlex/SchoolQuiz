package com.schoolquiz.entity.admin.response;

import java.util.List;

import com.schoolquiz.entity.ErrorData;

public class GetAnswerSearchResponse {
	
	private ErrorData errorData;
	private List<AnswerItem> answers;
	
	public GetAnswerSearchResponse(){
		errorData = new ErrorData();
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	public List<AnswerItem> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerItem> answers) {
		this.answers = answers;
	}
	
	

}
