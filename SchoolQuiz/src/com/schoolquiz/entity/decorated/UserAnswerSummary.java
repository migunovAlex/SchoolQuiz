package com.schoolquiz.entity.decorated;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.UserAnswer;

public class UserAnswerSummary {
	
	private ErrorData errorData;
	private UserAnswer userAnswer;
	
	public UserAnswerSummary(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public UserAnswer getUserAnswer() {
		return userAnswer;
	}
	public void setUserAnswer(UserAnswer userAnswer) {
		this.userAnswer = userAnswer;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; userAnswer - "+userAnswer;
	}
	

}
