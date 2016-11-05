package com.wuxianedu.wechat.activity.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.cn.R;
import com.wuxianedu.wechat.activity.GroupChatActivity;
import com.wuxianedu.wechat.activity.NewFriendsActivity;
import com.wuxianedu.wechat.activity.UserDetailsActivity;
import com.wuxianedu.wechat.activity.adapter.ContactAdapter;
import com.wuxianedu.wechat.activity.bean.User;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.activity.mi.XmppManager;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.PingYinUtil;
import com.wuxianedu.wechat.widgt.SideBar;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 通讯录页面
 * @author Administrator
 *
 */
public class ContactFragment extends Fragment implements OnClickListener{
	private View view;
	private ListView listView;
	private List<User> list;
	private ContactAdapter adapter;
	private TextView addf,qunl,publi;
	private boolean isisVisible = false;
	private SideBar sideBar;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(isVisibleToUser){
			isisVisible = true;
		}else{
			isisVisible = false;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view  == null){
			view  = inflater.inflate(R.layout.fragment_contact, null);
			init(view);
		}
		return view;
	}
	
	private void init(View view){
		listView = (ListView) view.findViewById(R.id.contact_listview);
		//列表listView的头布局
		View head = LayoutInflater.from(getActivity()).inflate(R.layout.contact_list_head, null);
		//搜索框
		EditText edit = (EditText) head.findViewById(R.id.contact_search);
		//搜索框的监听器
		edit.addTextChangedListener(new mysearch());
		
		//添加朋友
		addf = (TextView) head.findViewById(R.id.contacts_username);
		//群聊
		qunl = (TextView) head.findViewById(R.id.contacts_username2);
		//公众号
		publi = (TextView) head.findViewById(R.id.contacts_username3);
		//设置textview中插入的图片的大小
		CoreUtils.setImageWidthHeight(getActivity(), 45, 45, addf, R.drawable.icon_addfriend);
		CoreUtils.setImageWidthHeight(getActivity(), 45, 45, qunl, R.drawable.icon_qunliao);
		CoreUtils.setImageWidthHeight(getActivity(), 45, 45, publi, R.drawable.icon_public);
		//为listView添加头布局
		listView.addHeaderView(head);
		//设置点击监听
			addf.setOnClickListener(this);
			qunl.setOnClickListener(this);
			publi.setOnClickListener(this);
		TextView textView = (TextView) view.findViewById(R.id.contact_text);
		//索引
		sideBar = (SideBar) view.findViewById(R.id.contact_sidebar);
		sideBar.setListView(listView);
		sideBar.setTextView(textView);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent itent = new Intent(getActivity(),UserDetailsActivity.class);
				User user = adapter.getList().get(position -1 );
				itent.putExtra("user",user);
				startActivity(itent);
			}
		});
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
				new AlertDialog.Builder(getContext())
						.setItems(new String[]{"删除好友"}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == 0){
									String userName = list.get(position - 1).getUserName();
									if (XmppManager.getInstance().removeFriend(userName)){
										list.remove(position - 1);
										adapter.setList(list);
										Toast.makeText(getContext(),"删除好友成功",Toast.LENGTH_SHORT);
									}else {
										Toast.makeText(getContext(),"删除好友失败",Toast.LENGTH_SHORT);
									}
								}
							}
						}).show();
				return true;
			}
		});
	}

	//搜索框的监听
		class mysearch implements TextWatcher{
				private List<User> superList = new ArrayList<>();
				private List<User> subList = new ArrayList<>();
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
					
					String string = s.toString();
					/**
					 * 搜索框的内容查找
					 */
					if(CoreUtils.isEmpty(string)){//搜索框没有值时
						adapter.setList(list);
						addf.setVisibility(View.VISIBLE);
						qunl.setVisibility(View.VISIBLE);
						publi.setVisibility(View.VISIBLE);
					}else{ 
						addf.setVisibility(View.GONE);
						qunl.setVisibility(View.GONE);
						publi.setVisibility(View.GONE);
						superList.clear();
						subList.clear();
						for (User person : list) {//搜索框有值时
									String name = person.getUserName();
									if(CoreUtils.isChinese(string)){//搜索框输入的值为中文
										if(string.startsWith(name))//第一个字
										{
											superList.add(person);
										}else if(name.contains(string)){//包含字
													subList.add(person);
												}
									}else{//搜索框输入的值为英文
										
											String pinyin = PingYinUtil.getPingYin(person.getUserName());
											if(string.startsWith(pinyin)){
												superList.add(person);
											}else if(pinyin.contains(string)){
												superList.add(person);
											}
									}
								}
					addf.setVisibility(View.GONE);
					qunl.setVisibility(View.GONE);
					publi.setVisibility(View.GONE);
					superList.addAll(subList);
					//更新adapter中的list的值
					adapter.setList(superList);
					}
				}
		}

	//新朋友、群聊、公众号的点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.contacts_username:
			Intent itent = new Intent(getActivity(),NewFriendsActivity.class);
			startActivity(itent);
			break;
		case R.id.contacts_username2:
			Intent itent1 = new Intent(getActivity(),GroupChatActivity.class);
			itent1.putExtra("list", (Serializable)list);
			startActivity(itent1);
			break;
		case R.id.contacts_username3:
			break;
		}
		
	}

	@Override
	public void onResume() {
		list = XmppManager.getInstance().getFriendList();
		if (list == null){
			sideBar.setVisibility(SideBar.GONE);
		}else {
			sideBar.setVisibility(SideBar.VISIBLE);
		}
		//列表的适配器
		adapter = new ContactAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		super.onResume();
	}
}
