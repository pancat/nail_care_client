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
	private static Context ctx;
	private DatabaseOpenHelper helper;
	
	/**
	 * 初始化管理器
	 * @param ctx
	 */
	static public void init(Context ctx){
		if(instance == null){
			instance = new DatabaseManager(ctx);
			instance.ctx = ctx;
		}
	}
	
	/**
	 * 获取管理器
	 * @return
	 */
	static public DatabaseManager getInstance(){
		if(instance == null){
			init(ctx);
		}
		System.out.println(instance);
		return instance;
	}
	
	private DatabaseManager(Context ctx){
		helper = new DatabaseOpenHelper(ctx);
	}
	
	private DatabaseOpenHelper getHelper(){
		return helper;
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
