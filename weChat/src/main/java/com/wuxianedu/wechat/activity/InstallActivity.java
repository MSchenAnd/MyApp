package com.wuxianedu.wechat.activity;

import com.cn.R;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.FileLocalCache;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
/**
 * 设置页面
 * @author Administrator
 *
 */
public class InstallActivity extends BaseActivity{
	private Button exit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gonTvRight();
		setTvMid(R.string.install_title_text);
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_install;
	}
	@Override
	protected void init() {
		exit = (Button) findViewById(R.id.install_login_but);
		imagLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPupo();
				
				
			}
		});
		
	}
	
	
	private void showPupo(){
		View view = getLayoutInflater().inflate(R.layout.install_popuwindow, null);
		PopupWindow popu = new PopupWindow(view,500, 
				WindowManager.LayoutParams.WRAP_CONTENT, true);
		popu.setOutsideTouchable(true);
		popu.setBackgroundDrawable(new BitmapDrawable());
		popu.showAtLocation(view, Gravity.CENTER, 0, 0);
		TextView exitAcontent = (TextView) view.findViewById(R.id.content);
		TextView exitWeixin = (TextView) view.findViewById(R.id.exitweixin);
		exitAcontent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FileLocalCache.delSerializableData(InstallActivity.this,Constants.USER_INFO);
				Intent intent = new Intent(InstallActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
		exitWeixin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CoreUtils.finishActivity();
			}
		});
		
	}
	
	
	
	
	
	/*private void showDialog(){
	//	new AlertDialog.Builder(this).setView(R.layout.alert_dialog).show();
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		View view = getLayoutInflater().inflate(R.layout.alert_dialog, null);
		Button exitcontent = (Button) view.findViewById(R.id.setting_exitlogin);
		Button exitweixin = (Button) view.findViewById(R.id.setting_exit);
		exitcontent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FileLocalCache.delSerializableData(InstallActivity.this,Constants.USER_INFO);
				Intent intent = new Intent(InstallActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
		exitweixin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CoreUtils.finishActivity();
			}
		});
		
		dialog.setView(view);

		dialog.show();
		
	}*/

}
