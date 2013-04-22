package com.schoolquiz.entity.domain;

import java.util.List;

import com.schoolquiz.entity.ErrorData;

public class QuestionOutSummary {
	
	private ErrorData errorData;
	private QuestionOut question;
	private List<SubQuestionOut> subQuestions;
	private List<AnswerOut> answerList;
	
	public QuestionOutSummary(){
		errorData = new ErrorData();
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	public QuestionOut getQuestion() {
		return question;
	}

	public void setQuestion(QuestionOut question) {
		this.question = question;
	}

	public List<SubQuestionOut> getSubQuestions() {
		return subQuestions;
	}

	public void setSubQuestions(List<SubQuestionOut> subQuestions) {
		this.subQuestions = subQuestions;
	}

	public List<AnswerOut> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<AnswerOut> answerList) {
		this.answerList = answerList;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; question - "+question+"; subQuestions - "+subQuestions+"; answerList - "+answerList;
	}
	
	

}
