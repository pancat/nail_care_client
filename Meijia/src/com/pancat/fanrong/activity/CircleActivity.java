package com.pancat.fanrong.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.fragment.CircleFragment;


public class CircleActivity extends Activity implements FragmentCallback{
	
	private CircleFragment circleFragment;
	private FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle);
		setFragment();
	}

	@SuppressLint("NewApi")
	private void setFragment() {
		fragmentManager = getFragmentManager();
		//开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(circleFragment == null){
			//如果MessageFragment为空，则创建一个并添加到界面上
			circleFragment = new CircleFragment();
			transaction.add(R.id.content, circleFragment);
		}
		else{
			transaction.show(circleFragment);
		}
		transaction.commit();
	}

	@Override
	public void callback(Bundle arg) {
		if(arg != null){
			Intent intent = new Intent(CircleActivity.this,CircleItemActivity.class);
			intent.putExtras(arg);
			startActivity(intent);
		}
	}

	@Override
	public void finishActivity() {
		// TODO Auto-generated method stub
	
	}
	
	
}
