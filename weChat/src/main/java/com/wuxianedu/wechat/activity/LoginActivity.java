package com.wuxianedu.wechat.activity;
import com.cn.R;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.activity.core.MyApplication;
import com.wuxianedu.wechat.activity.mi.ChatService;
import com.wuxianedu.wechat.activity.mi.XmppManager;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.SPUtils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 登录页面
 * @author Administrator
 *
 */
public class LoginActivity extends BaseActivity{

	private EditText userName,password;
	private Button loginbut,registered_but;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTvMid(R.string.title_text);
		gonTvRight();
		userName = (EditText) findViewById(R.id.login_user);
		password = (EditText) findViewById(R.id.login_pass);
		loginbut = (Button) findViewById(R.id.login_but);
		registered_but = (Button) findViewById(R.id.login_registered);
		imagLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		registered_but.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转至注册页面
				CoreUtils.startActivity(LoginActivity.this, RegisteredActivity.class);
			}
		});
		loginbut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				judgment();
			}
		});
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_login;
	}
	
	//TODO 输入的内容的要求判断
	private void judgment(){
		String user,pass;
		//用户名
		user = userName.getText().toString();
		//密码
		pass = password.getText().toString();
		
		if(CoreUtils.isEmpty(user)){
			showToast("手机号码不能为空");
			return;
		}
		if(CoreUtils.isEmpty(pass)){
			showToast("密码不能为空");
			return;
		}
		if(pass.length() <= 3){
			showToast("密码太短");
			return;
		}
		login(user, pass);
	}
	private void login(final String name,final String pwd){
		new AsyncTask<String, Void, Boolean>(){
			@Override
			protected void onPreExecute() {
				showProgressDialog();
			}

			@Override
			protected Boolean doInBackground(String... params) {
				return XmppManager.getInstance().login(params[0], params[1]);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				hideProgressDialog();
				if (!result){
					showToast("登录失败");
					return;
				}
				MyApplication.username = name;
				SPUtils.put(LoginActivity.this, Constants.USER_NAME, name);
				SPUtils.put(LoginActivity.this, Constants.USER_PWD, pwd);
				Intent intent = new Intent(LoginActivity.this, ChatService.class);
				startService(intent);
				CoreUtils.startActivity(LoginActivity.this, MainActivity.class);
				LoginActivity.this.finish();
			}

		}.execute(name, pwd);
	}


	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

}
