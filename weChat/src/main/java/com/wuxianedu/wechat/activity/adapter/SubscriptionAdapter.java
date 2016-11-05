package com.wuxianedu.wechat.activity.adapter;

import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.bean.Subscription;
import com.wuxianedu.wechat.utils.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SubscriptionAdapter extends BaseAdapter{
	Context context;
	List<Subscription> list;
	public SubscriptionAdapter(Context context,List<Subscription> list){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.subscrips_list, null);
			viewHolder.userName = (TextView) convertView.findViewById(R.id.subscrips_username);
			viewHolder.news = (TextView) convertView.findViewById(R.id.subscrips_news);
			viewHolder.lastStr = (TextView) convertView.findViewById(R.id.subscrips_laststring);
			viewHolder.lastTime = (TextView) convertView.findViewById(R.id.subscrips_lasttime);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Subscription subscription = list.get(position);
		viewHolder.userName.setText(subscription.getName());
		if(subscription.getNewsNum() == 0){
			viewHolder.news.setVisibility(View.GONE);
		}else{
			viewHolder.news.setText(subscription.getNewsNum());
		}
		
		viewHolder.lastStr.setText(subscription.getLastStr());
		
		viewHolder.lastTime.setText(DateUtil.getDay(subscription.getLastTime()));
		
		return convertView;
	}
	class ViewHolder{
		
		TextView userName,lastStr,lastTime,news;
	}

}
