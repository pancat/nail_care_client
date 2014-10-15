package com.pancat.fanrong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.R;
import com.pancat.fanrong.common.User;

public class TestLocation extends Activity {

	public TextView addresstext, textcommmonaddress;
	public Button testlogin;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	public GeoCoder geo = null;
	public String glableaddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_testlocation);
		textcommmonaddress = (TextView) findViewById(R.id.textaddress);
		testlogin = (Button) findViewById(R.id.testlogin);
		addresstext = (TextView) findViewById(R.id.showaddr);
		addresstext.setText("准备定位");

		// 百度要求的初始化

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

		// 启动定位
		mLocationClient.start();

		Log.i("locat", "locat");
		Log.i("locat", "locat isstart" + mLocationClient.isStarted());
		Log.i("locat", "ruquset():" + mLocationClient.requestLocation());
		if (mLocationClient != null && mLocationClient.isStarted()) {
			Log.i("locat", "locat aaaaaaaaaaaaaaaaaaaaaa");
			mLocationClient.requestLocation();
		} else
			Log.d("Locsdk4", "locclient is null or not start");

		geo = GeoCoder.newInstance();
		geo.setOnGetGeoCodeResultListener(new GeoListener());
		/*
		 * GeoCodeOption optiont=new GeoCodeOption();
		 * optiont.address(glableaddress);
		 * Log.i("locat","glabledaddress=  "+glableaddress);
		 * geo.setOnGetGeoCodeResultListener(new GeoListener());
		 * geo.geocode(optiont);
		 */

	}

	public void onClick(View v) {
		//先判断用户登没登陆
		if (!User.getInstance().isUserLogined()) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else {
			switch (v.getId()) {
			case R.id.testlogin:
				
				break;
			case R.id.commonaddress:
				// 获取字符型地理位置，可用于显示，可以编码为经纬度
				String strAddress = User.getInstance().getCommonAddressString();
				// 获取经纬度类型位置，可以用于计算距离
				LatLng latAddress = User.getInstance().getCommonAddressLatLng();

				textcommmonaddress.setText(strAddress);
				StringBuffer sb = new StringBuffer(256);
				sb.append("字符类型地址：" + strAddress);
				sb.append("\n纬度：" + latAddress.latitude);
				sb.append("\n经度：" + latAddress.longitude);
				textcommmonaddress.setText(sb.toString());
				break;

			default:
				break;
			}
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:

				break;
			default:
				break;
			}
		}

	};

	public class GeoListener implements OnGetGeoCoderResultListener {

		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {
			// TODO Auto-generated method stub
			// 通过文字地址获得经纬度
			result.getLocation();
			LatLng lt = result.getLocation();
			;
			double a = lt.latitude;
			// 纬度
			Log.i("locat", "从文字反编码的纬度=" + lt.latitude);
			// 精度
			Log.i("locat", "从文字反编码的纬度" + lt.longitude);

			Message msg = new Message();
			msg.obj = result;
			msg.what = 0;
			handler.sendMessage(msg);
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			// TODO Auto-generated method stub
			// 通过经纬度获得文字地址
			result.getLocation();
		}

	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			Log.i("locat", "isstarted:" + mLocationClient.isStarted() + "");
			addresstext.setText("开始定位");
			// Receive Location
			/*
			 * StringBuffer sb = new StringBuffer(256); sb.append("time : ");
			 * sb.toString(); String jingwei = "latitude:" +
			 * location.getLatitude() + "   "+ "lontitude" +
			 * location.getLongitude();
			 */
			String address = location.getAddrStr();

			// 写到shared里
			User.getInstance().setCommonAddress(location.getLatitude(),
					location.getLongitude(), location.getAddrStr());

			Log.i("locat", "addr");

			addresstext.setText("当前定位位置为：\n " + address);

			glableaddress = address;
			GeoCodeOption optiont = new GeoCodeOption();
			optiont.address(glableaddress);
			optiont.city(glableaddress);

			Log.i("locat", "glabledaddress=  " + glableaddress);
			Log.i("locat", "glabledaddress=  " + optiont.toString());
			if (optiont != null) {
				geo.geocode(optiont);
			} else
				Log.i("locat", "options=  " + optiont.toString());

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
