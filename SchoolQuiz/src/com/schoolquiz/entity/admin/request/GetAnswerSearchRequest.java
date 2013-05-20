package com.schoolquiz.entity.admin.request;

public class GetAnswerSearchRequest {
	
	private String userSession;
	private String keyWord;
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	

}
