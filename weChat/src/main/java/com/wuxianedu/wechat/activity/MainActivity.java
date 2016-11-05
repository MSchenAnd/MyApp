package com.wuxianedu.wechat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cn.R;
import com.wuxianedu.wechat.activity.bean.UserInFo;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.activity.fragment.ContactFragment;
import com.wuxianedu.wechat.activity.fragment.FindFragment;
import com.wuxianedu.wechat.activity.fragment.MeFragment;
import com.wuxianedu.wechat.activity.fragment.WeiXinFragment;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.FileLocalCache;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
/**
 * 主页面（微信、通讯录、发现、我）
 * @author Administrator
 *
 */
public class MainActivity extends BaseActivity{
	private Fragment[] fragment = new Fragment[4];
	private RadioButton[] radioButton = new RadioButton[4];
	private ViewPager viewPager;
	private RadioGroup radioGroup;
	private int fragmentIndx = 0;
//	private UserInFo userInFor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}
	@Override
	protected void init() {
		fragment[0] = new WeiXinFragment();
		fragment[1] = new ContactFragment();
		fragment[2] = new FindFragment();
		fragment[3] = new MeFragment();
		radioButton[0] = (RadioButton) findViewById(R.id.main_rb_weixin);
		radioButton[1] = (RadioButton) findViewById(R.id.main_rb_contact);
		radioButton[2] = (RadioButton) findViewById(R.id.main_rb_find);
		radioButton[3] = (RadioButton) findViewById(R.id.main_rb_me);
		viewPager = (ViewPager) findViewById(R.id.main_viewpager);
		//屏幕下方的单选按钮（微信、通讯录、发现、我）
		radioGroup = (RadioGroup) findViewById(R.id.main_rg);
		viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
		setViewPagerChang();
		radioButtonChang();
		//隐藏左边的按钮
		gonImagLeft();
		
		//设置默认的fragment(微信页面)
		setImag(fragmentIndx);
		
	}
	//TODO viewPager的适配器
	class MyViewPagerAdapter extends FragmentPagerAdapter{

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public int getCount() {
			return fragment.length;
		}
		@Override
		public Fragment getItem(int arg0) {
			return fragment[arg0];
		}
	}
	
	//TODO 手势滑动切换fragment时下方的按钮相应的也一起改变
	public void setViewPagerChang(){
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				radioButton[arg0].setChecked(true);		
			}
			//切换（滚动）的速度
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
		});
	}
	//TODO 点击按钮时 改变现实的fragment
	public void radioButtonChang(){
		
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
			switch (checkedId) {
			case R.id.main_rb_weixin:
				fragmentIndx = 0;
				break;
			case R.id.main_rb_contact:
				fragmentIndx = 1;
				
				break;
			case R.id.main_rb_find:
				fragmentIndx = 2;
				break;
				
			case R.id.main_rb_me:
				fragmentIndx = 3;
				break;
			default:
				fragmentIndx = 0;
				break;
			}
			//定制当前页面
			viewPager.setCurrentItem(fragmentIndx, false);
			//调用设置fragment的标题
			setImag(fragmentIndx);
			}
		});
	}
	
	//TODO 设置每一个fragment所对应的标题栏
	public void setImag(int fragmentIndx){
		gonImagLeft();
		switch (fragmentIndx) {
		case Constants.WEIXIN://微信 页面
			setTvMid(R.string.main_weixin);
			//设置图片 并设置点击事件
			setRightImage(R.drawable.icon_add,new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					creatPopuWindow();
					
				}
			});
			break;
		case Constants.CONTACT://通讯录 页面
			setTvMid(R.string.main_contact);
			setRightImage(R.drawable.contact_menu_addfriend,new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this,NewFriendsActivity.class);
					startActivity(intent);
				}
			});
			break;
		case Constants.FIND://发现 页面
			setTvMid(R.string.main_find);
			gonImagRight();
			break;
		case Constants.ME://我的  页面
			setTvMid(R.string.main_me);
			gonImagRight();
			break;
		default:
			break;
		}
	}
	
	//TODO 弹窗（对话框）
	private void creatPopuWindow(){
		View view = getLayoutInflater().inflate(R.layout.add_weixin_listview, null);
		ListView listView = (ListView) view.findViewById(R.id.weixin_popu);
		listView.setAdapter(creatListView());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case Constants.GROUP:
					Intent intent = new Intent(MainActivity.this,GroupChatActivity.class);
					startActivity(intent);
					break;
				case Constants.ADD:
					Intent intent1 = new Intent(MainActivity.this,NewFriendsActivity.class);
					startActivity(intent1);
					break;
				default:
					break;
				}
				
			}
		});
		//创建弹窗
		PopupWindow popu = new PopupWindow(view, 550, ViewGroup.LayoutParams.WRAP_CONTENT, true);
		//点击以外的地方时关闭弹窗
		popu.setOutsideTouchable(true);
		popu.setBackgroundDrawable(new BitmapDrawable());
		//显示在按钮的下面
		popu.showAsDropDown(imageRight);
	}
	
	//TODO 弹窗里面的内容
	private SimpleAdapter creatListView(){
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("image", R.drawable.icon_menu_group);
		map.put("text", getResources().getString(R.string.main_popu_fqql));
		list.add(map);
		
		map = new HashMap<>();
		map.put("image", R.drawable.icon_menu_addfriend);
		map.put("text", getResources().getString(R.string.main_popu_addf));
		list.add(map);
		
		map = new HashMap<>();
		map.put("image", R.drawable.icon_menu_sao);
		map.put("text", getResources().getString(R.string.main_popu_sao));
		list.add(map);
		
		map = new HashMap<>();
		map.put("image", R.drawable.abv);
		map.put("text",getResources().getString(R.string.main_popu_pay));
		list.add(map);
		
		//listView的适配器
		SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,
												list, 
												R.layout.add_weixin_listview_child,
												new String[]{"image","text"},
												new int[]{R.id.weixin_child_image,R.id.weixin_child_text});
		
		
		
		return adapter;
	}
	
	//TODO 返回用户的信息
	public UserInFo getUserInFo(){
		//从SD卡中取出用户信息
		UserInFo userInFor = (UserInFo) FileLocalCache.getSerializableData(this, Constants.USER_INFO);
		if(userInFor == null){
			Log.e("-main-", "userinfo == null");
		}
		return userInFor;
	}

}
