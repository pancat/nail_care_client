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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OrderActivity extends FragmentActivity{
	private static final String TAG = "OrderActivity";
	
	private final static int ALLORDER = 0;
	private final static int WAITPAY = 1;
	private final static int WAITAFFIRM = 2;
	
	private FragmentManager fragmentManager;
	private OrderUserFragment orderUserFragment = null;
	private OrderProductFragment orderProductFragment = null;
	
	private TextView allOrder;
	private TextView waitPayOrder;
	private TextView waitAffirmOrder;
	private int curIndex = -1;
	
	private int menuSelColor;
	private int menuUnSelColor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_order);
		
		initView();
		setFragment();
	}

	@SuppressLint("NewApi")
	private void setFragment() {
		fragmentManager = getSupportFragmentManager();
		//开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		if(orderUserFragment == null){
			orderUserFragment = OrderUserFragment.newInstance(AuthorizeMgr.getInstance().getPersistedUser());
			transaction.add(R.id.fragment_order_user_fragment,orderUserFragment);
		}else{
			transaction.show(orderProductFragment);
		}
		
		if(orderProductFragment == null){
			updateData();
		}
		
		if(fragmentManager.findFragmentById(R.id.fragment_order_product_fragment) == null){
			transaction.add(R.id.fragment_order_product_fragment, orderProductFragment);
		}else{
			transaction.show(orderProductFragment);
		}
		
		transaction.commit();
	}
	
	private void initView(){
		allOrder = (TextView)findViewById(R.id.fragment_order_all);
		waitPayOrder = (TextView)findViewById(R.id.fragment_order_waitpay);
		waitAffirmOrder = (TextView)findViewById(R.id.fragment_order_waitaffirm);
		
		menuSelColor = getResources().getColor(R.color.labelblue);
		menuUnSelColor = getResources().getColor(R.color.c96);
		
		menuSel(ALLORDER);
		
		allOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuSel(ALLORDER);
			}
		});
		waitPayOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuSel(WAITPAY);
			}
		});
		waitAffirmOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuSel(WAITAFFIRM);
			}
		});
	}
	
	//当菜单键选择时，更改颜色及更新数据
	private void menuSel(int index){
		if(index == curIndex) return ;
		if(index <ALLORDER || index > WAITAFFIRM) return ;
		
		TextView[] tmp = {allOrder,waitPayOrder,waitAffirmOrder};
		if(curIndex >=0)
			tmp[curIndex].setTextColor(menuUnSelColor);
		tmp[index].setTextColor(menuSelColor);
		
	    curIndex = index;
	    updateData();
	}
	
	//当切换菜单键时，更新fragment数据
	private void updateData(){
		int loadTypes[] = {OrderProductFragment.LOAD_ALL,OrderProductFragment.LOAD_WAITPAY,OrderProductFragment.LOAD_WAITAFFIRM};
		if(orderProductFragment != null){
			orderProductFragment.updateData(loadTypes[curIndex>=0?curIndex:0]);
		}else{
			orderProductFragment = OrderProductFragment.newInstance(loadTypes[curIndex>=0?curIndex:0]);
		}
	}
}
