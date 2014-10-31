package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.adapter.GuessLikeAdapter;
import com.pancat.fanrong.bean.GuessLikeProduct;
import com.pancat.fanrong.view.ListLinearLayout;
import com.pancat.fanrong.view.ListViewWithLoad;
import com.pancat.fanrong.view.ListViewWithLoad.OnLoadListener;

@SuppressLint("ResourceAsColor")
public class HomeActivity extends Activity implements OnLoadListener{

	protected ScrollView scrollView;
	private ListViewWithLoad homeList;
	private GuessLikeAdapter guessLikeAdapter;
	private List<GuessLikeProduct> productList;
	
	List<GuessLikeProduct> list;
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				productList.addAll(list);
				guessLikeAdapter.notifyDataSetChanged();
				homeList.onLoadComplete();
				break;
			default:
				break;
			}
		}
		
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		LayoutInflater inflater = LayoutInflater.from(this);
		View headView = inflater.inflate(R.layout.headerview_home, null);
		productList = new ArrayList<GuessLikeProduct>();
		list = new ArrayList<GuessLikeProduct>();
		for(int i = 0;i < 5;i++){
			GuessLikeProduct product = new GuessLikeProduct();
			product.setDescription("Dell键盘");
			product.setDistance(10);
			product.setIconPath("http://fangmingdesign.cn/teaching/Platform/public/img/14147364656781414736376555.jpg");
			product.setPrice(10);
			product.setTitle("我的键盘");
			list.add(product);
		}
		productList.addAll(list);
		homeList = (ListViewWithLoad)findViewById(R.id.home_list);
		homeList.addHeaderView(headView);
		guessLikeAdapter = new GuessLikeAdapter(this, productList);
		homeList.setAdapter(guessLikeAdapter);
		homeList.setOnLoadListener(this);
	}

	@Override
	public void onLoad() {
		handler.sendEmptyMessage(1);
	}
	


}
