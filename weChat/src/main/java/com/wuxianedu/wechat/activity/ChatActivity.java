package com.wuxianedu.wechat.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cn.R;
import com.wuxianedu.wechat.activity.adapter.ChatSendAdapter;

import com.wuxianedu.wechat.activity.bean.ChatMsg;
import com.wuxianedu.wechat.activity.bean.SendMessage;
import com.wuxianedu.wechat.activity.bean.User;
import com.wuxianedu.wechat.activity.bean.UserInFo;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.activity.mi.XmppManager;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.ExpressionUtil;
import com.wuxianedu.wechat.utils.FileLocalCache;
import com.wuxianedu.wechat.utils.FileUtil;
import com.wuxianedu.wechat.utils.ImageUtil;

import com.wuxianedu.wechat.utils.L;
import com.wuxianedu.wechat.utils.Util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jivesoftware.smack.Chat;

/**
 * 聊天页面
 * @author Administrator
 *
 */
public class ChatActivity extends BaseActivity implements OnClickListener,OnLayoutChangeListener{
	private Button vioce,keboardLeft,phiz,chatmMenu,tallk,send;//语音、返回键、表情、菜单、按住说话、发送
	private EditText edit;//输入框
	private GridView grid,faceGrid;//菜单和表情
	private ListView listView;
	private InputMethodManager inputkeyboard;//软键盘
	private List<SendMessage> list = new ArrayList<>();
	private ChatSendAdapter adapter;
	private RelativeLayout rootLayout,bottomLayout;
	private String picturePath;
	private RelativeLayout voiceCenter;
	private TextView voiceCentertext;
	private ImageView toastImage;
	private MediaRecorder recording;
	private String audioPath;
	private MediaPlayer player;
	private long startTime,stopTime;
	private Chat chat;
	private User user;
	private ChatBroadcastReceiver chatBroadcastReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_chat;
	}
	@Override
	protected void init() {
		IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_MESSAGE_RECEIVE);
		chatBroadcastReceiver = new ChatBroadcastReceiver();
		registerReceiver(chatBroadcastReceiver,filter);
		//取跳转时存入intent中的数据
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("user");
		chat = XmppManager.getInstance().createChat(user.getUserName());
		//标题栏的标题
		setTvMid(user.getUserName());
		//显示右边的按钮
		setRightImage(R.drawable.icon_chat_user, null);
		//左边的按钮
		ImageView imagLeft = (ImageView) findViewById(R.id.tile_butleft);
		//发语音时屏幕中间的动画
		voiceCenter = (RelativeLayout) findViewById(R.id.centervoice);
		voiceCentertext = (TextView) voiceCenter.findViewById(R.id.centervoice_text);
		toastImage =  (ImageView) voiceCenter.findViewById(R.id.chat_macf);
		//获取键盘
		inputkeyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		vioce = (Button) findViewById(R.id.chat_voice);//语音
		keboardLeft = (Button) findViewById(R.id.chat_keyboardleft);//键盘
		phiz = (Button) findViewById(R.id.chat_phiz);//表情
		chatmMenu = (Button) findViewById(R.id.chat_downmenu_but);//菜单
		edit = (EditText) findViewById(R.id.chat_edit);//输入框
		tallk = (Button) findViewById(R.id.chat_tallk_but);//发送语音按钮
		grid = (GridView) findViewById(R.id.chat_downmewnu);//菜单视图
		send = (Button) findViewById(R.id.chat_send);//发送消息按钮
		faceGrid = (GridView) findViewById(R.id.chat_face);//表情视图
		listView = (ListView) findViewById(R.id.chat_list);
		rootLayout = (RelativeLayout) findViewById(R.id.root_id);
		bottomLayout = (RelativeLayout) findViewById(R.id.chat_undereath);
		//点击 监听器
		vioce.setOnClickListener(this);
		keboardLeft.setOnClickListener(this);
		phiz.setOnClickListener(this);
		chatmMenu.setOnClickListener(this);
		send.setOnClickListener(this);
		edit.setOnClickListener(this);
		imagLeft.setOnClickListener(this);
		rootLayout.addOnLayoutChangeListener(this);
		//给菜单视图添加数据
		SimpleAdapter adepter = new SimpleAdapter(this, intiDate(),
												R.layout.chat_grid,
												new String[]{"image","name"},
												new int[]{R.id.chat_gridimg,R.id.chate_gridname});
		grid.setAdapter(adepter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				grid.setVisibility(View.GONE);
				faceGrid.setVisibility(View.GONE);
				inputkeyboard.hideSoftInputFromWindow(edit.getWindowToken(), 0);
			}
		});
		//菜单的点击
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case Constants.PECTUR://进入图库
					Intent intent;
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
						intent = new Intent(Intent.ACTION_PICK,Media.EXTERNAL_CONTENT_URI);
					}else{
						intent = new Intent(Intent.ACTION_GET_CONTENT,null);
					}
					//设置获取类型：图片
					intent.setDataAndType(Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(intent, Constants.PECTUR);
					break;
				case Constants.CAMERAINTENT:// 拍照
					Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					String sdkPath = FileUtil.getSDCardPath() + "WeChat/image";
					FileUtil.checkDir(sdkPath);
					File file = new File(sdkPath, System.currentTimeMillis() + ".jpg");
					picturePath = file.getAbsolutePath();
					Uri uri = Uri.fromFile(file);
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(cameraIntent, Constants.CAMERAINTENT);
					break;
				default:
					break;
				}

			}
		});

		//得到表情的集合
		 final List<Map<String, Object>> listface = ExpressionUtil.buildExpressionsList(this);
		//把表情的图片写入显示图片的网格视图
		 faceGrid.setAdapter(new SimpleAdapter(this,
				listface,
				R.layout.chat_gridface,
				new String[]{"drawableRId"},
				new int[]{R.id.chat_gridface}));

		faceGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					//获得表情名
				String key = (String) listface.get(position).get("drawableId");
				//获得表情表示的意思（如：[得意]）
					key = ExpressionUtil.drawableIdToFaceName.get(key);
					//获取光标所在的位置
					int cursor = edit.getSelectionStart();
					//获取输入框中的值
					String string = edit.getText().toString();
					//string =string + key;
					StringBuilder builder = new StringBuilder(string);
					//在光标的位置插入表情
					string = builder.insert(cursor, key).toString();
					SpannableString composite = ExpressionUtil.decorateFaceInStr(ChatActivity.this, string);
				edit.setText(composite);
				edit.setSelection(cursor + key.length());
			}
		});
		//输入框内容改变时的监听
		edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(s)){
					//当输入的内容为空时，显示菜单按钮，隐藏发送按钮
					chatmMenu.setVisibility(View.VISIBLE);
					phiz.setVisibility(View.VISIBLE);
					send.setVisibility(View.GONE);

				}else{
					//当输入的内容不为空时，隐藏菜单按钮，显示发送按钮
					chatmMenu.setVisibility(View.GONE);
					faceGrid.setVisibility(View.GONE);
					send.setVisibility(View.VISIBLE);

				}
			}
		});
		player = new MediaPlayer();
		recording = new MediaRecorder();
		adapter = new ChatSendAdapter(this, list,player);
		listView.setAdapter(adapter);

		tallk.setOnTouchListener(new View.OnTouchListener() {
			//获取动画
			AnimationDrawable anima =  (AnimationDrawable) toastImage.getDrawable();
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean isSend = false;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://按下
					voiceCenter.setVisibility(View.VISIBLE);
					anima.start();
					voiceCentertext.setText("向上滑动，取消发送");
					startRecord();
					break;
				case MotionEvent.ACTION_MOVE://移动，不是以整个屏幕为基准，而是以点击的控件为基准
					if(event.getY() > 0){
						voiceCentertext.setText("向上滑动，取消发送");
					}else{
						voiceCentertext.setText("松开手指，取消发送");
					}
					break;
				case MotionEvent.ACTION_UP:
					if(event.getY() > 0){
						isSend = true;
					}else{
						isSend = false;
					}
					voiceCenter.setVisibility(View.GONE);
					boolean isShort =  stopRecord();
					if(isSend && isShort){
						senVoice();
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}
	//录制语音
	private void startRecord(){
		//设置录音源为麦克风
		recording.setAudioSource(MediaRecorder.AudioSource.MIC);
		//设置输出格式
		recording.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		//设置解码格式
		recording.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		//设置缓存路径
		String filePath = FileUtil.getSDCardPath() + "WeChat/voice/";
		//创建文件路径
		FileUtil.checkDir(filePath);
		//设置文件名
		audioPath = filePath + System.currentTimeMillis() + ".amr";
		//设置输出路径
		recording.setOutputFile(audioPath);
		startTime = System.currentTimeMillis();
		try {
			recording.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recording.start();
	}
	//停止录音
	private boolean stopRecord(){
		if(recording != null){
			recording.stop();
		}
		stopTime = System.currentTimeMillis();
		if( stopTime - startTime < 1000L){
			Util.toast(this, "按住时间过短，请重按", true);
			File file = new File(audioPath);
			if(file.exists()){
				file.delete();
			}
			return false;
		}else{
			return true;
		}

	}


	//获取路径
	@Override
	protected void onActivityResult(int requsetCod, int resultCod, Intent data) {
		switch (requsetCod) {
		case Constants.PECTUR://图库
			if(resultCod == 0 && null == data){
				return;
			}
			Uri uri = data.getData();
			//将uri转为局对路径
			picturePath = ImageUtil.getRealFilePath(this, uri);
			senImage();
			break;
		case Constants.CAMERAINTENT://相机
			if(resultCod == 0){
				return;
			}
			senImage();
			break;
		default:
			break;
		}
		super.onActivityResult(requsetCod, resultCod, data);
	}

	//输入文字
	private void senContent(){
		SendMessage sendMessage = new SendMessage();
		sendMessage.setTime(new Date());
		sendMessage.setContent(edit.getText().toString());
		sendMessage.setType(SendMessage.TEXT);
		sendMessage.setFrom(SendMessage.SEND);
		list.add(sendMessage);
		adapter.setList(list);
		listView.setSelection(list.size() - 1);
		XmppManager.getInstance().sendMsg(chat,edit.getText().toString());
		edit.setText("");
	}

	//输入图片
	private void senImage(){
		SendMessage sendMessage = new SendMessage();
		UserInFo userInfo = (UserInFo) FileLocalCache.getSerializableData(this, Constants.USER_INFO);
		sendMessage.setHead(userInfo.getHead());
		sendMessage.setTime(new Date());
		sendMessage.setPicturePath(picturePath);
		sendMessage.setType(SendMessage.IMAGE);
		list.add(sendMessage);
		adapter.setList(list);
		listView.setSelection(list.size() - 1);
	}

	//输入语音
	private void senVoice(){
		SendMessage sendMessage = new SendMessage();
		UserInFo userInfo = (UserInFo) FileLocalCache.getSerializableData(this, Constants.USER_INFO);
		sendMessage.setHead(userInfo.getHead());
		sendMessage.setTime(new Date());
		sendMessage.setType(SendMessage.VOICE);
		sendMessage.setAudioPath(audioPath);
		sendMessage.setScendTime(stopTime - startTime);
		sendMessage.setStartTime(startTime);
		list.add(sendMessage);
		adapter.setList(list);
		listView.setSelection(list.size() - 1);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_voice://语音
			vioce.setVisibility(View.GONE);
			keboardLeft.setVisibility(View.VISIBLE);
			edit.setVisibility(View.GONE);
			tallk.setVisibility(View.VISIBLE);
			phiz.setVisibility(View.GONE);
			grid.setVisibility(View.GONE);
			faceGrid.setVisibility(View.GONE);
			inputkeyboard.hideSoftInputFromWindow(edit.getWindowToken(), 0);
			break;
		case R.id.chat_keyboardleft://左边的键盘
			keboardLeft.setVisibility(View.GONE);
			vioce.setVisibility(View.VISIBLE);
			edit.setVisibility(View.VISIBLE);
			tallk.setVisibility(View.GONE);
			phiz.setVisibility(View.VISIBLE);
			grid.setVisibility(View.GONE);
			faceGrid.setVisibility(View.GONE);
			edit.requestFocus();
			inputkeyboard.showSoftInput(edit, 0);
			break;

		case R.id.chat_phiz://表情
			if(View.VISIBLE == faceGrid.getVisibility()){
				faceGrid.setVisibility(View.GONE);
				edit.requestFocus();
				inputkeyboard.showSoftInput(edit, 0);
			}else{
				faceGrid.setVisibility(View.VISIBLE);
				grid.setVisibility(View.GONE);
				inputkeyboard.hideSoftInputFromWindow(edit.getWindowToken(), 0);
			}

			break;
		case R.id.chat_downmenu_but://菜单
			if(View.VISIBLE == grid.getVisibility()){
				grid.setVisibility(View.GONE);
				edit.requestFocus();
				inputkeyboard.showSoftInput(edit, 0);
			}else{
				grid.setVisibility(View.VISIBLE);
				faceGrid.setVisibility(View.GONE);
				vioce.setVisibility(View.VISIBLE);
				keboardLeft.setVisibility(View.GONE);
				phiz.setVisibility(View.VISIBLE);
				tallk.setVisibility(View.GONE);
				edit.setVisibility(View.VISIBLE);
				edit.clearFocus();
				inputkeyboard.hideSoftInputFromWindow(edit.getWindowToken(), 0);
			}
			break;
		case R.id.chat_send://发送键
			senContent();
			edit.setText("");
		case R.id.chat_edit:
			grid.setVisibility(View.GONE);
			break;
		case R.id.tile_butleft://标题的返回键
			if(player != null && player.isPlaying()){
				player.stop();
			}
			finish();
		default:
			break;
		}
	}

	private List<Map<String, Object>> intiDate(){
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("image", R.drawable.chat_tool_photo);
		map.put("name", "图片");
		list.add(map);

		map = new HashMap<>();
		map.put("image", R.drawable.chat_tool_camera);
		map.put("name", "拍照");
		list.add(map);

		map = new HashMap<>();
		map.put("image", R.drawable.chat_tool_location);
		map.put("name", "位置");
		list.add(map);

		map = new HashMap<>();
		map.put("image", R.drawable.chat_tool_send_file);
		map.put("name", "文件");
		list.add(map);

		map = new HashMap<>();
		map.put("image", R.drawable.chat_tool_audio);
		map.put("name", "语音通话");
		list.add(map);

		map = new HashMap<>();
		map.put("image", R.drawable.chat_tool_video);
		map.put("name", "视频通话");
		list.add(map);

		return list;
	}
	//消除GridView与软键盘共存的情况
	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
			int oldBottom) {
		if(bottom != 0 && oldBottom!=0 && oldBottom - bottom>0){
			grid.setVisibility(View.GONE);
			faceGrid.setVisibility(View.GONE);
			bottomLayout.setVisibility(View.GONE);
			bottomLayout.setVisibility(View.VISIBLE);
			edit.requestFocus();
		}

	}
	//按返回键关闭player（退出页面时，player是不会自动停止的）
	@Override
	public void onBackPressed() {
		if(player != null && player.isPlaying()){
			player.stop();
		}
		super.onBackPressed();
	}


	//资源回收
	@Override
	protected void onDestroy() {
		if(player != null){
			player.release();
			player = null;
		}
		if(recording != null){
			recording.release();
			recording = null;
		}
		unregisterReceiver(chatBroadcastReceiver);
		super.onDestroy();
	}

	class ChatBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			ChatMsg chatMsg = intent.getParcelableExtra("message");
			if (chatMsg.getSender().split("@")[0].equals(user.getUserName())) {
					SendMessage sendMessage = new SendMessage();
					sendMessage.setFrom(SendMessage.RECEIVE);
					sendMessage.setType(SendMessage.TEXT);
					sendMessage.setContent(chatMsg.getBody());
					sendMessage.setTime(new Date());
					Message message = handler.obtainMessage();
					message.what = 1;
					message.obj = sendMessage;
					message.sendToTarget();
				//将广播的结束码改为OK，代表接受到的广播消息已被处理
				//为ChatService中MesageBroadcastReceiver判断提供依据
					setResultCode(Activity.RESULT_OK);
			}
		}
	}
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1){
				SendMessage sendMessage = (SendMessage) msg.obj;
				L.e("ChatActivity===========" + sendMessage.getFrom());
				list.add(sendMessage);
				adapter.setList(list);
			}
		}
	};

}
