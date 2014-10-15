package com.pancat.fanrong.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.pancat.fanrong.MainActivity;
import com.pancat.fanrong.R;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.fragment.LoginFragment;
import com.pancat.fanrong.fragment.SigninFragment;

public class LoginActivity extends Activity implements FragmentCallback {
	private LoginFragment loginFragment;
	private SigninFragment signinFragment;
	private FragmentManager fragmentManager;
	private Button logbtn2, regbtn2;
	int colorpink = Color.parseColor("#FA8072");
	int colordefault = android.graphics.Color.WHITE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_login);
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
		if (loginFragment == null) {
			// 如果MessageFragment为空，则创建一个并添加到界面上
			loginFragment = new LoginFragment();
			transaction.add(R.id.content, loginFragment);
		} else {
			transaction.show(loginFragment);
		}
		transaction.commit();
	}

	@SuppressLint("NewApi")
	private void setlogFragment() {
		fragmentManager = getFragmentManager();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		loginFragment = new LoginFragment();

		transaction.replace(R.id.content, loginFragment);

		transaction.commit();
	}

	@SuppressLint("NewApi")
	private void setregFragment() {
		fragmentManager = getFragmentManager();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		signinFragment = new SigninFragment();

		transaction.replace(R.id.content, signinFragment);

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

	@Override
	public void callback(Bundle arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishActivity() {
		// TODO Auto-generated method stub
		finish();
	}
	
	/*
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	// TODO Auto-generated method stub  
	if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){  
	    //需要处理  
		Intent it=new Intent(this,MainActivity.class);
		startActivity(it);
		
		
	}  
	    return false;  
	}  
    */	
}
