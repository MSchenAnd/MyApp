package com.wuxianedu.wechat.activity.adapter;

import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.DateUtil;
import com.wuxianedu.wechat.utils.L;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeiXinAdapter extends BaseAdapter{
	
	Context context;
	List<Person> list;
	public WeiXinAdapter(Context context,List<Person> list){
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.weixin_list, null);
			//头像
			viewHolder.idImg = (ImageView) convertView.findViewById(R.id.weixinlist_img);
			//最后一条消息
			viewHolder.lastnews = (TextView) convertView.findViewById(R.id.weixinlist_laststring);
			//未读消息数
			viewHolder.news = (TextView) convertView.findViewById(R.id.weixinlist_news);
			//用户名
			viewHolder.userName = (TextView) convertView.findViewById(R.id.weixinlist_username);
			//最后一条消息的时间
			viewHolder.time = (TextView) convertView.findViewById(R.id.weixinlist_lasttime);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//取出所对应的对象，给子条目写入值
		Person person = list.get(position);
		if(position != 0){
			//从网络上获取头像并写入
			CoreUtils.getPicture(person.getHead(), viewHolder.idImg);
		}else{
			Drawable drawable = context.getResources().getDrawable(R.drawable.icon_public);
			viewHolder.idImg.setImageDrawable(drawable);
			
		}
		//写入用户名
		viewHolder.userName.setText(person.getUserName());
		//写入最后一条消息
		viewHolder.lastnews.setText(person.getLastStr());
		L.e(person.getNewsNum());
		//写入未读消息数
		if(person.getNewsNum() == 0){
			viewHolder.news.setVisibility(View.GONE);
		}else{
			viewHolder.news.setVisibility(View.VISIBLE);
			viewHolder.news.setText(String.valueOf(person.getNewsNum()));
			
		}
		
		//写入最后一条消息的时间
		viewHolder.time.setText(DateUtil.getDay(person.getLastTime()));
		
		return convertView;
	}
	class ViewHolder{
		ImageView idImg;
		TextView userName,time,news,lastnews;
	}

}
