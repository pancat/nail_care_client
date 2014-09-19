package com.pancat.fanrong.util;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串处理工具类
 * @author trhuo
 *
 */
@SuppressLint("SimpleDateFormat")
public class StringUtils {
	
	/**
	 * 字符串拼接
	 * @param join 
	 * @param strArray 
	 * @return
	 */
	public static String join(String join,String[] strArray){
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<strArray.length;i++){
			if(i == (strArray.length - 1)){
				sb.append(strArray[i]);
			}
			else{
				sb.append(strArray[i]).append(join);
			}
		}
		return new String(sb);
	}
	
	/**
	 * 字符串转化为日期
	 * @param dateStr
	 * @return
	 */
	public static Date getDateByString(String dateStr){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
