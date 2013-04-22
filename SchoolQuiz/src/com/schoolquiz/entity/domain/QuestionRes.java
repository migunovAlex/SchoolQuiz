package com.schoolquiz.entity.domain;

import java.util.List;

import com.schoolquiz.entity.Answer;

public class QuestionRes {
	
	private String questionText;
	private Long questionId;
	private List<AnswerOut> answerList;
	private boolean right;
	private List<QuestionRes> subquestions;
	
	
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public List<AnswerOut> getAnswerList() {
		return answerList;
	}
	public void setAnswerList(List<AnswerOut> answerList) {
		this.answerList = answerList;
	}
	public boolean isRight() {
		return right;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
	public List<QuestionRes> getSubquestions() {
		return subquestions;
	}
	public void setSubquestions(List<QuestionRes> subquestions) {
		this.subquestions = subquestions;
	}
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	
	
	

}
