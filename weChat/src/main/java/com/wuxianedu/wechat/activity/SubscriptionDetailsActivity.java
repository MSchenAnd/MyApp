package com.wuxianedu.wechat.activity;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cn.R;
import com.wuxianedu.wechat.activity.adapter.SubscriptionAdapterDetails;
import com.wuxianedu.wechat.activity.bean.Subscription;
import com.wuxianedu.wechat.activity.bean.SubscriptionDetails;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.DateUtil;
import com.wuxianedu.wechat.utils.GetJsonUtils;
import com.wuxianedu.wechat.utils.L;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class SubscriptionDetailsActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_subscriptiondetails;
	}

	@Override
	protected void init() {
		//从intent中取值
		Intent intent = getIntent();
		Subscription subscription = (Subscription) intent.getSerializableExtra("subscription");
		//写入标题
		setTvMid(subscription.getName());
		//把json解析出来，并用list集合保存
		List<SubscriptionDetails> list = parseJson(GetJsonUtils.getJsonString(this, "subscribeDetails.js"));
		ListView listView = (ListView) findViewById(R.id.subscription_details);
		ImageView imagLeft = (ImageView) findViewById(R.id.tile_butleft);
		listView.setAdapter(new SubscriptionAdapterDetails(this, list));
		imagLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
//解析json文件
	private List<SubscriptionDetails> parseJson(String string){
		List<SubscriptionDetails> list = new ArrayList<>();
		try {
			JSONObject jsonObject1 = new JSONObject(string);
			int status = jsonObject1.getInt("status");
			if(status != 0){
				return null;
			}
			JSONArray jsonArray = jsonObject1.getJSONArray("subscribeDetails");
			for(int i = 0;i < jsonArray.length();i ++){
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				SubscriptionDetails subscriptionDetails = new SubscriptionDetails();
				subscriptionDetails.setContent(jsonObject2.getString("content"));
				subscriptionDetails.setImage(jsonObject2.getString("image"));
				subscriptionDetails.setTime(DateUtil.parseStringDate(DateUtil.DATE_SDF, jsonObject2.getString("time")));
				subscriptionDetails.setTitle(jsonObject2.getString("title"));
				list.add(subscriptionDetails);
			}
			L.e(list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
