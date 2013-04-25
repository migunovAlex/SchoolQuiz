package com.schoolquiz.entity.admin.response;

import java.util.List;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.admin.GroupForAdmin;

public class GetQuestionsForGroupResponse {
	
	private ErrorData errorData;
	private List<QuestionForAdmin> questionGroups;
	
	public GetQuestionsForGroupResponse(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public List<QuestionForAdmin> getQuestionGroups() {
		return questionGroups;
	}
	public void setQuestionGroups(List<QuestionForAdmin> questions) {
		this.questionGroups = questions;
	}
	
	

}
