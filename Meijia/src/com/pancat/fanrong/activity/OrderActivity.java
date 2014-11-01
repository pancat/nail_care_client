package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.User;
import com.pancat.fanrong.fragment.OrderFragment;
import com.pancat.fanrong.fragment.OrderProductFragment;
import com.pancat.fanrong.fragment.OrderUserFragment;
import com.pancat.fanrong.mgr.AuthorizeMgr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class OrderActivity extends FragmentActivity{
	
	//private OrderFragment orderFragment;
	private FragmentManager fragmentManager;
	private OrderUserFragment orderUserFragment;
	private OrderProductFragment orderProductFragment = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_order);
		setFragment();
	}

	@SuppressLint("NewApi")
	private void setFragment() {
		fragmentManager = getSupportFragmentManager();
		//开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		if(orderUserFragment == null){
			//如果MessageFragment为空，则创建一个并添加到界面上
			orderUserFragment = OrderUserFragment.newInstance(AuthorizeMgr.getInstance().getPersistedUser());
			
			transaction.add(R.id.fragment_order_user_fragment,orderUserFragment);
		}
		else{
			transaction.show(orderProductFragment);
		}
		
		if(orderProductFragment == null){
			orderProductFragment =	OrderProductFragment.newInstance(OrderProductFragment.LOAD_ALL);
			transaction.add(R.id.fragment_order_product_fragment, orderProductFragment);
		}else{
			transaction.show(orderProductFragment);
		}
		transaction.commit();
	}
	
}
