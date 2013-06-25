package com.schoolquiz.persistence;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;
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
import com.schoolquiz.entity.admin.response.AnswerEntity;
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
		try{
			Hibernate.initialize(question);
			Hibernate.initialize(question.getQuestionGroup());
		}catch(Exception e){
			return null;
		}
//		if(question!=null)	Hibernate.initialize(question.getQuestionAnswerList());
//		System.out.println("question-answer - "+question.getQuestionAnswerList());
		return question;
	}
	

	@Override
	public Answer getAnswer(long answerId) {
		Answer answer = (Answer) currentSession().load(Answer.class, answerId);
		try{
			Hibernate.initialize(answer);
		} catch(Exception e){
			return null;
		}
		currentSession().flush();
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
	public List<AnswerEntity> getAnswersForQuestion(Question question) {
		List<AnswerEntity> answerList = new LinkedList<>();
		List<QuestionAnswer> questionAnswerList = currentSession().createCriteria(QuestionAnswer.class).add(Restrictions.eq("question", question)).list();
		System.out.println("QuestionAnswer List - "+questionAnswerList);
		for(QuestionAnswer questionAnswer:questionAnswerList){
				
			Hibernate.initialize(questionAnswer.getAnswer());
			AnswerEntity answerEntity = new AnswerEntity();
			answerEntity.setId(questionAnswer.getAnswer().getId());
			answerEntity.setAnswerText(questionAnswer.getAnswer().getAnswerText());
			answerEntity.setEnabled(questionAnswer.getAnswer().getEnabled());
			answerEntity.setRight(questionAnswer.getAnswer().getEnabled());
			answerEntity.setRight(questionAnswer.isRight());
			System.out.println(questionAnswer.getAnswer());
			answerList.add(answerEntity);
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
		
		if(questionAnswerList!=null){
			try{
				for(QuestionAnswer questionAnswer:questionAnswerList){
					Hibernate.initialize(questionAnswer);
					Hibernate.initialize(questionAnswer.getAnswer());
					Hibernate.initialize(questionAnswer.getQuestion());
				}
			}catch(Exception e){
				return null;
			}
		}
		
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
		List<QuestionGroup> questionGroups = currentSession().createCriteria(QuestionGroup.class).list();
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
		try{
			Hibernate.initialize(resultGroup);
		} catch(Exception e){
			return null;
		}
		return resultGroup;
	}

	@Override
	public Question addQuestion(Question addedQuestion) {
		Long savedId=null;
		try{
			savedId = (Long) currentSession().save(addedQuestion);
		}catch(HibernateException ex){
			return null;
		}
		addedQuestion.setId(savedId);
		
		return addedQuestion;
	}

	@Override
	public Question updateQuestion(Question questionToEdit) {
		try{
			currentSession().update(questionToEdit);
		}catch(HibernateException ex){
			return null;
		}
		return questionToEdit;
	}

	@Override
	public Question deleteQuestion(Question deletedQuestion) {
		try{
			deletedQuestion.setDeleted(true);
			updateQuestion(deletedQuestion);
		}catch(HibernateException ex){
			return null;
		}
		return deletedQuestion;
	}

	@Override
	public Answer saveAnswer(Answer answerToAdd) {
		try{
			Long id = (Long) currentSession().save(answerToAdd);
			answerToAdd.setId(id);
		}catch(HibernateException ex){
			return null;
		}
		
		return answerToAdd;
	}

	@Override
	public Answer updateAnswer(Answer answerToEdit) {
		try{
			currentSession().update(answerToEdit);
		}catch(HibernateException ex){
			return null;
		}
		
		return answerToEdit;
	}

	@Override
	public List<Answer> searchAnswers(String keyWord) {
		List<Answer> answerList = null;
		if(keyWord==null){
			keyWord = "";
		}
		answerList = currentSession().createCriteria(Answer.class).add(Restrictions.ilike("answerText", keyWord+"%")).list();
		
		return answerList;
	}

	@Override
	public Question getQuestionWithAnswers(Long questionId) {
		Question resultQuestion = (Question) currentSession().load(Question.class, questionId);
		try{
			Hibernate.initialize(resultQuestion);
			List<QuestionAnswer> questionAnswerList = getQuestionAnswerList(resultQuestion);
			for(QuestionAnswer questionAnswer:questionAnswerList){
				Hibernate.initialize(questionAnswer);
				Hibernate.initialize(questionAnswer.getAnswer());
			}
		}catch(Exception ex){
			return null;
		}
		
		return resultQuestion;
	}

	@Override
	public QuestionAnswer addQuestionAnswer(QuestionAnswer questionAnswer) {
		try{
			Long id = (Long) currentSession().save(questionAnswer);
			questionAnswer.setId(id);
		}catch(Exception e){
			return null;
		}
		return questionAnswer;
	}

	@Override
	public QuestionAnswer deleteQuestionAnswer(QuestionAnswer questionAnswer) {
		try{
			currentSession().delete(questionAnswer);
		}catch(Exception e){
			System.out.println("Exception - "+e.getMessage());
			return null;
		}
		return questionAnswer;
	}

	@Override
	public QuestionAnswer getQuestionAnswer(long l) {
		QuestionAnswer questionAnswer = (QuestionAnswer) currentSession().load(QuestionAnswer.class,  l);
		try{
			Hibernate.initialize(questionAnswer);
			Hibernate.initialize(questionAnswer.getAnswer());
			Hibernate.initialize(questionAnswer.getQuestion());
		}catch(Exception e){
			return questionAnswer;
		}
		return questionAnswer;
	}

	@Override
	public QuestionAnswer updateQuestionAnswer(QuestionAnswer questionAnswerToEdit) {
		try{
			currentSession().update(questionAnswerToEdit);
		}catch(HibernateException ex){
			return null;
		}
		
		return questionAnswerToEdit;
	}

	@Override
	public List<UserResult> getUserResultsByDate(Date selectedDate) {
		Date finishedDate = new Date(selectedDate.getTime());
		selectedDate.setHours(0);
		selectedDate.setMinutes(0);
		selectedDate.setSeconds(0);
		finishedDate.setHours(23);
		finishedDate.setMinutes(59);
		finishedDate.setSeconds(59);
		List<UserResult> resultList = null;
		
		resultList = currentSession().createCriteria(UserResult.class).add(Restrictions.between("startTime", selectedDate.getTime(), finishedDate.getTime())).addOrder(Order.asc("compIp")).list();
		
		return resultList;
	}


}
