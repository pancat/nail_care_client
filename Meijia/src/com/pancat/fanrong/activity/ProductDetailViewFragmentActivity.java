package com.pancat.fanrong.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.adapter.MoreTypeViewForGVAdapter;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.common.FreeTimeTableView;
import com.pancat.fanrong.fragment.FreeTimeTableFragment;
import com.pancat.fanrong.fragment.ProductAuthorFragment;
import com.pancat.fanrong.fragment.ProductDetailFragment;
import com.pancat.fanrong.fragment.TestFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class ProductDetailViewFragmentActivity extends ActionBarActivity {
    private static final String TAG = "ProductDetailViewFragmentActivity";
    
    private static final String DETAILTAG = "detail";
    private static final String AUTHORTAG = "author";
    
    private FragmentManager fragmentManager = null;

    private ProductDetailFragment productDetailFragment = null;
    private ProductAuthorFragment productAuthorFragment = null;
    private FragmentTransaction fragmentTransaction = null;
    FreeTimeTableView timeView = null;
    private ActionBar actionBar = null;
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO 自动生成的方法存根
		super.onCreate(bundle);
		setContentView(R.layout.product_detail_view);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		
		if(fragmentTransaction == null ){
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
		}
		
		//repair bug
        if(fragmentManager.findFragmentByTag(DETAILTAG)!=null)
        	fragmentTransaction.remove(fragmentManager.findFragmentByTag(DETAILTAG));
	    if(fragmentManager.findFragmentByTag(AUTHORTAG) != null)
	    	fragmentTransaction.remove(fragmentManager.findFragmentByTag(AUTHORTAG));
		
        
		productDetailFragment = ProductDetailFragment.newInstance(b.getString(Product.KEY));
		fragmentTransaction.add(R.id.product_detail_view_fragment, productDetailFragment,DETAILTAG);
		
		productAuthorFragment = ProductAuthorFragment.newInstance(b.getString(Product.KEY));
		fragmentTransaction.add(R.id.product_detail_view_fragment, productAuthorFragment,AUTHORTAG);
		
		//fragmentTransaction.addToBackStack(TAG);
		fragmentTransaction.commit();
		
		//点击屏幕中其它地方，取消timeView窗口
		LayoutInflater.from(this).inflate(R.layout.product_detail_view, null).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismissWindow();
			}
		});
		Log.d(TAG, b.getString(Product.KEY));
	}
	
	//初始化ActionBar
	public void InitActionBar()
	{  
		actionBar =  getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.timetable, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.timetable:
			//showWindow();
			(new TestFragment()).show(getSupportFragmentManager(), TAG);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    
	public void showWindow(){
		Map<Integer,Map<Integer,Integer>>m = new HashMap<Integer,Map<Integer,Integer>>();
		Map<Integer,Integer>ma = new HashMap<Integer,Integer>();
		for(int i=9;i<18;i++)ma.put(i, MoreTypeViewForGVAdapter.IDEL);
		m.put(1, ma);
	
		if(timeView == null){
			timeView = (new FreeTimeTableView(this)).setDatas(m);
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			
		//	timeView.setVisibility(View.GONE);
			
			Log.d(TAG, ""+"pageH:"+dm.heightPixels+"h:"+timeView.getHeight()+"reH:"+timeView.getMeasuredHeight());
			//params.topMargin = dm.heightPixels /2;
			//params.topMargin = dm.heightPixels - timeView.getH();
			params.gravity = Gravity.BOTTOM;
			params.bottomMargin = 0;
			
			ViewGroup.LayoutParams lp = timeView.getLayoutParams();
			
			addContentView(timeView, params);
		}
		else{
			if(timeView.getVisibility() == View.VISIBLE)
				timeView.setVisibility(View.GONE);
			else timeView.setVisibility(View.VISIBLE);
		}
			
	}
	
	public void dismissWindow(){
		if(timeView != null){
			//((ViewGroup)timeView.getParent()).removeView(timeView);
			timeView.setVisibility(View.GONE);
		}
	}
}
