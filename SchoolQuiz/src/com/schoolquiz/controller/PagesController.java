package com.schoolquiz.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.admin.CheckSessionSummary;
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
	
	@RequestMapping(value="/answersPage", method=RequestMethod.GET)
	public String getAnswers(@RequestParam("userSession") String userSession){
		System.out.println("recieve request to get answers form - "+userSession);
		CheckSessionSummary sessionSummary = adminService.checkAdminSession(userSession);
		if(sessionSummary.getErrorData().getErrorCode()!=ErrorData.CODE_OK) return "loginForm";
		adminService.updateSessionActivity(userSession);
		
		return "answersPage";
	}
	
	@RequestMapping(value="/resultsPage", method=RequestMethod.GET)
	public String getResultsPage(@RequestParam("userSession") String userSession){
		System.out.println("recieve request to get results form - "+userSession);
		CheckSessionSummary sessionSummary = adminService.checkAdminSession(userSession);
		if(sessionSummary.getErrorData().getErrorCode()!=ErrorData.CODE_OK) return "loginForm";
		adminService.updateSessionActivity(userSession);
		
		return "resultsPage";
	}
	
	@RequestMapping(value="/questionsInGroups", method=RequestMethod.GET)
	public String getQuestionsInGroups(@RequestParam("userSession") String userSession){
		System.out.println("recieve request to get questionsInGroups form - "+userSession);
		CheckSessionSummary sessionSummary = adminService.checkAdminSession(userSession);
		if(sessionSummary.getErrorData().getErrorCode()!=ErrorData.CODE_OK) return "loginForm";
		adminService.updateSessionActivity(userSession);
		
		return "questionsInGroups";
	}
	
	
	
}
