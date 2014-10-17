package com.pancat.fanrong.common;

import com.pancat.fanrong.common.FreeTimeTableView.AdapterFinishUpdateCallbacks;
import com.pancat.fanrong.fragment.FreeTimeTableFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerFixWrapContent extends ViewPager implements AdapterFinishUpdateCallbacks {
    private boolean isSettingHeight = false;
    private int heights = 0;
	public ViewPagerFixWrapContent(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ViewPagerFixWrapContent(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
 
	 public void setVariableHeight()
	 {
	     // super.measure() calls finishUpdate() in adapter, so need this to stop infinite loop
	     if (!this.isSettingHeight)
	     {
	         this.isSettingHeight = true;

	         int maxChildHeight = 0;
	         int widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
	         
	         for (int i = 0; i < getChildCount(); i++)
	         {
	             View child = getChildAt(i);
	             child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, MeasureSpec.UNSPECIFIED));
	             maxChildHeight = child.getMeasuredHeight() > maxChildHeight ? child.getMeasuredHeight() : maxChildHeight;
	         }

	         int height = maxChildHeight + getPaddingTop() + getPaddingBottom();
	         int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height*3+2, MeasureSpec.EXACTLY);
	         heights = height*3+2;
	        // Log.d("GONG", ""+getChildCount()+"h:"+height);
	         super.measure(widthMeasureSpec, heightMeasureSpec);
	         requestLayout();
              
	         this.isSettingHeight = false;
	     }
	 }
	 
	@Override
	public void onFinishUpdate() {
		// TODO Auto-generated method stub
		setVariableHeight();
	}
	public int getH()
	{
		if(heights >0 ) return heights;
		else return 350;
	}
}
