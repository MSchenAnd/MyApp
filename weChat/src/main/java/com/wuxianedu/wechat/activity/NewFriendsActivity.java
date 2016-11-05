package com.wuxianedu.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cn.R;
import com.wuxianedu.wechat.activity.adapter.NewFriendsAdapter;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.activity.mi.XmppManager;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.DateUtil;
import com.wuxianedu.wechat.utils.GetJsonUtils;
import com.wuxianedu.wechat.widgt.ClearEditText;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
 
public class NewFriendsActivity extends BaseActivity{
	private  NewFriendsAdapter adapter;
	private View head;
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_newfriend;
	}
	@Override
	protected void init() {
		//标题
		setTvMid(R.string.newfriend_titletext);
		//右边的按钮
		setRightText(R.string.newfriend_righttext);
		listView = (ListView) findViewById(R.id.newfriend_listview);
		ImageButton backe = (ImageButton) findViewById(R.id.tile_butleft);
		head = LayoutInflater.from(this).inflate(R.layout.newfriend_head, null);
		List<String> list = new ArrayList<>();
		adapter = new NewFriendsAdapter(NewFriendsActivity.this, list);
		listView.setAdapter(adapter);
		listView.addHeaderView(head);
		backe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		//搜索按钮的监听事件
		head.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ClearEditText editText = (ClearEditText) head.findViewById(R.id.newfriend_search);
				String searchText = editText.getText().toString();
				if (!CoreUtils.isEmpty(searchText)){
					List<String> list = XmppManager.getInstance().searchUser(searchText);
					adapter.setList(list);
				}
			}
		});
	}

	
	




















	/*//解析添加朋友（addFriends）json文件
	public List<Person> parseJson(String string){
		List<Person> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(string);
			int status = jsonObject.getInt("status");
			if(status != 0){
				return null;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("contacts");
			for(int i = 0;i < jsonArray.length(); i++){
				Person person = new Person();
				JSONObject jsonObject1 = jsonArray.getJSONObject(i);
				person.setUserName(jsonObject1.getString("name"));//用户名
				person.setArea(jsonObject1.getString("area"));
				person.setWeCode(jsonObject1.getLong("weCode"));
				person.setHead(jsonObject1.getString("head"));//用户头像
				person.setAutograph(jsonObject1.getString("autograph"));
				person.setLastStr(jsonObject1.getString("lastStr"));
				person.setNewsNum(jsonObject1.getInt("newsNum"));
				person.setIsAdd(jsonObject1.getBoolean("isAdd"));
				person.setLastTime(DateUtil.parseStringDate(DateUtil.DATE_SDF, jsonObject1.getString("lastTime")));
				//
				JSONArray jsonArray2 = jsonObject1.getJSONArray("images");
				List<String> listimg = new ArrayList<>();
				//获取图片的List
				for(int j = 0;j< jsonArray2.length();j ++){
					listimg.add(jsonArray2.getString(j));
				}
				person.setImagList(listimg);
				list.add(person);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}*/
	
}
