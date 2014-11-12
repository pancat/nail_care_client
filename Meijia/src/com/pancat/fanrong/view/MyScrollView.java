package com.pancat.fanrong.view;

import com.pancat.fanrong.activity.OrderAuthorActivity;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView
{
	public MyScrollView(Context context) {
		super(context);
	}
	public MyScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);	
	}
	
	public MyScrollView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
	}

	
	
	private Boolean mIfOnMap = false;//是否点击到地图了

	@Override 
	public boolean onInterceptTouchEvent(MotionEvent ev) 
	{ 
		Log.v("test", "onInterceptTouchEvent:"+ev.getAction()+"mIfOnMap:"+mIfOnMap);
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			Rect rect = new Rect();
			if (OrderAuthorActivity.mMapView != null) {
				Log.v("test", "not null");
				OrderAuthorActivity.mMapView.getDrawingRect(rect);
			}
			mIfOnMap = !rect.contains(x, y);
			Log.i("touch map?", mIfOnMap+"" );
			if (mIfOnMap) {
				return false;
			}else {
					return super.onInterceptTouchEvent(ev);
			}
	}
		

}
