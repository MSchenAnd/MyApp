package com.wuxianedu.wechat.activity.core;
import com.cn.R;
import com.wuxianedu.wechat.utils.CoreUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity{
	protected ImageView imagLeft,imageRight;
	protected TextView tv_mid,tv_right;
	protected ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(getContentView());
		
		imagLeft = (ImageView) findViewById(R.id.tile_butleft);
		imageRight = (ImageView) findViewById(R.id.tile_butright);
		tv_mid = (TextView) findViewById(R.id.title_tv_mid);
		tv_right = (TextView) findViewById(R.id.title_tv_right);
		init();
		CoreUtils.addActivity(this);
	}
	protected abstract void init();
	protected abstract int getContentView();
	
	/**
	 * 隐藏左按钮
	 */
	protected void gonImagLeft() {
		imagLeft.setVisibility(View.GONE);
	}
	
	/**
	 * 隐藏右按钮
	 */
	protected void gonImagRight() {
		imageRight.setVisibility(View.GONE);
	}
	/**
	 * 隐藏右边文字
	 */
	protected void gonTvRight() {
		tv_right.setVisibility(View.GONE);
	}
	
	/**
	 * 输入标题文字
	 * @param name
	 */
	protected void setTvMid(@StringRes int name) {
		tv_mid.setText(name);
	}
	protected void setTvMid(String name) {
		tv_mid.setText(name);
	}
	
	/**
	 * 显示右边的按钮
	 */
	protected void setRightImage(@DrawableRes int image,OnClickListener onClickListener) {
		imageRight.setVisibility(View.VISIBLE);
		imageRight.setImageResource(image);
		tv_right.setVisibility(View.GONE);
		imageRight.setOnClickListener(onClickListener);
	}
	
	/**
	 * 显示右边的文字
	 */
	protected void setRightText(@StringRes int text) {
		imageRight.setVisibility(View.GONE);
		tv_right.setText(text);
		
	}
	protected void setRightText(String text) {
		imageRight.setVisibility(View.GONE);
		tv_right.setText(text);
		
	}
	
	
	/**
	 * Toast提示信息
	 * @param context
	 * @param str
	 */
	protected void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 对话框，以显示进度
	 */
	public void showProgressDialog() {
		progress = new ProgressDialog(this);
		progress.setMessage("加载中......");
		progress.show();
	}
	
	/**
	 * 隐藏对话框
	 */
	public void hideProgressDialog(){
		progress.dismiss();
		
	}
	
}
