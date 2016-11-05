package com.wuxianedu.wechat.activity.bean;

import java.io.Serializable;

public class UserInFo implements Serializable{
    private String status; //0,    
    private String message; //"登录成功",
    private String userId; //"1",
    private String userName; //"无限互联-登录",
    private Long userPhoneNum; //16626663666,
    private String head; //"http://pic.baike.soso.com/p/20131202/20131202103359-1685722094.jpg"}



	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getUserPhoneNum() {
		return userPhoneNum;
	}
	public void setUserPhoneNum(Long userPhoneNum) {
		this.userPhoneNum = userPhoneNum;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	@Override
	public String toString() {
		return "UserInFo [status=" + status + ", message=" + message + ", userId=" + userId + ", userName=" + userName
				+ ", userPhoneNum=" + userPhoneNum + ", head=" + head + "]";
	}
    
}
