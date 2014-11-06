package com.pancat.fanrong.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.bean.Product;
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
			circleList = (ArrayList<Circle>) query.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return circleList;
	}
	
	public void deleteCircleList() throws SQLException{
		Dao<Circle,Integer> dao = getHelper().getCircleDao();
		DeleteBuilder<Circle, Integer> deleteBuild = dao.deleteBuilder();
		deleteBuild.delete();
	}
	
	//增加产品
	public void addProduct(Product product){
		Dao<Product, Integer> dao = getHelper().getProductDao();
		if(product != null){
			try{
				dao.create(product);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public List<Product> getProduct(){
		Dao<Product,Integer> dao = getHelper().getProductDao();
		List<Product> productList = new ArrayList<Product>();
		QueryBuilder<Product, Integer> query = dao.queryBuilder();
		try{
			productList = query.query();
		}catch(Exception e){
			e.printStackTrace();
		}
		return productList;
	}
	
}
