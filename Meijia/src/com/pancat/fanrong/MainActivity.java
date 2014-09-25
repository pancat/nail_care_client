package com.pancat.fanrong;

import com.pancat.fanrong.R;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.baidu.android.pushservice.PushConstants;
import com.igexin.sdk.PushManager;
import android.util.Log;
import com.pancat.fanrong.activity.HomeActivity;
import com.pancat.fanrong.activity.MeActivity;
import com.pancat.fanrong.activity.MomentActivity;
import com.pancat.fanrong.activity.OrderActivity;
import com.pancat.fanrong.util.CommonPushMsgUtils;
import com.pancat.fanrong.util.ConfigHelperUtils;


@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements OnClickListener{
    //TAG 调试所用
	private static final String TAG = "MainActivity";
	
	private LinearLayout container;
	//底部四个tab
	private LinearLayout tabHome,tabOrder,tabMe,tabMoment;
	private Window subActivity;
	private int segment = 1;
	
	private Resources resource;
	
	//消息推送的配置选项
	private int msgPushType = ConfigHelperUtils.BAIDUMSGPUSH;
	//个推消息推送密钥  必须在主Activity中或服务中起动推送服务
	private String appkey = "";
	private String appsecret = "";
	private String appid = "";
	//百度消息推送密钥
	private String api_key = "";
	
	/**
	 * 第三方应用Master Secret，修改为正确的值，为了安全起见，请勿移动
	 */
	private static final String MASTERSECRET = "WL5D8yz7sZAR7h6P3Ufe65";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		container = (LinearLayout)findViewById(R.id.container);
		initBottomBar();
		segment = getIntent().getIntExtra("segment", 1);
		tabHome.setClickable(false);
		initMsgPush();//消息推送初始化
	}
	
	private void addView(){
		container.removeAllViews();
		Intent intent = new Intent();
		if(segment == 1){
			intent.setClass(MainActivity.this, HomeActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity1", intent);
		}
		else if(segment == 2){
			intent.setClass(MainActivity.this, OrderActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity2", intent);
		}
		else if(segment == 3){
			intent.setClass(MainActivity.this, MeActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity3", intent);
		}
		else if(segment == 4){
			intent.setClass(MainActivity.this, MomentActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity4", intent);
		}
		container.addView(subActivity.getDecorView());
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		addView();
	}

	private void initBottomBar(){
		tabHome = (LinearLayout)findViewById(R.id.tab_home);
		tabOrder = (LinearLayout)findViewById(R.id.tab_order);
		tabMe = (LinearLayout)findViewById(R.id.tab_me);
		tabMoment = (LinearLayout)findViewById(R.id.tab_moment);
		tabHome.setOnClickListener(this);
		tabOrder.setOnClickListener(this);
		tabMe.setOnClickListener(this);
		tabMoment.setOnClickListener(this);
	}
	
	public void onClick(View v){
		container.removeAllViews();
		resetBtn();
		Intent intent = new Intent();
		//改变点击的tab的图片和文字颜色并设置点击的tab为不可点击状态
		switch(v.getId()){
		case R.id.tab_home:
			ImageButton btnTabHome = (ImageButton)tabHome.findViewById(R.id.btn_tab_home);
			btnTabHome.setImageResource(R.drawable.tab_weixin_pressed);
			tabHome.setClickable(false);
			intent.setClass(MainActivity.this, HomeActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity1", intent);
			container.addView(subActivity.getDecorView());
			segment = 1;
			break;
		case R.id.tab_order:
			ImageButton btnTabOrder = (ImageButton)tabOrder.findViewById(R.id.btn_tab_order);
			btnTabOrder.setImageResource(R.drawable.tab_find_frd_pressed);
			tabOrder.setClickable(false);
			intent.setClass(MainActivity.this, OrderActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity2", intent);
			container.addView(subActivity.getDecorView());
			segment = 2;
			break;
		case R.id.tab_me:
			ImageButton btnTabMe = (ImageButton)tabMe.findViewById(R.id.btn_tab_me);
			btnTabMe.setImageResource(R.drawable.tab_address_pressed);
			tabMe.setClickable(false);
			
			intent.setClass(MainActivity.this,MeActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity3", intent);
			container.addView(subActivity.getDecorView());
			segment = 3;
			break;
		case R.id.tab_moment:
			ImageButton btnTabMoment = (ImageButton)tabMoment.findViewById(R.id.btn_tab_moment);
			btnTabMoment.setImageResource(R.drawable.tab_settings_pressed);
			
			intent.setClass(MainActivity.this, MomentActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity4", intent);
			container.addView(subActivity.getDecorView());
			segment = 4;
			break;
		default:break;
		}
	}
	
	/**
	 * 清除按钮选中状态并设置所有按钮为可点击
	 */
	private void resetBtn() {
		((ImageButton)tabHome.findViewById(R.id.btn_tab_home))
			.setImageResource(R.drawable.tab_weixin_normal);
		((ImageButton)tabOrder.findViewById(R.id.btn_tab_order))
			.setImageResource(R.drawable.tab_find_frd_normal);
		((ImageButton)tabMe.findViewById(R.id.btn_tab_me))
			.setImageResource(R.drawable.tab_address_normal);
		((ImageButton)tabMoment.findViewById(R.id.btn_tab_moment))
			.setImageResource(R.drawable.tab_settings_normal);
		tabHome.setClickable(true);
		tabOrder.setClickable(true);
		tabMe.setClickable(true);
		tabMoment.setClickable(true);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		getLocalActivityManager().getCurrentActivity().onTouchEvent(event);
		return true;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}
	
	private void initMsgPush(){
		
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
	    		   .detectDiskWrites()
	    		   .detectNetwork()
	    		   .penaltyLog()
	    		   .build());
	     StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
	    		   .detectLeakedSqlLiteObjects()
	    		   .penaltyLog()
	    		   .penaltyDeath()
	    		   .build());
	     
		try{
			if(msgPushType == ConfigHelperUtils.GETUIMSGPUSH){
				appid = ConfigHelperUtils.getMetaValue(this, "PUSH_APPID");
				appsecret = ConfigHelperUtils.getMetaValue(this,"PUSH_APPSECRET");
				appkey = ConfigHelperUtils.getMetaValue(this, "PUSH_APPKEY");
				Log.d(TAG, "appid:"+appid+" appsecret:"+appsecret+" appkey:"+appkey);
			}else if(msgPushType == ConfigHelperUtils.BAIDUMSGPUSH){
				api_key = ConfigHelperUtils.getMetaValue(this, "api_key");
				Log.v(TAG,"api_key:"+api_key);
			}
		}catch(NameNotFoundException e){
			Log.d(TAG,"don't found config key");
		}
		if(msgPushType == ConfigHelperUtils.GETUIMSGPUSH){
			//个推消息推送初始化
			PushManager.getInstance().initialize(getApplicationContext());
			Log.d(TAG,"jogrunner initialize success");
			
		}else if(msgPushType == ConfigHelperUtils.BAIDUMSGPUSH){
			//TODO 暂未实现百度云推送服务
			com.baidu.android.pushservice.PushManager.startWork(getApplicationContext(), 
					PushConstants.LOGIN_TYPE_API_KEY, api_key);
			
			//设置百度云推送通知格式
			CommonPushMsgUtils temp = new CommonPushMsgUtils(this, msgPushType);
			temp.setBaiDuNotificationLayoutId(R.layout.notification_custom_builder, 
					R.id.notification_icon, R.id.notification_title,R.id.notification_text, android.R.drawable.ic_media_play);
			temp.customNotificationStyle();
			Log.d(TAG,"hello world! jogrunner");
		}
	}
}
