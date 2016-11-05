package com.wuxianedu.wechat.activity;


import com.cn.R;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.activity.core.MyApplication;
import com.wuxianedu.wechat.activity.mi.XmppManager;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.SPUtils;


import android.graphics.drawable.Drawable;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jivesoftware.smack.packet.IQ;

/**
 * 注册页面
 * @author Administrator
 *
 */
public class RegisteredActivity extends BaseActivity{
	private EditText phone,password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//标题
		setTvMid(R.string.title_regtext);
		//隐藏右边的按钮
		gonTvRight();
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_registered;
	}
	@Override
	protected void init() {
		//输入手机号的输入框
		phone = (EditText) findViewById(R.id.registered_user);
		//输入密码的输入框
		password = (EditText) findViewById(R.id.registered_pass);
		//注册按钮
		Button registered = (Button) findViewById(R.id.registered_but);
		//初始化背景
		registered.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				judge();
			}
		});
	}

	private void judge() {
		String user = phone.getText().toString();
		String paw = password.getText().toString();
		if(CoreUtils.isEmpty(user)){
			showToast("手机号码不能为空");
			return;
		}
		if(CoreUtils.isEmpty(paw)){
			showToast("密码不能为空");
			return;
		}
		if(paw.trim().length() < 6){
			showToast("密码不能少于6位");
			return;
		}

		//注册
		reg(user, paw);
	}

	/**
	 * 注册
	 * @param name  用户名
	 * @param pwd  密码
	 * @param pwd  验证码
	 */
	private void reg(final String name, final String pwd){
		new AsyncTask<String,Void,IQ>() {

			@Override
			protected void onPreExecute() {
				showProgressDialog();
			}

			@Override
			protected IQ doInBackground(String... params) {
				IQ result = null;
				try {
					Thread.sleep(500);
					result = XmppManager.getInstance().reg(name, pwd);
					if (result != null && result.getType() == IQ.Type.RESULT){
						XmppManager.getInstance().login(params[0], params[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(IQ result) {
				hideProgressDialog();
				if (result == null){
					showToast("注册失败");
				}else if(result.getType() == IQ.Type.ERROR){
					if (result.getError().toString().equalsIgnoreCase("conflict(409)")){
						showToast("账号已存在!");
					}else{
						showToast("注册失败");
					}
				}else if(result.getType() == IQ.Type.RESULT){
					showToast("注册成功");
					MyApplication.username = name;
					SPUtils.put(RegisteredActivity.this, Constants.USER_NAME, name);
					SPUtils.put(RegisteredActivity.this, Constants.USER_PWD, pwd);
					CoreUtils.startActivity(RegisteredActivity.this, MainActivity.class);
					RegisteredActivity.this.finish();
				}
			}
		}.execute(name, pwd);
	}


}
