package com.wuxianedu.wechat.activity.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cn.R;
import com.wuxianedu.wechat.activity.ChatActivity;
import com.wuxianedu.wechat.activity.SubscriptionActivity;
import com.wuxianedu.wechat.activity.adapter.WeiXinAdapter;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.utils.DateUtil;
import com.wuxianedu.wechat.utils.GetJsonUtils;
import com.wuxianedu.wechat.utils.L;
import com.wuxianedu.wechat.utils.PingYinUtil;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * 微信 页面
 * @author Administrator
 *
 */
public class WeiXinFragment extends Fragment{
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.fragment_weixin, null);
			init();
		}
		L.e("------------WeiXinFragmentonCreateView-----------------");
		return view;
	}
	
	 private void init(){
		 //微信的内容
		 ListView listView = (ListView) view.findViewById(R.id.weixin_listview);
		 final List<Person> list = paseJson(GetJsonUtils.getJsonString(getActivity(), "wechat.js"));
		 
		 listView.setAdapter(new WeiXinAdapter(getActivity(), list));
		 listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(list.get(position).getWeCode()!= 10001L ){
					Intent itent = new Intent(getActivity(),ChatActivity.class);
					itent.putExtra("person", list.get(position));
					startActivity(itent);
				}else{
					Intent itent = new Intent(getActivity(),SubscriptionActivity.class);
					startActivity(itent);
				}
				/*
				Intent itent = new Intent(getActivity(),ChatActivity.class);
				itent.putExtra("person", list.get(position));
				startActivity(itent);*/
				
			}
		});
	 }
	 
	 
	//解析JSON
		public List<Person> paseJson(String string){
			List<Person> list1 = new ArrayList<>();
			try {
				//创建一个json对象：json1
				JSONObject jsonobject1 = new JSONObject(string);
				int status = jsonobject1.getInt("status");
				if(status != 0){
					return null;
				}
				//创建json1对象下的json数组：jsonarray1
				JSONArray jsonArray1 = jsonobject1.getJSONArray("contacts");
				for(int i = 0;i< jsonArray1.length();i ++){
					//创建json1对象下的jsonarray1数组里面的json对象
					JSONObject jsonobject2 = jsonArray1.getJSONObject(i);
					//person对象
					Person person = new Person();
					person.setUserName(jsonobject2.getString("name"));//用户名
					person.setArea(jsonobject2.getString("area"));
					person.setWeCode(jsonobject2.getLong("weCode"));
					person.setHead(jsonobject2.getString("head"));//用户头像
					person.setAutograph(jsonobject2.getString("autograph"));
					person.setLastStr(jsonobject2.getString("lastStr"));
					person.setNewsNum(jsonobject2.getInt("newsNum"));
					person.setIsAdd(jsonobject2.getBoolean("isAdd"));
					person.setLastTime(DateUtil.parseStringDate(DateUtil.DATE_SDF, jsonobject2.getString("lastTime")));
					//
					JSONArray jsonArray2 = jsonobject2.getJSONArray("images");
					List<String> listimg = new ArrayList<>();
					//获取图片的List
					for(int j = 0;j< jsonArray2.length();j ++){
						listimg.add(jsonArray2.getString(j));
					}
					person.setImagList(listimg);
				
					list1.add(person);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list1;
		}
	
}
