package com.wuxianedu.wechat.widgt;

import com.wuxianedu.wechat.utils.CoreUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MyRadioGroup extends RadioButton{

	public MyRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获得RadioButton中的drawable数组
		Drawable drawable = getCompoundDrawables()[1];
		int size = CoreUtils.dipToPixel(context, 28);
		drawable.setBounds(0, 0,  (int)(size*(96.0/84.0)), size);
		setCompoundDrawables(null, drawable, null, null);
	}



	

}
