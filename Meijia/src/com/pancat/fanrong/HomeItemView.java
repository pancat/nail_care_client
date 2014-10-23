package com.pancat.fanrong;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class HomeItemView extends FrameLayout {
	
	public HomeItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.home_item_view, this);
		
		TextView title = (TextView)findViewById(R.id.title);
		title.setText("这是一个不错的产品");
    }
}
