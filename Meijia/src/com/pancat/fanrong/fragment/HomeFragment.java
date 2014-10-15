package com.pancat.fanrong.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.j256.ormlite.dao.Dao;
import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.AdvertiseActivity;
import com.pancat.fanrong.activity.ProductViewFragmentActivity;
import com.pancat.fanrong.bean.AdBannerItem;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.db.DatabaseManager;
import com.pancat.fanrong.db.DatabaseOpenHelper;
import com.pancat.fanrong.handler.HandlerFactory;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import org.json.*;

@SuppressLint("NewApi")
public class HomeFragment extends Fragment{
	private static final String TAG = "state";
	private View contextView;
    
	//美甲作品及美妆作品按钮
	private ImageButton meijia;
	private ImageButton meizhuang;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contextView = inflater
				.inflate(R.layout.fragment_home, container, false);
		
		initLeftButtonArr();
		
		return contextView;
	}

	

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.e(TAG, "pause");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.e(TAG, "stop");
		super.onStop();
	}

	@Override
	public void onStart() {
		Log.e(TAG, "start");
		super.onStart(); 
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "destroy");
		System.exit(0);
		super.onDestroy();
	}

    
	//author Jogrunner
	private void initLeftButtonArr()
	{
		meijia = (ImageButton)contextView.findViewById(R.id.btn_meijia_works);
		meizhuang = (ImageButton)contextView.findViewById(R.id.btn_meizhuang_works);
		meijia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getActivity(),ProductViewFragmentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(Product.TYPE, Product.MEIJIA);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		meizhuang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getActivity(),ProductViewFragmentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(Product.TYPE, Product.MEIZHUANGE);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
}
