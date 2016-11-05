package com.wuxianedu.wechat.activity.bean;

import java.io.Serializable;
import java.util.Date;

public class SubscriptionDetails implements Serializable{

	private static final long serialVersionUID = -7311896094486989409L;
	private String title; //"（红楼梦）",
    private String image; //"http://www.2qqtouxiang.com/Pic/qqkj/2237/4.jpg",
    private String content; //"白海棠（红楼梦）半卷湘帘半掩门，碾冰为土玉为盆。偷来梨蕊三分白，借得梅花一缕魂。月窟仙人缝缟袂，秋闺怨女试啼痕。娇羞默默同谁诉，倦倚西风夜已昏。",
    private Date time;//"
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
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
	@Override
	public String toString() {
		return "SubscriptionDetails [title=" + title + ", image=" + image + ", content=" + content + ", time=" + time
				+ "]";
	}
    
    
}
