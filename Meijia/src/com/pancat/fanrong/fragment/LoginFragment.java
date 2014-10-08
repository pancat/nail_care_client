package com.pancat.fanrong.fragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.LogActivity;
import com.pancat.fanrong.activity.MeActivity;
import com.pancat.fanrong.common.ChangeMd5;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.PersistentCookieStore;
import com.pancat.fanrong.http.RequestParams;


@SuppressLint("NewApi")
public class LoginFragment extends Fragment {
	private View contextView;
	private Button loginbtn;
	private EditText username, password;
	int colorpink = Color.parseColor("#FA8072");
	int colordefault = android.graphics.Color.WHITE;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contextView = inflater.inflate(R.layout.fragment_me, container, false);
		loginbtn = (Button) contextView.findViewById(R.id.loginbtn);
		username = (EditText) contextView.findViewById(R.id.username);
		password = (EditText) contextView.findViewById(R.id.password);
		loginbtn.setOnClickListener(logIn);
		
		
		return contextView;
	}

	private OnClickListener logIn = new OnClickListener() {
		private String name;
		private String passwd;

		@Override
		public void onClick(View v) {
			name = username.getText().toString();
			passwd = password.getText().toString();

			if (name == null) {
				Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_LONG)
						.show();
			} else {
				// UserInter loguser=new UserInter(getActivity(),name,passwd);
				try {
					logmethod(name, passwd);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	};

	// 用户登录方法
	private void logmethod(String name, String password)
			throws NoSuchAlgorithmException {
		 String uame;
		 String passwd;
		 uame = username.getText().toString();
		 uame="123";
		

		int flag=0;
		String url = "user/login";
		RequestParams params = new RequestParams();
		params.put("username", name);
		// 将password转换为MD5
		MessageDigest md = MessageDigest.getInstance("MD5");

		String md5password = ChangeMd5.MD5(password, md);
		Log.i("pasword md5:", md5password);

		params.put("password", md5password);
		
		RestClient.getInstance().post(getActivity(), url, params,
				new AsyncHttpResponseHandler() {
			 
					@Override
					public void onFailure(Throwable error) {
						Toast.makeText(getActivity(), "数据提交失败",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// Response failed :(
					}
				
					@Override
					public void onSuccess(int statusCode, String content) {
						onSuccess(content);
						
						try {
							JSONObject jsonObject;
							jsonObject = new JSONObject(content.toString());

							System.out.println("loginis:" + content.toString());

							int id;
							int error_code;

							id = jsonObject.getInt("res_state");
							error_code = jsonObject.getInt("error_code");
							System.out.println("id=" + id + "errorcode="
									+ error_code);

							Log.i("id&errorcode:", Integer.toString(id)
									+ Integer.toString(error_code));

							if (id == 1) {
								Toast.makeText(getActivity(), "登录成功",
										Toast.LENGTH_LONG).show();
								
								SharedPreferences userInfo=getActivity().getSharedPreferences("userinfo",Activity.MODE_PRIVATE);
								SharedPreferences.Editor editor=userInfo.edit();
								String use= username.getText().toString();				
								editor.putString("username", use);
								editor.commit();
								
								RestClient.getInstance().setCookieStore();
								Log.i("setcookie", "setcookiestore111");
								
								//跳转到另一个activity
								Intent it=new Intent(getActivity(),MeActivity.class);
								//Intent intent=new Intent(MeActivity.this,LogActivity.class);
								startActivity(it);
								
							} else {
								Toast.makeText(getActivity(), "登录失败",
										Toast.LENGTH_LONG).show();
							}
							String tomes = "res_code=" + id + " errpr_code="
									+ error_code;
							Toast.makeText(getActivity(), tomes,
									Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//登录验证成功后，本机保存登录状态
						/*
						Message msg = new Message();
						msg.obj = content;
						msg.what = 0;
						handler.sendMessage(msg);
						*/
						//写入sharePreherences
						SharedPreferences userInfo=getActivity().getSharedPreferences("userinfo",Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor=userInfo.edit();
						String use= username.getText().toString();				
						editor.putString("username", use);
						editor.commit();
						
					//	AsyncHttpClient myClient = new AsyncHttpClient();
					//	PersistentCookieStore myCookieStore = new PersistentCookieStore(getActivity());
						RestClient.getInstance().setCookieStore();
						Log.i("setcookie", "setcookiestore111");
						//跳转到另一个activity
						
					}

				});
		
		

	}
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what){
			case 0:

				SharedPreferences userInfo=getActivity().getSharedPreferences("userinfo",Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor=userInfo.edit();
				String use= username.getText().toString();				
				editor.putString("username", use);
			default:break;
			}
		}
		
	};

}
