package com.pancat.fanrong.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * TODO: document your custom view class.
 */
public class ListViewInScrollView extends ListView {

	public ListViewInScrollView(Context context) {
		super(context);
	}

	public ListViewInScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ListViewInScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        		MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
