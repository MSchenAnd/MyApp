package com.wuxianedu.wechat.activity.bean;

import org.jivesoftware.smack.packet.RosterPacket;

import java.io.Serializable;

public class User extends Base implements Serializable{

	//用户ID
	private String userId;

	//用户头像
	private int userIcon;

	// 用户名
	public String userName;

	// 昵称
	private String nickName;
	
	//内容
	private String content;
	
	//时间
	private String time;
	
	//是否选中
	private boolean checked;

	public RosterPacket.ItemType type;

	public User(){}

	public User(String userName) {
		this.userName = userName;
	}

	public User(String userName, RosterPacket.ItemType type) {
		this.userName = userName;
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(int userIcon) {
		this.userIcon = userIcon;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
