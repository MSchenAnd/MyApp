package com.wuxianedu.wechat.activity.adapter;

import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.PictureActivity;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.utils.CoreUtils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 网格视图
 * @author Administrator
 *
 */
public class GroupAdapter extends BaseAdapter{
	Context context;
	List<Person> list;
	public GroupAdapter(Context context,List<Person> list){
		this.context = context;
		this.list = list;
	}
	
	public void setList(List<Person> list) {
		this.list = list;
		notifyDataSetChanged();
	}
	
	public List<Person> getList() {
		return list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.group_horiental_item, null);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.group_item_image);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
			//从网络上获取图片
			CoreUtils.getPicture(list.get(position).getHead(), viewHolder.image);
			
			viewHolder.image.setAlpha(list.get(position).getAlpha());
		return convertView;
	}
	
	class ViewHolder{
		ImageView image;
	
		
	}
	

}
