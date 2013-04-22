package com.schoolquiz.entity.domain;


public class QuestionOut {
	
	private long id;
	
	private int responseType;
	
	private String questionText;
	
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public int getResponseType() {
		return responseType;
	}
	public void setResponseType(int responseType) {
		this.responseType = responseType;
	}
	
	@Override
	public String toString(){
		return "id - "+id+"; responseType - "+responseType+"; questionText - "+questionText;
	}
	

}
