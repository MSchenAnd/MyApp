package com.wuxianedu.wechat.activity.mi;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.cn.R;
import com.wuxianedu.wechat.activity.ChatActivity;
import com.wuxianedu.wechat.activity.bean.ChatMsg;
import com.wuxianedu.wechat.utils.Constants;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.OfflineMessageManager;

import java.util.ArrayList;
import java.util.Iterator;

public class ChatService extends Service implements XmppManager.XmppManagerCallback{
	
	//接收到的消息集合，包括两种消息: 1，不在对话框界面的消息 2，离线消息
	private ArrayList<ChatMsg> messageList = new ArrayList<ChatMsg>();
	private MesageBroadcastReceiver mesageReceiver;

	@Override
	public void onCreate() {
		XmppManager.getInstance().setXmppManagerCallback(this);
		//注册消息接收广播
		IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_MESSAGE_RECEIVE);
		mesageReceiver = new MesageBroadcastReceiver();
		registerReceiver(mesageReceiver, filter);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void receiveMsg(Message message) {
		ChatMsg chatMsg = new ChatMsg();
		chatMsg.setSender(message.getFrom());
		chatMsg.setBody(message.getBody());
		sendReceiveMsgBroadCast(chatMsg);
	}

	/**
	 * 发送接收消息广播
	 * @param chatMsg
	 */
	private void sendReceiveMsgBroadCast(ChatMsg chatMsg) {
		Intent intent = new Intent();
		intent.setAction(Constants.INTENT_ACTION_MESSAGE_RECEIVE);
		intent.putExtra("message", chatMsg);
		/**
		 * MesageBroadcastReceiver指定为最后的接受者，Activity.RESULT_CANCELED指定初始的结果码，
		 * 如果ChatActivity中的广播接收者处理了这条广播，则结果码会在ChatActivity中被更改为Activity.RESULT_OK，
		 * 反之，ChatActivity中的广播接收者没有处理，则结果码仍然为Activity.RESULT_CANCELED
		 */
		sendOrderedBroadcast(intent, null, mesageReceiver, null, Activity.RESULT_CANCELED, null, null);
	}

	/**
	 * 消息广播
	 */
	private class MesageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			final ChatMsg chatMsg = (ChatMsg)intent.getParcelableExtra("message");
			final int resultCode = getResultCode();
			if (isOrderedBroadcast()) { // 判断是否是一个有序广播，true为是，false为不是
				if (resultCode != Activity.RESULT_OK) {
//					MyApplication.sp.play(MyApplication.soundId, 1, 1, 0, 0, 1);
					showMsgNotice(chatMsg);
				}
			}
		}
	}

	/**
	 * 显示消息通知
	 * @param chatMsg
	 */
	private void showMsgNotice(ChatMsg chatMsg) {
//		messageList.add(chatMsg);
//		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		nm.cancelAll();
//		Notification notification = new Notification(R.drawable.ic_launcher, "您有"+messageList.size()+"条新消息", System.currentTimeMillis());
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
//		notification.sound= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ms);
//		Intent intent = new Intent(this, ChatActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.putExtra("from", "notification");
//		intent.putParcelableArrayListExtra("messageList", messageList);
//		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.setLatestEventInfo(this, chatMsg.getSender().split("@")[0], chatMsg.getBody(), contentIntent);
//		nm.notify(0, notification);

		messageList.add(chatMsg);  //把消息实体加入到集合中
		//获取通知服务
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//取消所有
		nm.cancelAll();

		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentText("微信通知"); //设置通知的标题
		builder.setSmallIcon(R.drawable.icon); //设置通知的小图标
		builder.setContentText("您有"+messageList.size()+"条新消息"); //写入通知内容
//		builder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ms));
		Intent intent = new Intent(this, ChatActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("from", "notification");
		intent.putParcelableArrayListExtra("messageList", messageList);
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);
		//获得通知
		Notification notification = builder.getNotification();
		nm.notify(0, notification);
	}

	/**
	 * 获取离线消息
	 */
	public void getOfflineMessage() {
		OfflineMessageManager offlineMessageManager = new OfflineMessageManager(XmppManager.getInstance().getConnection());
		try {
			Iterator<Message> it = offlineMessageManager.getMessages();
			while (it.hasNext()) {
				Message message = it.next();
				ChatMsg chatMsg = new ChatMsg();
				chatMsg.setSender(message.getFrom());
				chatMsg.setBody(message.getBody());
				sendReceiveMsgBroadCast(chatMsg);
			}
			offlineMessageManager.deleteMessages();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		XmppManager.getInstance().closeConnection();
		unregisterReceiver(mesageReceiver);
	}

}
