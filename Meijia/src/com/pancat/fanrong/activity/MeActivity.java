package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pancat.fanrong.MainActivity;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.R;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;


public class MeActivity extends Activity {
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	public LatLng myLatLng;
	int age;
	String username, nick_name, email, avatar_uri;
	int id;
	String token;
	private Button btn3;
	private ListView listview;
	private TextView username_wig, nick_name_wig, age_wig, email_wig,
			avatar_uri_wig;
	int colorpink = Color.parseColor("#FA8072");
	int colordefault = android.graphics.Color.WHITE;
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

		super.onStart();
		MainApplication app = (MainApplication) getApplicationContext();
		app.setUserDate();
		
		// 获取用户的id和token
		id = app.getId();
		token = app.getToken();
	
		if (id == -1) {
			// 未登录
			Intent intent = new Intent(MeActivity.this, LogActivity.class);
			startActivity(intent);
			//btn3.setText("登录");
			
		} else {
			// 已登录，显示登录信息	
			btn3.setText("注销");
			btn3.setOnClickListener(logout);
			// 通过网络请求获取用户信息
			app.getUserDateFromServer();
			// 读取sharedperference,然后显示
			SharedPreferences userInfo = getSharedPreferences("userinfo",
					Activity.MODE_PRIVATE);
			username = userInfo.getString("username", "null");
			nick_name = userInfo.getString("nick_name", "null");
			age = userInfo.getInt("age", 0);
			email = userInfo.getString("email", "null");
			avatar_uri = userInfo.getString("avatar_uri", "null");

			username_wig.setText("username:" + username);
			nick_name_wig.setText("nick_name:" + nick_name);
			age_wig.setText("age:" + Integer.toString(age));
			email_wig.setText("email:" + email);
			avatar_uri_wig.setText("头像地址：" + avatar_uri);
		

		}
		/**
		 * 对自己进行定位
		 */
		
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		option.getAddrType();
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.d("Locsdk4", "locclient is null or not start");
        
		
		
		

	}

public class MyLocationListener implements BDLocationListener {
    	
    	
    	@Override
    	public void onReceiveLocation(BDLocation location) {

    		//Receive Location 
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
    		if (location.getLocType() == BDLocation.TypeGpsLocation){
    			sb.append("\nspeed : ");
    			sb.append(location.getSpeed());
    			sb.append("\nsatellite : ");
    			sb.append(location.getSatelliteNumber());
    			sb.append("\ndirection : ");
    			sb.append("\naddr : ");
    			sb.append(location.getAddrStr());
    			sb.append(location.getDirection());
    		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
    			sb.append("\naddr : ");
    			sb.append(location.getAddrStr());
    			//运营商信息
    			sb.append("\noperationers : ");
    			sb.append(location.getOperators());
    		}
    		
    		String jingwei="latitude:"+location.getLatitude()+"   "+"lontitude"+location.getLongitude();
    		String address=location.getAddrStr();
    		myLatLng=new LatLng(location.getLatitude(), location.getLongitude());
    		
    		SharedPreferences userInfo=getApplication().getSharedPreferences("userinfo",Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor=userInfo.edit();
						
			editor.putString("Address",address);
			editor.commit();
			
			
    		// TODO Auto-generated method stub
    		if(location == null) 
    			return;
    		
    		
			
    	}

    }



	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me);
		username_wig = (TextView) findViewById(R.id.username);
		nick_name_wig = (TextView) findViewById(R.id.nick_name);
		age_wig = (TextView) findViewById(R.id.age);
		email_wig = (TextView) findViewById(R.id.email);
		avatar_uri_wig = (TextView) findViewById(R.id.avatar_uri);
		listview = (ListView) findViewById(R.id.listview);
		btn3 = (Button) findViewById(R.id.btn3);
		
		
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.singlelist, new String[] { "title", "info" },
				new int[] { R.id.title, R.id.info });

		listview.setAdapter(adapter);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();

		map = new HashMap<String, Object>();
		map.put("title", "常用地址");
		map.put("info", "google 3");

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "我的收藏");
		map.put("info", "google 2");

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "我的红包");
		map.put("info", "google 3");

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "我的动态");
		map.put("info", "google 3");

		list.add(map);

		return list;
	}

	private OnClickListener gotologin = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得事件的按钮设置为粉色
			Intent intent = new Intent(MeActivity.this, LogActivity.class);
			startActivity(intent);

		}

	};

	private OnClickListener logout = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得事件的按钮设置为粉色
			SharedPreferences userInfo = getSharedPreferences("userinfo",
					Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = userInfo.edit();
			editor.clear();
			editor.commit();
			// MainApplication app=(MainApplication)getApplicationContext();
			// app.setUserDate();
			Intent intent = new Intent(MeActivity.this, MainActivity.class);
			startActivity(intent);

		}

	};

}
