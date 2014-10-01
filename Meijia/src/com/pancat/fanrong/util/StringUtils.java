package com.pancat.fanrong.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.pancat.fanrong.common.ChangeMd5;

/**
 * 字符串处理工具类
 * 
 * @author trhuo
 * 
 */
@SuppressLint("SimpleDateFormat")
public class StringUtils {

	/**
	 * 字符串拼接
	 * 
	 * @param join
	 * @param strArray
	 * @return
	 */
	public static String join(String join, String[] strArray) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strArray.length; i++) {
			if (i == (strArray.length - 1)) {
				sb.append(strArray[i]);
			} else {
				sb.append(strArray[i]).append(join);
			}
		}
		return new String(sb);
	}

	/**
	 * 字符串转化为日期
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDateByString(String dateStr) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String hashStringWithMD5(String string){
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return hex.toString();// 32位
	}
}
