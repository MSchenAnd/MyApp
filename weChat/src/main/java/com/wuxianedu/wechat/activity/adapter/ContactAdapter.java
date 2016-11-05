package com.wuxianedu.wechat.activity.adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.activity.bean.User;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.PingYinUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter implements SectionIndexer{
	Context context;
	List<User> list;
	public ContactAdapter(Context context,List<User> list){
		this.context = context;
		this.list = list;
		//list中的Person按照首字母进行排序
		Collections.sort(list, new Comparator<User>() {
			@Override
			public int compare(User lhs, User rhs) {
				String ping1 = PingYinUtil.converterToFirstSpell(lhs.getUserName());
				String ping2 = PingYinUtil.converterToFirstSpell(rhs.getUserName());
				return ping1.compareTo(ping2);
			}
		});
		
	}
	
		public void setList(List<User> list) {
			this.list = list;
		}
	
		public List<User> getList() {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.contacts_imag);
			viewHolder.text = (TextView) convertView.findViewById(R.id.contacts_username);
			viewHolder.techar = (TextView) convertView.findViewById(R.id.contacts_first);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		User user = new User();
		user = list.get(position);

		viewHolder.text.setText(user.getUserName());
		String nowF = PingYinUtil.converterToFirstSpell(user.getUserName());
		//判断当前的列表项是否为第一个
		if(position != 0){
		//当前列表项不是第一个
			User lastUser = new User();
			lastUser = list.get(position-1);
			String lastF = PingYinUtil.converterToFirstSpell(lastUser.getUserName());
			//判断此列表项的用户首字母是否与列表的上一项的用户首字母相等

			boolean isEquals = nowF.equals(lastF);
			if (isEquals) {
				//如果相等，则不显示字母
				viewHolder.techar.setVisibility(View.GONE);
			}else{
				//如果不相等，则显示字母
				viewHolder.techar.setText(nowF.substring(0,1).toUpperCase());
				viewHolder.techar.setVisibility(View.VISIBLE);
			}
		}else{
			//当前列表项是第一个
			viewHolder.techar.setText(nowF.substring(0,1).toUpperCase());
			viewHolder.techar.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView image;
		TextView text,techar;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		
		for (int j = 0; j < list.size(); j++) {
			User user = new User();
			user = list.get(j);
			int first =  PingYinUtil.converterToFirstSpell(user.getUserName().substring(0,1)).charAt(0);
			if(first == sectionIndex){
				return j;
			}
		}
		
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

}
