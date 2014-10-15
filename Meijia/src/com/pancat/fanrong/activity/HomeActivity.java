package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;
import com.pancat.fanrong.fragment.HomeFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class HomeActivity extends Activity{
	
	private HomeFragment homeFragment;
	private FragmentManager fragmentManager;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
	//	setFragment();
	}

	@SuppressLint("NewApi")
	private void setFragment() {
		fragmentManager = getFragmentManager();
		//开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(homeFragment == null){
			//如果MessageFragment为空，则创建一个并添加到界面上
			homeFragment = new HomeFragment();
			transaction.add(R.id.content, homeFragment);
		}
		else{
			transaction.show(homeFragment);
		}
		transaction.commit();
	}
	
	
}
