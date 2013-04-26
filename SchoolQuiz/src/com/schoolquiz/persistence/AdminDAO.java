package com.schoolquiz.persistence;

import java.util.List;

import com.schoolquiz.entity.Question;
import com.schoolquiz.entity.QuestionGroup;
import com.schoolquiz.entity.admin.AdminUser;
import com.schoolquiz.entity.admin.AdminUserSession;

public interface AdminDAO {
	
	public AdminUser checkUserCredentials(String userName, String password);
	
	public AdminUserSession createSession(AdminUser adminUser);
	
	public void closeActiveSessions(AdminUser adminUser);
	
	public Boolean checkActiveSession(String session);
	
	public AdminUser saveNewUser(String userName, String password);
	
	public AdminUserSession getAdminSession(String session);
	
	public List<AdminUserSession> getActiveSessions();
	
	public AdminUserSession updateSession(AdminUserSession userSession);
	
	public QuestionGroup addQuestionGroup(QuestionGroup questionGroup);
	
	public QuestionGroup updateQuestionGroup(QuestionGroup questionGroup);
	
	public Boolean deleteQuestionGroup(QuestionGroup questionGroup);

	public List<Question> getQuestionsForGroup(QuestionGroup groupToDelete);

	public Question updateQuestion(Question question);

}
