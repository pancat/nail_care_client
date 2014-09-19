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
	
	public static final String BASE_URL = "http://54.169.66.69/fanrong/index.php";
	
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void get(Context context,String url,RequestParams params,
			AsyncHttpResponseHandler responseHandler){
		client.setUserAgent(PhoneUtils.getUserAgent(context));
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	private static String getAbsoluteUrl(String relativeUrl){
		return BASE_URL + relativeUrl;
	}
	
	public static interface CallBaskFromHttp{
		void callback();
	}
}
