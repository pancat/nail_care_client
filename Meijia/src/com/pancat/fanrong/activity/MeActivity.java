package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;
import com.pancat.fanrong.fragment.MeFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class MeActivity extends Activity{
	
	private MeFragment meFragment;
	private FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me);
		setFragment();
	}

	@SuppressLint("NewApi")
	private void setFragment() {
		fragmentManager = getFragmentManager();
		//开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(meFragment == null){
			//如果MessageFragment为空，则创建一个并添加到界面上
			meFragment = new MeFragment();
			transaction.add(R.id.content, meFragment);
		}
		else{
			transaction.show(meFragment);
		}
		transaction.commit();
	}
}
