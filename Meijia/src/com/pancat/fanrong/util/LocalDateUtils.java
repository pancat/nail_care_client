package com.pancat.fanrong.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LocalDateUtils {
    //获取当前小时
	public static int getHour(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR);
	}
	//获取当前是哪一年
	public static int getYear(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	
	//获取从今天开始的Pos天yyyy-mm-dd
	public static String getDate(int pos){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.DATE, pos);
		return format.format(calendar.getTime());
	}
	
	//mm.dd
	public static String getMD(int pos){
		SimpleDateFormat format = new SimpleDateFormat("MM.dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.DATE, pos);
		return format.format(calendar.getTime());
	}
}
