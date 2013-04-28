package com.schoolquiz.entity.admin.response;

public class AnswerEntity {
	
	private Long id;
	private String answerText;
	private Boolean enabled;
	private Boolean right;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Boolean getRight() {
		return right;
	}
	public void setRight(Boolean right) {
		this.right = right;
	}
	
	

}
