package com.wuxianedu.wechat.activity.bean;

import java.io.Serializable;
import java.util.Date;

public class SendMessage implements Serializable{
	private static final long serialVersionUID = -4387883382964393818L;
	public static final int TEXT = 0, IMAGE = 1, VOICE = 2;
	public static final int SEND = 0, RECEIVE = 1;
	private Date time;
	private String head;
	private int type;
	private int from;
	private String content;
	private String picturePath;
	private String audioPath;
	private long scendTime;
	private long startTime;

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getScendTime() {
		return scendTime;
	}
	public void setScendTime(long scendTime) {
		this.scendTime = scendTime;
	}
	public String getAudioPath() {
		return audioPath;
	}
	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
