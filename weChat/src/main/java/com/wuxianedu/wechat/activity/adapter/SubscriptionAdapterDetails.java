package com.wuxianedu.wechat.activity.adapter;

import java.util.Date;
import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.bean.Subscription;
import com.wuxianedu.wechat.activity.bean.SubscriptionDetails;
import com.wuxianedu.wechat.activity.fragment.FindFragment;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.DateUtil;
import com.wuxianedu.wechat.utils.L;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SubscriptionAdapterDetails extends BaseAdapter{
	Context context;
	List<SubscriptionDetails> list;
	public SubscriptionAdapterDetails(Context context,List<SubscriptionDetails> list){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.subscribedetails_item, null);
			viewHolder.userName = (TextView) convertView.findViewById(R.id.sb_username);
			viewHolder.date = (TextView) convertView.findViewById(R.id.sd_time);
			viewHolder.time = (TextView) convertView.findViewById(R.id.sb_time);
			viewHolder.content = (TextView) convertView.findViewById(R.id.sb_content);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.sb_image);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		SubscriptionDetails subscriptionDetails = list.get(position);
		
		viewHolder.userName.setText(subscriptionDetails.getTitle());
		
		CoreUtils.getPicture(subscriptionDetails.getImage(), viewHolder.image);
		viewHolder.content.setText(subscriptionDetails.getContent());
		viewHolder.time.setText(DateUtil.dateToString(DateUtil.DF,subscriptionDetails.getTime()));
		viewHolder.date.setText(DateUtil.dateToString(DateUtil.DATE_LONG, subscriptionDetails.getTime()));
		return convertView;
	}
	class ViewHolder{
		ImageView image;
		TextView userName,date,time,content;
	}

}
