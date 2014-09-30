package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.pancat.fanrong.R;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.fragment.MeFragment;
import com.pancat.fanrong.fragment.MeRegFragment;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

public class MeActivity extends Activity {

	private MeFragment meFragment;
	private MeRegFragment meRegFragment;
	private FragmentManager fragmentManager;
	private Button btn3,btn4;
	private ListView listview;
	int colorpink = Color.parseColor("#FA8072");
	int colordefault = android.graphics.Color.WHITE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me);
		//setFragment();
	
		btn3=(Button)findViewById(R.id.btn3);
		btn3.setOnClickListener(gotolog);
		//btn4=(Button)findViewById(R.id.btn4);
		//btn4.setOnClickListener(testnet);
		listview=(ListView)findViewById(R.id.listview);
		//ArrayAdapter<String> aadata= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
		
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.singlelist,
				new String[]{"title","info"},
				new int[]{R.id.title,R.id.info});
		
		listview.setAdapter(adapter);
	}
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		
		map = new HashMap<String, Object>();
		map.put("title", "常用地址");
		map.put("info", "google 3");
		
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "我的收藏");
		map.put("info", "google 2");
		
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "我的红包");
		map.put("info", "google 3");
		
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "我的动态");
		map.put("info", "google 3");
		
		list.add(map);
		
		
		return list;
	}
	
	private OnClickListener gotolog = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得事件的按钮设置为粉色
			Intent intent=new Intent(MeActivity.this,LogActivity.class);
			startActivity(intent);

		}

	};
	private OnClickListener testnet = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			String url= "user/register";
			RequestParams params = new RequestParams();
			params.put("username","18011904");
			params.put("paseword","202cb962ac59075b964b07152d234b70");
			params.put("validation","54322");
			RestClient.getInstance().post(MeActivity.this, url, params, 
					new AsyncHttpResponseHandler(){
						@Override
						public void onFailure(Throwable error) {
							Toast.makeText(MeActivity.this, "数据提交失败", Toast.LENGTH_LONG).show();
						}
						
						@Override
						public void onSuccess(int statusCode, Header[] headers, String content) {
							onSuccess(statusCode, content);
							Toast.makeText(MeActivity.this, "数据提交成功", Toast.LENGTH_LONG).show();
						
						}
				
			});
			

		}
			
	};
	

	@SuppressLint("NewApi")
	private void setFragment() {
		fragmentManager = getFragmentManager();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (meFragment == null) {
			// 如果MessageFragment为空，则创建一个并添加到界面上
			meFragment = new MeFragment();
			transaction.add(R.id.content, meFragment);
		} else {
			transaction.show(meFragment);
		}
		transaction.commit();
	}

	@SuppressLint("NewApi")
	private void setlogFragment() {
		fragmentManager = getFragmentManager();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		meFragment = new MeFragment();

		transaction.replace(R.id.content, meFragment);

		transaction.commit();
	}

	@SuppressLint("NewApi")
	private void setregFragment() {
		fragmentManager = getFragmentManager();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		meRegFragment = new MeRegFragment();

		transaction.replace(R.id.content, meRegFragment);

		transaction.commit();
	}
}
