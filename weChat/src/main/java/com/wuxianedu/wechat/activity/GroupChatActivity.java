package com.wuxianedu.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.adapter.ContactAdapter;
import com.wuxianedu.wechat.activity.adapter.GroupAdapter;
import com.wuxianedu.wechat.activity.adapter.GroupChatAdapter;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.activity.core.BaseActivity;

import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.widgt.HorizontalListView;
import com.wuxianedu.wechat.widgt.SideBar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
/**
 * 群聊页面
 * @author Administrator
 *
 */
public class GroupChatActivity extends BaseActivity{
	private int number = 0;
	private GroupChatAdapter adapter;
	private List<Person> list;
	private ListView listView;
	private HorizontalListView horizontalListView;
	private List<Person> addlist = new ArrayList<>();
	private GroupAdapter groupadapter;
	private TextView addftext;
	private boolean canDel;
	private EditText edit;
	private boolean isTextEmpty;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_groupchat;
	}
	@Override
	protected void init() {
		setTvMid(R.string.groupchate_title);
		setRightText(R.string.groupchate_titleright_text);
		//好友列表
		listView = (ListView) findViewById(R.id.groupchat_list);
		View head = LayoutInflater.from(this).inflate(R.layout.groupchat_list_head, null);
		TextView text = (TextView) findViewById(R.id.groupchat_text);
		//添加头布局
		listView.addHeaderView(head);
		//搜索框
		edit = (EditText) findViewById(R.id.groupchat_search);
		//搜索框内容改变监听器
		edit.addTextChangedListener(new Mysearch());
		addftext = (TextView) head.findViewById(R.id.groupchate_choose_group);
		//索引
		SideBar sideBar = (SideBar) findViewById(R.id.contact_sidebar);
		sideBar.setListView(listView);
		sideBar.setTextView(text);
		new MyAsyncTask().execute();
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0){
					return;
				}
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.groupchat_checkbox);
				boolean check = checkBox.isChecked();
				Person person = adapter.getList().get(position-1);
				if(!check){
					addimage(person);
				}else{
					removeimage(person);
				}
			}
		});
		
		//TODO 水平的listView(搜索框中的图片)
		horizontalListView = (HorizontalListView) findViewById(R.id.groupchar_hlistview);
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Person person = addlist.get(position);
				removeimage(person);
				
			}
		});	
		
		//TODO 点击回退建，关闭此页面
		ImageButton back = (ImageButton) findViewById(R.id.tile_butleft);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}
	
	public void addimage(Person person){
		if(addlist.size() != 0){
			addlist.get(addlist.size()-1).setAlpha(1.0F);
			canDel = false;
		}
		//person中checkbox的状态为选中
		person.setCheck(true);
		//添加图片
		addlist.add(person);
		//动态设置宽度
		changWidth();
		groupadapter = new GroupAdapter(GroupChatActivity.this, addlist);
		horizontalListView.setAdapter(groupadapter);
		//更新adapter
		adapter.notifyDataSetChanged();
		//显示最后一张
		horizontalListView.setSelection(addlist.size()-1);
	}
	public void removeimage(Person person){
		
		//person中checkbox的状态为未选中
		person.setCheck(false);
		//删除图片
		addlist.remove(person);
		changWidth();
		groupadapter = new GroupAdapter(GroupChatActivity.this, addlist);
		horizontalListView.setAdapter(groupadapter);
		adapter.notifyDataSetChanged();
		horizontalListView.setSelection(addlist.size()-1);
	}
	
	//TODO 按删除键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_DEL && addlist.size() != 0 && isTextEmpty){
			Person person = addlist.get(addlist.size()-1);
				if(canDel){
					person.setAlpha(1.0F);
					canDel = false;
					removeimage(person);
				}else {
					person.setAlpha(0.5F);
					canDel = true;
					horizontalListView.setSelection(addlist.size()-1);
				}
				groupadapter.notifyDataSetChanged();
		}
			if(CoreUtils.isEmpty(edit.getText().toString())){
				isTextEmpty = true;
		}
		return super.onKeyDown(keyCode, event);
	}


	
	public void changWidth(){
		View view = findViewById(R.id.groupchar_searchigm);
		if(addlist.size() == 0){
			//显示搜索图片
			view.setVisibility(View.VISIBLE);
			setRightText("确定");
			return;
			
		}
		RelativeLayout.LayoutParams linear = (LayoutParams) horizontalListView.getLayoutParams();
		//新的宽度
		int newWith = addlist.size()*CoreUtils.dipToPixel(this, 50);
		//最大宽度
		int maxWith = CoreUtils.displayWidthHeight(this)[0] - CoreUtils.dipToPixel(this, 80);
		
		if(newWith > maxWith){
			linear.width = maxWith;
		}else{
			linear.width = newWith;
		}
		horizontalListView.setLayoutParams(linear);
		
		setRightText("确定(" + addlist.size() + ")");
		//隐藏搜索图片
		view.setVisibility(View.GONE);
	}
	
	class MyAsyncTask extends AsyncTask<Void, Void, List<Person>>{
		
		@Override
		protected void onPreExecute() {
			showProgressDialog();//显示进度框
			super.onPreExecute();
		}
		@Override
		protected List<Person> doInBackground(Void... params) {
			Intent intent = getIntent();
			//从intent中取值
			list = (List<Person>) intent.getSerializableExtra("list");
			return list;
		}
		@Override
		protected void onPostExecute(List<Person> result) {
		
			adapter = new GroupChatAdapter(GroupChatActivity.this,result);
			listView.setAdapter(adapter);
			hideProgressDialog();//隐藏进度框
		}
		
	}
	
	//搜索框的监听
			class Mysearch implements TextWatcher{
					private List<Person> superList = new ArrayList<>();
					private List<Person> subList = new ArrayList<>();
					
					//改变之前
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {			
					}
					//改变时
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}
					//改变后
					@Override
					public void afterTextChanged(Editable s) {
						isTextEmpty = false;
						String string = s.toString();
						/**
						 * 搜索框的内容查找
						 */
						if(CoreUtils.isEmpty(string)){//搜索框没有值时
							adapter.setList(list);
							addftext.setVisibility(View.VISIBLE);
						}else{ 
							superList.clear();
							subList.clear();
							for (Person person : list) {//搜索框有值时
										String name = person.getUserName();
										if(CoreUtils.isChinese(string)){//搜索框输入的值为中文
											if(string.startsWith(name))//第一个字
											{
												superList.add(person);
											}else if(name.contains(string)){//包含字
														subList.add(person);
													}
										}else{//搜索框输入的值为英文
											
												String pinyin = person.getNamePinyin();
												if(string.startsWith(pinyin)){
													superList.add(person);
												}else if(pinyin.contains(string)){
													superList.add(person);
												}
										}
									}
							
						superList.addAll(subList);
						//更新adapter中的list的值
						adapter.setList(superList);
						addftext.setVisibility(View.GONE);
						}
					}
			}

}
