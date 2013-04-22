package com.schoolquiz.entity.decorated;

import java.util.List;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.QuestionGroupOut;

public class QuestionGroupListOutSummary {
	
	private ErrorData errorData;
	private List<QuestionGroupOut> questionGroups;
	
	public QuestionGroupListOutSummary(){
		errorData = new ErrorData();
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	public List<QuestionGroupOut> getQuestionGroups() {
		return questionGroups;
	}

	public void setQuestionGroups(List<QuestionGroupOut> questionGroups) {
		this.questionGroups = questionGroups;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; questionGroups - "+questionGroups;
	}
	

}
