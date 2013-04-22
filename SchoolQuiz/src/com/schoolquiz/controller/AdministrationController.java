package com.schoolquiz.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.schoolquiz.entity.admin.decorated.AdminUserSessionSummary;
import com.schoolquiz.entity.admin.request.UserCredentials;
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
	
	

}
