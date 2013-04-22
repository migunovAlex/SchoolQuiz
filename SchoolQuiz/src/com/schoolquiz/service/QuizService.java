package com.schoolquiz.service;

import java.util.List;

import com.schoolquiz.entity.decorated.CheckUserAnswerSummary;
import com.schoolquiz.entity.decorated.QuestionGroupListSummary;
import com.schoolquiz.entity.decorated.QuestionOutListSummary;
import com.schoolquiz.entity.decorated.QuestionSummary;
import com.schoolquiz.entity.decorated.UserAnswerSummary;
import com.schoolquiz.entity.decorated.UserResultSummary;
import com.schoolquiz.entity.domain.QuestionOutSummary;
import com.schoolquiz.entity.domain.QuizOutResultSummary;
import com.schoolquiz.entity.domain.UserResultJsonOutSummary;

public interface QuizService {
	
	public UserResultSummary createNewUserResult(String userName, String userPcIp);
	
	public CheckUserAnswerSummary checkUserAnswer(String activeSession, long questionId, List<Long> answersId);
	
	public UserAnswerSummary saveUserAnswer(long userResultId, long questionId, long answerId);
	
	public QuestionSummary getRandomQuestion(String activeSession);
	
	public UserResultJsonOutSummary finishQuiz(String activeSession);
	
	public QuestionGroupListSummary getQuestionGroupList(String activeSession);
	
	public QuestionOutListSummary getQuestionsForGroup(String activeSession, long groupQuestionsId);
	
	public QuestionOutSummary getQuestion(String activeSession, Long questionId);
	
	public QuizOutResultSummary getQuizResult(String finishedSession);

}
