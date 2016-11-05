package com.wuxianedu.wechat.activity.bean;

import java.io.Serializable;
import java.util.Date;

public class Subscription implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8522208159228902058L;
	private String name; //"贾岛",
    private long weCode; //1008611,
    private String lastStr; //"十年磨一剑",
    private int newsNum; //0,
    private Date lastTime; //"2016-2-20 08:14:57"
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getWeCode() {
		return weCode;
	}
	public void setWeCode(long weCode) {
		this.weCode = weCode;
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
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	@Override
	public String toString() {
		return "Subscription [name=" + name + ", weCode=" + weCode + ", lastStr=" + lastStr + ", newsNum=" + newsNum
				+ ", lastTime=" + lastTime + "]";
	}
    
    
}
