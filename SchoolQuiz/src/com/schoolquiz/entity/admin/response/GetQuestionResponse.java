package com.schoolquiz.entity.admin.response;

import com.schoolquiz.entity.ErrorData;

public class GetQuestionResponse {
	
	private ErrorData errorData;
	private Long id;
	private Long questionGroupId;
	private String questionText;
	private int responseType;
	private Long parentQuestionId;
	private Boolean enabled;
	
	public GetQuestionResponse(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getQuestionGroupId() {
		return questionGroupId;
	}
	public void setQuestionGroupId(Long questionGroupId) {
		this.questionGroupId = questionGroupId;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public int getResponseType() {
		return responseType;
	}
	public void setResponseType(int responseType) {
		this.responseType = responseType;
	}
	public Long getParentQuestionId() {
		return parentQuestionId;
	}
	public void setParentQuestionId(Long parentQuestionId) {
		this.parentQuestionId = parentQuestionId;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	

}
