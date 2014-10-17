package com.pancat.fanrong.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.adapter.MoreTypeViewForGVAdapter;
import com.pancat.fanrong.fragment.FreeTimeTableFragment;
import com.pancat.fanrong.util.LocalDateUtils;
import com.pancat.fanrong.viewpagerindicator.TabPageIndicator;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class FreeTimeTableView extends LinearLayout {
	public static final String TAG = "FreeTimeTableView";
	
	public static final int SHOWDAYS = 7;
	private static final String BUSY = "忙";
	private static final String IDLE = "闲";
	private static final String TODAY = "今天";
	private static final String TOMORRORM = "明天";
	private static final String NEXTT = "后天";
	
	private String[] dateOfWeek = new String[SHOWDAYS];
	private ViewPagerFixWrapContent viewpage = null;
	private TabPageIndicator indicator = null;
	private List<Map<Integer,Integer>> datas = null;
	private List<Fragment> fragments = null;
	private boolean mDataChanged = true;
	private FragmentViewPageAdapter adapter = null;
	private FragmentManager fm = null;
	private LinearLayout layout = null;
	
	public FreeTimeTableView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		datas = new ArrayList<Map<Integer,Integer>>();
		fragments = new ArrayList<Fragment>();
		
		LayoutInflater inflater = LayoutInflater.from(context);
		layout = (LinearLayout)inflater.inflate(R.layout.tab_freetime_table_activity, this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(lp);
		
		//初始化组件
		viewpage = (ViewPagerFixWrapContent)layout.findViewById(R.id.tab_freetime_table_viewpager);
		indicator = (TabPageIndicator)layout.findViewById(R.id.tab_freetime_table_indicator);
		
		try{
			fm = ((FragmentActivity)context).getSupportFragmentManager();
		}catch(Exception e){
			Log.d(TAG,"Activity is subclass of FragmentActivity");
			e.printStackTrace();
		}
	}
	
	//设置数据
	public FreeTimeTableView setDatas(Map<Integer,Map<Integer,Integer>> map){
		makeStringArr(map);
		mDataChanged = true;
		return getView();
	}
	
	public FreeTimeTableView setFragmentManager(FragmentManager fm){
		this.fm = fm;
		return this;
	}
	public FreeTimeTableView getView() throws IllegalStateException{
		makeAllFragment();
		
		adapter = new FragmentViewPageAdapter(fm);
		adapter.setListener(viewpage);
		viewpage.setAdapter(adapter);
		indicator.setViewPager(viewpage);
		
		Log.d(TAG, "child"+viewpage.getChildCount());
		return this;
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d(TAG,"viewpage: "+viewpage.getMeasuredHeight()+"h:"+viewpage.getHeight());
		Log.d(TAG,"viewpage: "+indicator.getMeasuredHeight()+"h:"+indicator.getHeight());
	}

	
	@Override
	protected void measureChildWithMargins(View child,
			int parentWidthMeasureSpec, int widthUsed,
			int parentHeightMeasureSpec, int heightUsed) {
		// TODO Auto-generated method stub
		MarginLayoutParams mp = (MarginLayoutParams)child.getLayoutParams();
		
		int wSpec = definedMesure(parentHeightMeasureSpec,mp.topMargin+mp.bottomMargin , mp.height);
		child.measure(parentWidthMeasureSpec, wSpec);
	}

	private int definedMesure(int parentSpec,int padding,int childDim){
		int mode = MeasureSpec.getMode(parentSpec);
		int size = MeasureSpec.getSize(parentSpec);
		
		int maxsize = Math.max(size, size-padding);
		int resSize = 0;
		int resMode = 0;
		
		switch (mode) {
		case MeasureSpec.AT_MOST:
			if(childDim >= 0){
				resSize = childDim;
				resMode = MeasureSpec.EXACTLY;
			}else if(childDim == LayoutParams.MATCH_PARENT){
				resSize = size;
				resMode = MeasureSpec.AT_MOST;
			}else if(childDim == LayoutParams.WRAP_CONTENT){
				resSize = size;
				resMode = MeasureSpec.AT_MOST;
			}
			break;
		case MeasureSpec.EXACTLY:
				if(childDim >= 0){
					resSize = childDim;
					resMode = MeasureSpec.EXACTLY;
				}else if(childDim == LayoutParams.MATCH_PARENT){
					resSize = size;
					resMode = MeasureSpec.EXACTLY;
				}else if(childDim == LayoutParams.WRAP_CONTENT){
					resSize = size;
					resMode = MeasureSpec.AT_MOST;
				}
				break;
		case MeasureSpec.UNSPECIFIED:
			 if(childDim >= 0){
				 resSize = childDim;
				 resMode = MeasureSpec.EXACTLY;
			 }else if(childDim == LayoutParams.MATCH_PARENT){
				 resSize = 0;
				 resMode = MeasureSpec.UNSPECIFIED;
			 }else if(childDim == LayoutParams.WRAP_CONTENT){
				 resSize = 0;
				 resMode = MeasureSpec.UNSPECIFIED;
			 }
		default:
			break;
		}
		Log.d(TAG, resSize+"  "+resMode);
		return MeasureSpec.makeMeasureSpec(resSize, resMode);
	}
	public void makeAll(){
		if(datas.size() == 0){
			datas.add(null);
		}
		makeStringArr(null);
	}
	
	public void makeAllFragment(){
		if(!mDataChanged) return;
		
		if(datas.size() == 0)
			makeAll();
		
		if(fragments == null) fragments = new ArrayList<Fragment>();
		else fragments.clear();
		
		for(int i=0;i<SHOWDAYS; i++){
			fragments.add(FreeTimeTableFragment.newInstance(datas.get(i)));
		}
		mDataChanged = false;
	}
	
	public int getH(){
		return viewpage.getH();
	}
	public String getCurrentPageClickTime(){
		int curpage = indicator.getCurrentPage();
		Log.d(TAG, "curpage:"+curpage);
		
		String res = "";
		if(curpage != -1){
			res += LocalDateUtils.getDate(curpage);
		}
		return res;
	}

	private void makeStringArr(Map<Integer,Map<Integer,Integer>> map){
		if(datas == null){
			datas = new ArrayList<Map<Integer,Integer>>();
		}else{
			datas.clear();
		}
		
		for(int i=0;i<SHOWDAYS;i++){
			if(i == 0){
				int h = LocalDateUtils.getHour();
				
				//今天当前时间以前的小时已无作用
				Map<Integer,Integer> m = ((map != null) && map.containsKey(0))?map.get(0):new HashMap<Integer,Integer>();
				for(int j=FreeTimeTableFragment.START_TIME; j <= h+1 && j<= FreeTimeTableFragment.END_TIME; j++)
					m.put(j, MoreTypeViewForGVAdapter.BUSY);
				datas.add(m);
				continue;
			}
			if(map == null){
				datas.add(null);
				continue;
			}
			
			if(map.containsKey(i)){
				datas.add(map.get(i));
			}
			else{
				datas.add(null);
			}
		}
		
		//获取标签字符串
		for(int i=0;i<SHOWDAYS; i++){
			dateOfWeek[i] = getNthDay(i);
		}
	}
	
	private boolean isBusy(int pos){
		Map<Integer,Integer> m = datas.get(pos);
		if(m == null) return true;
		Iterator<Integer> iter = m.keySet().iterator();
		int idle = 0;
		while(iter.hasNext()){
			int v = iter.next();
			if(v == MoreTypeViewForGVAdapter.IDEL) idle++;
		}
		int total = FreeTimeTableFragment.END_TIME - FreeTimeTableFragment.START_TIME +1;
		if(idle >= total/3) return false;
		
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
	
	//一个显示每一页时间表的适配器
	public class FragmentViewPageAdapter extends FragmentPagerAdapter {
		private AdapterFinishUpdateCallbacks listener = null;
		
        public FragmentViewPageAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}
        
        public void setListener(ViewPager viewpager){
        	listener = (AdapterFinishUpdateCallbacks)viewpager;
        }
		@Override
        public Fragment getItem(int position) {
            return fragments.get(position);
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

		@Override
		public void finishUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			super.finishUpdate(container);
			if(this.listener!= null){
				this.listener.onFinishUpdate();
			}
		}
	
    }
	public interface AdapterFinishUpdateCallbacks
	{
	    void onFinishUpdate();
	}
}
