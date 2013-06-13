package com.schoolquiz.entity.admin.request;

import java.util.List;

public class EditAnswersFromQuestionRequest {
	
	private String userSession;
	private Long questionId;
	private Long answerId;
	private Boolean rightAnswer;
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	public Long getAnswerId() {
		return answerId;
	}
	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}
	public Boolean getRightAnswer() {
		return rightAnswer;
	}
	public void setRightAnswer(Boolean rightAnswer) {
		this.rightAnswer = rightAnswer;
	}
	
	

}
