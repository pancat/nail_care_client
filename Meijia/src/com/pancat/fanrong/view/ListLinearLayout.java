package com.pancat.fanrong.view;

import com.pancat.fanrong.adapter.GuessLikeAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class ListLinearLayout extends LinearLayout{

	private GuessLikeAdapter adapter;
	
	public ListLinearLayout(Context context) {
		super(context);
	}

	public ListLinearLayout(Context context,AttributeSet attrs) {
		super(context,attrs);
	}
	
	public void bindLinearLayout(){
		int count = adapter.getCount();
		for(int i = 0;i < count ; i++){
			View view = adapter.getView(i, null, null);
			addView(view);
		}
	}
	
	
	public void setAdapter(GuessLikeAdapter adapter){
		this.adapter = adapter;
	}
}
