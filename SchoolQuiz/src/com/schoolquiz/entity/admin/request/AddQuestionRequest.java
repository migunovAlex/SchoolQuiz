package com.schoolquiz.entity.admin.request;

public class AddQuestionRequest {
	
	private String userSession;
	private String questionText;
	private Integer responseType;
	private Long questionGroup;
	private Long questionParentId;
	private Boolean enabled;
	
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public Integer getResponseType() {
		return responseType;
	}
	public void setResponseType(Integer responseType) {
		this.responseType = responseType;
	}
	public Long getQuestionGroup() {
		return questionGroup;
	}
	public void setQuestionGroup(Long questionGroup) {
		this.questionGroup = questionGroup;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Long getQuestionParentId() {
		return questionParentId;
	}
	public void setQuestionParentId(Long questionParentId) {
		this.questionParentId = questionParentId;
	}
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	
	

}
