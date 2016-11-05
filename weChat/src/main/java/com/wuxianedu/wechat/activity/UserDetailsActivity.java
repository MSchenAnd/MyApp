package com.wuxianedu.wechat.activity;

import com.cn.R;
import com.wuxianedu.wechat.activity.adapter.GridAdapter;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.activity.bean.User;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.utils.CoreUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 用户详情
 * @author Administrator
 *
 */
public class UserDetailsActivity extends BaseActivity implements OnClickListener{
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	//绑定布局
		@Override
		protected int getContentView() {
			
			return R.layout.activity_userdetails;
		}
	@Override
	protected void init() {
		//标题文字
		setTvMid(R.string.userdetails_title);
		//标题栏的右边按钮
		setRightImage(R.drawable.icon_more,null);
		
		//取出跳转时传入的值
		Intent intent = getIntent();
		ImageButton back = (ImageButton) findViewById(R.id.tile_butleft);
		Button message = (Button) findViewById(R.id.userdetails_message);
		user = (User) intent.getSerializableExtra("user");
		//头像
		ImageView headImage = (ImageView) findViewById(R.id.weixinlist_img);
		//用户名
		TextView userName = (TextView) findViewById(R.id.userdetails_username);
		//微信号
		TextView wechatnum = (TextView) findViewById(R.id.userdetails_wechatnum);
		//地区
		TextView erea = (TextView) findViewById(R.id.userdetails_area_text);
		//个性签名
		TextView signature = (TextView) findViewById(R.id.userdetails_signature_text);
		//个人相册
		GridView gallery = (GridView) findViewById(R.id.userdetails_gallery);
		userName.setText(user.getUserName());
		wechatnum.append(String.valueOf(user.getUserName()));
		message.setOnClickListener(this);
		back.setOnClickListener(this);
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tile_butleft:
			finish();
			break;
		case R.id.userdetails_message:
			Intent intent = new Intent(this,ChatActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
			finish();
			break;
		}
		
	}

}
