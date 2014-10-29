package com.pancat.fanrong.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMyLocationClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MapViewLayoutParams.Builder;

import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.R;
import com.pancat.fanrong.common.User;
import com.pancat.fanrong.mgr.AuthorizeMgr;

public class TestLocation extends Activity {

	public TextView addresstext, textcommmonaddress;
	public Button testlogin;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	public GeoCoder geo = null;
	public String glableaddress;
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	boolean isFirstLoc = true;
	private ConnectionChangeReceiver mReceiver = new ConnectionChangeReceiver();
	private SDKReceiver msdkReceiver;
	private UiSettings us;

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

		// 初始化地图显示
		initBaiduMap();
		// 初始化百度地图组件
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);

		// MapViewLayoutParams.Builder().
		 mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

		 @Override
		 public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		 showclick(arg0);
		 }

		 @Override
		 public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		 return false;
		 }

		 });

		// 监听有没有网络连接
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
		mReceiver.returnnetstate();

		// }

		Log.i("locat", "locat");
		Log.i("locat", "locat isstart" + mLocationClient.isStarted());
		Log.i("locat", "ruquset():" + mLocationClient.requestLocation());
		if (mLocationClient != null && mLocationClient.isStarted()) {
			Log.i("locat", "locat aaaaaaaaaaaaaaaaaaaaaa");
			mLocationClient.requestLocation();
		} else
			Log.d("Locsdk4", "locclient is null or not start");
		/*
		 * geo = GeoCoder.newInstance(); geo.setOnGetGeoCodeResultListener(new
		 * GeoListener());
		 * 
		 * GeoCodeOption optiont=new GeoCodeOption();
		 * optiont.address(glableaddress);
		 * Log.i("locat","glabledaddress=  "+glableaddress);
		 * geo.setOnGetGeoCodeResultListener(new GeoListener());
		 * geo.geocode(optiont);
		 */

		// 获取屏幕宽度

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		LayoutParams lp = mMapView.getLayoutParams();
		lp.height = (int) (screenHeigh * 0.40);
		lp.width = (int) (screenWidth * 0.75);

		mMapView.setLayoutParams(lp);

		// 监听key是否有效
		// keyReceiver kyreceiver=new keyReceiver();
		// registerReceiver(kyreceiver,new
		// IntentFilter("SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR"));

	}

	private void showclick(LatLng latlng) {
		// TODO Auto-generated method stub
		Toast.makeText(this,
				"你点了" + latlng.latitude + " " + "," + " " + latlng.longitude,
				Toast.LENGTH_LONG).show();
		mLocationClient.stop();
		double lat = latlng.latitude;
		double lon = latlng.longitude;
		LatLng ptCenter = new LatLng(lat, lon);
		// 反Geo搜索
		geo = GeoCoder.newInstance();
		geo.setOnGetGeoCodeResultListener(new GeoListener());
		geo.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
	}

	public class keyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			Toast.makeText(context, "监听到块key无效", Toast.LENGTH_SHORT).show();

		}

	}

	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(getApplication(),
						"key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",
						Toast.LENGTH_SHORT).show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(getApplication(), "您的网络出错啦！", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private void initBaiduMap() {
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		msdkReceiver = new SDKReceiver();
		registerReceiver(msdkReceiver, iFilter);
	}

	public class ConnectionChangeReceiver extends BroadcastReceiver {
		private ConnectivityManager connectivityManager;
		private NetworkInfo info;
		boolean isnet = false;

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

				Log.i("net", "监听到网络状态改变");
				// Toast.makeText(context,"监听到网络改变", Toast.LENGTH_SHORT).show();
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					String name = info.getTypeName();
					Log.d("mark", "当前网络名称：" + name);
					Log.i("net", "当前网络名称" + name);
					isnet = true;
					Toast.makeText(context, "当前网络可用", Toast.LENGTH_LONG).show();
					mLocationClient.start();
				} else {
					Log.d("mark", "没有可用网络");
					Toast.makeText(context, "当前没有网络", Toast.LENGTH_LONG).show();
					Log.i("net", "没有可用网络");
					mLocationClient.stop();

				}
			}
		}

		public boolean returnnetstate() {
			return isnet;

		}

	}

	public void onClick(View v) {
		// 先判断用户登没登陆
		if (!AuthorizeMgr.getInstance().hasLogined()) {
			Intent intent = new Intent(this, SignInActivity.class);
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
			result.getAddress();
			Toast.makeText(getApplication(), "得到文字地址是：" + result.getAddress(),
					Toast.LENGTH_LONG).show();
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:

				break;

			case 1:
				// 在地图上显示自己的位置
				BDLocation location = (BDLocation) msg.obj;
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				us = mBaiduMap.getUiSettings();
				us.setCompassEnabled(true);
				// us.setZoomGesturesEnabled(false);
				// us.setCompassPosition(pt);
				// point pt=new point(Point);
				us.setCompassPosition(new Point(60, 60));
				Log.i("让不让显示指南针", "" + us.isCompassEnabled());
				mBaiduMap
						.setMapStatus(MapStatusUpdateFactory
								.newMapStatus(new MapStatus.Builder().zoom(15)
										.build()));
				// 在地图上显示

				mBaiduMap.setMyLocationData(locData);

				// 在地图上显示
				mBaiduMap
						.setMyLocationConfigeration(new MyLocationConfiguration(
								MyLocationConfiguration.LocationMode.NORMAL,
								false, null) {
						});

				if (isFirstLoc) {
					isFirstLoc = false;
					LatLng ll = new LatLng(location.getLatitude(),
							location.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				}
				break;
			default:
				break;
			}
		}

	};

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			Log.i("locat", "isstarted:" + mLocationClient.isStarted() + "");
			addresstext.setText("开始定位");

			Message msg = new Message();
			msg.what = 1;
			msg.obj = location;
			handler.sendMessage(msg);

			String address = location.getAddrStr();

			// 应该改为写到sqlite里去
			// 写到shared里
			User.getInstance().setCommonAddress(location.getLatitude(),
					location.getLongitude(), location.getAddrStr());

			Log.i("locat", "addr");

			addresstext.setText("当前定位位置为：\n " + address);

			glableaddress = address;

			// 通过文字地址转换为坐标地址
			// GeoCodeOption optiont = new GeoCodeOption();
			// optiont.address(glableaddress);
			// optiont.city(glableaddress);
			// if (optiont != null) {
			// geo.geocode(optiont);
			// } else
			// Log.i("locat", "options=  " + optiont.toString());

			if (location == null)
				return;

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
		mLocationClient.stop();
		mMapView.onDestroy();
	}
}
