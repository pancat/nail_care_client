package com.pancat.fanrong.util;

import com.pancat.fanrong.common.Constants;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 电话工具类
 * @author trhuo
 *
 */
public class PhoneUtils {
	// 得到手机的IMEI号，需要context参数
	public static String getIMEI(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getDeviceId();
	}

	// 得到手机的IMSI号，需要context参数
	public static String getIMSI(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getSubscriberId();
	}

	/**
	 * 检查网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	// 判断当前网络是否为wifi
	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	public static String getUserAgent(Context ctx) {
		StringBuilder ua = new StringBuilder("fanrong");
		PackageInfo info;
		try {
			info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
					0);
			ua.append('/' + info.versionName + '.' + info.versionCode);// App版本
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ua.append("/Android");// 手机系统平台
		ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
		ua.append("/" + android.os.Build.MODEL); // 手机型号
		ua.append("/" + Constants.channel);// 发布渠道
		ua.append("/" + getIMEI(ctx));// 设备唯一编码
		System.out.println("appUserAgent:" + ua.toString());
		return ua.toString();
	}

}
