package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.List;

import com.pancat.fanrong.R;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class ProductTabFragment extends Fragment
{
	private static final String TAG = "ProductTabFragment";
	
	private OnProductTabClickListenser onProductTabClickListenter = null;
	private List<String> tab = null;
	private Spinner spinner = null;
	private View currentView = null;
	
	public interface OnProductTabClickListenser
	{
		public void setOnProductTabClickListenser(String tab);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		
		currentView = inflater.inflate(R.layout.product_tabpage, container,false);
		return currentView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "yes enter");
		if(currentView != null)
		{
			spinner = (Spinner)currentView.findViewById(R.id.product_filter_tab);
		    List<String> temp = new ArrayList<String>();
		    temp.add("abc");
		    temp.add("cde");
			spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, temp));
		    Log.d(TAG,"yes set");
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		try
		{
			onProductTabClickListenter = (OnProductTabClickListenser)activity;
		}catch(Exception e)
		{
			
		}
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		
		tab = new ArrayList<String>();
		tab.add("Hot");
		tab.add("New");
	}
	
	
}
