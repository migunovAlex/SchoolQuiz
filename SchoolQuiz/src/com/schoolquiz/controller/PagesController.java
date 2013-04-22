package com.schoolquiz.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.admin.CheckSessionSummary;
import com.schoolquiz.entity.admin.FinishSessionSummary;
import com.schoolquiz.entity.admin.OperationGroupResponse;
import com.schoolquiz.entity.admin.decorated.AdminUserSessionSummary;
import com.schoolquiz.entity.admin.decorated.CustomQuestionGroupResponse;
import com.schoolquiz.entity.admin.request.AddGroupRequest;
import com.schoolquiz.entity.admin.request.DeleteGroupRequest;
import com.schoolquiz.entity.admin.request.EditGroupRequest;
import com.schoolquiz.entity.admin.request.GroupRequest;
import com.schoolquiz.entity.admin.request.UserCredentials;
import com.schoolquiz.entity.admin.request.UserSession;
import com.schoolquiz.service.AdminUserService;
import com.schoolquiz.service.QuizService;

@Controller
@RequestMapping("/pages")
public class PagesController {
	
	@Resource(name="quizService")
	private QuizService quizService;
	
	@Resource(name="adminService")
	private AdminUserService adminService;

	@RequestMapping(value="/gettestpage", method=RequestMethod.GET)
	public String getTestPage(){
		System.out.println("request to load testPage");
		
		return "testpage";
	}
	
	@RequestMapping(value="/loginForm", method=RequestMethod.GET)
	public String getLoginForm(){
		System.out.println("Get the login form");
		
		return "loginForm";
	}
	
	
	
	@RequestMapping(value="/main", method=RequestMethod.GET)
	public String getMainPage(@RequestParam("userSession") String userSession){
		System.out.println("recieve request to get main form - "+userSession);
		CheckSessionSummary sessionSummary = adminService.checkAdminSession(userSession);
		if(sessionSummary.getErrorData().getErrorCode()!=ErrorData.CODE_OK) return "loginForm";
		adminService.updateSessionActivity(userSession);
		
		return "mainPage";
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
	
	@RequestMapping(value="/json/getGroupList", method=RequestMethod.POST)
	public @ResponseBody CustomQuestionGroupResponse getGroupsForAdmin(@RequestBody GroupRequest groupRequest){
		System.out.println("Recieved request for user groups - "+groupRequest);
		
		return adminService.getGroupsForAdmin(groupRequest.getUserSession(), groupRequest.getNumberFrom(), groupRequest.getNumberOfItems());
	}
	
	@RequestMapping(value="/json/addGroup", method=RequestMethod.POST)
	public @ResponseBody OperationGroupResponse addGroupByAdmin(@RequestBody AddGroupRequest addGroupRequest){
		System.out.println("Recieved request to add group - "+addGroupRequest);
		
		return adminService.addGroup(addGroupRequest.getUserSession(), addGroupRequest.getGroupName());
	}
	
	@RequestMapping(value="/json/editGroup", method=RequestMethod.POST)
	public @ResponseBody OperationGroupResponse editGroupByAdmin(@RequestBody EditGroupRequest editGroupRequest){
		System.out.println("Recieved request to edit group - "+editGroupRequest);
		
		return adminService.editGroup(editGroupRequest.getUserSession(), editGroupRequest.getGroupId(), editGroupRequest.getGroupName(), editGroupRequest.isEnabled());
	}
	
	@RequestMapping(value="/json/deleteGroup", method=RequestMethod.POST)
	public @ResponseBody OperationGroupResponse deleteGroupByAdmin(@RequestBody DeleteGroupRequest deleteGroupRequest){
		System.out.println("Recieved request to delete group - "+deleteGroupRequest);
		
		return adminService.deleteGroup(deleteGroupRequest.getUserSession(), deleteGroupRequest.getGroupId());
	}
	
}
