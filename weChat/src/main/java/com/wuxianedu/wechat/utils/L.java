/**
 * @Project Name:CoreLibrary
 * @File Name:Utils.java
 * @Package Name:com.wuxianedu.utils
 * @Date:2016骞�鏈�5鏃ヤ笂鍗�:22:00
 * @Copyright(c)2016 www.wuxianedu.com Inc. All rights reserved.
*/

package com.wuxianedu.wechat.utils;

import android.util.Log;

/**
 * @ClassName:Utils 
 * @Function: TODO ADD FUNCTION. 
 * @Date:     2016年8月15日 上午9:22:00 
 * @author    Yimin.Li
 * @Copyright(c)2016 www.wuxianedu.com Inc. All rights reserved.
 */
public class L {
	private static boolean isDebug = true;
	
	private static final String TAG = "-Main-";
	
	
	public static void i(Object message){
		if (isDebug) {
			Log.i(TAG, message.toString());
		}
	}
	
	public static void e(Object message){
		if (isDebug) {
			Log.e(TAG, message.toString());
		}
	}
	
	public static void w(Object message){
		if (isDebug) {
			Log.w(TAG, message.toString());
		}
	}
	
	public static void d(Object message){
		if (isDebug) {
			Log.d(TAG, message.toString());
		}
	}
}

