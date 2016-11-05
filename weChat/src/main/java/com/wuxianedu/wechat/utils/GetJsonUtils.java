package com.wuxianedu.wechat.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;

public class GetJsonUtils {
	public static String getJsonString(Context context,String jsonFileName){
		StringBuilder builder = new StringBuilder();
		AssetManager manager = context.getResources().getAssets();
		BufferedReader bs = null;
		try {
			InputStream input = manager.open(jsonFileName);
			bs = new BufferedReader(new InputStreamReader(input));
			String date;
			while((date = bs.readLine())!=null)
				builder.append(date);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
				try {
					if(bs != null)
					bs.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		}
		return builder.toString();
	}
}
