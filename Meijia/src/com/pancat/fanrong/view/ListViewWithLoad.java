package com.pancat.fanrong.view;

import com.pancat.fanrong.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ListViewWithLoad extends ListView implements OnScrollListener{

	public static final int LOAD = 1;
	//是否可以下拉加载
	private boolean loadEnable = true;
	
	private int pageSize = 5;
	
	private OnLoadListener onLoadListener;
	
	private View footerView;
	
	private LayoutInflater inflater;
	
	private ProgressBar loadingBar;
	//是否正在加载
	private boolean isLoading;
	//是否全部加载
	private boolean isLoadFull;
	
	private int firstVisibleItem;
	
	private int scrollState;
	
	public ListViewWithLoad(Context context) {
		super(context);
		initView(context);
	}
	
	public ListViewWithLoad(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public ListViewWithLoad(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
    
	/**
	 * 初始化组件
	 * @param context
	 */
	private void initView(Context context) {
		inflater = LayoutInflater.from(context);
		footerView = inflater.inflate(R.layout.listview_footer, null);
		this.addFooterView(footerView);
		this.setOnScrollListener(this);
	}

	//监听加载更多
	public void setOnLoadListener(OnLoadListener onLoadListener){
		this.loadEnable = true;
		this.onLoadListener = onLoadListener;
	}
	
	/**
	 * 开启或者关闭加载更多
	 * @param loadEnable
	 */
	public void setLoadEnable(boolean loadEnable){
		this.loadEnable = loadEnable;
		this.removeFooterView(footerView);
	}
	
	public boolean isLoadEnable(){
		return loadEnable;
	}
	
	public int getPageSize(){
		return pageSize;
	}
	
	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}
	
	public void onLoad(){
		if(onLoadListener != null){
			onLoadListener.onLoad();
		}
	}
	
	public void onLoadComplete(){
		isLoading = false;
	}
	
	public interface OnLoadListener{
		public void onLoad();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
		needLoad(view,scrollState);
	}
	
	private void needLoad(AbsListView view,int scrollState){
		if(!loadEnable){
			return;
		}
		try{
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&&!isLoading&&view.getLastVisiblePosition() == view.getPositionForView(footerView)
				&&!isLoadFull){
			onLoad();
			isLoading = true;
		}
		}catch (Exception e) {
		}
	}
	
}
