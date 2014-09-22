package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.List;

import com.pancat.fanrong.R;
import com.pancat.fanrong.R.layout;

import android.app.ListActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;

public class MomentActivity extends ListActivity implements OnScrollListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moment);
		
		List<String> items = getItemList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
		setListAdapter(adapter);
	}
	
	protected List<String> getItemList()
	{
		List<String> list = new ArrayList<String>();
		for (int i = 1; i < 100; i ++)
			list.add("abc");
		return list;
	}
	

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
