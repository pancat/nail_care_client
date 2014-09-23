package com.pancat.fanrong;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.pancat.fanrong.fragment.HomeFragment;
import com.pancat.fanrong.fragment.MeFragment;
import com.pancat.fanrong.fragment.MomentFragment;
import com.pancat.fanrong.fragment.OrderFragment;


public class MainActivity extends Activity implements OnClickListener{

	private HomeFragment home;
	private OrderFragment order;
	private MeFragment me;
	private MomentFragment moment;
	
	//底部四个按钮
	private LinearLayout tabHome;
	private LinearLayout tabOrder;
	private LinearLayout tabMe;
	private LinearLayout tabMoment;
	
	//用于对Fragment进行管理
	private FragmentManager fragmentManager;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		fragmentManager = getFragmentManager();
		setTabSelection(0);
	}
	
	/**
	 * 初始化组件
	 */
	private void initViews(){
		tabHome = (LinearLayout) findViewById(R.id.tab_home);
		tabOrder = (LinearLayout) findViewById(R.id.tab_order);
		tabMe = (LinearLayout) findViewById(R.id.tab_me);
		tabMoment = (LinearLayout) findViewById(R.id.tab_moment);
		
		tabHome.setOnClickListener(this);
		tabOrder.setOnClickListener(this);
		tabMe.setOnClickListener(this);
		tabMoment.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tab_home:
			setTabSelection(0);
			break;
		case R.id.tab_order:
			setTabSelection(1);
			break;
		
		case R.id.tab_me:
			setTabSelection(2);
			break;
		case R.id.tab_moment:
			setTabSelection(3);
			break;
		default:break;
		}
	}
	
	/**
	 * 根据传入的index来设置选中的tab页
	 * @param index tab索引
	 */
	@SuppressLint("NewApi")
	private void setTabSelection(int index){
		//重置按钮
		resetBtn();
		//开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		//先隐藏掉所有的Fragment，防止多个Fragment显示在界面上
		hideFragment(transaction);
		switch(index){
		case 0:
			//改变控件的图片和文字颜色
			ImageButton btnTabHome = (ImageButton)tabHome.findViewById(R.id.btn_tab_home);
			btnTabHome.setImageResource(R.drawable.tab_weixin_pressed);
			if(home == null){
				//如果MessageFragment为空，则创建一个并添加到界面上
				home = new HomeFragment();
				
				transaction.add(R.id.content, home);
			}
			else{
				//MessageFragment不为空，直接显示出来
				transaction.show(home);
			}
			break;
		case 1:
			ImageButton btnTabOrder = (ImageButton)tabOrder.findViewById(R.id.btn_tab_order);
			btnTabOrder.setImageResource(R.drawable.tab_find_frd_pressed);
			if(order == null){
				order = new OrderFragment();
				transaction.add(R.id.content, order);
			}
			else{
				transaction.show(order);
			}
			break;
		case 2:
			ImageButton btnTabMe = (ImageButton)tabMe.findViewById(R.id.btn_tab_me);
			btnTabMe.setImageResource(R.drawable.tab_address_pressed);
			if(me == null){
				me = new MeFragment();
				transaction.add(R.id.content, me);
			}
			else{
				transaction.show(me);
			}
			break;
		case 3:
			ImageButton btnTabMoment = (ImageButton)tabMoment.findViewById(R.id.btn_tab_moment);
			btnTabMoment.setImageResource(R.drawable.tab_settings_pressed);
			if(moment == null){
				moment = new MomentFragment();
				transaction.add(R.id.content, moment);
			}
			else{
				transaction.show(moment);
			}
			break;
		default:break;
		}
		transaction.commit();
	}

	/**
	 * 清除按钮选中状态
	 */
	private void resetBtn() {
		((ImageButton)tabHome.findViewById(R.id.btn_tab_home))
			.setImageResource(R.drawable.tab_weixin_normal);
		((ImageButton)tabOrder.findViewById(R.id.btn_tab_order))
			.setImageResource(R.drawable.tab_find_frd_normal);
		((ImageButton)tabMe.findViewById(R.id.btn_tab_me))
			.setImageResource(R.drawable.tab_address_normal);
		((ImageButton)tabMoment.findViewById(R.id.btn_tab_moment))
			.setImageResource(R.drawable.tab_settings_normal);
	}

	/**
	 * 将所有的Fragment置为隐藏状态
	 * @param transaction 操作Fragment的事务
	 */
	@SuppressLint("NewApi")
	private void hideFragment(FragmentTransaction transaction) {
		if(home != null){
			transaction.hide(home);
		}
		if(order != null){
			transaction.hide(order);
		}
		if(me != null){
			transaction.hide(me);
		}
		if(moment != null){
			transaction.hide(moment);
		}
	}

}
