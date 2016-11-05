package com.wuxianedu.wechat.activity.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMsg implements Parcelable {

	private String sender; // 发送者
	private String body; // 发送的消息

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(sender);
		dest.writeString(body);
	}

	/**
	 * 从序列化对象中读取数据
	 */
	public static final Creator<ChatMsg> CREATOR = new Creator<ChatMsg>() {

		@Override
		public ChatMsg createFromParcel(Parcel source) {
			ChatMsg bean = new ChatMsg();
			bean.sender = source.readString();
			bean.body = source.readString();
			return bean;
		}

		@Override
		public ChatMsg[] newArray(int size) {
			return null;
		}

	};
}
