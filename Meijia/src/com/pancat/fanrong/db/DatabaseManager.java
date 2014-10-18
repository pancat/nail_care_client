package com.pancat.fanrong.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.bean.User;

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
	
	static public DatabaseManager getInstance(){
		Context context = MainApplication.getAppContext();
		return getInstance(context);
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
	
	public void addCircle(Circle circle){
		Dao<Circle,Integer> dao = getHelper().getCircleDao();
		if(circle != null){
			try {
				dao.create(circle);
				System.out.println("0000");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Circle> getCircles(){
		Dao<Circle,Integer> dao = getHelper().getCircleDao();
		List<Circle> circleList = new ArrayList<Circle>();
		QueryBuilder<Circle, Integer> query = dao.queryBuilder();
		try {
			circleList = query.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return circleList;
	}
}
