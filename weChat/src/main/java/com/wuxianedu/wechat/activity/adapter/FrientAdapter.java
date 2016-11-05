package com.wuxianedu.wechat.activity.adapter;

import java.io.Serializable;
import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.PictureActivity;
import com.wuxianedu.wechat.activity.bean.Friends;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.DateUtil;

import android.content.Context;
import android.content.Intent;
import android.provider.Telephony.Sms.Conversations;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class FrientAdapter extends BaseAdapter{
	Context context;
	List<Friends> list;
	public  FrientAdapter(Context context,List<Friends> list){
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_friends_list, null);
			viewHolder.childUserimg = (ImageView) convertView.findViewById(R.id.friends_userimg);
			viewHolder.childUsername = (TextView) convertView.findViewById(R.id.friends_username);
			viewHolder.childShuo = (TextView) convertView.findViewById(R.id.friend_shuo);
			viewHolder.grid = (GridView) convertView.findViewById(R.id.friend_grid_image);
			viewHolder.time = (TextView) convertView.findViewById(R.id.friends_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final Friends friends =  list.get(position);
		CoreUtils.getPicture(friends.getHead(), viewHolder.childUserimg);
		viewHolder.childUsername.setText(friends.getUserName());
		viewHolder.childShuo.setText(friends.getContent());
		//网格视图的适配器
		viewHolder.grid.setAdapter(new GridAdapter(context, list.get(position).getImageList()));
		
		viewHolder.grid.setOnItemClickListener(new OnItemClickListener() {
			//点击图片的监听器
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(context,PictureActivity.class);
				intent.putExtra(Constants.IMAGE, (Serializable)friends.getImageList());
				intent.putExtra(Constants.IMAGEPOSITION, position);
				context.startActivity(intent);
				
			}
		});
		
		
		viewHolder.time.setText(DateUtil.getDay(friends.getTime()));
		
		return convertView;
	}
	
	
	class ViewHolder{
		ImageView childUserimg;
		TextView childUsername,childShuo,time;
		GridView grid;
		
	}

}
