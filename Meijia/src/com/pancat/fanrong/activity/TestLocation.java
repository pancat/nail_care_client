package com.pancat.fanrong.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMyLocationClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.User;
import com.pancat.fanrong.mgr.AuthorizeMgr;

public class TestLocation extends Activity {

	public TextView addresstext;
	private EditText addressdetail;
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
	private Marker mMarkerA;
	private String commonAddress;
	private String commondetail;
	private double commonLatitude;
	private double commonLongitude;
    private ImageView locationicon;
    
    
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_testlocation);
		//textcommmonaddress = (TextView) findViewById(R.id.textaddress);
		testlogin = (Button) findViewById(R.id.testlogin);
		addressdetail=(EditText)findViewById(R.id.addressdetail);
		addresstext = (TextView) findViewById(R.id.showaddr);
		addresstext.setText("准备定位");


		
		mLocationClient = new LocationClient(MainApplication.getAppContext());

		mLocationClient.registerLocationListener(myListener);

		// 设置定位参数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setOpenGps(true);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);

		// 初始化地图显示
		initBaiduMap();
		// 初始化百度地图组件
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);

		
		Resources rs=getApplication().getResources();
		Drawable dr=rs.getDrawable(R.drawable.icon_geo);
	//	bitmap bm=BitmapFactory.decodeResource(R.drawable.icon_geo, 1);
		 locationicon=new ImageView(this);
		 
		// locationicon.setImageMatrix(matrix);
    	locationicon.setImageDrawable(dr);
		locationicon.setImageResource(R.drawable.icon_geo);
		locationicon.setVisibility(View.VISIBLE);
		// 百度要求的初始化


		// mBaiduMap.setBuildingsEnabled(false);
		// Toast.makeText(getApplication(),"是否允许楼块"+
		// mBaiduMap.isBuildingsEnabled(), Toast.LENGTH_LONG).show();

		// 百度地图点击事件
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				mLocationClient.stop();
				// 通过点击的经纬度转换为坐标地址
				geo = GeoCoder.newInstance();
				ReverseGeoCodeOption option = new ReverseGeoCodeOption();
				option.location(arg0);
				geo.setOnGetGeoCodeResultListener(new GeoListener());
				// geo.reverseGeoCode(new
				// ReverseGeoCodeOption().location(ptCenter));
				geo.reverseGeoCode(option);

				mBaiduMap.setMyLocationEnabled(false);
				// 显示地图标志
				LatLng llA = arg0;
				if (mMarkerA != null)
					mMarkerA.remove();
				OverlayOptions ooA = new MarkerOptions().position(llA)
						.icon(bdA).zIndex(9).draggable(true);
				mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
				commonLatitude=arg0.latitude;
				commonLongitude=arg0.longitude;
			}

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

		});
		// 百度地图覆盖物单击
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				if (arg0 == mMarkerA)
					Toast.makeText(getApplication(), "点击了覆盖物",
							Toast.LENGTH_LONG).show();

				return false;
			}

		});

		mBaiduMap.setOnMyLocationClickListener(new OnMyLocationClickListener() {

			@Override
			public boolean onMyLocationClick() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplication(), "点击了定位图标", Toast.LENGTH_LONG)
						.show();
				return false;
			}

		});
		// 监听有没有网络连接
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
		mReceiver.returnnetstate();

		mLocationClient.start();

		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		} else
			Log.d("Locsdk4", "locclient is null or not start");

		// 获取屏幕宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		LayoutParams lp = mMapView.getLayoutParams();
		lp.height = (int) (screenHeigh * 0.70);
		// lp.width = (int) (screenWidth * 0.75);
		mMapView.setLayoutParams(lp);

		Point pt=new Point(70,lp.height-50);
		MapViewLayoutParams params=new MapViewLayoutParams.Builder().point(pt).align(
				MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM).width(80).height(80).build();
	
		
		locationicon.setLayoutParams(params);
		locationicon.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mLocationClient.start();
			}
			
		});
		mMapView.addView(locationicon);
		
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		}




	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//Toast.makeText(getApplication(), " "+mMapView.getHeight(), Toast.LENGTH_SHORT).show();
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
		
			// Toast.makeText(getApplication(), "得到文字地址是："
			// +result.getAddress(),Toast.LENGTH_LONG).show();
			ReverseGeoCodeResult.AddressComponent ac = result
					.getAddressDetail();
			String add = ac.city + ac.district + ac.street
					+ ac.streetNumber;
			// Toast.makeText(getApplication(), "具体地址是："
			// +add,Toast.LENGTH_LONG).show();
			addresstext.setText(add);
			commonAddress=add;
			testlogin.setOnClickListener(saveaddrlistener);

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
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					String name = info.getTypeName();
					Log.d("mark", "当前网络名称：" + name);
					isnet = true;
				} else {
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
			//	mLocationClient.start();
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

			case 1:

				final BDLocation location = (BDLocation) msg.obj;
				String address = location.getCity()+location.getDistrict() +location.getStreet()+
				location.getStreetNumber()+" ";
				Log.i("locat", "addr");
				location.getLocType();	
				addresstext.setText(address);	
				// write into user bean
				testlogin.setText("设为常用");
				mLocationClient.stop();
				
				
			    commonAddress=location.getCity()+" "+location.getStreet()+" "+location.getStreetNumber()+" ";
				commonLatitude=location.getLatitude();
				commonLongitude=location.getLongitude();
			
				
				testlogin.setOnClickListener(saveaddrlistener);

				// 在地图上显示自己的位置
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				us = mBaiduMap.getUiSettings();
				us.setCompassEnabled(true);
				us.setCompassPosition(new Point(60, 60));
				Log.i("让不让显示指南针", "" + us.isCompassEnabled());
				mBaiduMap
						.setMapStatus(MapStatusUpdateFactory
								.newMapStatus(new MapStatus.Builder().zoom(16)
										.build()));

				// 显示地图标志

				LatLng llA = new LatLng(location.getLatitude(),
						location.getLongitude());
				if (mMarkerA != null)
					mMarkerA.remove();
				OverlayOptions ooA = new MarkerOptions().position(llA)
						.icon(bdA).zIndex(9).draggable(true);
				mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);

				break;
			default:
				break;
			}
		}

	

	};
	
	private void setAddressIntoUser() {
		// TODO Auto-generated method stub
		
		User user = AuthorizeMgr.getInstance().getUser();
		user.setAddress(commonAddress+commondetail);
		user.setLatitude(commonLatitude);
		user.setLongitude(commonLongitude);
		//写入数据库
		AuthorizeMgr.getInstance().persistUser(user);
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			Log.i("locat", "isstarted:" + mLocationClient.isStarted() + "");
			// addresstext.setText("开始定位");
			testlogin.setText("正在定位");
			if (location != null) {
				Message msg = new Message();
				msg.what = 1;
				msg.obj = location;
				handler.sendMessage(msg);
			}
			else
				return;

		}

	}
	
private OnClickListener saveaddrlistener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			// write common address into user table
			commondetail=addressdetail.getText().toString();
			//commonAddress+=detail;
			setAddressIntoUser();
				
			User user = AuthorizeMgr.getInstance().getUser();
			// 获取字符型地理位置，可用于显示，可以编码为经纬度
			String commonAddress = user.getAddress();
			// 获取经纬度类型位置，可以用于计算距离
			double commonlatitude = user.getLatitude();
			double commonlongitude = user.getLongitude();
			StringBuffer sb = new StringBuffer(256);
			sb.append("保存成功!");
			sb.append("当前常用地址：" + commonAddress);
			 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
	           imm.hideSoftInputFromWindow(addressdetail.getWindowToken(),0);
		//	textcommmonaddress.setText(sb.toString());
			Toast.makeText(getApplication(), sb, Toast.LENGTH_SHORT).show();
		}
	};
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
		mLocationClient.stop();
		mMapView.onDestroy();
	}
}
