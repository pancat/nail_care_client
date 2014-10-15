package com.pancat.fanrong.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pancat.fanrong.MainActivity;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.LoginActivity;
import com.pancat.fanrong.common.ChangeMd5;
import com.pancat.fanrong.common.Constants;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.common.User;

import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

@SuppressLint("NewApi")
public class SigninFragment extends Fragment {
	private View contextView;
	private EditText username, passwd, repasswd, testnum;
	private Button regbtn;

	private FragmentCallback fragmentCallback;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contextView = inflater.inflate(R.layout.fragment_mereg, container,
				false);

		username = (EditText) contextView.findViewById(R.id.username);
		passwd = (EditText) contextView.findViewById(R.id.passwd);
		repasswd = (EditText) contextView.findViewById(R.id.repasswd);
		testnum = (EditText) contextView.findViewById(R.id.testnum);
		regbtn = (Button) contextView.findViewById(R.id.regbtn);

		regbtn.setOnClickListener(regnewuser);

		return contextView;
	}

	// 用户注册方法
	private void regmethod(String name, String password, String testnum)
			{
		String url = "user/register";
		// String url= "user/login";
		RequestParams params = new RequestParams();
		params.put("username", name);
		// 将password转换为MD5
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			String md5password = ChangeMd5.MD5(password, md);

			Log.i("pasword md5:", md5password);

			params.put("password", md5password);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
		params.put("validation", testnum);
		RestClient.getInstance().post(getActivity(), url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						Toast.makeText(getActivity(), "数据提交失败",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(String content) {

						 Toast.makeText(getActivity(), "数据提交成功",
						 Toast.LENGTH_LONG).show();
						// 跳转到个人界面并显示出个人信息
						// 跳转到个人界面并显示出个人信息
						
						try {

							JSONObject jsonObject = new JSONObject(content
									.toString());
							System.out.println(content.toString());

							int id,res_state;
							int error_code;
						

							//id = jsonObject.getInt("res_state");
						//	error_code = jsonObject.getInt("error_code");
						//	System.out.println("id=" + id + "errorcode="
						//			+ error_code);

							String token, username1, nick_name, email, avatar_uri;

							res_state = jsonObject.getInt("res_state");
							error_code = jsonObject.getInt("error_code");
							id = jsonObject.getInt("id");
						//	token = jsonObject.getString("token");
							username1 = jsonObject.getString("username");
							nick_name = jsonObject.getString("nick_name");
							email = jsonObject.getString("email");
							avatar_uri = jsonObject.getString("avatar_uri");
							String tomes = "res_code=" + res_state + " errpr_code="+ error_code;
						//	Toast.makeText(getActivity(), tomes,Toast.LENGTH_LONG).show();
							Log.i("res & error_code", tomes);
							
							 if(res_state==1)
							 {
								//Toast.makeText(getActivity(), "注册成功",Toast.LENGTH_LONG).show();
								Log.i("注册状态", "注册成功");
								SharedPreferences userInfo=getActivity().getSharedPreferences("userinfo",Activity.MODE_PRIVATE);
								SharedPreferences.Editor editor=userInfo.edit();
											
								editor.putString("username",username1);
										editor.putInt("id", id);
								//editor.putString("token", token);
								editor.putString("nick_name", nick_name);
								editor.putString("email", email);
								editor.putString("avatar_uri", avatar_uri);
								
								
								editor.commit();
								
								User user=User.getInstance();
								user.setUserDate();
								
								RestClient.getInstance().setCookieStore();
								Log.i("setcookie", "setcookiestore111");
								
								fragmentCallback.finishActivity(); 
								/*
								Intent it=new Intent(getActivity(),MainActivity.class);
								startActivity(it);
								*/
							 }
							 else{
								// Toast.makeText(getActivity(), "注册失败",Toast.LENGTH_LONG).show();
								 Log.i("注册状态", "注册失败");
							 }
								
							
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// 注册成功后自动登录

					}

				});

	}


	private OnClickListener regnewuser = new OnClickListener() {

		private String name, pass1, pass2, testn;

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			name = username.getText().toString();
			pass1 = passwd.getText().toString();
			pass2 = repasswd.getText().toString();
			testn = testnum.getText().toString();

			if (testn.length() != 5) {
				Toast.makeText(getActivity(), "不是5位效验码", Toast.LENGTH_LONG)
						.show();
			}
			else if (!pass1.equals(pass2)) {
				Toast.makeText(getActivity(), "两次密码不一致", Toast.LENGTH_LONG)
						.show();
			} else {
				// 进行注册
				// UserInter usernew=new
				// UserInter(getActivity(),name,pass1,testn);
			
					regmethod(name, pass1, testn);
				
					// TODO Auto-generated catch block
			
				/*
				 * try { //regmethodtwo(name, pass1, testn); regmethod(name,
				 * pass1, testn); } catch (NoSuchAlgorithmException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */

			}
		}

	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fragmentCallback = (LoginActivity) activity;
	}
}
