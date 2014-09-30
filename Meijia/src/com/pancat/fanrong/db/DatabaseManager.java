package com.pancat.fanrong.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.pancat.fanrong.bean.User;

/**
 * 
 * @author trhuo
 *
 */
public class DatabaseManager {
	
	private static  DatabaseManager instance;
	private static Context context;
	private DatabaseOpenHelper helper;
	
	/**
	 * 获取管理器
	 * @return
	 */
	static public DatabaseManager getInstance(Context context){
		if(instance == null){
			instance = new DatabaseManager(context);
			DatabaseManager.context = context;
		}
		return instance;
	}
	
	private DatabaseManager(Context ctx){
		helper = new DatabaseOpenHelper(ctx);
	}
	
	public DatabaseOpenHelper getHelper(){
		return helper;
	}
	
	public static DatabaseOpenHelper getHelper(Context context){
		DatabaseManager inst = getInstance(context);
		return inst.helper;
	}
	
	/**
	 * 
	 * @param users
	 */
	public void add(User user){
		if(user != null){
				try {
					getHelper().getUserDao().create(user);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}
