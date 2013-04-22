package com.schoolquiz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.schoolquiz.entity.Answer;
import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.Question;
import com.schoolquiz.entity.QuestionAnswer;
import com.schoolquiz.entity.QuestionGroup;
import com.schoolquiz.entity.UserAnswer;
import com.schoolquiz.entity.UserResult;
import com.schoolquiz.entity.decorated.CheckUserAnswerSummary;
import com.schoolquiz.entity.decorated.QuestionGroupListSummary;
import com.schoolquiz.entity.decorated.QuestionOutListSummary;
import com.schoolquiz.entity.decorated.QuestionSummary;
import com.schoolquiz.entity.decorated.UserAnswerSummary;
import com.schoolquiz.entity.decorated.UserResultSummary;
import com.schoolquiz.entity.domain.AnswerOut;
import com.schoolquiz.entity.domain.GroupResOut;
import com.schoolquiz.entity.domain.QuestionOut;
import com.schoolquiz.entity.domain.QuestionOutSummary;
import com.schoolquiz.entity.domain.QuestionRes;
import com.schoolquiz.entity.domain.QuizOutResultSummary;
import com.schoolquiz.entity.domain.SubQuestionOut;
import com.schoolquiz.entity.domain.UserResultJsonOut;
import com.schoolquiz.entity.domain.UserResultJsonOutSummary;
import com.schoolquiz.persistence.QuizDAO;

public class QuizServiceImpl implements QuizService{
	
	private static long sleepTime =1000*60;
	private static long sessionLostActivity = 1000*60*60;
	
	private SessionThread sessionRunnable;
	private Thread sessionThread;

	@Autowired
	private QuizDAO quizDao;
	
	private QuizServiceImpl(){
		sessionRunnable = new SessionThread();
		sessionThread = new Thread(sessionRunnable);
		sessionThread.start();
	}
	
	@Override
	public UserResultSummary createNewUserResult(String userName, String userPcIp) {
		UserResultSummary userResSummary = new UserResultSummary();
		if(userName==null || userPcIp==null){
			userResSummary.getErrorData().setErrorCode(ErrorData.NO_ENOUGH_PARAMS);
			userResSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NOT_ENOUGH_PARAMS);
			return userResSummary;
		}
		
		UserResult userResult = quizDao.createNewUserResult(userName, userPcIp);
		userResSummary.setUserResult(userResult);
		
		return userResSummary;
	}

	@Override
	public CheckUserAnswerSummary checkUserAnswer(String activeSession, long questionId, List<Long> answersId) {
		CheckUserAnswerSummary userAnswerSummary = new CheckUserAnswerSummary();
		
		userAnswerSummary.setErrorData(checkUserSession(activeSession));
		if(userAnswerSummary.getErrorData().getErrorCode()!=ErrorData.CODE_OK) return userAnswerSummary;
		
		if(answersId==null||questionId==0){
			userAnswerSummary.getErrorData().setErrorCode(ErrorData.NO_ENOUGH_PARAMS);
			userAnswerSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NOT_ENOUGH_PARAMS);
			return userAnswerSummary;
		}
		
		UserResult userResult = quizDao.getUserResultBySession(activeSession);
		Question question = quizDao.getQuestion(questionId);
		
		for(Long answer:answersId){
			UserAnswer userAnswer = new UserAnswer();
			userAnswer.setUserResult(userResult);
			userAnswer.setQuestion(question);
			Answer ans = quizDao.getAnswer(answer);
			if(ans==null) continue;
			userAnswer.setAnswer(ans);
			quizDao.saveUserAnswer(userAnswer);
		}
		
		return checkRes(activeSession, questionId, answersId);
	}
	
	private CheckUserAnswerSummary checkRes(String userSession, long questionId, List<Long> answersId){
		CheckUserAnswerSummary userAnswerSummary = new CheckUserAnswerSummary();
		Question question = quizDao.getQuestion(questionId);
		if(question==null){
			userAnswerSummary.getErrorData().setErrorCode(ErrorData.WRONG_PARAMS);
			userAnswerSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_WRONG_PARAMS);
			return userAnswerSummary;
		}
		
		List<QuestionAnswer> questionAnswerList = quizDao.getQuestionAnswerList(question);
		if(questionAnswerList==null){
			userAnswerSummary.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			userAnswerSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return userAnswerSummary;
		}
		
		int rightAnswersCount=0;
		for(QuestionAnswer questionAnswer:questionAnswerList){
			if(questionAnswer.isRight()==true) rightAnswersCount++;
		}
		System.out.println("Right count answers in DB - "+rightAnswersCount);
		System.out.println("Count answers recieved - "+answersId.size());
		
		if(rightAnswersCount!=answersId.size()){
			userAnswerSummary.setResult(false);
			return userAnswerSummary;
		}
		
		for(QuestionAnswer questionAnswer:questionAnswerList){
			if(questionAnswer.isRight()==true){
				if(!answersId.contains(questionAnswer.getAnswer().getId())){
					userAnswerSummary.setResult(false);
					return userAnswerSummary;
				}
			}
		}
		
		userAnswerSummary.setResult(true);
		
		System.out.println("Load answerList for question "+questionId+"; - "+questionAnswerList);
		
		return userAnswerSummary;
	}

	@Override
	public UserAnswerSummary saveUserAnswer(long userResultId, long questionId,	long answerId) {
		UserAnswerSummary userAnswerSummary = new UserAnswerSummary();
		if(userResultId<1 || questionId<1 || answerId<1){
			userAnswerSummary.getErrorData().setErrorCode(ErrorData.WRONG_PARAMS);
			userAnswerSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_WRONG_PARAMS);
			return userAnswerSummary;
		}
		UserResult userResult = quizDao.getUserResult(userResultId);
		Question question = quizDao.getQuestion(questionId);
		Answer answer = quizDao.getAnswer(answerId);
		
		if(userResult==null || question==null || answer==null){
			userAnswerSummary.getErrorData().setErrorCode(ErrorData.WRONG_PARAMS);
			userAnswerSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_WRONG_PARAMS);
			return userAnswerSummary;
		}
		
//		UserAnswer userAnswer = quizDao.saveUserAnswer(userResult, question, answer);
//		userAnswerSummary.setUserAnswer(userAnswer);
		
		
		return userAnswerSummary;
	}

	@Override
	public QuestionSummary getRandomQuestion(String activeSession) {
		QuestionSummary questionSummary = new QuestionSummary();
		
		UserResult userRes = quizDao.getUserResultBySession(activeSession);
		if(userRes==null){
			questionSummary.getErrorData().setErrorCode(ErrorData.WRONG_SESSION);
			questionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_WRONG_SESSION);
			return questionSummary;
		}
		
		System.out.println("User result for session - "+activeSession+" = "+userRes.getResult());
		
		if(userRes.getResult()!=UserResult.QUIZ_STARTED){
			questionSummary.getErrorData().setErrorCode(ErrorData.INACTIVE_SESSION);
			questionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_INACTIVE_SESSION);
			return questionSummary;
		}
		
		List<UserAnswer> userAnswers = quizDao.getUserAnswersByUserResult(userRes);
		if(userAnswers==null){		
			questionSummary.setQuestion(quizDao.getRandomQuestion());
			return questionSummary;
		}
		if(userAnswers.size()==0){
			questionSummary.setQuestion(quizDao.getRandomQuestion());
			return questionSummary;
		}
		
		List<Long> questionsAnswered = new LinkedList<Long>();
		for(UserAnswer userAnswer:userAnswers){
			questionsAnswered.add(userAnswer.getQuestion().getId());
		}
		
		Question question = quizDao.getRandomQuestion(questionsAnswered);
		if(question==null){
			questionSummary.getErrorData().setErrorCode(ErrorData.CANT_PERFORM_ACTION);
			questionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_CANT_PERFORM_ACTION);
			return questionSummary;
		}
		questionSummary.setQuestion(question);
		
		return questionSummary;
	}

	@Override
	public UserResultJsonOutSummary finishQuiz(String activeSession) {
		UserResultJsonOutSummary userResultOutSummary = new UserResultJsonOutSummary();
		
		
		userResultOutSummary.setErrorData(checkUserSession(activeSession));
		if(userResultOutSummary.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			return userResultOutSummary;
		}
		
		UserResult userResult = quizDao.getUserResultBySession(activeSession);
		userResult.setResult(UserResult.QUIZ_FINISHED);
		
		userResult.setEndTime(new Date().getTime());
		
		userResult = quizDao.updateUserResult(userResult);
		if(userResult==null){
			userResultOutSummary.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			userResultOutSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
			return userResultOutSummary;
		}
		
		UserResultJsonOut userResOut = new UserResultJsonOut();
		userResOut.setId(userResult.getId());
		userResOut.setCompIp(userResult.getCompIp());
		userResOut.setUserName(userResult.getUserName());
		userResOut.setResult(userResult.getResult());
		userResOut.setStartTime(userResult.getStartTime());
		userResOut.setEndTime(userResult.getEndTime());
		userResOut.setSessionId(userResult.getSessionId());
	
		
		userResultOutSummary.setUserResult(userResOut);
		
		return userResultOutSummary;
	}

	@Override
	public QuestionGroupListSummary getQuestionGroupList(String activeSession) {
		QuestionGroupListSummary questionGroupListSummary = new QuestionGroupListSummary();
		
		questionGroupListSummary.setErrorData(checkUserSession(activeSession));
		if(questionGroupListSummary.getErrorData().getErrorCode()!=ErrorData.CODE_OK) return questionGroupListSummary;
		
		List<QuestionGroup> questionGroups = quizDao.getQuestionGroupList();
		System.out.println("Recieved list of groups in service layer - "+questionGroups);
		questionGroupListSummary.setQuestionGroupList(questionGroups);
		System.out.println("Data set to questionGroupListSummary");
		return questionGroupListSummary;
	}

	@Override
	public QuestionOutListSummary getQuestionsForGroup(String activeSession, long groupQuestionsId) {
		QuestionOutListSummary questionListSummary = new QuestionOutListSummary();
		
		questionListSummary.setErrorData(checkUserSession(activeSession));
		if(questionListSummary.getErrorData().getErrorCode()!=ErrorData.CODE_OK) return questionListSummary;
		
		List<Question> questionsForGroup = quizDao.getQuestionsForGroup(groupQuestionsId);
		if(questionsForGroup==null) return questionListSummary;
		List<QuestionOut> questionsOut = new LinkedList<QuestionOut>();
		for(Question question:questionsForGroup){
			QuestionOutSummary questionSummary = getQuestion(activeSession, question.getId());
			QuestionOut questionOut = questionSummary.getQuestion();
			if(questionOut!=null){
				questionsOut.add(questionOut);
			}
		}
		questionListSummary.setQuestions(questionsOut);
		return questionListSummary;
	}
	
	@Override
	public QuizOutResultSummary getQuizResult(String finishedSession) {
		QuizOutResultSummary quizResult = new QuizOutResultSummary(); 
		quizResult.setErrorData(checkUserSession(finishedSession));
		if(quizResult.getErrorData().getErrorCode()==ErrorData.WRONG_SESSION){
			return quizResult;
		}
		if(quizResult.getErrorData().getErrorCode()==ErrorData.CODE_OK){
			quizResult.getErrorData().setErrorCode(ErrorData.QUIZ_MUST_BE_FINISHED);
			quizResult.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_QUIZ_MUST_BE_FINISHED);
			return quizResult;
		}
		if(quizResult.getErrorData().getErrorCode()!=ErrorData.INACTIVE_SESSION){
			return quizResult;
		}
		quizResult.getErrorData().setErrorCode(ErrorData.CODE_OK);
		quizResult.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_OK);
		
		UserResult userResult = quizDao.getUserResultBySession(finishedSession);
		List<UserAnswer> userAnswers = quizDao.getUserAnswersByUserResult(userResult);
		System.out.println("Found user Answers - "+userAnswers);
		if(userAnswers==null) return quizResult;
		
		List<GroupResOut> groupResList = new LinkedList<GroupResOut>();
		
		for(UserAnswer userAnswer:userAnswers){
			String groupName = userAnswer.getQuestion().getQuestionGroup().getGroupName();
			Long groupId = userAnswer.getQuestion().getQuestionGroup().getId();
			GroupResOut groupToAddAnswer = null;
			for(GroupResOut groupResOut:groupResList){
				if(groupResOut.getGroupId().equals(groupId)){
					groupToAddAnswer = groupResOut;
				}
			}
			if(groupToAddAnswer==null){
				groupToAddAnswer = new GroupResOut();
				groupToAddAnswer.setGroupId(userAnswer.getQuestion().getQuestionGroup().getId());
				groupToAddAnswer.setGroupName(groupName);
				groupToAddAnswer.setQuestionResList(new LinkedList<QuestionRes>());
				groupResList.add(groupToAddAnswer);
			}
			
			QuestionRes currentQuestionRes = null;
			for(GroupResOut groupResOut:groupResList){
				if(groupResOut.getQuestionResList()==null) continue;
				for(QuestionRes questionRes:groupResOut.getQuestionResList()){
					if(questionRes.getQuestionId().equals(userAnswer.getQuestion().getId())){
						currentQuestionRes = questionRes;
						break;
					}
				}
			}
			if(currentQuestionRes==null){
				currentQuestionRes = new QuestionRes();
				currentQuestionRes.setQuestionId(userAnswer.getQuestion().getId());
				currentQuestionRes.setQuestionText(userAnswer.getQuestion().getQuestionText());
				currentQuestionRes.setAnswerList(new LinkedList<AnswerOut>());
				if(userAnswer.getQuestion().getParentId()==null){
					groupToAddAnswer.getQuestionResList().add(currentQuestionRes);
				}
			}
			if(userAnswer.getQuestion().getParentId()!=null){
				QuestionRes parentQuestionRes = null;
				for(GroupResOut groupResOut:groupResList){
					if(groupResOut.getQuestionResList()==null) continue;
					for(QuestionRes questionRes:groupResOut.getQuestionResList()){
						if(questionRes.getQuestionId().equals(userAnswer.getQuestion().getParentId())){
							parentQuestionRes = questionRes;
							break;
						}
					}
				}
				if(parentQuestionRes==null){
					parentQuestionRes = new QuestionRes();
					Question parentQuestionData = quizDao.getQuestion(userAnswer.getQuestion().getParentId());
					parentQuestionRes.setQuestionId(parentQuestionData.getId());
					parentQuestionRes.setQuestionText(parentQuestionData.getQuestionText());
					parentQuestionRes.setSubquestions(new LinkedList<QuestionRes>());
					GroupResOut groupToAdd = null;
					for(GroupResOut groupResOut:groupResList){
						if(groupResOut.getGroupId().equals(parentQuestionData.getQuestionGroup().getId())){
							groupToAdd = groupResOut;
							break;
						}
					}
					if(groupToAdd==null){
						groupToAdd = new GroupResOut();
						groupToAdd.setQuestionResList(new LinkedList<QuestionRes>());
						groupResList.add(groupToAdd);
					}
					groupToAdd.getQuestionResList().add(parentQuestionRes);
				}
				parentQuestionRes.getSubquestions().add(currentQuestionRes);
			}
			
			AnswerOut answerOut = new AnswerOut();
			answerOut.setId(userAnswer.getAnswer().getId());
			answerOut.setAnswerText(userAnswer.getAnswer().getAnswerText());
			currentQuestionRes.getAnswerList().add(answerOut);
			
		}
		
		for(GroupResOut groupRes:groupResList){
			for(QuestionRes questionRes:groupRes.getQuestionResList()){
				if(questionRes.getSubquestions()==null){
					if(questionRes.getAnswerList()==null) continue;
					List<Long> ansIds = new ArrayList<Long>();
					for(AnswerOut answer:questionRes.getAnswerList()){
						ansIds.add(answer.getId());
					}
					CheckUserAnswerSummary answerSumm = checkRes(finishedSession, questionRes.getQuestionId(), ansIds);
					questionRes.setRight(answerSumm.isResult());
				}else{
					for(QuestionRes innerQuestion:questionRes.getSubquestions()){
						List<Long> ansIds = new ArrayList<Long>();
						for(AnswerOut answer:innerQuestion.getAnswerList()){
							ansIds.add(answer.getId());
						}
						CheckUserAnswerSummary answerSumm = checkRes(finishedSession, innerQuestion.getQuestionId(), ansIds);
						innerQuestion.setRight(answerSumm.isResult());
					}
				}
				
			}
		}
		
		quizResult.setGroupResOutList(groupResList);
		
		return quizResult;
	}
	
	private ErrorData checkUserSession(String activeSession){
		ErrorData errorData = new ErrorData();
		
		UserResult userRes = quizDao.getUserResultBySession(activeSession);
		if(userRes==null){
			System.out.println("Wrong session");
			errorData.setErrorCode(ErrorData.WRONG_SESSION);
			errorData.setErrorDescription(ErrorData.DESCRIPTION_WRONG_SESSION);
			return errorData;
		}
		
		if(userRes.getResult()!=UserResult.QUIZ_STARTED){
			System.out.println("Inactive session");
			errorData.setErrorCode(ErrorData.INACTIVE_SESSION);
			errorData.setErrorDescription(ErrorData.DESCRIPTION_INACTIVE_SESSION);
		}
		
		return errorData;
	}
	
	@Override
	public QuestionOutSummary getQuestion(String activeSession, Long questionId) {
		QuestionOutSummary questionOutSummary = new QuestionOutSummary();
		if(activeSession==null || questionId==null){
			questionOutSummary.getErrorData().setErrorCode(ErrorData.NO_ENOUGH_PARAMS);
			questionOutSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NOT_ENOUGH_PARAMS);
			return questionOutSummary;
		}
		
		questionOutSummary.setErrorData(checkUserSession(activeSession));
		if(questionOutSummary.getErrorData().getErrorCode()!=ErrorData.CODE_OK) return questionOutSummary;
		
		Question question = quizDao.getQuestion(questionId);
		if(question==null) return questionOutSummary;
		QuestionOut questionOut = new QuestionOut();
		questionOut.setId(question.getId());
		questionOut.setQuestionText(question.getQuestionText());
		questionOut.setResponseType(question.getResponseType());
		System.out.println("question - "+questionOut);
		questionOutSummary.setQuestion(questionOut);
		
		List<Answer> answers = quizDao.getAnswersForQuestion(question);
		List<AnswerOut> answerOutList = new LinkedList<AnswerOut>();
		if(answers==null) return questionOutSummary;
		for(Answer answer:answers){
			AnswerOut answerOut = new AnswerOut();
			answerOut.setId(answer.getId());
			answerOut.setAnswerText(answer.getAnswerText());
			answerOutList.add(answerOut);
		}
		questionOutSummary.setAnswerList(answerOutList);
		
		List<Question> subQuestions = quizDao.getSubQuestions(question);
		System.out.println("SubQuestions - "+subQuestions);
		if(subQuestions!=null){
			List<SubQuestionOut> subQuestionsOut = new LinkedList<SubQuestionOut>();
			for(Question q:subQuestions){
				SubQuestionOut subQOut = new SubQuestionOut();
				subQOut.setId(q.getId());
				subQOut.setQuestionText(q.getQuestionText());
				subQuestionsOut.add(subQOut);
			}
			questionOutSummary.setSubQuestions(subQuestionsOut);
		}
		
		
		return questionOutSummary;
	}
	
	private List<UserResult> getActiveUserResults(){
		return quizDao.getActiveUserResults();
		
	}
	
	private class SessionThread implements Runnable{

		@Override
		public void run() {
			for(;;){
				
				try {
					Thread.currentThread().sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				List<UserResult> activeUserResults = getActiveUserResults();
				if(activeUserResults==null) continue;
				long currentTime = new Date().getTime();
				for(UserResult userResult:activeUserResults){
					long currentSessionDifference = currentTime - userResult.getStartTime();
					if(currentSessionDifference>sessionLostActivity){
						userResult.setResult(UserResult.QUIZ_TIMEOUT);
						userResult.setEndTime(new Date().getTime());
						quizDao.updateUserResult(userResult);
					}
				}
			}
			
		}
		
	}


}
