package com.wuxianedu.wechat.activity.adapter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.PictureActivity;
import com.wuxianedu.wechat.activity.bean.SendMessage;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.DateUtil;
import com.wuxianedu.wechat.utils.ExpressionUtil;
import com.wuxianedu.wechat.utils.ImageUtil;
import com.wuxianedu.wechat.utils.L;
import com.wuxianedu.wechat.widgt.RoundImageView;

import android.R.anim;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jivesoftware.smack.Chat;

public class ChatSendAdapter extends BaseAdapter{
	private Context context;
	private List<SendMessage> list;
	private List<String> imageUrl;
	private MediaPlayer player;
	private AnimationDrawable animation;
	private boolean cklick = true;
	private long startTime = 0;
	public ChatSendAdapter(Context context,List<SendMessage> list,MediaPlayer player){
		this.context = context;
		this.list = list;
		this.player = player;
	}
	//获取图片url的集合，用于图片浏览
	public List<SendMessage> getSendMessageImageLitst(){
		imageUrl = new ArrayList<>();
		List<SendMessage> newList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if(SendMessage.IMAGE == list.get(i).getType()){
				SendMessage sendMessage = list.get(i);
				newList.add(sendMessage);
				imageUrl.add(sendMessage.getPicturePath());
			}
		}
		return newList;
	}
	
	public void setList(List<SendMessage> list) {
		this.list = list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		SendMessage sendMessage = list.get(position);
		L.e("ChatSendAdapter===========" + sendMessage.getFrom());
		if (sendMessage.getFrom() == SendMessage.RECEIVE){
			return SendMessage.RECEIVE;
		}else {
			return SendMessage.SEND;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		int mForm = getItemViewType(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			if (mForm == SendMessage.SEND){
				convertView = LayoutInflater.from(context).inflate(R.layout.chat_tallk_list, null);
			}else {
				convertView = LayoutInflater.from(context).inflate(R.layout.chat_tallk_list_receive, null);
			}

			viewHolder.head = (RoundImageView) convertView.findViewById(R.id.chat_userimg_me);
			//文字
			viewHolder.content = (TextView) convertView.findViewById(R.id.chat_tallk_item);
			//图片
			viewHolder.image = (ImageView) convertView.findViewById(R.id.chat_tallk_imag);
			//语音
			viewHolder.voice = (ImageView) convertView.findViewById(R.id.chat_tallk_voice);
			//消息的时间
			viewHolder.time = (TextView) convertView.findViewById(R.id.tallk_time);
			//语音的时长
			viewHolder.second = (TextView) convertView.findViewById(R.id.voice_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final SendMessage sendMessage = list.get(position);
		CoreUtils.getPicture(sendMessage.getHead(), viewHolder.head);
		viewHolder.time.setText(DateUtil.dateToString(DateUtil.SDF, sendMessage.getTime()));
		switch (sendMessage.getType()) {
		case SendMessage.TEXT://当输入的内容为文字时和表情时
			viewHolder.content.setVisibility(View.VISIBLE);
			//解析文字加表情的复合文本
			SpannableString string = ExpressionUtil.decorateFaceInStr(context, sendMessage.getContent());
			viewHolder.content.setText(string);
			viewHolder.image.setVisibility(View.GONE);
			viewHolder.voice.setVisibility(View.GONE);
			viewHolder.second.setVisibility(View.GONE);
			break;
		case SendMessage.IMAGE://发送图片
			viewHolder.content.setVisibility(View.GONE);
			viewHolder.image.setVisibility(View.VISIBLE);
			viewHolder.voice.setVisibility(View.GONE);
			viewHolder.second.setVisibility(View.GONE);
			Bitmap bitmap = ImageUtil.getSmallBitmap(sendMessage.getPicturePath(), 200, 200);
			viewHolder.image.setImageBitmap(bitmap);
			//CoreUtils.getPicture(sendMessage.getPicturePath(), viewHolder.image);
			viewHolder.image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(context,PictureActivity.class);
					List<SendMessage> newList = getSendMessageImageLitst();
					int index = newList.indexOf(sendMessage);
					//图片url的list
					intent.putExtra(Constants.IMAGE, (Serializable)imageUrl);
					//第几张图片
					intent.putExtra(Constants.IMAGEPOSITION, index);
					context.startActivity(intent);
				}
			});
			break;
		case SendMessage.VOICE://发送语音
			viewHolder.content.setVisibility(View.GONE);
			viewHolder.image.setVisibility(View.GONE);
			viewHolder.voice.setVisibility(View.VISIBLE);
			viewHolder.second.setVisibility(View.VISIBLE);
			viewHolder.second.setText(String.valueOf(sendMessage.getScendTime() / 1000L) + " '' ");
			//点击播放录音
			viewHolder.voice.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(animation != null && player.isPlaying()){
						animation.stop();
						animation.selectDrawable(0);
						stopPlayer();
						if(startTime != sendMessage.getStartTime()){
							animation = (AnimationDrawable) viewHolder.voice.getDrawable();
							startTime = sendMessage.getStartTime();
							play(sendMessage.getAudioPath());
							animation.start();
						}
					}else{
						animation = (AnimationDrawable) viewHolder.voice.getDrawable();
						startTime = sendMessage.getStartTime();
						play(sendMessage.getAudioPath());
						animation.start();
					}
				}
			});
			break;
		default:
			break;
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView image,voice;
		RoundImageView head;
		TextView time,content,second;
	}

	private void play(String path){
		if(player != null){
			player.reset();//重置player进入初始化状态
			try {
				player.setDataSource(path);
				player.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
			player.start();
			player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					animation.stop();
					animation.selectDrawable(0);
					animation = null;
				}
			});
		}
	}
	
	private void stopPlayer(){
		if(player != null && player.isPlaying()){
			player.stop();
			L.e("shenysoaewenfwa");
		}
	}
}
