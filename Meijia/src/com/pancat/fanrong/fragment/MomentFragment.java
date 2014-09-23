package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.List;

import com.pancat.fanrong.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class MomentFragment extends ListFragment implements OnScrollListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_moment, container, false);
		return view;
	}
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	List<String> items = getItemList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, items);
		setListAdapter(adapter);
    }
	
	@Override
	public void onStart()
	{
		super.onStart();
		getListView().setOnScrollListener(this);
	}
	
	protected List<String> getItemList()
	{
		List<String> list = new ArrayList<String>();
		for (int i = 1; i < 100; i ++)
			list.add("abc");
		return list;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
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
