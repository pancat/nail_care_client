package com.pancat.fanrong.view;

import com.pancat.fanrong.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;


public class LoadingFooter {

	protected View mLoadingFooter;
	
	protected State mState = State.Idle;
	
	private TextView noData;
	private TextView loadFull;
	private TextView more;
	private ProgressBar loading;
	
	public static enum State{
		Idle,TheEnd,Loading
	}
	
	public LoadingFooter(Context context){
		mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.loading_footer, null);
		loadFull = (TextView) mLoadingFooter.findViewById(R.id.loadFull);
		noData = (TextView) mLoadingFooter.findViewById(R.id.noData);
		more = (TextView) mLoadingFooter.findViewById(R.id.more);
		loading = (ProgressBar) mLoadingFooter.findViewById(R.id.loading);
		mLoadingFooter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		setState(State.Idle);
	}
	
	public View getView(){
		return mLoadingFooter;
	}
	
	public State getState(){
		return mState;
	}
	
	public void setState(final State state,long delay){
		mLoadingFooter.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				setState(state);
			};
		}, delay);
	}
	
	
	public void setState(State state){
		if(mState == state){
			return;
		}
		mState = state;
		mLoadingFooter.setVisibility(View.VISIBLE);
		switch(state){
		case Loading:
			loadFull.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			more.setVisibility(View.VISIBLE);
			noData.setVisibility(View.GONE);
			break;
		case TheEnd:
			loadFull.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.GONE);
			break;
		default:
			mLoadingFooter.setVisibility(View.GONE);
		}
	}
	
	
	public interface OnLoadListener{
		public void onLoad();
	};
	
}
