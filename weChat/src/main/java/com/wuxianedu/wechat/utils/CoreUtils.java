package com.wuxianedu.wechat.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.cn.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class CoreUtils {
	private static List<Activity> list = new ArrayList<Activity>();
	private static float sDensity = 0;
	//判断字符串去掉头和尾的空格后是否为空
	public static boolean isEmpty(String str){
		return str.trim().length() == 0 || str == null;
	}
	
	
	
	//跳转页面
	public static <T> void startActivity(Context context,Class<T> class1){
		Intent intent = new Intent(context,class1);
		context.startActivity(intent);
	}
	
	public static void addActivity(Activity activity){
		if(!list.contains(activity)){
			list.add(activity);
		}
	}
	public static void removActivity(Activity activity){
		if(list.contains(activity)){
			list.remove(activity);
		}
	}
	public static void finishActivity(){
		for (Activity activity : list) {
			activity.finish();
		}
		list.clear();
	}
	
	public static void exitProcess(){
		finishActivity();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	/**
	 * dp转换为像素
	 * @param context
	 * @param nDip
	 * @return
	 */
	public static int dipToPixel(Context context, int nDip) {
		if (sDensity == 0) {
			final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			sDensity = dm.density;
		}
		return (int) (sDensity * nDip);
	}
	
	//从internet上获取图片
	public static void getPicture(String string,final ImageView imageView){
		x.image().loadDrawable(string, ImageOptions.DEFAULT, new CommonCallback<Drawable>() {
			
			@Override
			public void onSuccess(Drawable drawable) {
				imageView.setImageDrawable(drawable);
			}
			
			@Override
			public void onFinished() {				
			}
			
			@Override
			public void onError(Throwable arg0, boolean arg1) {
			}
			
			@Override
			public void onCancelled(CancelledException arg0) {
			}
		});
	}
	//判断是否为中文
	public static boolean isChinese(String str){
		Pattern p=Pattern.compile("^[\u4e00-\u9fa5]*$");
		Matcher m=p.matcher(str);
	    if(m.matches()){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	
	//自定义textView中drawable的图片大小
	public static void setImageWidthHeight(Context context,int height,int width, TextView view,int imag){
		int sizeWidth = dipToPixel(context,width);
		int sizeHeight = dipToPixel(context,height);
		Drawable drawable=context.getResources().getDrawable(imag);
		drawable.setBounds(0, 0,(int)(sizeWidth*(96.0/84.0)),(int)(sizeHeight*(96.0/84.0)));
		view.setCompoundDrawables(drawable, null, null, null); 
	}
	
	//动态的宽度
	public static int[] displayWidthHeight(Activity activity){
		int[] display = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		display[0] = dm.widthPixels;
		display[1] = dm.heightPixels;
		return display;
	}
	
	
	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int spToPixel(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	
	//用正则表达式判断输入的手机号是否正确
	public static boolean isPhone(String string){
		Pattern pattern = Pattern.compile("^[1][3|4|5|7|8][0-9]{9}$");
		Matcher matcher = pattern.matcher(string);
		matcher.pattern();
		return matcher.matches();
	}
	
	public static boolean isPassword(String string){
		Pattern pattern = Pattern.compile("^[0-9a-bA-a]\\w{5,10}");
		Matcher matcher = pattern.matcher(string);
		matcher.pattern();
		return matcher.matches();
	}
}
