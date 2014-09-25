package com.pancat.fanrong;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.pancat.fanrong.activity.HomeActivity;
import com.pancat.fanrong.activity.MeActivity;
import com.pancat.fanrong.activity.MomentActivity;
import com.pancat.fanrong.activity.OrderActivity;


@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements OnClickListener{
	
	private LinearLayout container;
	//底部四个tab
	private LinearLayout tabHome,tabOrder,tabMe,tabMoment;
	private Window subActivity;
	private int segment = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		container = (LinearLayout)findViewById(R.id.container);
		initBottomBar();
		segment = getIntent().getIntExtra("segment", 1);
		tabHome.setClickable(false);
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
	
}
