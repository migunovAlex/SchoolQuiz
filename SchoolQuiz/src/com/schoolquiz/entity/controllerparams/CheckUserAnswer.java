package com.schoolquiz.entity.controllerparams;

import java.util.List;

public class CheckUserAnswer {
	
	private String userSession;
	private Long questionId;
	private List<Long> answerIds;
	
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
	
	@Override
	public String toString(){
		return "userSession - "+userSession+"; questionId - "+questionId+"; answerIds - "+answerIds;
	}

}
