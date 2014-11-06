package com.pancat.fanrong.view;

import com.pancat.fanrong.grid.StaggeredGridView;
import com.pancat.fanrong.view.LoadingFooter.OnLoadListener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;


public class PageStaggeredGridView extends StaggeredGridView implements OnScrollListener{

	
	private LoadingFooter mLoadingFooter;
	
	private OnLoadListener mOnLoadListener;
	
	public PageStaggeredGridView(Context context) {
		super(context);
		init();
	}
	
	public PageStaggeredGridView(Context context,AttributeSet attrs){
		super(context,attrs);
		init();
	}
	
	public PageStaggeredGridView(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		init();
	}
	
	private void init(){
		mLoadingFooter = new LoadingFooter(getContext());
		addFooterView(mLoadingFooter.getView());
		setOnScrollListener(this);
	}
	
	public void setLoadListener(OnLoadListener listener){
		mOnLoadListener = listener;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(mLoadingFooter.getState() == LoadingFooter.State.Loading
				||mLoadingFooter.getState() == LoadingFooter.State.TheEnd){
			return;
		}
		if(firstVisibleItem + visibleItemCount >= totalItemCount
				&&totalItemCount!=0
				&&totalItemCount!=getHeaderViewsCount()+getFooterViewsCount()
				&&mOnLoadListener!=null){
			mLoadingFooter.setState(LoadingFooter.State.Loading);
			mOnLoadListener.onLoad();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	public void setState(LoadingFooter.State state){
		mLoadingFooter.setState(state);
	}
	
	public void setState(LoadingFooter.State state,long delay){
		mLoadingFooter.setState(state, delay);
	}
}
