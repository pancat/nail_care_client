package com.pancat.fanrong;


import java.io.File;

import android.content.Context;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pancat.fanrong.bean.User;
import com.pancat.fanrong.mgr.AuthorizeMgr;

//百度云必须要求实现
public class MainApplication extends FrontiaApplication {


	private static Context appContext;

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
		appContext = getApplicationContext();
		
		initUserInfo();
		
		initImageLoader(getApplicationContext());
	}

	public void initUserInfo()
	{
		AuthorizeMgr mgr = AuthorizeMgr.getInstance();
		User user = mgr.getPersistedUser();
		if (user != null)
		{
			mgr.setUser(user);
		}
	}

	
	public static Context getAppContext() {
		return appContext;
	}
	
	public static void initImageLoader(Context context){
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,"imageloader/cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .threadPriority(Thread.NORM_PRIORITY - 2)
        .denyCacheImageMultipleSizesInMemory()
        .memoryCache(new LruMemoryCache(8 * 1024 * 1024))	
        .discCacheSize(10 * 1024 * 1024)	
        .discCacheFileNameGenerator(new Md5FileNameGenerator()) 
        .tasksProcessingOrder(QueueProcessingType.LIFO)	
        .discCache(new UnlimitedDiscCache(cacheDir))	//自定义缓存文件路径
        .discCacheFileCount(100)			//缓存的文件数量
        .threadPoolSize(3)					//线程池内加载的数量			
        .build();
		ImageLoader.getInstance().init(config);
	}
}
