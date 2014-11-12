package com.pancat.fanrong.mgr;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.bean.User;
import com.pancat.fanrong.common.Constants;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.db.DatabaseManager;
import com.pancat.fanrong.db.DatabaseOpenHelper;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

/*
 * @author realxie
 * 用来进行用户登录信息的验证
 * 提供全局唯一实例
 */
public class AuthorizeMgr {

	private static AuthorizeMgr mInstance;
	private User mUser = null;

	private AuthorizeMgr() {
	}

	public static AuthorizeMgr getInstance() {
		synchronized (MainApplication.getAppContext()) {
			if (mInstance == null) {
				mInstance = new AuthorizeMgr();
			}
		}
		return mInstance;
	}

	public AuthorizeStatus getSignInStatus() {
		return AuthorizeStatus.NOT_SIGN_IN;
	}

	private AuthorizeStatus clearUserDB() {
		DatabaseOpenHelper helper = DatabaseManager.getInstance().getHelper();
		Dao<User, Integer> dao = helper.getUserDao();

		try {
			List<User> list = dao.queryForAll();
			dao.delete(list);
		} catch (SQLException e) {
			e.printStackTrace();
			return AuthorizeStatus.FAIL;
		}

		return AuthorizeStatus.SUCCESS;
	}

	public AuthorizeStatus setLogout() {
		AuthorizeStatus ret = AuthorizeStatus.SUCCESS;
		if (clearUserDB() == AuthorizeStatus.FAIL) {
			ret = AuthorizeStatus.FAIL;
		}
		File file=new File(Constants.USER_IMAGE_PATH);
		if(file.exists()){
			file.delete();
		}
		setUser(null);
		return ret;
	}

	public AuthorizeStatus persistUser(User user) {
		if (clearUserDB() == AuthorizeStatus.FAIL) {
			return AuthorizeStatus.FAIL;
		}

		try {
			DatabaseOpenHelper helper = DatabaseManager.getInstance()
					.getHelper();
			Dao<User, Integer> dao = helper.getUserDao();
			dao.create(user);
		} catch (SQLException e) {
			e.printStackTrace();
			return AuthorizeStatus.PERSIST_FAIL;
		}
		return AuthorizeStatus.SUCCESS;
	}

	public User getPersistedUser() {
		DatabaseOpenHelper helper = DatabaseManager.getInstance().getHelper();
		Dao<User, Integer> dao = helper.getUserDao();

		try {
			List<User> list = dao.queryForAll();
			return list.size() > 0 ? list.get(0) : null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setUser(User user) {
		mUser = user;
	}

	public User getUser() {
		return mUser;
	}

	public boolean hasLogined() {
		return mUser != null;
	}

	public static boolean isLoginSuccess(String content) {
		try {
			JSONObject jsonObject = new JSONObject(content.toString());
			return isLoginSuccess(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isLoginSuccess(JSONObject jsonObject) {

		Log.i("JsonObject", jsonObject.toString());
		Log.i("JsonObject", "get json");
		try {
			int code = jsonObject.getInt("code");

			Log.e("res_state", code + "");
			// int error_code = jsonObject.getInt("error_code");
			return code == 1;
		} catch (JSONException e) {
			Log.i("JsonObject", "didnot get res_state");
			e.printStackTrace();
		}
		return false;
	}

	public static void setLastUserInfomationFromSer() {
		// 通过id 和sessionid 发送请求，获取用户的各类信息
		String url = "user/get_user_info";
		RequestParams params = new RequestParams();
		params.put("id",
				String.valueOf(AuthorizeMgr.getInstance().getUser().getId()));
		Log.i("sessionid", AuthorizeMgr.getInstance().getUser().getSessionid());
		params.put("sessionid", AuthorizeMgr.getInstance().getUser()
				.getSessionid());// 不用md5登录

		RestClient.getInstance().post(MainApplication.getAppContext(), url,
				params, adBannerReadyHandler);
	}

	final static AsyncHttpResponseHandler adBannerReadyHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			Log.i("login fail", "login fail");
			Message msg = new Message();
			msg.what = 1;
			// mSignInHandler.sendMessage(msg);
		}

		@Override
		public void onSuccess(String content) {
			Log.i("get_user_info json", content);
			super.onSuccess(content);
			// User user = AuthorizeMgr.parseUserFromJsonText(content);
			try {
				JSONObject jsonObject = new JSONObject(content.toString());
				
				User user=AuthorizeMgr.getInstance().getUser();
				String nickname = jsonObject.getString("nick_name");
				user.setNickname(nickname);

				String email = jsonObject.getString("email");
				user.setEmail(email);

				String avatarUri = jsonObject.getString("avatar_uri");
				user.setAvatarUri(avatarUri);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public static User parseUserFromJsonText(String content) {

		try {
			User user = new User();

			JSONObject jsonObject = new JSONObject(content.toString());

			if (isLoginSuccess(jsonObject) == false) {
				return null;
			}
			Log.i("JsonObject", "get json");

			int id = jsonObject.getInt("id");
			user.setId(id);

			String sessionid = jsonObject.getString("sessionid");
			user.setSessionid(sessionid);

			String username = jsonObject.getString("username");
			user.setUsername(username);

			String nickname = jsonObject.getString("nick_name");
			user.setNickname(nickname);

			String email = jsonObject.getString("email");
			user.setEmail(email);

			String avatarUri = jsonObject.getString("avatar_uri");
			user.setAvatarUri(avatarUri);

			return user;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
