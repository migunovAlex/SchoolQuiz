package com.schoolquiz.entity.decorated;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.Question;

public class QuestionSummary {
	
	private ErrorData errorData;
	private Question question;
	
	public QuestionSummary(){
		errorData = new ErrorData();
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; question - "+question;
	}

}
