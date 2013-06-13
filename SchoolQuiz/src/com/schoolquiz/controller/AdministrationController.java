package com.schoolquiz.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.admin.CheckSessionSummary;
import com.schoolquiz.entity.admin.DeleteGroupResponse;
import com.schoolquiz.entity.admin.FinishSessionSummary;
import com.schoolquiz.entity.admin.OperationGroupResponse;
import com.schoolquiz.entity.admin.decorated.AdminUserSessionSummary;
import com.schoolquiz.entity.admin.decorated.CustomQuestionGroupResponse;
import com.schoolquiz.entity.admin.request.AddAnswerRequest;
import com.schoolquiz.entity.admin.request.AddAnswersToQuestionRequest;
import com.schoolquiz.entity.admin.request.AddGroupRequest;
import com.schoolquiz.entity.admin.request.AddQuestionRequest;
import com.schoolquiz.entity.admin.request.DeleteAnswerRequest;
import com.schoolquiz.entity.admin.request.DeleteGroupRequest;
import com.schoolquiz.entity.admin.request.DeleteQuestionRequest;
import com.schoolquiz.entity.admin.request.EditAnswerRequest;
import com.schoolquiz.entity.admin.request.EditAnswersFromQuestionRequest;
import com.schoolquiz.entity.admin.request.EditGroupRequest;
import com.schoolquiz.entity.admin.request.EditQuestionRequest;
import com.schoolquiz.entity.admin.request.GetAnswerRequest;
import com.schoolquiz.entity.admin.request.GetAnswerSearchRequest;
import com.schoolquiz.entity.admin.request.GetAnswersForQuestionRequest;
import com.schoolquiz.entity.admin.request.GetGroupsDictRequest;
import com.schoolquiz.entity.admin.request.GetQuestionGroupRequest;
import com.schoolquiz.entity.admin.request.GetQuestionRequest;
import com.schoolquiz.entity.admin.request.GetQuestionsForGroup;
import com.schoolquiz.entity.admin.request.GroupRequest;
import com.schoolquiz.entity.admin.request.RemoveAnswersFromQuestionRequest;
import com.schoolquiz.entity.admin.request.UserCredentials;
import com.schoolquiz.entity.admin.request.UserSession;
import com.schoolquiz.entity.admin.response.AddAnswerResponse;
import com.schoolquiz.entity.admin.response.AddAnswersToQuestionResponse;
import com.schoolquiz.entity.admin.response.DeleteAnswerResponse;
import com.schoolquiz.entity.admin.response.EditAnswerResponse;
import com.schoolquiz.entity.admin.response.EditAnswersFromQuestionResponse;
import com.schoolquiz.entity.admin.response.GetAnswerResponse;
import com.schoolquiz.entity.admin.response.GetAnswerSearchResponse;
import com.schoolquiz.entity.admin.response.GetAnswersForQuestionResponse;
import com.schoolquiz.entity.admin.response.GetGroupsDictResponse;
import com.schoolquiz.entity.admin.response.GetQuestionGroupResponse;
import com.schoolquiz.entity.admin.response.GetQuestionResponse;
import com.schoolquiz.entity.admin.response.GetQuestionsForGroupResponse;
import com.schoolquiz.entity.admin.response.RemoveAnswersFromQuestionResponse;
import com.schoolquiz.service.AdminUserService;
import com.schoolquiz.service.QuizService;



@Controller
@RequestMapping("/json")
public class AdministrationController {
	
	@Resource(name="quizService")
	private QuizService quizService;
	
	@Resource(name="adminService")
	private AdminUserService adminService;
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public @ResponseBody AdminUserSessionSummary postUserCredentials(@RequestBody UserCredentials adminUser){
		System.out.println("UserData - "+adminUser);
		AdminUserSessionSummary result = adminService.checkUser(adminUser.getUsername(), adminUser.getPassword());
		
		return result;
	}
	
	@RequestMapping(value="/checkUserSession", method=RequestMethod.POST)
	public @ResponseBody CheckSessionSummary checkActiveSession(@RequestBody UserSession userSession){
		CheckSessionSummary sessionSummary = null;
		if(userSession==null){
			sessionSummary = new CheckSessionSummary();
			sessionSummary.getErrorData().setErrorCode(ErrorData.WRONG_PARAMS);
			sessionSummary.getErrorData().setErrorDescription(ErrorData.DESCRIPTION_WRONG_PARAMS);
			return sessionSummary;
		}
		sessionSummary = adminService.checkAdminSession(userSession.getUserSession());
		return sessionSummary;
	}
	
	@RequestMapping(value="/finishUserSession", method=RequestMethod.POST)
	public @ResponseBody FinishSessionSummary finishSession(@RequestBody UserSession userSession){
		FinishSessionSummary finishSessionSummary = adminService.finishSession(userSession.getUserSession());
		return finishSessionSummary;
	}
	
	@RequestMapping(value="/getGroupList", method=RequestMethod.POST)
	public @ResponseBody CustomQuestionGroupResponse getGroupsForAdmin(@RequestBody GroupRequest groupRequest){
		System.out.println("Recieved request for user groups - "+groupRequest);
		
		return adminService.getGroupsForAdmin(groupRequest.getUserSession(), groupRequest.getNumberFrom(), groupRequest.getNumberOfItems());
	}
	
	@RequestMapping(value="/addGroup", method=RequestMethod.POST)
	public @ResponseBody OperationGroupResponse addGroupByAdmin(@RequestBody AddGroupRequest addGroupRequest){
		System.out.println("Recieved request to add group - "+addGroupRequest);
		
		return adminService.addGroup(addGroupRequest);
	}
	
	@RequestMapping(value="/editGroup", method=RequestMethod.POST)
	public @ResponseBody OperationGroupResponse editGroupByAdmin(@RequestBody EditGroupRequest editGroupRequest){
		System.out.println("Recieved request to edit group - "+editGroupRequest);
		
		return adminService.editGroup(editGroupRequest);
	}
	
	@RequestMapping(value="/deleteGroup", method=RequestMethod.POST)
	public @ResponseBody DeleteGroupResponse deleteGroupByAdmin(@RequestBody DeleteGroupRequest deleteGroupRequest){
		System.out.println("Recieved request to delete group - "+deleteGroupRequest);
		
		return adminService.deleteGroup(deleteGroupRequest);
	}
	
	@RequestMapping(value="/getQuestionGroup", method=RequestMethod.POST)
	public @ResponseBody GetQuestionGroupResponse getQuestionGroup(@RequestBody GetQuestionGroupRequest getQuestionGroupRequest){
		System.out.println("Recieved request to get group - "+getQuestionGroupRequest);
		
		return adminService.getQuestionGroup(getQuestionGroupRequest);
	}
	
	
	@RequestMapping(value="/getQuestionListForGroup", method=RequestMethod.POST)
	public @ResponseBody GetQuestionsForGroupResponse getQuestionsForGroup(@RequestBody GetQuestionsForGroup getQuestionsForGroupRequest){
		System.out.println("Recieved request to get questions for group - "+getQuestionsForGroupRequest);
		
		return adminService.getQuestionsForGroup(getQuestionsForGroupRequest);
	}
	
	
	@RequestMapping(value="/addQuestion", method=RequestMethod.POST)
	public @ResponseBody OperationGroupResponse addQuestion(@RequestBody AddQuestionRequest addQuestionRequest){
		System.out.println("Recieved request to add question - "+addQuestionRequest);
		
		return adminService.addQuestion(addQuestionRequest);
	}
	
	@RequestMapping(value="/editQuestion", method=RequestMethod.POST)
	public @ResponseBody OperationGroupResponse editQuestion(@RequestBody EditQuestionRequest editQuestionRequest){
		System.out.println("Recieved request to edit question - "+editQuestionRequest);
		
		return adminService.editQuestion(editQuestionRequest);
	}
	
	@RequestMapping(value="/deleteQuestion", method=RequestMethod.POST)
	public @ResponseBody OperationGroupResponse deleteQuestion(@RequestBody DeleteQuestionRequest deleteQuestionRequest){
		System.out.println("Recieved request to delete question - "+deleteQuestionRequest);
		
		return adminService.deleteQuestion(deleteQuestionRequest);
	}
	
	@RequestMapping(value="/getQuestion", method=RequestMethod.POST)
	public @ResponseBody GetQuestionResponse getQuestion(@RequestBody GetQuestionRequest getQuestionRequest){
		System.out.println("Recieved request to get question - "+getQuestionRequest);
		
		return adminService.getQuestion(getQuestionRequest);
	}
	
	@RequestMapping(value="/getAnswersForQuestion", method=RequestMethod.POST)
	public @ResponseBody GetAnswersForQuestionResponse getAnswersForQuestion(@RequestBody GetAnswersForQuestionRequest getAnswerRequest){
		System.out.println("Recieved request to get answers for question - "+getAnswerRequest);
		
		return adminService.getAnswersForQuestion(getAnswerRequest);
	}
	
	@RequestMapping(value="/getAnswer", method=RequestMethod.POST)
	public @ResponseBody GetAnswerResponse getAnswer(@RequestBody GetAnswerRequest getAnswerRequest){
		System.out.println("Recieved request to get answer - "+getAnswerRequest);
		
		return adminService.getAnswer(getAnswerRequest);
	}
	
	
	@RequestMapping(value="/addAnswer", method=RequestMethod.POST)
	public @ResponseBody AddAnswerResponse addAnswer(@RequestBody AddAnswerRequest addAnswerRequest){
		System.out.println("Recieved request to add answer - "+addAnswerRequest);
		
		return adminService.addAnswer(addAnswerRequest);
	}
	
	@RequestMapping(value="/editAnswer", method=RequestMethod.POST)
	public @ResponseBody EditAnswerResponse editAnswer(@RequestBody EditAnswerRequest editAnswerRequest){
		System.out.println("Recieved request to edit answer - "+editAnswerRequest);
		
		return adminService.editAnswer(editAnswerRequest);
	}
	
	@RequestMapping(value="/deleteAnswer", method=RequestMethod.POST)
	public @ResponseBody DeleteAnswerResponse deleteAnswer(@RequestBody DeleteAnswerRequest deleteAnswerRequest){
		System.out.println("Recieved request to delete answer - "+deleteAnswerRequest);
		
		return adminService.deleteAnswer(deleteAnswerRequest);
	}
	
	@RequestMapping(value="/getGroupDict", method=RequestMethod.POST)
	public @ResponseBody GetGroupsDictResponse getGroupsDict(@RequestBody GetGroupsDictRequest getGroupsDictRequest){
		return adminService.getGroupsDict(getGroupsDictRequest);
	}
	
	@RequestMapping(value="/getAnswerSearch", method=RequestMethod.POST)
	public @ResponseBody GetAnswerSearchResponse getAnswerSearch(@RequestBody GetAnswerSearchRequest getAnswerSearchRequest){
		return adminService.getAnswerSearch(getAnswerSearchRequest);
	}
	
	@RequestMapping(value="/addAnswersToQuestion", method=RequestMethod.POST)
	public @ResponseBody AddAnswersToQuestionResponse addAnswersToQuestion(@RequestBody AddAnswersToQuestionRequest addAnswersToQuestionRequest){
		return adminService.addAnswersToQuestion(addAnswersToQuestionRequest);
	}
	
	@RequestMapping(value="/removeAnswersFromQuestion", method=RequestMethod.POST)
	public @ResponseBody RemoveAnswersFromQuestionResponse removeAnswersFromQuestion(@RequestBody RemoveAnswersFromQuestionRequest removeAnswersFromQuestionRequest){
		return adminService.removeAnswersFromQuestion(removeAnswersFromQuestionRequest);
	}
	
	@RequestMapping(value="/editAnswersFromQuestion", method=RequestMethod.POST)
	public @ResponseBody EditAnswersFromQuestionResponse editAnswersFromQuestion(@RequestBody EditAnswersFromQuestionRequest editAnswersFromQuestionRequest){
		return adminService.editAnswerFromQuestion(editAnswersFromQuestionRequest);
	}

}
