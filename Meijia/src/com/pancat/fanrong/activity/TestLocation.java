package com.pancat.fanrong.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.R;

public class TestLocation extends Activity {
	
	public TextView addresstext;
	
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SDKInitializer.initialize(MainApplication.getAppContext());
		
		setContentView(R.layout.activity_testlocation);
		
		addresstext=(TextView)findViewById(R.id.showaddr);
		addresstext.setText("准备定位");
		
		//百度要求的初始化
         
		mLocationClient = new LocationClient(MainApplication.getAppContext());
		
		mLocationClient.registerLocationListener(myListener);
		
		//设置定位参数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		mLocationClient.setLocOption(option);
		
		//启动定位
		mLocationClient.start();
		
		
		Log.i("locat","locat");
		Log.i("locat","locat isstart"+mLocationClient.isStarted());
		Log.i("locat","ruquset():"+mLocationClient.requestLocation());
		if (mLocationClient != null && mLocationClient.isStarted())
			{Log.i("locat","locat aaaaaaaaaaaaaaaaaaaaaa");
			mLocationClient.requestLocation();
			}
		else
			Log.d("Locsdk4", "locclient is null or not start");
		
	}
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.i("locat","yes,start");
			Log.i("locat",mLocationClient.isStarted()+"");
			addresstext.setText("开始定位");
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				Log.i("locat","type gps");
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				Log.i("locat","type network");
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			
			
			String jingwei = "latitude:" + location.getLatitude() + "   "
					+ "lontitude" + location.getLongitude();
			String address = location.getAddrStr();
			Log.i("locat","addr");
			String ssstr=sb.toString();
			System.out.println("aaaaaaaaaaaa addr info="+ address);
			System.out.println("aaaaaaaaaaaa addr info="+ ssstr);
			// TODO Auto-generated method stub
			addresstext.setText(address);
			
			if (location == null)
				return;

		}
		
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLocationClient.stop();
		
	}
}
