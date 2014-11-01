package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;
import com.pancat.fanrong.fragment.OrderInfoFragment;
import com.pancat.fanrong.temp.SampleData;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderInfoActivity extends FragmentActivity {

	private LinearLayout fragmentLayout;
	private FragmentManager fragmentManager;
	private LinearLayout suspendBtn;
	private TextView goPayBtn;
	private OrderInfoFragment orderInfoFragment;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_order_info);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_titlebar_back);
		
		fragmentLayout = (LinearLayout)findViewById(R.id.activity_order_info);
		
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transcation = fragmentManager.beginTransaction();
		orderInfoFragment = OrderInfoFragment.newInstance(SampleData.getnerateSmapleOrder().get(0));
		transcation.add(R.id.activity_order_info, orderInfoFragment);
		transcation.commit();
		
		showSuspendPayButton();
	}
	
   private void showSuspendPayButton()
   {
	   suspendBtn = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.pay_button, null);
	   goPayBtn = (TextView)suspendBtn.findViewById(R.id.pay_button_pay);
	   if(orderInfoFragment != null)
		   ((TextView)suspendBtn.findViewById(R.id.pay_button_money)).setText(orderInfoFragment.getTotalMoney()+"");
	   FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
	   
	   lp.gravity = Gravity.BOTTOM;
	   addContentView(suspendBtn, lp);
   }
}
