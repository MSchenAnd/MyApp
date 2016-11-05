package com.wuxianedu.wechat.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cn.R;
import com.wuxianedu.wechat.activity.adapter.SubscriptionAdapter;
import com.wuxianedu.wechat.activity.bean.Subscription;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.utils.DateUtil;
import com.wuxianedu.wechat.utils.GetJsonUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
/**
 * 订阅号
 * @author Administrator
 *
 */
public class SubscriptionActivity extends BaseActivity{
	private List<Subscription> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_subscription;
	}
	
	@Override
	protected void init() {
		setTvMid(R.string.subscription_title);
		//返回键
		ImageView imagLeft = (ImageView) findViewById(R.id.tile_butleft);
		list = new ArrayList<>();
		//解析json文件
		list = paseJson(GetJsonUtils.getJsonString(this, "subscribe.js"));
		ListView listView = (ListView) findViewById(R.id.subscription_list);
		listView.setAdapter(new SubscriptionAdapter(this, list));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(SubscriptionActivity.this,SubscriptionDetailsActivity.class);
				intent.putExtra("subscription", (Serializable)list.get(position));
				startActivity(intent);
			}
		});
		//返回键的点击事件
		imagLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

	
	//解析json文件
	public List<Subscription> paseJson(String string){
		List<Subscription> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(string);
			int status = jsonObject.getInt("status");
			if(status != 0){
				return null;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("subscribe");
			for(int i = 0; i < jsonArray.length(); i ++){
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				Subscription subscription = new Subscription();
				subscription.setName(jsonObject2.getString("name"));
				subscription.setWeCode(jsonObject2.getLong("weCode"));
				subscription.setLastStr(jsonObject2.getString("lastStr"));
				subscription.setNewsNum(jsonObject2.getInt("newsNum"));
				
				subscription.setLastTime(DateUtil.parseStringDate(DateUtil.DATE_SDF, jsonObject2.getString("lastTime")));
				list.add(subscription);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	

}
