package com.pancat.fanrong.common;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.http.AsyncHttpClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.PersistentCookieStore;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.util.PhoneUtils;

/**
 *
 * @author trhuo
 *
 */
public class RestClient {

	private static RestClient instance;

	private RestClient() {
	}

	public static synchronized RestClient getInstance() {
		if (instance == null) {
			instance = new RestClient();
		}
		return instance;
	}

	private static AsyncHttpClient client = new AsyncHttpClient();

	@Deprecated
	public void get(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.setUserAgent(PhoneUtils.getUserAgent(context));
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public void setCookieStore(){
		PersistentCookieStore cookieStore = new PersistentCookieStore(MainApplication.getAppContext());
		client.setCookieStore(cookieStore);
		Log.i("setcookie", "setcookies");
	}

	public void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.setUserAgent(PhoneUtils.getUserAgent(MainApplication.getAppContext()));
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public void post(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.setUserAgent(PhoneUtils.getUserAgent(context));
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	/**
	 * 测试使用，url地址未拼接BASE_URL
	 * @param context
	 * @param absoluteUrl
	 * @param params
	 * @param responseHandler
	 */
	public void postFromAbsoluteUrl(Context context , String absoluteUrl ,RequestParams params,
			AsyncHttpResponseHandler responseHandler){
		client.setUserAgent(PhoneUtils.getUserAgent(context));
		client.post(absoluteUrl, params, responseHandler);
	}


	public String getAbsoluteUrl(String relativeUrl) {
		if (relativeUrl.toLowerCase().startsWith("http://")
				|| relativeUrl.toLowerCase().startsWith("https://")) {
			return relativeUrl;
		} else {
			return Constants.BASE_URL + relativeUrl;
		}
	}

	public static interface CallBaskFromHttp {
		void callback();
	}

	/**
	 *	从url获取json字符串
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String getStringFromUrl(String url) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
	}
}

