package com.schoolquiz.entity.admin.response;

public class UserResultsEntity {
	
	private String dateOfAnswer;
	private String timeOfAnswer;
	private String computerIP;
	private String Answerer;
	private String questionAnswer;
	private String userAnswer;
	private Boolean right;
	
	public String getDateOfAnswer() {
		return dateOfAnswer;
	}
	public void setDateOfAnswer(String dateOfAnswer) {
		this.dateOfAnswer = dateOfAnswer;
	}
	public String getTimeOfAnswer() {
		return timeOfAnswer;
	}
	public void setTimeOfAnswer(String timeOfAnswer) {
		this.timeOfAnswer = timeOfAnswer;
	}
	public String getComputerIP() {
		return computerIP;
	}
	public void setComputerIP(String computerIP) {
		this.computerIP = computerIP;
	}
	public String getAnswerer() {
		return Answerer;
	}
	public void setAnswerer(String answerer) {
		Answerer = answerer;
	}
	public String getQuestionAnswer() {
		return questionAnswer;
	}
	public void setQuestionAnswer(String questionAnswer) {
		this.questionAnswer = questionAnswer;
	}
	public String getUserAnswer() {
		return userAnswer;
	}
	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}
	public Boolean getRight() {
		return right;
	}
	public void setRight(Boolean right) {
		this.right = right;
	}
	
	

}
