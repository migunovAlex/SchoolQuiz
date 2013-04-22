package com.schoolquiz.entity.domain;

import java.util.List;

import com.schoolquiz.entity.ErrorData;

public class QuizOutResultSummary {
	
	private ErrorData errorData;
	private List<GroupResOut> groupResOutList;
	

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	public List<GroupResOut> getGroupResOutList() {
		return groupResOutList;
	}

	public void setGroupResOutList(List<GroupResOut> groupResOutList) {
		this.groupResOutList = groupResOutList;
	}
	
	
	

}
