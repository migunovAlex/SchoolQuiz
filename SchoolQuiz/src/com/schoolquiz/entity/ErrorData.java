package com.schoolquiz.entity;

public class ErrorData {
	
	public static int CODE_OK = 200;
	public static int NO_ENOUGH_PARAMS = 405;
	public static int WRONG_PARAMS = 406;
	public static int WRONG_SESSION = 407;
	public static int INACTIVE_SESSION = 408;
	public static int CANT_PERFORM_ACTION = 409;
	public static int SOMETHING_WRONG = 409;
	public static int QUIZ_MUST_BE_FINISHED = 410;
	public static int NO_SUCH_QUESTION_GROUP = 420;
	public static int NO_SUCH_QUESTION = 420;
	
	public static String DESCRIPTION_OK = "OK";
	public static String DESCRIPTION_NOT_ENOUGH_PARAMS = "Not enough params";
	public static String DESCRIPTION_WRONG_PARAMS = "Wrong params";
	public static String DESCRIPTION_WRONG_SESSION = "Wrong session identificator";
	public static String DESCRIPTION_INACTIVE_SESSION = "Inactive session identificator";
	public static String DESCRIPTION_CANT_PERFORM_ACTION = "Can not to perform the action";
	public static String DESCRIPTION_SOMETHING_WRONG = "Error. Something wrong";
	public static String DESCRIPTION_QUIZ_MUST_BE_FINISHED = "Quiz must be finished before you request for results";
	public static String DESCRIPTION_NO_SUCH_QUESTION_GROUP = "There is no group with such id";
	public static String DESCRIPTION_NO_SUCH_QUESTION = "There is no question with such id";
	
	private int errorCode;
	private String errorDescription;
	
	public ErrorData(){
		errorCode = CODE_OK;
		errorDescription = DESCRIPTION_OK;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	

	@Override
	public String toString(){
		return "errorCode - "+errorCode+"; errorDescription - "+errorDescription;
	}

}
