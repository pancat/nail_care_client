package com.pancat.fanrong.mgr;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.bean.User;
import com.pancat.fanrong.db.DatabaseManager;
import com.pancat.fanrong.db.DatabaseOpenHelper;

/*
 * @author realxie
 * 用来进行用户登录信息的验证
 * 提供全局唯一实例
 */
public class AuthorizeMgr {

	private static AuthorizeMgr mInstance;
	private User   mUser = null;

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


	private AuthorizeStatus clearUserDB()
	{
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
		if (clearUserDB() == AuthorizeStatus.FAIL)
		{
			ret = AuthorizeStatus.FAIL;
		}
		setUser(null);
		return ret;
	}

	public AuthorizeStatus persistUser(User user) {
		if (clearUserDB() == AuthorizeStatus.FAIL)
		{
			return AuthorizeStatus.FAIL;
		}

		try {
			DatabaseOpenHelper helper = DatabaseManager.getInstance().getHelper();
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

	public void setUser(User user)
	{
		mUser = user;
	}

	public User getUser() {
		return mUser;
	}

	public boolean hasLogined()
	{
		return mUser != null;
	}

	public static boolean isLoginSuccess(String content)
	{
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
			//int error_code = jsonObject.getInt("error_code");
			return code == 1;
		} catch (JSONException e) {
			Log.i("JsonObject", "didnot get res_state");
			e.printStackTrace();
		}
		return false;
	}


	public static User parseUserFromJsonText(String content) {

		try {
			User user = new User();

			JSONObject jsonObject = new JSONObject(content.toString());

			if (isLoginSuccess(jsonObject) == false)
			{
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
