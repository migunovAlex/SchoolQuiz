package com.schoolquiz.entity;

public class QuestionGroupOut {
	
	private long questionGroupId;
	private String questionGroupName;
	
	
	public long getQuestionGroupId() {
		return questionGroupId;
	}
	public void setQuestionGroupId(long questionGroupId) {
		this.questionGroupId = questionGroupId;
	}
	public String getQuestionGroupName() {
		return questionGroupName;
	}
	public void setQuestionGroupName(String questionGroupName) {
		this.questionGroupName = questionGroupName;
	}
	
	@Override
	public String toString(){
		return "id - "+questionGroupId+"; groupName - "+questionGroupName;
	}

}
