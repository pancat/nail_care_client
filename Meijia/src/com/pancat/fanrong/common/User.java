package com.pancat.fanrong.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

public class User  {
	private User() {
	}
	
	int id;
	String token;
	String username, nick_name, email, avatar_uri;
	int age;
	public Context context = MainApplication.getAppContext();
	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}
	public String getNick_name() {return nick_name;}
	public void setNick_name(String nick_name) {this.nick_name = nick_name;}
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	public String getAvatar_uri() {return avatar_uri;}
	public void setAvatar_uri(String avatar_uri) {this.avatar_uri = avatar_uri;}
	public int getAge() {return age;}
	public void setAge(int age) {this.age = age;}
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
	
    private static User instance;
	
	public static User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}
	public void getUserDateFromServer() {
		// TODO Auto-generated method stub
		
		setUserDate();
	
		if (id == -1) {
		//	Toast.makeText(this, "没有用户登录", Toast.LENGTH_LONG).show();
			Log.i("if user exist","false" );
		}
		else{
		//	Toast.makeText(this, "有用户登录", Toast.LENGTH_LONG).show();
			Log.i("if user exist","true" );
			RequestParams params = new RequestParams();
			params.put("id", String.valueOf(id));
			params.put("token", token);
			String url = "user/get_user_info";
			RestClient.getInstance().get(url, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(Throwable error) {

						}

						@Override
						public void onSuccess(String content) {
							System.out.println(content.toString());
							Log.i("userinfo", content.toString());

							// 吧得到的信息写入到sharedperference
							int res_state, id;
							int error_code;
							String token, username1, nick_name, email, avatar_uri;
							JSONObject jsonObject;
							try {
								jsonObject = new JSONObject(content.toString());

								res_state = jsonObject.getInt("res_state");
								error_code = jsonObject.getInt("error_code");
								id = jsonObject.getInt("id");
								token = jsonObject.getString("token");
								username1 = jsonObject.getString("username");
								nick_name = jsonObject.getString("nick_name");
								email = jsonObject.getString("email");
								avatar_uri = jsonObject.getString("avatar_uri");

								SharedPreferences userInfo =context.getSharedPreferences(
										"userinfo", Activity.MODE_PRIVATE);
								SharedPreferences.Editor editor = userInfo
										.edit();

								editor.putString("username", username1);
								editor.putInt("id", id);
								editor.putString("token", token);
								editor.putString("nick_name", nick_name);
								editor.putString("email", email);
								editor.putString("avatar_uri", avatar_uri);
								editor.commit();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							setUserDate();

						}

					});
		}

	}

	public void setUserDate() {
		// TODO Auto-generated method stub
		id = -1;
		token = null;
		// 读取sharedperference
		SharedPreferences userInfo = context.getSharedPreferences("userinfo",
				Activity.MODE_PRIVATE);
		id = userInfo.getInt("id", -1);
		token = userInfo.getString("token", "null");
		username = userInfo.getString("username", "null");
		nick_name = userInfo.getString("nick_name", "null");
		age = userInfo.getInt("age", 0);
		email = userInfo.getString("email", "null");
		avatar_uri = userInfo.getString("avatar_uri", "null");

	}
	
	
}
