package com.schoolquiz.persistence;

import java.util.List;

import com.schoolquiz.entity.Answer;
import com.schoolquiz.entity.Question;
import com.schoolquiz.entity.QuestionAnswer;
import com.schoolquiz.entity.QuestionGroup;
import com.schoolquiz.entity.UserAnswer;
import com.schoolquiz.entity.UserResult;
import com.schoolquiz.entity.admin.response.AnswerEntity;
import com.schoolquiz.entity.decorated.QuestionSummary;

public interface QuizDAO {
	
	public UserResult createNewUserResult(String userName, String userPcIp);
	
	public boolean checkUserAnswer(long answerId);
	
	public UserAnswer saveUserAnswer(UserAnswer userAnswer);
	
	public Question getRandomQuestion();
	
	public UserResult getUserResult(long userResultId);
	
	public Question getQuestion(long questionId);
	
	public Answer getAnswer(long answerId);

	UserResult getUserResultBySession(String session);

	public List<UserAnswer> getUserAnswersByUserResult(UserResult userRes);

	public Question getRandomQuestion(List<Long> questionsAnswered);
	
	public List<QuestionGroup> getQuestionGroupList();
	
	public List<QuestionGroup> getQuestionGroups(int currentItem, int itemsNumber);
	
	public long getGroupNumbers();
	
	public List<Question> getQuestionsForGroup(long groupId);
	
	public List<AnswerEntity> getAnswersForQuestion(Question question);
	
	public List<Question> getSubQuestions(Question question);
	
	public UserResult updateUserResult(UserResult userResult);
	
	public List<QuestionAnswer> getQuestionAnswerList(Question question);
	
	public List<UserResult> getActiveUserResults();
	
	public QuestionAnswer getQuestionAnswer(Question question, Answer answer);
	
	public QuestionGroup getQuestionGroup(long groupId);

	public Question addQuestion(Question addedQuestion);

	public Question updateQuestion(Question questionToEdit);

	public Question deleteQuestion(Question deletedQuestion);
	
//	public List<UserAnswer> getUserAnswersForQuestion();

}
