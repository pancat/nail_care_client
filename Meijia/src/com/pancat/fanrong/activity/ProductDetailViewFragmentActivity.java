package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.customview.FreeTimeTableView;
import com.pancat.fanrong.fragment.ProductAuthorFragment;
import com.pancat.fanrong.fragment.ProductDetailFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
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
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ProductDetailViewFragmentActivity extends ActionBarActivity {
    private static final String TAG = "ProductDetailViewFragmentActivity";
    
    private static final String DETAILTAG = "detail";
    private static final String AUTHORTAG = "author";
    
    private FragmentManager fragmentManager = null;

    private ProductDetailFragment productDetailFragment = null;
    private ProductAuthorFragment productAuthorFragment = null;
    private FragmentTransaction fragmentTransaction = null;
    private PopupWindow freeTimePopWindow = null;
    private FreeTimeTableView timeView = null;
    
    private ActionBar actionBar = null;
    
    private Product product = null;
    private TextView orderButton = null;
    
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO 自动生成的方法存根
		super.onCreate(bundle);
		setContentView(R.layout.product_detail_view);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		product = (Product)b.getSerializable(Product.KEY);
		
		Log.d(TAG, product.toString());
		if(fragmentTransaction == null ){
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
		}
		
		//repair bug
        if(fragmentManager.findFragmentByTag(DETAILTAG)!=null)
        	fragmentTransaction.remove(fragmentManager.findFragmentByTag(DETAILTAG));
	    if(fragmentManager.findFragmentByTag(AUTHORTAG) != null)
	    	fragmentTransaction.remove(fragmentManager.findFragmentByTag(AUTHORTAG));
		
		productDetailFragment = ProductDetailFragment.newInstance(product);
		fragmentTransaction.add(R.id.product_detail_view_fragment, productDetailFragment,DETAILTAG);
		
		productAuthorFragment = ProductAuthorFragment.newInstance(product);
		fragmentTransaction.add(R.id.product_detail_view_fragment, productAuthorFragment,AUTHORTAG);
		
		//fragmentTransaction.addToBackStack(TAG);
		fragmentTransaction.commit();
		
		InitAndShowOrderButton();
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
			showWindow();
			//(new TestFragment()).show(getSupportFragmentManager(), TAG);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    
	public void showWindow(){
		ArrayList<Map<Integer,Integer>>m = new ArrayList<Map<Integer,Integer>>();
		Map<Integer,Integer>ma = new HashMap<Integer,Integer>();
		for(int i=9;i<18;i++)ma.put(i, FreeTimeTableView.IDLE);
		m.add(ma);
	
		if(freeTimePopWindow == null){
			timeView = (new FreeTimeTableView(this).setData(m));
			freeTimePopWindow = new PopupWindow(timeView);
			freeTimePopWindow.setFocusable(true);
			freeTimePopWindow.setOutsideTouchable(true);
			freeTimePopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			freeTimePopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
			freeTimePopWindow.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.blue)));
		}
		freeTimePopWindow.update();
		if(freeTimePopWindow.isShowing()) dismissWindow();
		else {
			freeTimePopWindow.showAtLocation(findViewById(R.id.product_detail_view_fragment), Gravity.BOTTOM|Gravity.LEFT, 0, 0);
		}
	}
	
	public void dismissWindow(){
		if(freeTimePopWindow != null){
			freeTimePopWindow.dismiss();
		}
	}
	
	public void InitAndShowOrderButton(){
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		LinearLayout layout = (LinearLayout)layoutInflater.inflate(R.layout.product_order_button, null);
		orderButton = (TextView)layout.findViewById(R.id.product_order_button);
		
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		
		lp.bottomMargin = 0;
		lp.gravity = Gravity.BOTTOM |Gravity.CENTER;
		
		layout.removeView(orderButton);
		this.addContentView(orderButton, lp);
		
		final Context context = this;
		
		//预约按钮监听事件
		orderButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View arg0) {
				//TODO 这里需要判断是否登录 @empark
				// Class.getInstance().isLogin()
				// do something and if not login ,return ;
				Intent intent = new Intent(context,OrderAuthorActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Product.KEY, product);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
}
