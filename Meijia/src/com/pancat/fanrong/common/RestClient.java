package com.pancat.fanrong.common;

import android.content.Context;

import com.pancat.fanrong.http.AsyncHttpClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.util.PhoneUtils;

/**
 * 
 * @author trhuo
 *
 */
public class RestClient {
	
	private static RestClient instance;
	
	private RestClient(){}
	public static synchronized RestClient getInstance(){
		if(instance == null){
			instance = new RestClient();
		}
		return instance;
	}
	
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public void get(Context context,String url,RequestParams params,
			AsyncHttpResponseHandler responseHandler){
		client.setUserAgent(PhoneUtils.getUserAgent(context));
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public void post(Context context,String url,RequestParams params,
			AsyncHttpResponseHandler responseHandler){
		client.setUserAgent(PhoneUtils.getUserAgent(context));
		client.post(getAbsoluteUrl(url), params,responseHandler);
	}
	
	private String getAbsoluteUrl(String relativeUrl){
		return Constants.BASE_URL + relativeUrl;
	}
	
	public static interface CallBaskFromHttp{
		void callback();
	}
}
