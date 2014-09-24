package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;
import com.pancat.fanrong.fragment.MomentFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class MomentActivity extends Activity{
	
	private MomentFragment momentFragment;
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
			momentFragment = new MomentFragment();
			transaction.add(R.id.content, momentFragment);
		}
		else{
			transaction.show(momentFragment);
		}
		transaction.commit();
	}
}
