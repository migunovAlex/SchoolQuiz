package com.schoolquiz.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.schoolquiz.entity.Answer;
import com.schoolquiz.entity.Question;
import com.schoolquiz.entity.QuestionAnswer;
import com.schoolquiz.entity.QuestionGroup;
import com.schoolquiz.entity.UserAnswer;
import com.schoolquiz.entity.UserResult;
import com.schoolquiz.entity.decorated.QuestionSummary;
import com.schoolquiz.utils.SessionGenerator;

@Transactional
public class QuizDAOImpl implements QuizDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	
	private static List<Question> activeQuestions;
	
	private Session currentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	private void loadQuestions(){
//		activeQuestions = new ArrayList<Question>();
		activeQuestions = currentSession().createCriteria(Question.class).add(Restrictions.like("questionText", "%")).list();
		for(Question question:activeQuestions){
//			Hibernate.initialize(question.getAnswerList());
		}
	}
	
	@Override
	public UserResult createNewUserResult(String userName, String userPcIp) {
		UserResult userResult=null;
		userResult = new UserResult();
		userResult.setUserName(userName);
		userResult.setCompIp(userPcIp);
		userResult.setStartTime(new Date().getTime());
		
		UserResult foundUserResult = null;
		String generatedSession = null;
		for(;;){
			generatedSession = SessionGenerator.getNewSessionValue();
			System.out.println("Generated session - "+generatedSession);
			foundUserResult = getUserResultBySession(generatedSession);
			if(foundUserResult==null) break;
		}
		
		userResult.setSessionId(generatedSession);
		userResult.setResult(UserResult.QUIZ_STARTED);
		
		long resId = (Long) currentSession().save(userResult);
		
		userResult.setId(resId);
		
		return userResult;
	}
	
	@Override
	public UserResult getUserResultBySession(String session){
		UserResult result=null;
		List<UserResult> userResults = currentSession().createCriteria(UserResult.class).add(Restrictions.like("sessionId", session)).list();
		if(userResults.size()>0){
			result=userResults.get(0);
		}
		return result;
		
	}
	

	@Override
	public boolean checkUserAnswer(long answerId) {
		
		Answer answer = (Answer) currentSession().load(Answer.class, answerId);
		if(answer==null) return false;
		return true;
	}

	@Override
	public UserAnswer saveUserAnswer(UserAnswer userAnswer) {
				
		long id = (Long) currentSession().save(userAnswer);
		
		userAnswer.setId(id);
		
		return userAnswer;
	}

	@Override
	public Question getRandomQuestion() {
		Question resultQuestion = null;
		if(activeQuestions==null) loadQuestions();
		Random rand = new Random();
		resultQuestion = activeQuestions.get(rand.nextInt(activeQuestions.size()));
		
		
//		Object result = currentSession().createCriteria(Question.class).setProjection(Projections.rowCount()).uniqueResult();
//		Integer count = Integer.parseInt(result.toString());
//		System.out.println("Number of the questions in DB - "+count);
//		Random rand = new Random();
//		int recordToLoad = rand.nextInt(count+1);
//		if(recordToLoad==0) recordToLoad++;
//		long idToLoad = Long.parseLong(recordToLoad+"");
//		System.out.println("Load item number - "+idToLoad);
//		Question resultQuestion = (Question) currentSession().load(Question.class, idToLoad);
//		if(resultQuestion!=null) Hibernate.initialize(resultQuestion.getAnswerList());
		return resultQuestion;
	}

	@Override
	public UserResult getUserResult(long userResultId) {
		UserResult userResult = (UserResult) currentSession().load(UserResult.class, userResultId);
		return userResult;
	}

	@Override
	public Question getQuestion(long questionId) {
		Question question = (Question) currentSession().load(Question.class, questionId);
		if(question!=null){
			Hibernate.initialize(question);
			Hibernate.initialize(question.getQuestionGroup());
		}
//		if(question!=null)	Hibernate.initialize(question.getQuestionAnswerList());
//		System.out.println("question-answer - "+question.getQuestionAnswerList());
		return question;
	}

	@Override
	public Answer getAnswer(long answerId) {
		Answer answer = (Answer) currentSession().load(Answer.class, answerId);
		return answer;
	}

	@Override
	public List<UserAnswer> getUserAnswersByUserResult(UserResult userRes) {
		
		List<UserAnswer> userAnswers = currentSession().createCriteria(UserAnswer.class).add(Restrictions.like("userResult", userRes)).list();
		for(UserAnswer userAnswer:userAnswers){
			Hibernate.initialize(userAnswer.getQuestion());
			Hibernate.initialize(userAnswer.getAnswer());
			Hibernate.initialize(userAnswer.getQuestion().getQuestionGroup());
		}
		
		
		return userAnswers;
	}

	@Override
	public Question getRandomQuestion(List<Long> questionsAnswered) {
		if(activeQuestions==null) loadQuestions();
		Random rand = new Random();
		Question resultQuestion = null;
		int i=0;
		if(questionsAnswered.size()==activeQuestions.size()) return null;
		for(;;){
			int ql = rand.nextInt(activeQuestions.size());
			Long questionToLoad = Long.parseLong(ql+"");
			System.out.println("Contains - "+questionsAnswered.contains(questionToLoad));
			if(!questionsAnswered.contains(questionToLoad)){
				resultQuestion = activeQuestions.get(ql);
				break;
			}
		}
		
		return resultQuestion;
	}

	@Override
	public List<QuestionGroup> getQuestionGroupList() {
		
		List<QuestionGroup> questionGroups = currentSession().createCriteria(QuestionGroup.class).add(Restrictions.like("groupName", "%")).list();
		return questionGroups;
	}

	@Override
	public List<Question> getQuestionsForGroup(long groupId) {
		QuestionGroup questionGroup = (QuestionGroup) currentSession().load(QuestionGroup.class, groupId);
		if(questionGroup==null) return null;
		Hibernate.initialize(questionGroup.getQuestionList());
		
		
		return questionGroup.getQuestionList();
	}

	@Override
	public List<Answer> getAnswersForQuestion(Question question) {
		List<Answer> answerList = new LinkedList<Answer>();
		List<QuestionAnswer> questionAnswerList = currentSession().createCriteria(QuestionAnswer.class).add(Restrictions.eq("question", question)).list();
		System.out.println("QuestionAnswer List - "+questionAnswerList);
		for(QuestionAnswer questionAnswer:questionAnswerList){
			Hibernate.initialize(questionAnswer.getAnswer());
			System.out.println(questionAnswer.getAnswer());
			answerList.add(questionAnswer.getAnswer());
		}
		
		
		return answerList;
	}

	@Override
	public List<Question> getSubQuestions(Question question) {
		List<Question> subQuestions = currentSession().createCriteria(Question.class).add(Restrictions.eq("parentId", question.getId())).list();
		return subQuestions;
	}

	@Override
	public UserResult updateUserResult(UserResult userResult) {
		UserResult updatedUserResult = null;
		try{
			currentSession().update(userResult);
			updatedUserResult = userResult;
		}catch(HibernateException e){
			e.printStackTrace();
		}
		
		
		return updatedUserResult;
	}

	@Override
	public List<QuestionAnswer> getQuestionAnswerList(Question question) {
		
		List<QuestionAnswer> questionAnswerList = currentSession().createCriteria(QuestionAnswer.class).add(Restrictions.eq("question", question)).list();
		
		return questionAnswerList;
	}

	@Override
	public List<UserResult> getActiveUserResults() {
		List<UserResult> activeResults = currentSession().createCriteria(UserResult.class).add(Restrictions.eq("result", UserResult.QUIZ_STARTED)).list();
		return activeResults;
	}

	@Override
	public QuestionAnswer getQuestionAnswer(Question question, Answer answer) {
		QuestionAnswer result = null;
		List<QuestionAnswer> questionAnswers = currentSession().createCriteria(QuestionAnswer.class).add(Restrictions.eq("question", question)).add(Restrictions.eq("answer", answer)).list();
		if(questionAnswers!=null){
			if(questionAnswers.size()>0){
				result = questionAnswers.get(0);
			}
		}
		return result;
	}

	@Override
	public List<QuestionGroup> getQuestionGroups(int currentItem, int itemsNumber) {
		List<QuestionGroup> questionGroups = currentSession().createCriteria(QuestionGroup.class).setFirstResult(currentItem).setMaxResults(itemsNumber).list();
		return questionGroups;
	}
	
	@Override
	public long getGroupNumbers(){
		long numberOfItems = (long) currentSession().createCriteria(QuestionGroup.class).setProjection(Projections.countDistinct("id")).uniqueResult();
		return numberOfItems;
	}

	@Override
	public QuestionGroup getQuestionGroup(long groupId) {
		QuestionGroup resultGroup = (QuestionGroup) currentSession().load(QuestionGroup.class, groupId);
		return resultGroup;
	}


}
