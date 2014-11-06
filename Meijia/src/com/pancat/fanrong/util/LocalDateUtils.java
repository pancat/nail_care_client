package com.pancat.fanrong.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class LocalDateUtils {
	public static final String TAG = "LocalDateUtils";
    //获取当前小时
	public static int getHour(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY); //24小时制
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
	
	//支持格式如下：
	//2014-11-11 10:11:20
	//2014.11.11 10:11:20
	public static Date getDateFromString(String content){
		String format1 = "yyyy-mm-dd HH:mm:ss";
		String format2 = "yyyy.mm.dd HH:mm:ss";
		try{
			if(content.indexOf("-")!=-1){
				SimpleDateFormat sdf1 = new SimpleDateFormat(format1);
				return sdf1.parse(content);
			}else if(content.indexOf(".")!=-1){
				SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
				return sdf2.parse(content);
			}
		}catch(Exception e){
			e.printStackTrace();
			Log.d(TAG,"日期格式错误!");
		}
		return null;
	}
	
	//支持. ：组合字符串日期,必须是前缀提供，否则识别后果由自己负责
	public static Date getDateFromAllString(String content){
		Date date = new Date();
		String [] ar = content.split(" ");
		int index = 0;
		
		//去掉空字符串
		for(int i=0;i<ar.length; i++){
			if(ar[i].equals(""))
				ar[index++] = ar[i];
		}
		int year,month,day,hour,minute,second,ms;
		
		int [] res = new int[3];
		int l;
		if(index >0){
			l = ParseStringToArr(ar[0], res);
			date.setYear(l>0?res[0]:date.getYear());
			date.setMonth(l>0 && res[1] < 13 ? res[1]:date.getMonth());
			date.setDate(l>2 && res[2] <32 ? res[2]:date.getDate());
		}else if(index > 1){
			l = ParseStringToArr(ar[1], res);
			date.setHours(l>0 && res[0] < 25 ? res[0]:date.getHours());
			date.setMinutes(l>0 && res[1] < 61 ? res[1]:date.getMinutes());
			date.setSeconds(l > 0 && res[2] < 61 ? res[2]: date.getSeconds());
		}
		
		return date;
	}
	
	private static int  ParseStringToArr(String str,int[] res){
		String [] tmp;
		if(str.indexOf("-") != -1){
			tmp = str.split("-");
		}else if(str.indexOf(".") != -1){
			tmp = str.split(".");
		}else if(str.indexOf(":") != -1){
			tmp = str.split(":");
		}else if(str.matches("[0-9]{1,4}")){
			tmp = new String[1];
			tmp[0] = str;
		}else{
			return 0;
		}
		int l = 0;
		
		try{
			for(l=0;l<3 && l< tmp.length; l++){
				res[l] = Integer.parseInt(str);
				if(res[l] > 10000 || res[l] < 0) throw new Exception();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return l;
	}
}
