package com.schoolquiz.entity.controllerparams;

public class QuestionForGroup {
	
	private String userSession;
	private Long questionGroup;
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public Long getQuestionGroup() {
		return questionGroup;
	}
	public void setQuestionGroup(Long questionGroup) {
		this.questionGroup = questionGroup;
	}
	
	@Override
	public String toString(){
		return "userSession - "+userSession+"; questionGroup - "+questionGroup;
	}

}
