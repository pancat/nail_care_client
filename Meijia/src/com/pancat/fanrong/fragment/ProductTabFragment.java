package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.common.FilterQueryAndParse;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ProductTabFragment extends Fragment
{
	private static final String TAG = "ProductTabFragment";
	
	private static final int LeftAndRightBottomBlank = 20;
	
	//按Tab监听器
	private OnProductTabClickListenser onProductTabClickListenter = null;
	private List<String> tab = null;
	private TextView filter = null;
	private View currentView = null;
	private View bottomLine = null;
	private TextView hotTab = null;
	private TextView newTab = null;
	private ImageView arrowDown = null;
	private String productType = Product.MEIJIA;
	private PopupWindow popWindow = null;
	private TempAdapter filteradapter = null;
	
	private int curIndex = -1;
	
	public interface OnProductTabClickListenser
	{
		public void setOnProductTabClickListenser(int tab,Map<String,Object> map);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		
		currentView = inflater.inflate(R.layout.product_tabpage, container,false);
		return currentView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		//Log.d(TAG, "yes enter");
		if(currentView != null)
		{
			filter = (TextView)currentView.findViewById(R.id.product_filter_tab);
			bottomLine = (View)currentView.findViewById(R.id.bottom_line);
			hotTab = (TextView)currentView.findViewById(R.id.product_hot_tab);
			newTab = (TextView)currentView.findViewById(R.id.product_new_tab);
			arrowDown = (ImageView)currentView.findViewById(R.id.product_tabpage_arrowdown);
			arrowDown.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.arrow_down_grey_jog));
			InitTabEvent();
            
			changeBottomLine(0);
			onProductTabClickListenter.setOnProductTabClickListenser(0, null);

		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		try
		{
			onProductTabClickListenter = (OnProductTabClickListenser)activity;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
	}
	
	private void InitTabEvent()
	{
		Map<String,Object>param = new HashMap<String,Object>();
		param.put(FilterQueryAndParse.Q_KEYWORD, FilterQueryAndParse.HOT);
		param.put(FilterQueryAndParse.Q_QUERY_TYPE, FilterQueryAndParse.QT_1);
		hotTab.setOnClickListener(new TabOnClickEvent(param,0));
		
		param.put(FilterQueryAndParse.Q_KEYWORD, FilterQueryAndParse.NEW);
		param.put(FilterQueryAndParse.Q_QUERY_TYPE, FilterQueryAndParse.QT_2);
		//param.put(ProductViewFragment.QUERY_TYPE, "0");
		newTab.setOnClickListener(new TabOnClickEvent(param,1));
		
		InitFilterEvent();
	}
	
	private void InitFilterEvent()
	{
		filter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				changePopWindowState();
			}
		});
	}
	
	//依靠状态来打开或者关闭弹出窗口
	private void changePopWindowState()
	{
		if(popWindow == null){
			popW();
			return ;
		}
		if(!popWindow.isShowing()){
			popW();
		}else{
			popWindow.dismiss();
		}
	}
	
	//弹出窗口的显示创建
	private void popW()
	{
		if(popWindow == null){
			View v = LayoutInflater.from(getActivity()).inflate(R.layout.tab_filter_list, null);
			ListView list = (ListView)v.findViewById(R.id.tab_filter_list_listview);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View lv,
						int pos, long arg3) {
					String text = adapter.getItemAtPosition(pos).toString();
					
					Map<String,Object>map = new HashMap<String, Object>();
					if(text.contains("-"))
					{
						String[] ar = text.split("-");
						map.put(FilterQueryAndParse.Q_SELECT_LEFT, ar[0]);
						map.put(FilterQueryAndParse.Q_SELECT_RIGHT, ar[1]);
					}
					else{
						map.put(FilterQueryAndParse.Q_SELECT_LEFT, "600");
					}
					
					filter.setText(text);
					onProductTabClickListenter.setOnProductTabClickListenser(2, map);
					
					if(popWindow!=null) changePopWindowState();
				}
			});
			
            
		    filteradapter = new TempAdapter();
		    list.setAdapter(filteradapter);
		    
		    DisplayMetrics dm = new DisplayMetrics();
			
		    popWindow = new PopupWindow(v,filter.getWidth()+200,400);
		    popWindow.setFocusable(true);  
		    popWindow.setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(R.color.blue)));
		    
		    //popWindow.setBackgroundDrawable(new BitmapDrawable());  
		    popWindow.setOutsideTouchable(true); 
		    popWindow.update();  
		}
		popWindow.showAsDropDown(filter);
	}
	
	
	//改变下划线位置
	public void changeBottomLine(int which)
	{
		DisplayMetrics dm = new DisplayMetrics();
		dm = getActivity().getResources().getDisplayMetrics();
		int ScreenWidth = (int)(dm.widthPixels);
		int avgWidth =  ScreenWidth / 3 ;
		Log.d(TAG, String.valueOf(avgWidth));
		
		int startWidth = avgWidth * which + LeftAndRightBottomBlank ;
		int endWidth = avgWidth + startWidth - 2*LeftAndRightBottomBlank; 
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)bottomLine.getLayoutParams();
		Log.d(TAG, String.valueOf(lp.leftMargin)+":"+String.valueOf(lp.rightMargin));
		
		lp.setMargins(startWidth, lp.topMargin, ScreenWidth - endWidth, lp.bottomMargin);
		bottomLine.setLayoutParams(lp);
        bottomLine.setPadding(0, 0, 0, 0);
        
        //set FilterText;
        if(which == 2){
        	if(filter.getText() == getResources().getText(R.string.filter))
        		filter.setText("0-200");
        }
        
        changeColor(which);
	}
	
	//按钮监听
	private class TabOnClickEvent implements View.OnClickListener
	{
        private Map<String,Object> params = null;
        private int curIndex = 0;
        
        public TabOnClickEvent(Map<String,Object>param,int index)
        {
        	params = param;
        	//TODO 判断
        	curIndex = index;
        }
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			//changeBottomLine(curIndex);
			if(onProductTabClickListenter != null)
			{
				onProductTabClickListenter.setOnProductTabClickListenser(curIndex,params);
			}
		}
		
	}
	
	private void changeColor(int index){
		TextView [] a = new TextView[]{hotTab,newTab,filter};
		if(index >= 3 || index < 0 || (index == curIndex)){
			return ;
		}
		if(curIndex != -1){
			a[curIndex].setTextColor(getActivity().getResources().getColor(R.color.labelgrey));
		}
		if(curIndex == 2){
			arrowDown.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.arrow_down_grey_jog));
		}
		curIndex = index;
		a[curIndex].setTextColor(getActivity().getResources().getColor(R.color.labelblue));
		if(curIndex == 2){
			arrowDown.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.arrow_down_blue_jog));
		}
	}
	
	private class TempAdapter extends BaseAdapter{
        private List<String> datas;
        
		public TempAdapter() {
			super();
			datas = new ArrayList<String>();
			datas.add("0-200");
			datas.add("200-299");
			datas.add("300-399");
			datas.add("400-499");
			datas.add("500-599");
			datas.add("高于600");
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null){
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_filter_list_item, parent,false);
			}
			((TextView)convertView.findViewById(R.id.tab_filter_list_item)).setText(getItem(position));
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public String getItem(int arg0) {
			// TODO Auto-generated method stub
			return datas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		
	}

}
