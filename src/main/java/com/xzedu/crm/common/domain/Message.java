package com.xzedu.crm.common.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{
	private String code;//处理成功或者失败的标记1成功0失败
	private String message;//错误的话，返回错因
	private Object otherData;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getOtherData() {
		return otherData;
	}
	public void setOtherData(Object otherData) {
		this.otherData = otherData;
	}
	
}
