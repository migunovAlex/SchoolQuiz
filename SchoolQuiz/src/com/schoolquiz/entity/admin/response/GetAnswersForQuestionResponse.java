package com.schoolquiz.entity.admin.response;

import java.util.List;

import com.schoolquiz.entity.ErrorData;

public class GetAnswersForQuestionResponse {
	
	private ErrorData errorData;
	private List<AnswerEntity> answerList;
	
	public GetAnswersForQuestionResponse(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public List<AnswerEntity> getAnswerList() {
		return answerList;
	}
	public void setAnswerList(List<AnswerEntity> answerList) {
		this.answerList = answerList;
	}
	
	
	

}
