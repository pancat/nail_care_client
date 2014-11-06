package com.pancat.fanrong.fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.OrderInfoActivity;
import com.pancat.fanrong.bean.Order;
import com.pancat.fanrong.bean.Order.ORDERSTATE;
import com.pancat.fanrong.customview.ProductShow;
import com.pancat.fanrong.temp.SampleData;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderProductFragment extends Fragment {
	private static final String TAG = "OrderProductFragment";
	
	public static final int LOAD_ALL = 0;
	public static final int LOAD_WAITPAY = 1;
	public static final int LOAD_WAITAFFIRM = 2;
	
	private ArrayList<Order> orderList;
	private ListView listView;
	private BaseAdapter adapter;
	private boolean selState = false;
	private boolean[] selArr;
	private View selView = null;
	private TextView cancelBtn;
	private TextView delBtn;
	private TextView payBtn;
	
	private boolean isdel;
	private boolean ispay;
	private int nopay;
	private int selCount;
	
	//其中参数为加载类型：LOAD_ALL,LOAD_WAITPAY,LOAD_WAITAFFIRM
	public static OrderProductFragment newInstance(int loadType){
		OrderProductFragment instance = new OrderProductFragment();
		instance.orderList = new ArrayList<Order>();
		instance.orderList = loadData(loadType);
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_product, container,false);
		listView = (ListView)v.findViewById(R.id.fragment_order_product_list);
		listView.setDivider(null);
		
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
	
	public boolean updateData(int loadType){
		if(loadType < LOAD_ALL || loadType > LOAD_WAITAFFIRM) return false;
		
		orderList = loadData(loadType);
		if(selState){
			longClickItem();
		}
		adapter.notifyDataSetChanged();
		
		return false;
	}
	
	//显示选择菜单视图
	private void showSelView(){
		if(selView == null){
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			selView = inflater.inflate(R.layout.order_del_pay, null,false);
			
			cancelBtn = (TextView)selView.findViewById(R.id.order_del_pay_cancel);
			delBtn = (TextView)selView.findViewById(R.id.order_del_pay_del);
			payBtn = (TextView)selView.findViewById(R.id.order_del_pay_pay);
			
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					longClickItem();
				}
			});
			delBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(!isdel){
						Toast.makeText(getActivity(), "你没有选择任何订单", Toast.LENGTH_LONG).show();
						return ;
					}
					delOrders();
					//if(adapter != null)
						//adapter.notifyDataSetChanged();
					//dismissSelView();
				}
			});	
			payBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(!ispay){
						Toast.makeText(getActivity(), "你没有选择任何可支付的订单", Toast.LENGTH_LONG).show();
						return ;
					}
					goPay();
				}
			});
			
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.BOTTOM;
			
			getActivity().addContentView(selView, lp);
		}
		delBtn.setBackgroundResource(R.drawable.unsel_btn);
		payBtn.setBackgroundResource(R.drawable.unsel_btn);
		selView.setVisibility(View.VISIBLE);
	}
	
	//关闭选择菜单视图
	private void dismissSelView(){
		if(selView != null)
		{
			selView.setVisibility(View.GONE);
		}
	}
	
	private void delOrders(){
		//删除一些订单
		ArrayList<Order> res = new ArrayList<Order>();
		for(int i=0; i<selArr.length && i< orderList.size(); i++)
			if(!selArr[i])
				res.add(orderList.get(i));
		orderList = res;
		longClickItem();
	}
	
	private void goPay(){
		//去支付
		String tmp = "";
		ArrayList<Order> orders = new ArrayList<Order>();
		for(int i=0; i<selArr.length && i< orderList.size(); i++){
			if(selArr[i]){
				tmp += i+"=选中 ;";
				orders.add(orderList.get(i));
			}
		}
		Log.d(TAG, tmp);
		if(selState)
			longClickItem();
		
		Intent intent = new Intent(getActivity(),OrderInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Order.KEY, orders);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	//去订单的详情页面
	private void goOrderDetail(int pos){
		
	}
	
	private void initSelArr(){
		selArr = new boolean[orderList.size()];
	}
	
	private void longClickItem(){
		if(selState){
			selState = false;
			orderList.remove(orderList.size()-1);
			adapter.notifyDataSetChanged();
			dismissSelView();
		}else{
			selState = true; //更改状态
			
			isdel = false;
			ispay = false;
			nopay = 0;
			selCount = 0;
			
			orderList.add(new Order()); //增加空白
			initSelArr(); //初始化选择数组
			adapter.notifyDataSetChanged();//通知数据改变
			showSelView();//显示菜单
		}
	}
	private void setBtnBkg(){
		if(selCount > 0){
			delBtn.setBackgroundResource(R.drawable.pink_btn);
			isdel = true;
		}else{
			delBtn.setBackgroundResource(R.drawable.unsel_btn);
			isdel = false;
		}
		
		if(nopay > 0 || selCount==0 ){
			payBtn.setBackgroundResource(R.drawable.unsel_btn);
			ispay = false;
		}else{
			payBtn.setBackgroundResource(R.drawable.pink_btn);
			ispay = true;
		}
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
			if((pos == orderList.size()-1) && selState)
			{
				courtView = getVerticalView();
				return courtView;
			}
			
			ViewHolder vh;
			if(courtView == null){
				LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
				courtView = layoutInflater.inflate(R.layout.fragment_order_product_item, parent,false);
				vh = new ViewHolder();
				vh.orderState = (TextView)courtView.findViewById(R.id.fragment_order_product_item_state);
				vh.orderPrice = (TextView)courtView.findViewById(R.id.fragment_order_product_item_price);
				vh.orderPayBtn = (TextView)courtView.findViewById(R.id.fragment_order_product_item_pay);
				vh.orderSelBtn = (ImageView)courtView.findViewById(R.id.fragment_order_product_item_selorder);
				vh.orderProduct = (LinearLayout)courtView.findViewById(R.id.fragment_order_product_item_product);
				vh.orderInfos = (LinearLayout)courtView.findViewById(R.id.fragment_order_product_item_infos);
				
				//vh.orderProductImg = (ImageView)courtView.findViewById(R.id.fragment_order_item_prdocutimg);
				//vh.orderProductName = (TextView)courtView.findViewById(R.id.fragment_order_product_item_name);
				//vh.orderProductNum = (TextView)courtView.findViewById(R.id.fragment_order_product_item_num);
				
				courtView.setTag(vh);
			}else{
				vh = (ViewHolder)courtView.getTag();
			}
			
			Order order = (Order)getItem(pos);
			
			final ViewHolder fvh = vh;
			final int index = pos;
			final Order.ORDERSTATE state = order.getState();
			vh.orderInfos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(selState){ //在选择状态中
						//如果已经选中,变成没有选中
						if(index < selArr.length && selArr[index]){
							fvh.orderSelBtn.setImageResource(R.drawable.check_unsel);
							selArr[index] = false;
							Log.d(TAG, index+" 取消");
							if(state != Order.ORDERSTATE.WAITPAY){
								nopay--;
							}
							selCount--;
							setBtnBkg();
						}else{ //没有选中
							fvh.orderSelBtn.setImageResource(R.drawable.check_select);
							if(index < selArr.length){
								selArr[index] = true;
								Log.d(TAG, index+" 选中");
								if(state != Order.ORDERSTATE.WAITPAY){
									nopay++;
								}
								selCount++;
								setBtnBkg();
							}
						}
					}else{ //在未选择状态中
						goOrderDetail(index);
					}
				}
			});
			
			vh.orderInfos.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					longClickItem();
					return true;
				}
			});
			
			//直接点击支付按钮
			vh.orderPayBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					initSelArr();
					if(index < selArr.length)
						selArr[index] = true;
					goPay();
				}
			});
			
			vh.orderState.setText(order.getStateStr());
			vh.orderPrice.setText("¥"+order.getPrice());
			
			/*
			 * 在这里需要添加vh.orderPayBtn事件
			 */
			
			if(selState){
				vh.orderPayBtn.setVisibility(View.GONE);
				vh.orderSelBtn.setVisibility(View.VISIBLE);
				if(pos <selArr.length && selArr[pos])
					vh.orderSelBtn.setImageResource(R.drawable.check_select);
				else  vh.orderSelBtn.setImageResource(R.drawable.check_unsel);
			}else{
				if(order.getState() == ORDERSTATE.WAITPAY)
					vh.orderPayBtn.setVisibility(View.VISIBLE);
				else vh.orderPayBtn.setVisibility(View.GONE);
				vh.orderSelBtn.setVisibility(View.GONE);
			}
			
			
			//vh.orderProductName.setText(order.getProduct().getProductTitle());
			//vh.orderProductNum
			vh.orderProduct.removeAllViews();
			vh.orderProduct.addView(ProductShow.getView(getActivity(), order.getProduct()));
			
			return courtView;
		}
		
		private class ViewHolder{
			public TextView orderState;
			public TextView orderPrice;
			public TextView orderPayBtn;
			public ImageView orderSelBtn;
			public LinearLayout orderProduct;
			public LinearLayout orderInfos;
			//public ImageView orderProductImg;
			//public TextView orderProductName;
			//public TextView orderProductNum;
		}
	}
	private View getVerticalView()
	{
		AbsListView.LayoutParams vlp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,120);
		View v = new View(getActivity());
		v.setBackgroundColor(getActivity().getResources().getColor(R.color.background_order));
		v.setLayoutParams(vlp);
		return v;
	}
}
