package com.pancat.fanrong.fragment;

import java.util.ArrayList;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Order;
import com.pancat.fanrong.temp.SampleData;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class OrderProductFragment extends Fragment {
	private static final String TAG = "OrderProductFragment";
	
	public static final int LOAD_ALL = 0;
	public static final int LOAD_WAITPAY = 1;
	public static final int LOAD_WAITAFFIRM = 2;
	
	private ArrayList<Order> orderList;
	private ListView listView;
	private BaseAdapter adapter;
	
	//其中参数为加载类型：LOAD_ALL,LOAD_WAITPAY,LOAD_WAITAFFIRM
	public static OrderProductFragment newInstance(int loadType){
		OrderProductFragment instance = new OrderProductFragment();
		instance.orderList = new ArrayList<Order>();
		instance.orderList = loadData(loadType);
		Log.d(TAG, TAG+": "+instance.orderList.size());
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_product, container,false);
		listView = (ListView)v.findViewById(R.id.fragment_order_product_list);
		adapter = new CustomAdapter();
		for(int i=0;i<orderList.size();i++)
			Log.d(TAG,orderList.get(i).getStateStr());
		
		listView.setAdapter(adapter);
		return v;
	}

	private static ArrayList<Order> loadData(int loadType){
		switch (loadType) {
		case LOAD_ALL:
			//TODO
			break;
		case LOAD_WAITPAY:
			//TODO
		case LOAD_WAITAFFIRM:
			//TODO
		default: 
			break;
		}
		
		//加载样例数据
		return SampleData.getnerateSmapleOrder();
	}
	private class CustomAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orderList.size();
		}

		@Override
		public Object getItem(int pos) {
			// TODO Auto-generated method stub
			return orderList.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			// TODO Auto-generated method stub
			return pos;
		}

		@Override
		public View getView(int pos, View courtView, ViewGroup parent) {
			ViewHolder vh;
			if(courtView == null){
				LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
				courtView = layoutInflater.inflate(R.layout.fragment_order_product_item, parent,false);
				vh = new ViewHolder();
				vh.orderState = (TextView)courtView.findViewById(R.id.fragment_order_product_item_state);
				vh.orderPrice = (TextView)courtView.findViewById(R.id.fragment_order_product_item_price);
				vh.orderPayBtn = (TextView)courtView.findViewById(R.id.fragment_order_product_item_pay);
				vh.orderInfoBtn = (ImageView)courtView.findViewById(R.id.fragment_order_product_item_orderdetail);
				vh.orderProductImg = (ImageView)courtView.findViewById(R.id.fragment_order_item_prdocutimg);
				vh.orderProductName = (TextView)courtView.findViewById(R.id.fragment_order_product_item_name);
				vh.orderProductNum = (TextView)courtView.findViewById(R.id.fragment_order_product_item_num);
				
				courtView.setTag(vh);
			}else{
				vh = (ViewHolder)courtView.getTag();
			}
			
			Order order = (Order)getItem(pos);
			vh.orderState.setText(order.getStateStr());
			vh.orderPrice.setText(order.getPrice()+"");
			/*
			 * 在这里需要添加vh.orderPayBtn及vh.orderInfoBtn事件
			 */
			vh.orderProductName.setText(order.getProduct().getProductTitle());
			//vh.orderProductNum
			
			return courtView;
		}
		private class ViewHolder{
			public TextView orderState;
			public TextView orderPrice;
			public TextView orderPayBtn;
			public ImageView orderInfoBtn;
			public ImageView orderProductImg;
			public TextView orderProductName;
			public TextView orderProductNum;
		}
	}
}
