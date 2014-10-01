package com.pancat.fanrong;

import android.content.Context;

import com.baidu.frontia.FrontiaApplication;

//百度云必须要求实现
public class MainApplication extends FrontiaApplication {
	
	private static Context appContext;
	@Override
    public void onCreate() {
        super.onCreate();
        
        appContext = getApplicationContext();
    }
	
	public static Context getAppContext()
	{
		return appContext;
	}
}
