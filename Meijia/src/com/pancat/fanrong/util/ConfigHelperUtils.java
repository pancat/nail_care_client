package com.pancat.fanrong.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class ConfigHelperUtils {
     
	  //百度，或者个推的配置选项
	  public static final int BAIDUMSGPUSH = 0;
	  public static final int GETUIMSGPUSH = 1;
	  
	 /*
	  * 获取AndroidManifest中的Meta信息
	  */
	 public static String getMetaValue(Context context, String metaKey) throws NameNotFoundException {
	        Bundle metaData = null;
	        String apiKey = null;
	        if (context == null || metaKey == null) {
	            return null;
	        }
	        try {
	            ApplicationInfo ai = context.getPackageManager()
	                    .getApplicationInfo(context.getPackageName(),
	                            PackageManager.GET_META_DATA);
	            if (null != ai) {
	                metaData = ai.metaData;
	            }
	            if (null != metaData) {
	                apiKey = metaData.getString(metaKey);
	            }
	        } catch (NameNotFoundException e) {
	        	throw e;
	        }
	        return apiKey;
	    }
}
