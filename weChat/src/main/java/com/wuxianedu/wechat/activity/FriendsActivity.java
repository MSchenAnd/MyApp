package com.wuxianedu.wechat.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.cn.R;
import com.wuxianedu.wechat.activity.adapter.FrientAdapter;
import com.wuxianedu.wechat.activity.bean.Friends;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.activity.bean.UserInFo;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.DateUtil;
import com.wuxianedu.wechat.utils.FileLocalCache;
import com.wuxianedu.wechat.utils.GetJsonUtils;
import com.wuxianedu.wechat.utils.L;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
/**
 * 朋友圈页面
 * @author Administrator
 *
 */
public class FriendsActivity extends BaseActivity{
	private ImageView bg,idImage,childUserimg,imag1,imag2,imag3;
	private TextView userName,childUsername,childShuo;
	private Friends friends;
	private ListView listView;
	private List<Friends> list = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_friends;
	}
	
	@Override
	protected void init() {
		setTvMid(R.string.pyq_title);
		ImageView imagLeft = (ImageView) findViewById(R.id.tile_butleft);
		View listViewHead = LayoutInflater.from(FriendsActivity.this).inflate(R.layout.activity_friends_head, null);
		bg = (ImageView) listViewHead.findViewById(R.id.friends_bg_image);
		idImage = (ImageView) listViewHead.findViewById(R.id.friends_id_image);
		userName = (TextView) listViewHead.findViewById(R.id.user_name);
		listView = (ListView) findViewById(R.id.friends_list);
		//解析朋友圈的json文件
		parseJson(GetJsonUtils.getJsonString(this, "userFriendsCircle.js"));
		//添加头布局
		listView.addHeaderView(listViewHead, null, false);
		//从SD卡中取数据
		UserInFo userInFo = (UserInFo) FileLocalCache.getSerializableData(this, Constants.USER_INFO);
		//获取好友的用户名 并显示出来
		String name = userInFo.getUserName();
		userName.setText(name);
		//获取好友的头像并从网上加载显示出
		String head = userInFo.getHead();
		CoreUtils.getPicture(head,idImage);
		//显示标题栏的右边按钮
		setRightImage(R.drawable.icon_talk,new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//图标的点击
			}
		});
		listView.setAdapter(new FrientAdapter(this,list));
		imagLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	//朋友圈的json解析
	private void parseJson(String string){
		try {
			JSONObject jsonobject = new JSONObject(string);
			int status = jsonobject.getInt("status");
			if(status != 0){
				return;
			}
			//本用户的朋友圈的背景
			String backGround = jsonobject.getString("backGround");
			CoreUtils.getPicture(backGround, bg);
			JSONArray jsonArray = jsonobject.getJSONArray("friends");
			
			for(int i = 0;i < jsonArray.length();i ++){
				friends = new Friends();
				List<String> imagList = new ArrayList<>();
				JSONObject jsonobject1 = jsonArray.getJSONObject(i);
				//好友的用户名
				friends.setUserName(jsonobject1.getString("userName"));
				//好友的头像
				friends.setHead(jsonobject1.getString("head"));
				//
				friends.setContent(jsonobject1.getString("content"));
				//发表的时间
				friends.setTime(DateUtil.parseStringDate(DateUtil.DATE_SDF, jsonobject1.getString("time")));
				//朋友圈的图片
				JSONArray jsonarray = jsonobject1.getJSONArray("images");
				
				for(int j = 0; j < jsonarray.length();j++){
					
					imagList.add(jsonarray.getString(j));
				}	
				friends.setImageLists(imagList);
				list.add(friends);
			}
			L.e(list);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	

}
