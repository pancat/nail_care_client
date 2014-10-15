package com.pancat.fanrong.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pancat.fanrong.bean.AdBannerItem;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.bean.User;

/**
 * 
 * @author trhuo
 *
 */
public class DatabaseOpenHelper extends OrmLiteSqliteOpenHelper{
	
	private static final String DATABASE_NAME = "fanrong";
	private static final int DATABASE_VERSION = 	2;
	
	private Dao<User,Integer> userDao;
	private Dao<AdBannerItem, Integer> adBannerItemDao;
	private Dao<Circle,Integer> circleDao;
	
	public DatabaseOpenHelper(Context ctx){
		super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(arg1, User.class);
			TableUtils.createTable(arg1, AdBannerItem.class);
			TableUtils.createTable(arg1, Circle.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * version发生改变时触发
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		
	}
	
	/**
	 * 获取Dao操作类
	 * @return
	 */
	public Dao<User,Integer> getUserDao(){
		if(userDao == null){
			try {
				userDao = getDao(User.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return userDao;
	}
	
	public Dao<AdBannerItem, Integer> getAdBannerItemDao()
	{
		if(adBannerItemDao == null){
			try {
				adBannerItemDao = getDao(AdBannerItem.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return adBannerItemDao;
	}
	
	public Dao<Circle,Integer> getCircleDao(){
		if(circleDao == null){
			try {
				circleDao = getDao(Circle.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return circleDao;
	}
}
