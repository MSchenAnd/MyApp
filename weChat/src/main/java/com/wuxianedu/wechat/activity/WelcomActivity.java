package com.wuxianedu.wechat.activity;


import com.cn.R;
import com.wuxianedu.wechat.activity.bean.UserInFo;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.FileLocalCache;
import com.wuxianedu.wechat.utils.SPUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

/**
 * 欢迎页面
 * @author Administrator
 *
 */
public class WelcomActivity extends  AppCompatActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_welcom);
		handler.sendEmptyMessageDelayed(0, 3000L);
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (SPUtils.get(WelcomActivity.this,Constants.USER_NAME,null) != null){
				Intent intent = new Intent(WelcomActivity.this,MainActivity.class);
				startActivity(intent);
			}else{
				CoreUtils.startActivity(WelcomActivity.this, LoginActivity.class);
			}
			finish();
		};
		
		
	};

}
