package com.schoolquiz.entity.decorated;

import java.util.List;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.domain.QuestionOut;

public class QuestionOutListSummary {
	
	private ErrorData errorData;
	private List<QuestionOut> questions;
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public List<QuestionOut> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionOut> questions) {
		this.questions = questions;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; questions - "+questions;
	}

}
