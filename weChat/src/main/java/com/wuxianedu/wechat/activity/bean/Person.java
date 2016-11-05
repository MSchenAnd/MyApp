package com.wuxianedu.wechat.activity.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Person implements Serializable{
 
	
	private static final long serialVersionUID = 7454001637674726333L;
	private String userName;
	private String area;//"唐.孟郊 "
	private long weCode; //":1008611,
    private String head;   //: "http://www.2qqtouxiang.com/Pic/qqkj/2237/4.jpg",
    private String autograph; //":"人生若只如初见，何事秋风悲画扇。 ",
    private String lastStr;  //":"人生若只如初见，何事秋风悲画扇。 ",
    private int newsNum;  //":1,
    private boolean isAdd;  //":true,
    private Date lastTime;  //":"2016-02-24 09:40:05",
    private List<String> imagList;
    private String namePinyin;
    private String firstPinyin;
    private boolean check;
    private float alpha = 1.0F;
   
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	public String getNamePinyin() {
		return namePinyin;
	}
	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}
	public String getFirstPinyin() {
		return firstPinyin;
	}
	public void setFirstPinyin(String firstPinyin) {
		this.firstPinyin = firstPinyin;
	}
	
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public long getWeCode() {
		return weCode;
	}
	public void setWeCode(long weCode) {
		this.weCode = weCode;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getAutograph() {
		return autograph;
	}
	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}
	public String getLastStr() {
		return lastStr;
	}
	public void setLastStr(String lastStr) {
		this.lastStr = lastStr;
	}
	public int getNewsNum() {
		return newsNum;
	}
	public void setNewsNum(int newsNum) {
		this.newsNum = newsNum;
	}
	public boolean getIsAdd() {
		return isAdd;
	}
	public void setIsAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public List<String> getImagList() {
		return imagList;
	}
	public void setImagList(List<String> imagList) {
		this.imagList = imagList;
	}
	@Override
	public String toString() {
		return "Person [userName=" + userName + ", area=" + area + ", weCode=" + weCode + ", head=" + head
				+ ", autograph=" + autograph + ", lastStr=" + lastStr + ", newsNum=" + newsNum + ", isAdd=" + isAdd
				+ ", lastTime=" + lastTime + ", imagList=" + imagList + ", namePinyin=" + namePinyin + ", firstPinyin="
				+ firstPinyin + ", check=" + check + "]";
	}
    
	
}
