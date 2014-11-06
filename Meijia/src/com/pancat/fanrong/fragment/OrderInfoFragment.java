package com.pancat.fanrong.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Order;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.customview.AllAuthorAllProductOrderView;
import com.pancat.fanrong.customview.AllProductOrderView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderInfoFragment extends Fragment {
	public static final String TAG = "OrderInfoFragment";
	public enum PAYBTNSTATE{UNKOWN,WEIXIN,BANK};
	
	private LinearLayout productsView;
	//private Product product;
	private TextView alterBtn;
	private TextView userNameTV;
	private TextView userPhoneTV;
	private TextView userAddrTV;
	private TextView userTimeTV;
	private ImageView weixinBtn;
	private ImageView bankBtn;
	private ArrayList<Order> orders;
	
	private PAYBTNSTATE curIndexForPayBtn = PAYBTNSTATE.UNKOWN;
	private int selPayImg;
	private int unselPayImg;
	
	public static OrderInfoFragment newInstance(Order order){
		OrderInfoFragment instance = new OrderInfoFragment();
		instance.orders = new ArrayList<Order>();
		instance.orders.add(order);
		return instance;
	}
	public static OrderInfoFragment newInstance(ArrayList<Order>orders){
		OrderInfoFragment instance = new OrderInfoFragment();
		instance.orders = orders;
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_order_info, container,false);
			curIndexForPayBtn = PAYBTNSTATE.UNKOWN;
			
			productsView = (LinearLayout)v.findViewById(R.id.fragment_order_info_products);
			alterBtn = (TextView)v.findViewById(R.id.fragment_order_info_alter);
			userNameTV = (TextView)v.findViewById(R.id.fragment_order_info_username);
			userPhoneTV = (TextView)v.findViewById(R.id.fragment_order_info_phone);
			userAddrTV = (TextView)v.findViewById(R.id.fragment_order_info_addr);
			userTimeTV = (TextView)v.findViewById(R.id.fragment_order_info_time);
			weixinBtn = (ImageView)v.findViewById(R.id.fragment_order_info_weixinpay);
			bankBtn = (ImageView)v.findViewById(R.id.fragment_order_info_bankpay);
			
			//selPayImg = getActivity().getResources().getDrawable(R.drawable.check_select);
			initEventListener();
			
			AllAuthorAllProductOrderView pvs = AllAuthorAllProductOrderView.getView(getActivity(), getProducts());
			productsView.addView(pvs);
			
			return v;
	}
	
	private ArrayList<Product> getProducts(){
		ArrayList<Product> products= new ArrayList<Product>();
		for(Order order:orders){
			for(Product p:order.getProducts()){
				products.add(p);
			}
		}
		return products;
	}
	
	private void initEventListener(){
		
		//修改按钮监听事件
		alterBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		if((orders != null) && (!orders.isEmpty())){
			Order order = orders.get(0);
			userNameTV.setText(order.getUser().getNickname());
			userPhoneTV.setText(order.getUser().getUsername());
			userAddrTV.setText(order.getUser().getAddress());
			userTimeTV.setText(order.getOrderTime());
		}
		
		weixinBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(curIndexForPayBtn != PAYBTNSTATE.WEIXIN){
					weixinBtn.setImageResource(R.drawable.check_select);
					if(curIndexForPayBtn != PAYBTNSTATE.UNKOWN)
						bankBtn.setImageResource(R.drawable.check_unsel);
					curIndexForPayBtn = PAYBTNSTATE.WEIXIN;
				}
				
			}
		});
		
		bankBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(curIndexForPayBtn != PAYBTNSTATE.BANK){
					bankBtn.setImageResource(R.drawable.check_select);
					if(curIndexForPayBtn != PAYBTNSTATE.UNKOWN)
						weixinBtn.setImageResource(R.drawable.check_unsel);
					curIndexForPayBtn = PAYBTNSTATE.BANK;
				}
				
			}
		});
	}
	
	public double getTotalMoney(){
		double money = 0;
		for(Order order:orders)
			money += order.getPrice();
		DecimalFormat df = new DecimalFormat("#.00");
		
		return Double.valueOf(df.format(money));
	}
}
