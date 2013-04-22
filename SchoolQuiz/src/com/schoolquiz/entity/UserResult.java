package com.schoolquiz.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="userresult")
public class UserResult {
	
	public static int QUIZ_STARTED = 1;
	public static int QUIZ_FINISHED = 2;
	public static int QUIZ_TIMEOUT = 3;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private long id;
	
	@OneToMany(cascade={CascadeType.REFRESH}, fetch=FetchType.LAZY, mappedBy="userResult")
	private List<UserAnswer> userAnswers;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="COMP_IP")
	private String compIp;
	
	@Column(name="START_TIME")
	private long startTime;
	
	@Column(name="END_TIME")
	private long endTime;
	
	@Column(name="RESULT")
	private int result;
	
	@Column(name="SESSION_ID")
	private String sessionId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompIp() {
		return compIp;
	}

	public void setCompIp(String compIp) {
		this.compIp = compIp;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public List<UserAnswer> getUserAnswers() {
		return userAnswers;
	}

	public void setUserAnswers(List<UserAnswer> userAnswers) {
		this.userAnswers = userAnswers;
	}

	@Override
	public String toString(){
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append("id - "+id);
		strBuilder.append("; userName - "+userName);
		strBuilder.append("; userSession - "+sessionId);
		strBuilder.append("; compIp - "+compIp);
		if(startTime!=0){
			strBuilder.append("; startDate - "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(startTime)));
		}
		if(endTime!=0){
			strBuilder.append("; endDate - "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime)));
		}
		
		strBuilder.append("; result - "+result);

		
		return strBuilder.toString();
	}
	
	

}
