package com.pancat.fanrong;


import android.content.Context;


import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.SDKInitializer;

import com.pancat.fanrong.bean.User;
import com.pancat.fanrong.mgr.AuthorizeMgr;

//百度云必须要求实现
public class MainApplication extends FrontiaApplication {


	private static Context appContext;

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
		appContext = getApplicationContext();
		
		initUserInfo();
		 
		
	}

	public void initUserInfo()
	{
		AuthorizeMgr mgr = AuthorizeMgr.getInstance();
		User user = mgr.getPersistedUser();
		if (user != null)
		{
			mgr.setUser(user);
	     new Thread(new Runnable() {
		    public void run() {
			 //   	//从网络上获取最新的用户信息
			    	AuthorizeMgr.setLastUserInfomationFromSer();
			    }
		     }).start();
			
		}
	}

	
	public static Context getAppContext() {
		return appContext;
	}
}
