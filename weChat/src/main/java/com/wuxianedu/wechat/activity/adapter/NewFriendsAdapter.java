package com.wuxianedu.wechat.activity.adapter;

import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.activity.mi.XmppManager;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.L;
import com.wuxianedu.wechat.utils.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewFriendsAdapter extends BaseAdapter{
	Context context;
	List<String> list;
	public NewFriendsAdapter(Context context,List<String> list){
		this.context = context;
		this.list = list;
	}
	public void setList(List<String> list) {
		this.list = list;
		notifyDataSetChanged();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		Drawable check = context.getResources().getDrawable(R.drawable.newfriend_check);
		final Drawable checked = context.getResources().getDrawable(R.drawable.newfriend_checked);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.newfriend_list, null);
			//用户头像
			viewHolder.idImage = (ImageView) convertView.findViewById(R.id.newfriend_listimg);
			//用户名
			viewHolder.text = (TextView) convertView.findViewById(R.id.newfriend_listusername);
			//是否添加
			viewHolder.but = (Button) convertView.findViewById(R.id.newfriend_listbut);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String person = list.get(position);
		viewHolder.text.setText(person);
		viewHolder.but.setTag(position);
		viewHolder.but.setBackgroundDrawable(check);
		viewHolder.but.setText(R.string.newfriend_but);
		viewHolder.but.setClickable(true);
		viewHolder.but.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = (int) v.getTag();
				Boolean isadd = XmppManager.getInstance().addFriend(list.get(position));
				if (isadd){
					Button bu = (Button) v;
					bu.setBackgroundDrawable(checked);
					bu.setText(R.string.newfriend_but1);
					bu.setClickable(false);
					Util.toast(context,"添加成功",true);
				}else {
					Util.toast(context,"添加失败",true);
				}
			}
		});
		return convertView;
	}
	class ViewHolder{
		ImageView idImage;
		TextView text;
		Button but;
	}

}
