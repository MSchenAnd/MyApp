package com.wuxianedu.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import com.cn.R;
import com.wuxianedu.wechat.activity.core.BaseActivity;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.CoreUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 图片的浏览页面
 * @author Administrator
 *
 */
public class PictureActivity extends BaseActivity{
	private ViewPager viewPager;
	private List<String> image = new ArrayList<>();
	private TextView text;
	private List<View> list;
	private int i = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
	}
	@Override
	protected int getContentView() {
		
		return R.layout.activity_pictur;
	}
	/**
	 * 初始化数据
	 */
	@Override
	protected void init() {
		//写入标题文字
		setTvMid(R.string.picture_title);
		viewPager = (ViewPager) findViewById(R.id.picture_image);
		imagLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		text = (TextView) findViewById(R.id.p_number);
		Intent intent = getIntent();
		//获取intent传过来的图片数组
		image = (List<String>) intent.getSerializableExtra(Constants.IMAGE);
		int position = intent.getIntExtra(Constants.IMAGEPOSITION, 0);
		list = new ArrayList<>();
		/**
		 * 创建图片视图
		 */
		for(int i = 0; i < image.size(); i ++){
			View view = getLayoutInflater().inflate(R.layout.picture_item, null);
			list.add(view);
		}
		//设置当前显示的第几张图片
		text.setText(position + 1 + "/" + image.size());
		CoreUtils.getPicture(image.get(position), (ImageView)list.get(position).findViewById(R.id.p_image));
		
		list.get(position).findViewById(R.id.p_image).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setCurrentItem(position);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				i++;
				text.setText(position + 1 + "/" + image.size());
				CoreUtils.getPicture(image.get(position), (ImageView)list.get(position).findViewById(R.id.p_image));
				list.get(position).findViewById(R.id.p_image).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						finish();
					}
				});
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {	
			}
		});
	}
	class MyPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			return image.size();
		}

		/**
		 * 页面切换时，销毁上一个视图
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}
		
		/**
		 * 实例化Item视图
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}
		/**
		 * 判断视图是否是实例化instantiateItem的对象
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
	}
	
	

}
