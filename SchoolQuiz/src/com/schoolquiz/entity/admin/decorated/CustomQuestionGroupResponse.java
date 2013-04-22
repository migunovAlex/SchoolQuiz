package com.schoolquiz.entity.admin.decorated;

import java.util.List;

import com.schoolquiz.entity.ErrorData;
import com.schoolquiz.entity.admin.GroupForAdmin;

public class CustomQuestionGroupResponse {
	
	private ErrorData errorData;
	private String page;
	private String total;
	private String records;
	private List<GroupForAdmin> groupList;
	
	public CustomQuestionGroupResponse(){
		errorData = new ErrorData();
	}
	
	public ErrorData getErrorData() {
		return errorData;
	}
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getRecords() {
		return records;
	}
	public void setRecords(String records) {
		this.records = records;
	}
	public List<GroupForAdmin> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<GroupForAdmin> groupList) {
		this.groupList = groupList;
	}
	
	@Override
	public String toString(){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("page - "+page);
		strBuilder.append("\n");
		strBuilder.append("total - "+total);
		strBuilder.append("\n");
		strBuilder.append("records - "+records);
		strBuilder.append("\n");
		strBuilder.append("GROUPS:");
		strBuilder.append("\n");
		if(groupList!=null){
			for(GroupForAdmin group:groupList){
				strBuilder.append("group id - "+group.getId());
				strBuilder.append("\n");
				strBuilder.append("group name - "+group.getGroupName());
				strBuilder.append("\n");
				strBuilder.append("group enabled - "+group.isEnabled());
				strBuilder.append("\n");
			}
		}
		return strBuilder.toString();
	}
	

}
