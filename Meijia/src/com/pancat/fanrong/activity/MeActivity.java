package com.pancat.fanrong.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.pancat.fanrong.MainActivity;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.R;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.common.User;

public class MeActivity extends Activity implements FragmentCallback {
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener2();

	int age;
	String username, nick_name, email, avatar_uri;
	int id;
	String token;
	private Button loginOrout;
	private ListView listview;
    private TextView nickname;
	int colorpink = Color.parseColor("#FA8072");
	int colordefault = android.graphics.Color.WHITE;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// MainApplication app = (MainApplication) getApplicationContext();
		// app.setUserDate();
		// 获取用户的id和token
		// id = app.getId();
		// token = app.getToken();

		User user = User.getInstance();
		user.setUserDate();
		id = user.getId();
		token = user.getToken();
		Log.i("User", "id= " + id);
		Log.i("User", "token= " + token);

		if (id == -1) {
			// 未登录
			loginOrout.setText("登录");
			loginOrout.setOnClickListener(login);
			// Intent intent = new Intent(MeActivity.this, LogActivity.class);
			// startActivity(intent);

		} else {
			// 已登录，显示登录信息
			loginOrout.setText("注销");
			loginOrout.setOnClickListener(logout);
			// 通过网络请求获取用户信息

			// app.getUserDateFromServer();

			user.getUserDateFromServer();
			// 读取sharedperference,然后显示
			SharedPreferences userInfo = getSharedPreferences("userinfo",
					Activity.MODE_PRIVATE);
			username = userInfo.getString("username", "null");
			nick_name = userInfo.getString("nick_name", "null");
			age = userInfo.getInt("age", 0);
			email = userInfo.getString("email", "null");
			avatar_uri = userInfo.getString("avatar_uri", "null");
			
			nickname.setText(nick_name);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

		super.onStart();

		/**
		 * 对自己进行定位
		 */
		SDKInitializer.initialize(MainApplication.getAppContext());
		mLocationClient = new LocationClient(MainApplication.getAppContext());
		mLocationClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);

		mLocationClient.setLocOption(option);
		mLocationClient.start();
		Log.i("locat", "locat");
		Log.i("locat", "locat isstart" + mLocationClient.isStarted());
		Log.i("locat", "ruquset():" + mLocationClient.requestLocation());
		if (mLocationClient != null && mLocationClient.isStarted()) {
			Log.i("locat", "locat aaaaaaaaaaaaaaaaaaaaaa");
			mLocationClient.requestLocation();
		} else
			Log.d("Locsdk4", "locclient is null or not start");

	}

	public class MyLocationListener2 implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.i("locat", "yes,start");
			Log.i("locat", mLocationClient.isStarted() + "");
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
				Log.i("locat", "type gps");
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
				Log.i("locat", "type network");
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}

			String jingwei = "latitude:" + location.getLatitude() + "   "
					+ "lontitude" + location.getLongitude();
			String address = location.getAddrStr();
			Log.i("locat", "addr");
			String ssstr = sb.toString();
			System.out.println("aaaaaaaaaaaa addr info=" + address);
			System.out.println("aaaaaaaaaaaa addr info=" + ssstr);
			// TODO Auto-generated method stub
			if (location == null)
				return;

		}

	}

	PopupWindow pw;
	Button btncancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me);

		loginOrout = (Button) findViewById(R.id.loginOrout);
		Log.i("context", "Me context   " + MainApplication.getAppContext());
		nickname=(TextView)findViewById(R.id.nick_name);
		

	}

	private OnClickListener what = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			 pw.dismiss();
		}

	};

	public void show() {
		
		View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow, null);
		pw = new PopupWindow(contentView, 500, 400);
		pw.setOutsideTouchable(true);
		pw.setFocusable(true);
		pw.update();
		pw.showAtLocation(findViewById(R.id.scrollview), Gravity.CENTER, 0, 0);
	
		btncancle = (Button) findViewById(R.id.BtnOK);
		btncancle.setText("hahahaha");
	    //btncancle.setOnClickListener(what);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.redpacket:
		//	show();
			break;
		case R.id.commonaddress:
			Intent it = new Intent(this, TestLocation.class);
			startActivity(it);
			break;
		case R.id.discount:
			break;
		case R.id.collection:
			break;
		case R.id.messagecenter:
			break;
		case R.id.servicephone:
			break;

		case R.id.usercircleimage:
			if (id == -1) {
				Toast.makeText(this, "您还没登录", Toast.LENGTH_LONG).show();
			} else {

			}
			break;

		default:
			break;
		}
	}

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
	private OnClickListener login = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MeActivity.this, LogActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}

	};

	@Override
	public void callback(Bundle arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishActivity() {
		// TODO Auto-generated method stub

	}

}
