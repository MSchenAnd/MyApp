package com.wuxianedu.wechat.activity.fragment;



import com.cn.R;
import com.wuxianedu.wechat.activity.InstallActivity;
import com.wuxianedu.wechat.activity.core.MyApplication;
import com.wuxianedu.wechat.utils.CoreUtils;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 我的页面
 * @author Administrator
 *
 */
public class MeFragment extends Fragment{
	private View view;
	private TextView userPhonNum,me_user_name;
	private RelativeLayout userinfo,gallery,collect,wallet,card_pack,expression,install;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(view == null){
			view = inflater.inflate(R.layout.fragment_me, null);
			init();
			buttonIntent(view);
		}
		return view;
	}
	private void init(){
		userPhonNum = (TextView)view.findViewById(R.id.me_user_num);
		me_user_name = (TextView) view.findViewById(R.id.me_user_name);
		userPhonNum.append(MyApplication.username);
		me_user_name.setText(MyApplication.username);
	}
	
	private void buttonIntent(View view){
		userinfo = (RelativeLayout) view.findViewById(R.id.me_install);
		userinfo.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//跳转至设置页面
				CoreUtils.startActivity(getActivity(), InstallActivity.class);
			}
		});
	}
	
	
}
