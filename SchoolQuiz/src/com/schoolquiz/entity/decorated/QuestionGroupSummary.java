package com.schoolquiz.entity.decorated;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.QuestionGroup;

public class QuestionGroupSummary {
	
	private ErrorData errorData;
	private QuestionGroup questionGroup;
	
	public QuestionGroupSummary(){
		errorData = new ErrorData();
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	public QuestionGroup getGuestionGroup() {
		return questionGroup;
	}

	public void setGuestionGroup(QuestionGroup questionGroup) {
		this.questionGroup = questionGroup;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; questionGroup - "+questionGroup;
	}

}
