package com.schoolquiz.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="useranswers")
public class UserAnswer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	
	@ManyToOne(cascade={CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@JoinColumn(name="USER_RESULT_ID")
	private UserResult userResult;
	
	@ManyToOne(cascade={CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@JoinColumn(name="QUESTION_ID")
	private Question question;
	
	@ManyToOne(cascade={CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@JoinColumn(name="ANSWER_ID")
	private Answer answer;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserResult getUserResult() {
		return userResult;
	}

	public void setUserResult(UserResult userResult) {
		this.userResult = userResult;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	
	
	@Override
	public String toString(){
		return "UserAnswerId - "+id;
	}
	

}
