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
	
	private CircleFragment momentFragment;
	private FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		setFragment();
	}

	@SuppressLint("NewApi")
	private void setFragment() {
		fragmentManager = getFragmentManager();
		//开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(momentFragment == null){
			//如果MessageFragment为空，则创建一个并添加到界面上
			momentFragment = new CircleFragment();
			transaction.add(R.id.content, momentFragment);
		}
		else{
			transaction.show(momentFragment);
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