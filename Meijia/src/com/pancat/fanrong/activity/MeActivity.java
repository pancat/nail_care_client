package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pancat.fanrong.R;


public class MeActivity extends Activity {

	
	private Button btn3;
	private ListView listview;
	int colorpink = Color.parseColor("#FA8072");
	int colordefault = android.graphics.Color.WHITE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me);
		
		/**
		 * 检测用户是否登录
		 */
	
		btn3=(Button)findViewById(R.id.btn3);
		btn3.setOnClickListener(gotologin);
		
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
	
	private OnClickListener gotologin = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得事件的按钮设置为粉色
			Intent intent=new Intent(MeActivity.this,LogActivity.class);
			startActivity(intent);

		}

	};
	
			
}
