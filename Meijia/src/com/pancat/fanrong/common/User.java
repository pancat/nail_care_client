package com.pancat.fanrong.common;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

public class User {
	private User() {
	}

	int id = -1;
	String token;
	String username, nick_name, email, avatar_uri;
	int age;

	String address;
	Double Latitude, Longitude;

	public Context context = MainApplication.getAppContext();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar_uri() {
		return avatar_uri;
	}

	public void setAvatar_uri(String avatar_uri) {
		this.avatar_uri = avatar_uri;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	// 获取用户id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// 获取用户token
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private static User instance;

	public static User getInstance() {
		if (instance == null) {
			instance = new User();
			instance.setUserDate();
		}
		return instance;
	}

	// 从服务器获取用户信息写入shared
	public void getUserDateFromServer() {
		// TODO Auto-generated method stub

		setUserDate();

		if (id == -1) {
			// Toast.makeText(this, "没有用户登录", Toast.LENGTH_LONG).show();
			Log.i("if user exist", "false");
		} else {
			// Toast.makeText(this, "有用户登录", Toast.LENGTH_LONG).show();
			Log.i("if user exist", "true");
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

								SharedPreferences userInfo = context
										.getSharedPreferences("userinfo",
												Activity.MODE_PRIVATE);
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

	// 判断是否有用户登录
	public boolean isUserLogined() {
		if (id == -1)
			return false;
		else
			return true;
	}

	// 从文件中获取用户信息
	public void setUserDate() {
		// TODO Auto-generated method stub
		id = -1;
		token = null;
		// 读取sharedperference
		File f1 = new File(
				"/data/data/com.pancat.fanrong/shared_prefs/userinfo.xml");
		File f2 = new File(
				"/data/data/com.pancat.fanrong/shared_prefs/userinfo.xml");
		if (!f1.exists()) {
			Log.i("file exist", "not exist");
		} else {
			SharedPreferences userInfo = context.getSharedPreferences(
					"userinfo", Activity.MODE_PRIVATE);

			id = userInfo.getInt("id", -1);
			token = userInfo.getString("token", "null");
			username = userInfo.getString("username", "null");
			nick_name = userInfo.getString("nick_name", "null");
			age = userInfo.getInt("age", 0);
			email = userInfo.getString("email", "null");
			avatar_uri = userInfo.getString("avatar_uri", "null");

			if (!f2.exists()) {
				Log.i("file exist", "not exist");
			} else {
				SharedPreferences commonaddress = context.getSharedPreferences(
						"commonaddress", Activity.MODE_PRIVATE);
				Latitude = Double.valueOf(commonaddress.getString("Latitude",
						"null"));
				Longitude = Double.valueOf(commonaddress.getString("Longitude",
						"null"));
				address = commonaddress.getString("address", "null");
			}
		}

	}

	// 使用这个方法获得字符串表示的地理位置，获取后可以编码为经纬度类型
	public String getCommonAddressString() {
		if (address != null)
			return address;
		else
			return null;
	}

	// 使用这个方法获得经纬度类型的地理位置，获取后可以改为字符型地理位置
	public LatLng getCommonAddressLatLng() {
		if (Latitude != null && Longitude != null) {
			LatLng it = new LatLng(Latitude, Longitude);
			return it;
		} else {
			return null;
		}
	}

	public void setAddress(double Latitude, double Longitude, String address) {
		this.Latitude = Latitude;
		this.Longitude = Longitude;
		this.address = address;
	}

	public void setCommonAddress(double Latitude, double Longitude,
			String address) {

		SharedPreferences userInfo = context.getSharedPreferences(
				"commonaddress", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = userInfo.edit();
		editor.putString("Latitude", String.valueOf(Latitude));
		editor.putString("Longitude", String.valueOf(Longitude));
		editor.putString("address", String.valueOf(address));

		setAddress(Latitude, Longitude, address);
		editor.commit();
	}
}
