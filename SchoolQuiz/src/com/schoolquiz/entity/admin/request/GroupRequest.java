package com.schoolquiz.entity.admin.request;

public class GroupRequest {
	
	private String userSession;
	private int numberFrom;
	private int numberOfItems;
	
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public int getNumberFrom() {
		return numberFrom;
	}
	public void setNumberFrom(int numberFrom) {
		this.numberFrom = numberFrom;
	}
	public int getNumberOfItems() {
		return numberOfItems;
	}
	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}
	
	@Override
	public String toString(){
		return "userSession - "+userSession+"; numberFrom - "+numberFrom+"; itemsNumber - "+numberOfItems;
	}

}
