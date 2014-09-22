package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.List;

import com.pancat.fanrong.R;
import com.pancat.fanrong.R.layout;

import android.app.ListActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MomentActivity extends ListActivity implements OnScrollListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moment);
		
		List<String> items = getItemList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
		setListAdapter(adapter);
		
		super.getListView().setOnScrollListener(this);
	}
	
	protected List<String> getItemList()
	{
		List<String> list = new ArrayList<String>();
		for (int i = 1; i < 100; i ++)
			list.add("abc");
		return list;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Log.e("log","" + position + ", " + id);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
		Log.e("scroll", "" + firstVisibleItem + ", " + visibleItemCount + ", " + totalItemCount);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
		Log.e("scroll", "" + scrollState);
	}
}
