package com.wuxianedu.wechat.activity.fragment;

import com.cn.R;
import com.wuxianedu.wechat.activity.FriendsActivity;
import com.wuxianedu.wechat.utils.CoreUtils;
import com.wuxianedu.wechat.utils.L;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
/**
 * 发现页面
 * @author Administrator
 *
 */
public class FindFragment extends Fragment implements OnClickListener{
	private View view;
	private RelativeLayout pyq,saoyisao,yaoyiyao,fjr,plp,gw,yx;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_find, null);
		pyq = (RelativeLayout) view.findViewById(R.id.find_pyq);
		pyq.setOnClickListener(this);
		L.e("------------FindFragmentonCreateView-----------------");
		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.find_pyq:
			CoreUtils.startActivity(getActivity(), FriendsActivity.class);
			break;

		default:
			break;
		}
		
	}
	
}
