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
import com.schoolquiz.entity.admin.request.AddGroupRequest;
import com.schoolquiz.entity.admin.request.DeleteGroupRequest;
import com.schoolquiz.entity.admin.request.EditGroupRequest;
import com.schoolquiz.entity.admin.request.GetQuestionsForGroup;
import com.schoolquiz.entity.admin.request.GroupRequest;
import com.schoolquiz.entity.admin.request.UserCredentials;
import com.schoolquiz.entity.admin.request.UserSession;
import com.schoolquiz.entity.admin.response.GetQuestionsForGroupResponse;
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
	
	
	@RequestMapping(value="/getQuestionListForGroup", method=RequestMethod.POST)
	public @ResponseBody GetQuestionsForGroupResponse getQuestionfForGroup(@RequestBody GetQuestionsForGroup getQuestionsForGroupRequest){
		System.out.println("Recieved request to get questions for group - "+getQuestionsForGroupRequest);
		
		return adminService.getQuestionsForGroup(getQuestionsForGroupRequest);
	}
	
	

}
