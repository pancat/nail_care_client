package com.pancat.fanrong.util;



import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.pancat.fanrong.MainActivity;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.bean.User;
import com.pancat.fanrong.mgr.AuthorizeMgr;

public class MapUtil {

	LocationClient mLocationClient = null;
	BDLocationListener myListener = new MyLocationListener();
   public String address=null;
   private static MapUtil mInstance;
	private User   mUser = null;


	public static MapUtil getInstance() {
		synchronized (MainApplication.getAppContext()) {
			if (mInstance == null) {
				mInstance = new MapUtil();
			}
		}
		return mInstance;
	}
	
	public void location() {
	
		mLocationClient = new LocationClient(MainApplication.getAppContext());

		mLocationClient.registerLocationListener(myListener);

		// 设置定位参数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}
	
	public String getAddress(){
		
		if(address!=null)
		return address;
		else
			return null;
		
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			address = location.getAddrStr();
			Log.i("maputil","thisthisthis is " + address);
			
			Message msg = new Message();
			msg.what = 1;
			msg.obj = location;
			MainActivity.handler2.sendMessage(msg);
			
			mLocationClient.stop();
			if (location == null)
				return;

		}

	}


}
