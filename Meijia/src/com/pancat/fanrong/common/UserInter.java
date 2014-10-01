package com.pancat.fanrong.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

public class UserInter{
	static Activity act;
	private String name;
	private String password;
	private String testnum;
	public UserInter(Activity act,String name,String password){
		this.act=act;
		this.name=name;
		this.password=password;
	}
	
	public UserInter(Activity act,String name,String password,String testnum){
		this.act=act;
		this.name=name;
		this.password=password;
		this.testnum=testnum;
	}
	
	public void logmethod()
			throws NoSuchAlgorithmException {
		
		String url= "user/login";
		RequestParams params = new RequestParams();
		params.put("username", name);
		// 将password转换为MD5
		MessageDigest md = MessageDigest.getInstance("MD5");

		String md5password = ChangeMd5.MD5(password, md);
		Log.i("pasword md5:", md5password);

		//params.put("password", md5password);
		params.put("password", password);
		
		RestClient.getInstance().post(act, url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						Toast.makeText(act, "数据提交失败this is userinter",
								Toast.LENGTH_LONG).show();
					}
					 @Override
				     public void onFailure(Throwable error, String content)
				 {
				         // Response failed :(
				     }
					@Override
					public void onSuccess(String content) {
						
						System.out.println("loginis:"+content.toString());
						
						try {
							JSONObject jsonObject;
							jsonObject = new JSONObject(content.toString());
						
						System.out.println("loginis:"+content.toString());
						
						int id;
						int error_code;
						
						  id = jsonObject.getInt("res_state");  
                          error_code = jsonObject.getInt("error_code");  
                          System.out.println("id="+id+"errorcode="+error_code);
                          if(id==1)
                          {
                        	  Toast.makeText(act, "登录成功",
        								Toast.LENGTH_LONG).show();
                          }else{
                        	  Toast.makeText(act, "登录失败",
      								Toast.LENGTH_LONG).show();
                          }
                        String tomes="res_code="+id+" errpr_code="+error_code;
                          Toast.makeText(act, tomes,
  								Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					

				});

	}
	
	public void regmethod()
			throws NoSuchAlgorithmException {
		
		
		String url = "user/register";
		// String url= "user/login";
		RequestParams params = new RequestParams();
		params.put("username", name);
		// 将password转换为MD5
		MessageDigest md = MessageDigest.getInstance("MD5");

		String md5password = ChangeMd5.MD5(password, md);
		Log.i("pasword md5:", md5password);

		params.put("password", md5password);

		params.put("validation", testnum);
		RestClient.getInstance().post(act, url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						Toast.makeText(act, "数据提交失败 this is userinter",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(String content) {

					//	Toast.makeText(act, "数据提交成功 this is userinter",
					//			Toast.LENGTH_LONG).show();
						// 跳转到个人界面并显示出个人信息
						Message msg = new Message();
						msg.obj = content;
						msg.what = 0;
						try {
							
							JSONObject jsonObject = new JSONObject(content.toString());
							System.out.println(content.toString());
							
							int id;
							int error_code;
							
							  id = jsonObject.getInt("res_state");  
                              error_code = jsonObject.getInt("error_code");  
                              System.out.println("id="+id+"errorcode="+error_code);
                              if(id==1)
                              {
                            	  Toast.makeText(act, "注册成功",
            								Toast.LENGTH_LONG).show();
                              }else{
                            	  Toast.makeText(act, "注册失败",
          								Toast.LENGTH_LONG).show();
                              }
                            String tomes="res_code="+id+" errpr_code="+error_code;
                              Toast.makeText(act, tomes,
      								Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});

	}
}
