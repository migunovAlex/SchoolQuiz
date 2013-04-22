package com.schoolquiz.entity.decorated;

import java.util.List;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.QuestionGroup;

public class QuestionGroupListSummary {
	
	private ErrorData errorData;
	private List<QuestionGroup> questionGroupList;
	
	public QuestionGroupListSummary(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public List<QuestionGroup> getQuestionGroupList() {
		return questionGroupList;
	}
	public void setQuestionGroupList(List<QuestionGroup> questionGroupList) {
		this.questionGroupList = questionGroupList;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData + "; questionGroupList - "+questionGroupList;
	}
	

}
