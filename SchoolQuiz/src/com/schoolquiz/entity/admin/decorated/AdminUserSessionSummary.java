package com.schoolquiz.entity.admin.decorated;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.admin.AdminUserSession;

public class AdminUserSessionSummary {
	
	private ErrorData errorData;
	private AdminUserSession adminUserSession;
	
	
	public AdminUserSessionSummary(){
		errorData = new ErrorData();
		errorData.setErrorCode(ErrorData.CODE_OK);
		errorData.setErrorDescription(ErrorData.DESCRIPTION_OK);
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	public AdminUserSession getAdminUserSession() {
		return adminUserSession;
	}

	public void setAdminUserSession(AdminUserSession adminUserSession) {
		this.adminUserSession = adminUserSession;
	}
	
	@Override
	public String toString(){
		return "errorData - "+errorData+"; adminUserSession - "+adminUserSession;
	}

}
