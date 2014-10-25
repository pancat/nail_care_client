package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;
import com.pancat.fanrong.fragment.OrderFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class OrderActivity extends Activity{
	
	private OrderFragment orderFragment;
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
		if(orderFragment == null){
			//如果MessageFragment为空，则创建一个并添加到界面上
			orderFragment = new OrderFragment();
			transaction.add(R.id.content, orderFragment);
		}
		else{
			transaction.show(orderFragment);
		}
		transaction.commit();
	}
	
}
