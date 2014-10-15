package com.pancat.fanrong;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.SDKInitializer;

import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

//百度云必须要求实现
public class MainApplication extends FrontiaApplication {


	private static Context appContext;

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
		appContext = getApplicationContext();

	}

	
	public static Context getAppContext() {
		return appContext;
	}
}
