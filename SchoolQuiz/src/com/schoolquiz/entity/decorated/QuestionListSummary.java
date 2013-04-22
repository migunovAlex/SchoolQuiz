package com.schoolquiz.entity.decorated;

import java.util.List;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.Question;

public class QuestionListSummary {
	
	private ErrorData errorData;
	private List<Question> questions;
	
	public QuestionListSummary(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; questions - "+questions;
	}
	

}
