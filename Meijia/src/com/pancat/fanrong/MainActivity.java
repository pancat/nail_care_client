package com.pancat.fanrong;


import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pancat.fanrong.activity.HomeActivity;
import com.pancat.fanrong.activity.MeActivity;
import com.pancat.fanrong.activity.OrderActivity;
import com.pancat.fanrong.activity.ServiceActivity;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {

	private LinearLayout container;
	private Button btnHome,btnOrder,btnService,btnMe;
	private Window subActivity;
	private int segment = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		container = (LinearLayout)findViewById(R.id.container);
		initBottomBar();
		segment = getIntent().getIntExtra("segment", 1);
		btnHome.setClickable(false);
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
			intent.setClass(MainActivity.this, ServiceActivity.class);
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
		btnHome = (Button)findViewById(R.id.btn_home);
		btnOrder = (Button)findViewById(R.id.btn_order);
		btnMe = (Button)findViewById(R.id.btn_me);
		btnService = (Button)findViewById(R.id.btn_service);
	}
	
	public void onClick(View v){
		container.removeAllViews();
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.btn_home:
			btnHome.setBackgroundColor(Color.parseColor("#0087cb"));
			btnOrder.setBackgroundColor(Color.parseColor("#dddddd"));
			btnMe.setBackgroundColor(Color.parseColor("#dddddd"));
			btnService.setBackgroundColor(Color.parseColor("#dddddd"));
			btnHome.setTextColor(Color.WHITE);
			btnOrder.setTextColor(Color.BLACK);
			btnMe.setTextColor(Color.BLACK);
			btnService.setTextColor(Color.BLACK);
			btnHome.setClickable(false);
			btnOrder.setClickable(true);
			btnMe.setClickable(true);
			btnService.setClickable(true);
			intent.setClass(MainActivity.this, HomeActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity1", intent);
			container.addView(subActivity.getDecorView());
			segment = 1;
			break;
		case R.id.btn_order:
			btnHome.setBackgroundColor(Color.parseColor("#dddddd"));
			btnOrder.setBackgroundColor(Color.parseColor("#0087cb"));
			btnMe.setBackgroundColor(Color.parseColor("#dddddd"));
			btnService.setBackgroundColor(Color.parseColor("#dddddd"));
			btnHome.setTextColor(Color.BLACK);
			btnOrder.setTextColor(Color.WHITE);
			btnMe.setTextColor(Color.BLACK);
			btnService.setTextColor(Color.BLACK);
			btnHome.setClickable(true);
			btnOrder.setClickable(false);
			btnMe.setClickable(true);
			btnService.setClickable(true);
			intent.setClass(MainActivity.this, OrderActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity2", intent);
			container.addView(subActivity.getDecorView());
			segment = 2;
			break;
		case R.id.btn_me:
			btnHome.setBackgroundColor(Color.parseColor("#dddddd"));
			btnOrder.setBackgroundColor(Color.parseColor("#dddddd"));
			btnMe.setBackgroundColor(Color.parseColor("#0087cb"));
			btnService.setBackgroundColor(Color.parseColor("#dddddd"));
			btnHome.setTextColor(Color.BLACK);
			btnOrder.setTextColor(Color.BLACK);
			btnMe.setTextColor(Color.WHITE);
			btnService.setTextColor(Color.BLACK);
			btnHome.setClickable(true);
			btnOrder.setClickable(true);
			btnMe.setClickable(false);
			btnService.setClickable(true);
			intent.setClass(MainActivity.this, MeActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity3", intent);
			container.addView(subActivity.getDecorView());
			segment = 3;
			break;
		case R.id.btn_service:
			btnHome.setBackgroundColor(Color.parseColor("#dddddd"));
			btnOrder.setBackgroundColor(Color.parseColor("#dddddd"));
			btnMe.setBackgroundColor(Color.parseColor("#dddddd"));
			btnService.setBackgroundColor(Color.parseColor("#0087cb"));
			btnHome.setTextColor(Color.BLACK);
			btnOrder.setTextColor(Color.BLACK);
			btnMe.setTextColor(Color.BLACK);
			btnService.setTextColor(Color.WHITE);
			btnHome.setClickable(true);
			btnOrder.setClickable(true);
			btnMe.setClickable(true);
			btnService.setClickable(false);
			intent.setClass(MainActivity.this, ServiceActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity4", intent);
			container.addView(subActivity.getDecorView());
			segment = 4;
			break;
		default:break;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		getLocalActivityManager().getCurrentActivity().onTouchEvent(event);
		return true;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		
		return true;
	}
	

}
