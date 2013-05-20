package com.schoolquiz.entity.admin.response;

import java.util.List;

import com.schoolquiz.entity.ErrorData;

public class GetGroupsDictResponse {
	
	private ErrorData errorData;
	private List<DictItem> dictItems;
	
	public GetGroupsDictResponse(){
		errorData = new ErrorData();
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	public List<DictItem> getDictItems() {
		return dictItems;
	}

	public void setDictItems(List<DictItem> dictItems) {
		this.dictItems = dictItems;
	}
	
	

}
