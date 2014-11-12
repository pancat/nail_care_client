package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
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
import com.baidu.mapapi.map.MyLocationConfiguration;
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
import com.pancat.fanrong.bean.Order;
import com.pancat.fanrong.common.User;
import com.pancat.fanrong.customview.FreeTimeTableView;
import com.pancat.fanrong.customview.FreeTimeTableView.OnButtonClick;
import com.pancat.fanrong.mgr.AuthorizeMgr;
import com.pancat.fanrong.util.LocalDateUtils;

public class OrderAuthorActivity extends FragmentActivity implements
		OnButtonClick {
	private static final String TAG = "OrderAuthorActivity";
	private TextView mTime;
	private TextView mPosition;
	private TextView mDetail;
	private View mMoreLabel;
	private EditText mMore;
	private EditText mDetailmsg;
	private ImageView timeCancel;
	private ImageView positionCancel;
	private ImageView detailCancel;
	private TextView affirmOrder;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	public GeoCoder geo = null;
	public String glableaddress;
	public static MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	boolean isFirstLoc = true;
	private UiSettings us;
	private ImageView locationicon;
	private Marker mMarkerA;
	private PopupWindow freeTimePopWindow = null;
	private FreeTimeTableView timeView = null;
	private boolean isfirstloc = true;
	private ScrollView scrollview;
	private LinearLayout headlinear;
	private LinearLayout maplinear;

	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_author_activity);
		mMapView = (MapView) findViewById(R.id.order_author_activity_map);
		mBaiduMap = mMapView.getMap();
		scrollview = (ScrollView) findViewById(R.id.order_author_scroll);
		maplinear = (LinearLayout) findViewById(R.id.maplinearout);
		headlinear = (LinearLayout) findViewById(R.id.headlinear);
		

		maplinear.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Toast.makeText(getApplication(), "你使用了down事件",
							Toast.LENGTH_SHORT).show();
					mMapView.requestDisallowInterceptTouchEvent(true);
				

					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Toast.makeText(getApplication(), "你使用了up事件",
							Toast.LENGTH_SHORT).show();
					mMapView.requestDisallowInterceptTouchEvent(false);
		
					return true;
				} else {
					return false;
				}
			}
		});
		mMapView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Toast.makeText(getApplication(), "你使用了down事件",
							Toast.LENGTH_SHORT).show();
					mMapView.requestDisallowInterceptTouchEvent(true);
	

					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Toast.makeText(getApplication(), "你使用了up事件",
							Toast.LENGTH_SHORT).show();
					mMapView.requestDisallowInterceptTouchEvent(false);
			
					return true;
				} else {
					return false;
				}
			}
		});
		
	
		
		initView();

		mLocationClient.start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	// 添加响应事件
	private void initView() {
		mTime = (TextView) findViewById(R.id.order_author_activity_time);
		mPosition = (TextView) findViewById(R.id.order_author_activity_position);
		mDetail = (TextView) findViewById(R.id.order_author_activity_detail);
		timeCancel = (ImageView) findViewById(R.id.order_author_activity_cancel_time);
		positionCancel = (ImageView) findViewById(R.id.order_author_activity_cancel_position);
		detailCancel = (ImageView) findViewById(R.id.order_author_activity_cancel_detail);
		affirmOrder = (TextView) findViewById(R.id.order_author_activity_affirm);
		// 初始化不显示取消按钮
		timeCancel.setVisibility(View.GONE);
		positionCancel.setVisibility(View.GONE);
		detailCancel.setVisibility(View.GONE);
		mMoreLabel = findViewById(R.id.order_author_activity_morelabel);
		mMore = (EditText) findViewById(R.id.order_author_activity_moremsg);
		mDetailmsg = (EditText) findViewById(R.id.order_author_activity_detailmsg);
		mDetailmsg.setVisibility(View.GONE);
		mMore.setVisibility(View.GONE);

		// mDetailmsg.setHeight(mDetail.getHeight());
		hideSoftKeyBoard(mTime);

		(LayoutInflater.from(this)
				.inflate(R.layout.order_author_activity, null))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dismissWindow();
					}
				});

		mDetailmsg
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == EditorInfo.IME_ACTION_DONE) {
							mDetail.setText(mDetailmsg.getText().toString());
							// Toast.makeText(getApplication(), "yew",
							// Toast.LENGTH_LONG).show();
							detailCancel.setVisibility(View.VISIBLE);
							mDetailmsg.setVisibility(View.GONE);
							return true;
						}
						return false;
					}
				});

		initmap();
		setListenEvent();
		setCancelListener();
	}

	@SuppressLint("NewApi")
	private void initmap() {
		// TODO Auto-generated method stub
		// 初始化百度地图组件

		// 获取屏幕宽度设置地图显示的格式
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		LayoutParams lp = mMapView.getLayoutParams();
		lp.height = (int) (screenHeigh * 0.40);
		lp.width = (int) (screenWidth);
		mMapView.setLayoutParams(lp);

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				mPosition.setClickable(false);
				// Toast.makeText(getApplication(),"你点了地图"
				// ,Toast.LENGTH_LONG).show();
				showclick(arg0);

				mBaiduMap.setMyLocationEnabled(false);
				// 显示地图标志
				LatLng llA = arg0;
				if (mMarkerA != null)
					mMarkerA.remove();
				OverlayOptions ooA = new MarkerOptions().position(llA)
						.icon(bdA).zIndex(9).draggable(true);
				mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			}

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

		});
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

		// mLocationClient.start();

		Resources rs = getApplication().getResources();
		Drawable dr = rs.getDrawable(R.drawable.icon_geo);
		// bitmap bm=BitmapFactory.decodeResource(R.drawable.icon_geo, 1);
		locationicon = new ImageView(this);

		locationicon.setImageDrawable(dr);
		locationicon.setImageResource(R.drawable.icon_geo);
		locationicon.setVisibility(View.VISIBLE);

		Point pt = new Point(70, lp.height - 50);
		MapViewLayoutParams params = new MapViewLayoutParams.Builder()
				.point(pt)
				.align(MapViewLayoutParams.ALIGN_LEFT,
						MapViewLayoutParams.ALIGN_BOTTOM).width(60).height(60)
				.build();

		locationicon.setLayoutParams(params);
		locationicon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mLocationClient.start();
			}

		});
		mMapView.addView(locationicon);
	}

	private void showclick(LatLng latlng) {
		// TODO Auto-generated method stub
		// Toast.makeText(this,"你点了" + latlng.latitude + " " + "," + " " +
		// latlng.longitude,Toast.LENGTH_LONG).show();
		mLocationClient.stop();
		double lat = latlng.latitude;
		double lon = latlng.longitude;
		LatLng ptCenter = new LatLng(lat, lon);
		// 反Geo搜索
		geo = GeoCoder.newInstance();
		geo.setOnGetGeoCodeResultListener(new GeoListener());
		geo.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
	}

	public class GeoListener implements OnGetGeoCoderResultListener {

		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {
			// TODO Auto-generated method stub
			// 通过文字地址获得经纬度
			result.getLocation();
			LatLng lt = result.getLocation();
			double a = lt.latitude;
			// 纬度
			Log.i("locat", "从文字反编码的纬度=" + lt.latitude);
			// 精度
			Log.i("locat", "从文字反编码的纬度" + lt.longitude);
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			// TODO Auto-generated method stub
			// 通过经纬度获得文字地址
			// result.getAddress();
			// Toast.makeText(getApplication(), "得到文字地址是：" +
			// result.getAddress(),Toast.LENGTH_LONG).show();
			mPosition.setText(result.getAddress());

			positionCancel.setVisibility(View.VISIBLE);

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
				us.setCompassPosition(new Point(60, 60));
				mBaiduMap.setMyLocationEnabled(true);

				mBaiduMap
						.setMapStatus(MapStatusUpdateFactory
								.newMapStatus(new MapStatus.Builder().zoom(15)
										.build()));
				// 在地图上显示
				mBaiduMap
						.setMyLocationConfigeration(new MyLocationConfiguration(
								MyLocationConfiguration.LocationMode.NORMAL,
								false, null) {
						});// 设置定位图层参数
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
				mLocationClient.stop();
				if (!isfirstloc) {
					mPosition.setText(location.getAddrStr());
					positionCancel.setVisibility(View.VISIBLE);
					mPosition.setClickable(false);

				} else {
					isfirstloc = false;
				}
				break;
			default:
				break;
			}
		}

	};

	// 百度定位监听器
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			Log.i("locat", "开始定位 isstarted:" + mLocationClient.isStarted() + "");

			Message msg = new Message();
			msg.what = 1;
			msg.obj = location;
			handler.sendMessage(msg);
			return;

		}

	}

	// 添加响应事件
	private void setListenEvent() {
		// 设置时间组件的监听事件
		mTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				hideSoftKeyBoard(view);
				showWindow();
			}
		});

		// 通过实现一个地理位置 跳转到可搜索页面
		mPosition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// mPosition.setClickable(false);

				final String commonaddress = AuthorizeMgr.getInstance()
						.getUser().getAddress();
				// detailCancel.setVisibility(View.VISIBLE);

				// mLocationClient.start();
				// ask whether use common address
				// Log.i("commonaddress", commonaddress)

				new AlertDialog.Builder(OrderAuthorActivity.this)
						.setTitle("常用地址")
						.setMessage(commonaddress)
						.setNegativeButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										mPosition.setText(commonaddress);
										positionCancel
												.setVisibility(View.VISIBLE);
										mPosition.setClickable(false);
									}
								})
						.setPositiveButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

									}
								}).show();

			}
		});

		// 详细地址点击
		mDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mDetailmsg.getVisibility() == View.VISIBLE) {
					mDetailmsg.setVisibility(View.GONE);
				} else {
			
					mDetailmsg.setVisibility(View.VISIBLE);
				}

			}
		});

		mMoreLabel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mMore.getVisibility() == View.VISIBLE)
					mMore.setVisibility(View.GONE);
				else
					mMore.setVisibility(View.VISIBLE);
			}
		});

		final Context context = this;
		// 提交按钮监听事件
		affirmOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, OrderInfoActivity.class);
				startActivity(intent);
			}
		});
	}

	// 一些必须的参数是否为空,为空返回null,否则返回Order对象
	private Order isNeedAreaNull() {
		String time = mTime.getText().toString();
		String position = mPosition.getText().toString();
		String detail = mDetail.getText().toString();
		String more = mMore.getText().toString();
		if (isNullForString(time) || isNullForString(position)
				|| isNullForString(detail)) {
			Log.d(TAG, "有信息为空");
			Toast.makeText(this, "必填信息不能为空", Toast.LENGTH_LONG).show();
			return null;
		}
		Order order = new Order();
		order.setOrderTime(time);
		order.setTime(LocalDateUtils.getDate(0));
		order.setUser(new User());
		// order.setProduct();
		return order;
	}

	private boolean isNullForString(String s) {
		if ((s == null) || s.equals(""))
			return true;
		return false;
	}

	private void setCancelListener() {
		timeCancel.setOnClickListener(new OnClickListener() {
			final TextView timeTmp = mTime;
			final String str = getResources().getString(R.string.order_time);

			@Override
			public void onClick(View v) {
				timeTmp.setText(str);
				v.setVisibility(View.GONE);
			}
		});

		positionCancel.setOnClickListener(new OnClickListener() {
			final TextView positionTmp = mPosition;
			final String str = getResources().getString(R.string.location);

			@Override
			public void onClick(View v) {
				positionTmp.setText(str);
				mPosition.setClickable(true);
				v.setVisibility(View.GONE);
			}
		});

		detailCancel.setOnClickListener(new OnClickListener() {
			final TextView detailTmp = mDetail;
			final String str = getResources().getString(
					R.string.detail_location);

			@Override
			public void onClick(View v) {
				detailTmp.setText(str);
				v.setVisibility(View.GONE);
			}
		});

	}

	// 显示时间窗口
	public void showWindow() {
		ArrayList<Map<Integer, Integer>> m = new ArrayList<Map<Integer, Integer>>();
		Map<Integer, Integer> ma = new HashMap<Integer, Integer>();
		for (int i = 9; i < 18; i++)
			ma.put(i, FreeTimeTableView.IDLE);
		m.add(ma);

		if (freeTimePopWindow == null) {
			timeView = (new FreeTimeTableView(this).setData(m));
			timeView.setButtonClickListener(this);

			freeTimePopWindow = new PopupWindow(timeView);
			freeTimePopWindow.setFocusable(true);
			freeTimePopWindow.setOutsideTouchable(true);
			freeTimePopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			freeTimePopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
			freeTimePopWindow.setBackgroundDrawable(new ColorDrawable(this
					.getResources().getColor(R.color.blue)));

		}
		freeTimePopWindow.update();
		if (freeTimePopWindow.isShowing())
			dismissWindow();
		else {
			freeTimePopWindow.showAtLocation(
					findViewById(R.id.order_author_activity), Gravity.BOTTOM
							| Gravity.LEFT, 0, 0);
		}
	}

	public void dismissWindow() {
		if (freeTimePopWindow != null) {
			freeTimePopWindow.dismiss();
		}
	}

	// 隐藏软件键盘
	private void hideSoftKeyBoard(View view) {
		InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	// 时间表点击回调事件
	@Override
	public void setOnButtonClick(String time) {
		if (time == null || time.equals("")) {
			Toast.makeText(this, "你不能点击别人已经预约过的时间,请重试!", Toast.LENGTH_LONG);
			return;
		}

		if (mTime != null) {
			mTime.setText(time);
			timeCancel.setVisibility(View.VISIBLE);
			dismissWindow();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// unregisterReceiver(mReceiver);
		if (mLocationClient.isStarted())
			mLocationClient.stop();

		mMapView.onDestroy();
	}

}
