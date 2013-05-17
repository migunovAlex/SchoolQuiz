package com.schoolquiz.entity.admin.request;

public class EditQuestionRequest {
	
	private String userSession;
	private Long questionId;
	private String questionText;
	private Long responseType;
	private Long questionGroup;
	private Long questionParentId;
	private Boolean enabled;
	
	
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
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public Long getResponseType() {
		return responseType;
	}
	public void setResponseType(Long responseType) {
		this.responseType = responseType;
	}
	public Long getQuestionGroup() {
		return questionGroup;
	}
	public void setQuestionGroup(Long questionGroup) {
		this.questionGroup = questionGroup;
	}
	public Long getQuestionParentId() {
		return questionParentId;
	}
	public void setQuestionParentId(Long questionParentId) {
		this.questionParentId = questionParentId;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	

}
