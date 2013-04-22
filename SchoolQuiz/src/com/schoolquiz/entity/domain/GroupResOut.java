package com.schoolquiz.entity.domain;

import java.util.List;

public class GroupResOut {
	
	private String groupName;
	private Long groupId;
	private List<QuestionRes> questionResList;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<QuestionRes> getQuestionResList() {
		return questionResList;
	}
	public void setQuestionResList(List<QuestionRes> questionResList) {
		this.questionResList = questionResList;
	}
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	@Override
	public String toString(){
		return "groupId - "+groupId+"; groupName - "+groupName+"; QuestionResList - "+questionResList;
	}

}
