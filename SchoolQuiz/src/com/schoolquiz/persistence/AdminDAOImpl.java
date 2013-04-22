package com.schoolquiz.persistence;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.schoolquiz.entity.QuestionGroup;
import com.schoolquiz.entity.admin.AdminUser;
import com.schoolquiz.entity.admin.AdminUserSession;
import com.schoolquiz.utils.SessionGenerator;

@Transactional
public class AdminDAOImpl implements AdminDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	private static final String passwordChange = "Jackson";
	
	private Session currentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	
	
	@Override
	public AdminUser checkUserCredentials(String userName, String password) {
		
		String convertedPass = converPassToHEX(password+passwordChange);
		List<AdminUser> adminUsers = currentSession().createCriteria(AdminUser.class).add(Restrictions.eq("userName", userName)).add(Restrictions.eq("password", convertedPass)).list();
		if(adminUsers.size()==0) return null;
		return adminUsers.get(0);
		
	}
	
	@Override
	public AdminUserSession createSession(AdminUser adminUser) {
		AdminUserSession resultUserSession = null;
		
		resultUserSession = new AdminUserSession();
		resultUserSession.setActive(true);
		resultUserSession.setAdminUser(adminUser);
		resultUserSession.setLastActivity(new Date().getTime());
		String generatedSession = null;
		AdminUserSession foundSession = null;
		for(;;){
			generatedSession = SessionGenerator.getNewSessionValue();
			foundSession = getAdminUserSessionBySession(generatedSession);
			if(foundSession==null) break;
		}
		resultUserSession.setSession(generatedSession);
		Long id = (Long) currentSession().save(resultUserSession);
		if(id==null) return null;
		resultUserSession.setId(id);
		
		return resultUserSession;
	}
	
	private AdminUserSession getAdminUserSessionBySession(String session){
		AdminUserSession result = null;
		List<AdminUserSession> sessionList = currentSession().createCriteria(AdminUserSession.class).add(Restrictions.eq("session", session)).list();
		if(sessionList.size()>0){
			result = sessionList.get(0);
		}
		return result;
	}

	@Override
	public Boolean checkActiveSession(String session) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String converPassToHEX(String password) {
//		System.out.println("Coming to convert pass - "+password);
		StringBuffer strPassBuf = new StringBuffer(password);
		StringBuffer finalPass = null;
		strPassBuf.append(passwordChange);
		try {
			MessageDigest mdg = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		finalPass = new StringBuffer();
		byte[] mdBytes = strPassBuf.toString().getBytes();
		int i;
		for(i=0; i<mdBytes.length;i++){
			finalPass.append(Integer.toString((mdBytes[i]&0xff)+0x100,16).substring(1));
		}
		
//		System.out.println("Converted Password - "+finalPass.toString());
		return finalPass.toString();
	}

	@Override
	public AdminUser saveNewUser(String userName, String password) {
		AdminUser resultUser = new AdminUser();
		resultUser.setUserName(userName);
		resultUser.setPassword(converPassToHEX(password+passwordChange));
		Long id = (Long) currentSession().save(resultUser);
		if(id==null) return null;
		resultUser.setId(id);
		
		return resultUser;
	}
	
	@Override
	public AdminUserSession updateSession(AdminUserSession userSession){
		AdminUserSession returnSession = null;
		try{
			currentSession().update(userSession);
			returnSession = userSession;
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnSession;
	}



	@Override
	public void closeActiveSessions(AdminUser adminUser) {
		List<AdminUserSession> userSessions = currentSession().createCriteria(AdminUserSession.class).add(Restrictions.eq("adminUser", adminUser)).add(Restrictions.eq("active", true)).list();
		System.out.println("Previous opened sessions - "+userSessions.size());
		if(userSessions.size()==0) return;
		for(AdminUserSession userSession:userSessions){
			userSession.setActive(false);
			updateSession(userSession);
		}
	}



	@Override
	public AdminUserSession getAdminSession(String session) {
		AdminUserSession result = null;
		List<AdminUserSession> userSessions = currentSession().createCriteria(AdminUserSession.class).add(Restrictions.eq("session", session)).list();
		if(userSessions.size()==0) return result;
		
		result = userSessions.get(0);
		
		return result;
	}



	@Override
	public List<AdminUserSession> getActiveSessions() {
		
		return currentSession().createCriteria(AdminUserSession.class).add(Restrictions.eq("active", true)).list();
	}



	@Override
	public QuestionGroup addQuestionGroup(QuestionGroup questionGroup) {
		Long id = null;
		try{
			id = (Long) currentSession().save(questionGroup);
		}catch(HibernateException e){
			
		}
		if(id==null) return null; 
		questionGroup.setId(id);
		
		return questionGroup;
	}



	@Override
	public QuestionGroup updateQuestionGroup(QuestionGroup questionGroup) {
		
		try{
			currentSession().update(questionGroup);
		}catch(HibernateException e){
			return null;
		}
		
		return questionGroup;
	}



	@Override
	public Boolean deleteQuestionGroup(QuestionGroup questionGroup) {
		try{
			currentSession().delete(questionGroup);
		}catch(HibernateException e){
			return false;
		}
		return true;
	}





}
