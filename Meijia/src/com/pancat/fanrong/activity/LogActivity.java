package com.pancat.fanrong.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pancat.fanrong.R;
import com.pancat.fanrong.fragment.MeFragment;
import com.pancat.fanrong.fragment.MeRegFragment;

public class LogActivity extends Activity {
	private MeFragment meFragment;
	private MeRegFragment meRegFragment;
	private FragmentManager fragmentManager;
	private Button logbtn2, regbtn2;
	int colorpink = Color.parseColor("#FA8072");
	int colordefault = android.graphics.Color.WHITE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		setFragment();
		regbtn2 = (Button) findViewById(R.id.regbtn2);
		logbtn2 = (Button) findViewById(R.id.logbtn2);
		logbtn2.setBackgroundColor(colorpink);
		regbtn2.setBackgroundColor(colordefault);		
		regbtn2.setOnClickListener(showreg);
		logbtn2.setOnClickListener(showlog);
	}

	private OnClickListener showlog = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得事件的按钮设置为粉色
			logbtn2.setBackgroundColor(colorpink);
			regbtn2.setBackgroundColor(colordefault);

			// 显示登录页面的fragment
			setlogFragment();

		}

	};

	private OnClickListener showreg = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得事件的按钮设置为粉色

			regbtn2.setBackgroundColor(colorpink);
			logbtn2.setBackgroundColor(colordefault);

			// 显示注册页面的fragment
			setregFragment();

		}

	};
	
	@SuppressLint("NewApi")
	private void setFragment() {
		fragmentManager = getFragmentManager();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (meFragment == null) {
			// 如果MessageFragment为空，则创建一个并添加到界面上
			meFragment = new MeFragment();
			transaction.add(R.id.content, meFragment);
		} else {
			transaction.show(meFragment);
		}
		transaction.commit();
	}

	@SuppressLint("NewApi")
	private void setlogFragment() {
		fragmentManager = getFragmentManager();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		meFragment = new MeFragment();

		transaction.replace(R.id.content, meFragment);

		transaction.commit();
	}

	@SuppressLint("NewApi")
	private void setregFragment() {
		fragmentManager = getFragmentManager();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		meRegFragment = new MeRegFragment();

		transaction.replace(R.id.content, meRegFragment);

		transaction.commit();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
