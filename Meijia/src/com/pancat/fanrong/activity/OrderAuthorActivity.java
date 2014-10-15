package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.adapter.MoreTypeViewForGVAdapter;
import com.pancat.fanrong.fragment.FreeTimeTableFragment;
import com.pancat.fanrong.fragment.FreeTimeTableFragment.onItemClickToActivity;
import com.pancat.fanrong.util.LocalDateUtils;
import com.pancat.fanrong.viewpagerindicator.IconPagerAdapter;
import com.pancat.fanrong.viewpagerindicator.LinePageIndicator;
import com.pancat.fanrong.viewpagerindicator.TabPageIndicator;
import com.pancat.fanrong.viewpagerindicator.TitlePageIndicator;
import com.pancat.fanrong.viewpagerindicator.UnderlinePageIndicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class OrderAuthorActivity extends FragmentActivity  implements onItemClickToActivity{
    private static final String TAG = "OrderAuthorActivity";
	//关于时间表一些常量设置
	private static final int SHOWDAY = 7;
	private static final String BUSY = "忙";
	private static final String IDLE = "闲";
	private static final String TODAY = "今天";
	private static final String TOMORRORM = "明天";
	private static final String NEXTT = "后天";
	
	private EditText mTime;
	private EditText mPosition;
	private EditText mDetail;
	
	//时间弹出窗口
	private ViewPager viewpage = null;
	private TitlePageIndicator indicator = null;
	private View freeTimeTableLayout = null;
	
	//时间表标题
	private String[] dateOfWeek = new String[SHOWDAY];
	private ArrayList<Map<Integer,Integer>> arMap = null;
	
	private List<Fragment> list = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_author_activity);
		
		Map<Integer,Map<Integer,Integer>>m = new HashMap<Integer,Map<Integer,Integer>>();
		Map<Integer,Integer>ma = new HashMap<Integer,Integer>();
		ma.put(15, MoreTypeViewForGVAdapter.IDEL);
		ma.put(18, MoreTypeViewForGVAdapter.IDEL);
		m.put(1, ma);
		makeStringArr(m);
		setFragmentList();
		initView();
	}
	
	//设置时间表中的框架
	private void setFragmentList()
	{
		if(list == null) list = new ArrayList<Fragment>();
		else list.clear();
		
		for(int i=0;i<SHOWDAY; i++){
        	list.add(FreeTimeTableFragment.newInstance(arMap.get(i)));
		}
	}
	
	
	//获取编辑取，并添加响应事件
	private void initView(){
		mTime = (EditText)findViewById(R.id.order_author_activity_time);
		mPosition = (EditText)findViewById(R.id.order_author_activity_position);
		mDetail = (EditText)findViewById(R.id.order_author_activity_detail);
		hideSoftKeyBoard(mTime);
		
		setListenEvent();
	}
	
	//添加响应事件
	private void setListenEvent()
	{   
		//设置时间组件的监听事件
		mTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				hideSoftKeyBoard(view);
				showWindow(view);
			}
		});
		mTime.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean arg1) {
				// TODO Auto-generated method stub
				hideSoftKeyBoard(view);
			}
		});
		//通过实现一个地理位置 跳转到可搜索页面
		mPosition.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//详细地址点击
		mDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void showWindow(View view){
		//获取点击时间控件时出现的子视图并设置
		if(freeTimeTableLayout == null){
			LayoutInflater inflater = LayoutInflater.from(this);
			freeTimeTableLayout = inflater.inflate(R.layout.tab_freetime_table_activity, null);
			viewpage = (ViewPager)freeTimeTableLayout.findViewById(R.id.gong);
			indicator = (TitlePageIndicator)freeTimeTableLayout.findViewById(R.id.indicator);
			FragmentPagerAdapter adapter = new FragmentViewPageAdapter(getSupportFragmentManager());
			viewpage.setAdapter(adapter);
			indicator.setViewPager(viewpage);
		 
	        /*indicator.setSelectedColor(0xFFCC0000);
	        indicator.setBackgroundColor(0xFFCCCCCC);
	        indicator.setFadeDelay(1000);
	        indicator.setFadeLength(1000);*/
	        
		//设置视图显示的位置
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			params.topMargin = 100;
			params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
			params.topMargin = view.getTop()+view.getHeight()+view.getPaddingTop();
			addContentView(freeTimeTableLayout, params);
		}
	}
	
	public void dismissWindow(){
		if(freeTimeTableLayout != null){
			((ViewGroup)freeTimeTableLayout.getParent()).removeView(freeTimeTableLayout);
			freeTimeTableLayout = null;
		}
	}
	//隐藏软件键盘
	private void hideSoftKeyBoard(View view)
	{
        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	class FragmentViewPageAdapter extends FragmentPagerAdapter {
		
        public FragmentViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return dateOfWeek[position];
        }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dateOfWeek.length;
		}
    }
	
	private void makeStringArr(Map<Integer,Map<Integer,Integer>> map){
		if(arMap == null){
			arMap = new ArrayList<Map<Integer,Integer>>();
		}else{
			arMap.clear();
		}
		
		for(int i=0;i<SHOWDAY;i++){
			if(i == 0){
				int h = LocalDateUtils.getHour();
				Log.d(TAG,String.valueOf(h));
				
				//今天当前时间以前的小时已无作用
				Map<Integer,Integer> m = ((map != null) && map.containsKey(0))?map.get(0):new HashMap<Integer,Integer>();
				for(int j=FreeTimeTableFragment.START_TIME; j <= h; j++)
					m.put(j, MoreTypeViewForGVAdapter.BUSY);
				arMap.add(m);
				continue;
			}
			if(map == null){
				arMap.add(null);
				continue;
			}
			
			if(map.containsKey(i)){
				arMap.add(map.get(i));
			}
			else{
				arMap.add(null);
			}
		}
		
		//获取标签字符串
		for(int i=0;i<SHOWDAY; i++){
			dateOfWeek[i] = getNthDay(i);
		}
	}
	
	private boolean isBusy(int pos){
		Map<Integer,Integer> m = arMap.get(pos);
		if(m == null) return true;
		Iterator<Integer> iter = m.keySet().iterator();
		int idle = 0;
		while(iter.hasNext()){
			int v = iter.next();
			if(v == MoreTypeViewForGVAdapter.IDEL) idle++;
		}
		int total = FreeTimeTableFragment.END_TIME - FreeTimeTableFragment.START_TIME +1;
		if(idle >= total/2) return false;
		
		return true;
	}
	private String getNthDay(int pos){
		boolean busy = isBusy(pos);
		String str = "(" + (busy?BUSY:IDLE ) +")";
		
		switch (pos) {
		case 0:
			 return TODAY+str;
		case 1: 
			 return TOMORRORM+str;
		case 2: 
			 return NEXTT+str;
		default:
			return LocalDateUtils.getMD(pos)+str;
		}
	}

	
	//监听页面点击等事件
	@Override
	public void setonItemClickToActivity(int time) {
		// TODO Auto-generated method stub
		int curpage = indicator.getCurrentPage();
		String res = "";
		if(curpage != -1){
			res += LocalDateUtils.getDate(curpage);
		}
		res +=" "+time+":00";
		if(mTime != null){
			mTime.setText(res);
			dismissWindow();
		}
	}
	
}
