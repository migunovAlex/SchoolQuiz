package com.schoolquiz.entity.controllerparams;

public class SessionObject {
	
	private String userSession;

	public String getUserSession() {
		return userSession;
	}

	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	
	@Override
	public String toString(){
		return "userSession - "+userSession;
	}

}
