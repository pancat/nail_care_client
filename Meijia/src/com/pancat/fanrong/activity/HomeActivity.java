package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.adapter.HomeProductListAdapter;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.fragment.HomeFragment;

@SuppressLint("ResourceAsColor")
public class HomeActivity extends Activity {

	private HomeFragment homeFragment;
	private FragmentManager fragmentManager;
	protected ScrollView scrollView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		scrollView = (ScrollView) findViewById(R.id.home_scroll_view);
		// setFragment();
		setListView();
		
		setScrollViewListener();
	}
	protected void onResume() {
		super.onResume();
	}

	private void setScrollViewListener() {
		scrollView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.getScrollY() >= scrollView.getHeight())
				{
					Log.i("ScrollView", "滑动到底");
				}
				else if (v.getScrollY() < 0)
				{
					Log.i("ScrollView", "滑动到顶");
				}
				return false;
			}
		});
	}

	private void setListView() {
		// 绑定Layout里面的ListView
		ListView list = (ListView) findViewById(R.id.list_view);
		list.setBackgroundColor(getResources().getColor(R.color.grey));

		list.post(new Runnable() {
			@Override
			public void run() {
				scrollView.scrollTo(0, 0);
			}

		});
		// 生成动态数组，加入数据
		List<Object> listItem = new ArrayList<Object>();
		for (int i = 0; i < 1; i++) {
			listItem.add(new Product(null));
		}

		// 添加并且显示
		list.setAdapter(new HomeProductListAdapter(this, list, listItem));
	}


}
