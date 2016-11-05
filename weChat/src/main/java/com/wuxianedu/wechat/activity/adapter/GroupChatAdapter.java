package com.wuxianedu.wechat.activity.adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.bean.Person;
import com.wuxianedu.wechat.utils.CoreUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
/**
 * 群聊
 * @author Administrator
 *
 */
public class GroupChatAdapter extends BaseAdapter implements SectionIndexer{
	Context context;
	List<Person> list;
	public GroupChatAdapter(Context context,List<Person> list){
		this.context = context;
		this.list = list;
	
		//list中的Person按照首字母进行排序
		Collections.sort(list, new Comparator<Person>() {

			@Override
			public int compare(Person lhs, Person rhs) {
			
				return lhs.getFirstPinyin().compareTo(rhs.getFirstPinyin());
			}
		});
		
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
			convertView = LayoutInflater.from(context).inflate(R.layout.groupchat_list_child, null);
			
			viewHolder.image = (ImageView) convertView.findViewById(R.id.groupchat_imag);
			viewHolder.text = (TextView) convertView.findViewById(R.id.groupchat_username);
			viewHolder.techar = (TextView) convertView.findViewById(R.id.groupchat_child_first);
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.groupchat_checkbox);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Person person = new Person();
		person = list.get(position);
		viewHolder.checkBox.setChecked(person.isCheck());
		
		//获取用户的头像
		CoreUtils.getPicture(person.getHead(), viewHolder.image);
		viewHolder.text.setText(person.getUserName());
		//判断当前的列表项是否为第一个
		if(position != 0){
		//当前列表项不是第一个
			Person lastPerson = new Person();
			lastPerson = list.get(position-1);
			//判断此列表项的用户首字母是否与列表的上一项的用户首字母相等
			boolean isEquals = person.getFirstPinyin().equals(lastPerson.getFirstPinyin());
			if (isEquals) {
				//如果相等，则不显示字母
				viewHolder.techar.setVisibility(View.GONE);
			}else{
				//如果不相等，则显示字母
				viewHolder.techar.setText(person.getFirstPinyin());
				viewHolder.techar.setVisibility(View.VISIBLE);
			}
		}else{
			//当前列表项是第一个
			viewHolder.techar.setText(person.getFirstPinyin());
			viewHolder.techar.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView image;
		TextView text,techar;
		CheckBox checkBox;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int j = 0; j < list.size(); j++) {
			Person person = new Person();
			person = list.get(j);
			int first = person.getFirstPinyin().charAt(0);
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
