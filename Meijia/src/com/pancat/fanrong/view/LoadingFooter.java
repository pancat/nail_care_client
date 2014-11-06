package com.pancat.fanrong.view;

import com.pancat.fanrong.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;


public class LoadingFooter {

	protected View mLoadingFooter;
	
	protected State mState = State.Idle;
	
	public static enum State{
		Idle,TheEnd,Loading
	}
	
	public LoadingFooter(Context context){
		mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.loading_footer, null);
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
			break;
		case TheEnd:
			break;
		default:
			mLoadingFooter.setVisibility(View.GONE);
		}
	}
	
	
	public interface OnLoadListener{
		public void onLoad();
	};
	
}
