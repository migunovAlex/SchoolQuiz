package com.schoolquiz.entity.admin.request;

import java.util.List;

public class AddAnswersToQuestionRequest {
	
	private String userSession;
	private Long questionId;
	private List<Long> answerIds;
	private List<Boolean> rightAnswers;
	
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
	public List<Long> getAnswerIds() {
		return answerIds;
	}
	public void setAnswerIds(List<Long> answerIds) {
		this.answerIds = answerIds;
	}
	public List<Boolean> getRightAnswers() {
		return rightAnswers;
	}
	public void setRightAnswers(List<Boolean> rightAnswers) {
		this.rightAnswers = rightAnswers;
	}
	
	

}
