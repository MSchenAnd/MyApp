package com.wuxianedu.wechat.activity.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Friends implements Serializable{

	private static final long serialVersionUID = 2055300225093737857L;
	
   private String userName;   //: "（红楼梦）",
   private String head;   //: "http://www.2qqtouxiang.com/Pic/qqkj/2237/4.jpg",
   private String content;   //: "白海棠（红楼梦）半卷湘帘半掩门，碾冰为土玉为盆。偷来梨蕊三分白，借得梅花一缕魂。月窟仙人缝缟袂，秋闺怨女试啼痕。娇羞默默同谁诉，倦倚西风夜已昏。",
   private Date time;   //: "2016-2-17 23:00:10",
   List<String> imageList;
   
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public List<String> getImageList() {
		return imageList;
	}
	public void setImageLists(List<String> imagList) {
		this.imageList = imagList;
	}
	@Override
	public String toString() {
		return "Friends [userName=" + userName + ", head=" + head + ", content=" + content + ", time=" + time
				+ ", personList=" + imageList + "]";
	}
	
}
