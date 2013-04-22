package com.schoolquiz.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.QuestionGroup;
import com.schoolquiz.entity.QuestionGroupOut;
import com.schoolquiz.entity.controllerparams.CheckUserAnswer;
import com.schoolquiz.entity.controllerparams.GetGroupObject;
import com.schoolquiz.entity.controllerparams.QuestionForGroup;
import com.schoolquiz.entity.controllerparams.SessionObject;
import com.schoolquiz.entity.controllerparams.User;
import com.schoolquiz.entity.decorated.CheckUserAnswerSummary;
import com.schoolquiz.entity.decorated.QuestionGroupListOutSummary;
import com.schoolquiz.entity.decorated.QuestionGroupListSummary;
import com.schoolquiz.entity.decorated.QuestionOutListSummary;
import com.schoolquiz.entity.decorated.UserResultSummary;
import com.schoolquiz.entity.domain.QuestionOutSummary;
import com.schoolquiz.entity.domain.QuizOutResultSummary;
import com.schoolquiz.entity.domain.UserResultJsonOut;
import com.schoolquiz.entity.domain.UserResultJsonOutSummary;
import com.schoolquiz.service.QuizService;


@Controller
@RequestMapping("/service")
public class QuizController {
	
	@Resource(name="quizService")
	private QuizService quizService;
	
	@RequestMapping(value="/json/createUserResult", method=RequestMethod.POST)
	public @ResponseBody UserResultJsonOutSummary createUserResultJson(@RequestBody User userName, HttpServletRequest request){
			
		System.out.println("Recieved userName - "+userName+"; request - "+request);
		
		UserResultJsonOutSummary userResultOutSummary = new UserResultJsonOutSummary();
		
		UserResultJsonOut userResultJsonOut = null;
		
		UserResultSummary userResultSum = quizService.createNewUserResult(userName.getName(), request.getRemoteAddr());
		System.out.println("Created userResult - "+userResultSum);
		if(userResultSum.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			userResultOutSummary.setErrorData(userResultSum.getErrorData());
			return userResultOutSummary;
		}
		
		userResultJsonOut = new UserResultJsonOut();
		userResultJsonOut.setId(userResultSum.getUserResult().getId());
		userResultJsonOut.setUserName(userResultSum.getUserResult().getUserName());
		userResultJsonOut.setCompIp(userResultSum.getUserResult().getCompIp());
		userResultJsonOut.setSessionId(userResultSum.getUserResult().getSessionId());
		userResultJsonOut.setStartTime(userResultSum.getUserResult().getStartTime());
		userResultJsonOut.setResult(userResultSum.getUserResult().getResult());
		
		
		userResultOutSummary.setUserResult(userResultJsonOut);
		
		return userResultOutSummary;
		
	}
	
	@RequestMapping(value="/json/createUserResult", method=RequestMethod.GET)
	public @ResponseBody UserResultJsonOutSummary createUserResultJsonGet(@RequestParam("name") String userName, HttpServletRequest request){
		
		UserResultJsonOutSummary userResultOutSummary = new UserResultJsonOutSummary();
		
		UserResultJsonOut userResultJsonOut = null;
		
		UserResultSummary userResultSum = quizService.createNewUserResult(userName, request.getRemoteAddr());
		System.out.println("Created userResult - "+userResultSum);
		if(userResultSum.getErrorData().getErrorCode()!=ErrorData.CODE_OK){
			userResultOutSummary.setErrorData(userResultSum.getErrorData());
			return userResultOutSummary;
		}
		
		userResultJsonOut = new UserResultJsonOut();
		userResultJsonOut.setId(userResultSum.getUserResult().getId());
		userResultJsonOut.setUserName(userResultSum.getUserResult().getUserName());
		userResultJsonOut.setCompIp(userResultSum.getUserResult().getCompIp());
		userResultJsonOut.setSessionId(userResultSum.getUserResult().getSessionId());
		userResultJsonOut.setStartTime(userResultSum.getUserResult().getStartTime());
		userResultJsonOut.setResult(userResultSum.getUserResult().getResult());
		
		
		userResultOutSummary.setUserResult(userResultJsonOut);
		
		return userResultOutSummary;
		
	}
	
	@RequestMapping(value="/json/getQuestionGroups", method=RequestMethod.POST)
	public @ResponseBody QuestionGroupListOutSummary getQuestionGroups(@RequestBody SessionObject userSession){
		System.out.println("Recieved sessionUser - "+userSession);
		
		QuestionGroupListOutSummary questionGroupListOutSummary = new QuestionGroupListOutSummary();
		QuestionGroupListSummary questionGroupListSum = quizService.getQuestionGroupList(userSession.getUserSession());
		System.out.println("Data recieved to controller - "+questionGroupListSum);
		questionGroupListOutSummary.setErrorData(questionGroupListSum.getErrorData());
		if(questionGroupListSum.getQuestionGroupList()!=null){
			List<QuestionGroupOut> questionGroupOutList = new ArrayList<QuestionGroupOut>();
			for(QuestionGroup questionGroup:questionGroupListSum.getQuestionGroupList()){
				QuestionGroupOut questionGroupOut = new QuestionGroupOut();
				questionGroupOut.setQuestionGroupId(questionGroup.getId());
				questionGroupOut.setQuestionGroupName(questionGroup.getGroupName());
				questionGroupOutList.add(questionGroupOut);
			}
			questionGroupListOutSummary.setQuestionGroups(questionGroupOutList);
		}
		
		
		return questionGroupListOutSummary;
	
	}
	
	@RequestMapping(value="/json/getQuestionGroups", method=RequestMethod.GET)
	public @ResponseBody QuestionGroupListOutSummary getQuestionGroupsGet(@RequestParam("userSession") String userSession){
		QuestionGroupListOutSummary questionGroupListOutSummary = new QuestionGroupListOutSummary();
		QuestionGroupListSummary questionGroupListSum = quizService.getQuestionGroupList(userSession);
		System.out.println("Data recieved to controller - "+questionGroupListSum);
		questionGroupListOutSummary.setErrorData(questionGroupListSum.getErrorData());
		if(questionGroupListSum.getQuestionGroupList()!=null){
			List<QuestionGroupOut> questionGroupOutList = new ArrayList<QuestionGroupOut>();
			for(QuestionGroup questionGroup:questionGroupListSum.getQuestionGroupList()){
				QuestionGroupOut questionGroupOut = new QuestionGroupOut();
				questionGroupOut.setQuestionGroupId(questionGroup.getId());
				questionGroupOut.setQuestionGroupName(questionGroup.getGroupName());
				questionGroupOutList.add(questionGroupOut);
			}
			questionGroupListOutSummary.setQuestionGroups(questionGroupOutList);
		}
		
		
		return questionGroupListOutSummary;
	
	}
	
	
	@RequestMapping(value="/json/getQuestionForGroup", method=RequestMethod.GET)
	public @ResponseBody QuestionOutListSummary getQuestionsForGroupGet(@RequestParam("userSession") String userSession, @RequestParam("questionGroup") Long questiongroup){
		QuestionOutListSummary questionListSum = quizService.getQuestionsForGroup(userSession, questiongroup);
		
		if(questionListSum==null){
			questionListSum = new QuestionOutListSummary();
			questionListSum.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			questionListSum.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
		}
		
		return questionListSum;
	}
	
	@RequestMapping(value="/json/getQuestionForGroup", method=RequestMethod.POST)
	public @ResponseBody QuestionOutListSummary getQuestionsForGroup(@RequestBody QuestionForGroup questionForGroup){
		System.out.println("Recieved params - "+questionForGroup);
		
		QuestionOutListSummary questionListSum = quizService.getQuestionsForGroup(questionForGroup.getUserSession(), questionForGroup.getQuestionGroup());
		
		if(questionListSum==null){
			questionListSum = new QuestionOutListSummary();
			questionListSum.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			questionListSum.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
		}
		
		return questionListSum;
	}
	
	
	@RequestMapping(value="/json/getQuestion", method=RequestMethod.POST)
	public @ResponseBody QuestionOutSummary getQuestion(@RequestBody GetGroupObject getGroupObject){
		
		System.out.println("recieved object - "+getGroupObject);
		QuestionOutSummary questionOutSummary = quizService.getQuestion(getGroupObject.getUserSession(), getGroupObject.getQuestionId());
		
		if(questionOutSummary==null){
			questionOutSummary = new QuestionOutSummary();
			questionOutSummary.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			questionOutSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
		}
		
		return questionOutSummary;
	}
	
	@RequestMapping(value="/json/getQuestion", method=RequestMethod.GET)
	public @ResponseBody QuestionOutSummary getQuestionGet(@RequestParam("userSession") String userSession, @RequestParam("questionId") Long questionId){
		QuestionOutSummary questionOutSummary = quizService.getQuestion(userSession, questionId);
		
		if(questionOutSummary==null){
			questionOutSummary = new QuestionOutSummary();
			questionOutSummary.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			questionOutSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
		}
		
		return questionOutSummary;
	}
	
	
	@RequestMapping(value="/json/checkUserAnswer", method=RequestMethod.POST)
	public @ResponseBody CheckUserAnswerSummary checkUserAnswer(@RequestBody CheckUserAnswer checkUserAnswer){
		CheckUserAnswerSummary checkUserAnswerSummary = null;
		System.out.println("Comint request data - "+checkUserAnswer);
		if(checkUserAnswer.getUserSession()==null || checkUserAnswer.getQuestionId()==null || checkUserAnswer.getAnswerIds()==null){
			checkUserAnswerSummary = new CheckUserAnswerSummary();
			checkUserAnswerSummary.getErrorData().setErrorCode(ErrorData.NO_ENOUGH_PARAMS);
			checkUserAnswerSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_NOT_ENOUGH_PARAMS);
			return checkUserAnswerSummary;
		}
		checkUserAnswerSummary = quizService.checkUserAnswer(checkUserAnswer.getUserSession(), checkUserAnswer.getQuestionId(), checkUserAnswer.getAnswerIds());
		
		if(checkUserAnswerSummary==null){
			checkUserAnswerSummary = new CheckUserAnswerSummary();
			checkUserAnswerSummary.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			checkUserAnswerSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
		}
		
		return checkUserAnswerSummary;
	}
	
	@RequestMapping(value="/json/checkUserAnswer", method=RequestMethod.GET)
	public @ResponseBody CheckUserAnswerSummary checkUserAnswerGet(@RequestParam("userSession") String userSession, @RequestParam("questionId") Long questionId, @RequestParam("answerIds") List<Long> answerIds){
		CheckUserAnswerSummary checkUserAnswerSummary = quizService.checkUserAnswer(userSession, questionId, answerIds);
		
		if(checkUserAnswerSummary==null){
			checkUserAnswerSummary = new CheckUserAnswerSummary();
			checkUserAnswerSummary.getErrorData().setErrorCode(ErrorData.SOMETHING_WRONG);
			checkUserAnswerSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_SOMETHING_WRONG);
		}
		
		return checkUserAnswerSummary;
	}
	
	
	@RequestMapping(value="/json/finishUserResult", method=RequestMethod.POST)
	public @ResponseBody UserResultJsonOutSummary finishUserResult(@RequestBody SessionObject sessionObject){	
		UserResultJsonOutSummary userResultSummary = quizService.finishQuiz(sessionObject.getUserSession());
		System.out.println("Return object from controller - "+userResultSummary);
		return userResultSummary;
	}
	
	@RequestMapping(value="/json/finishUserResult", method=RequestMethod.GET)
	public @ResponseBody UserResultJsonOutSummary finishUserResultGet(@RequestParam("userSession") String userSession){
		UserResultJsonOutSummary userResultSummary = quizService.finishQuiz(userSession);
		System.out.println("Return object from controller - "+userResultSummary);
		return userResultSummary;
	}
	
	@RequestMapping(value="/json/getQuizResults", method=RequestMethod.POST)
	public @ResponseBody QuizOutResultSummary getQuizResults(@RequestBody SessionObject userSession){
		QuizOutResultSummary quizResult = quizService.getQuizResult(userSession.getUserSession());
		System.out.println("Return object from controller - "+quizResult);
		return quizResult;
	}
	
	@RequestMapping(value="/json/getQuizResults", method=RequestMethod.GET)
	public @ResponseBody QuizOutResultSummary getQuizResultsGet(@RequestParam("userSession") String userSession){
		QuizOutResultSummary quizResult = quizService.getQuizResult(userSession);
		System.out.println("Return object from controller - "+quizResult);
		return quizResult;
	}
	
	
}
