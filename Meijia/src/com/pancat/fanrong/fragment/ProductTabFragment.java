package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ProductTabFragment extends Fragment
{
	private static final String TAG = "ProductTabFragment";
	
	private static final int LeftAndRightBottomBlank = 20;
	
	private OnProductTabClickListenser onProductTabClickListenter = null;
	private List<String> tab = null;
	private Spinner spinner = null;
	private View currentView = null;
	private View bottomLine = null;
	private TextView hotTab = null;
	private TextView newTab = null;
	private String productType = Product.MEIJIA;
	
	public interface OnProductTabClickListenser
	{
		public void setOnProductTabClickListenser(int tab,Map<String,String> map);
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
			spinner = (Spinner)currentView.findViewById(R.id.product_filter_tab);
			bottomLine = (View)currentView.findViewById(R.id.bottom_line);
			hotTab = (TextView)currentView.findViewById(R.id.product_hot_tab);
			newTab = (TextView)currentView.findViewById(R.id.product_new_tab);
			
			changeBottomLine(0);
			InitTabEvent();
			
		    List<String> temp = new ArrayList<String>();
		    temp.add("0-200");
		    temp.add("200-299");
		    temp.add("300-399");
		    temp.add("400-499");
		    temp.add("500-599");
		    temp.add("高于600");
		    
		    //TODO 这里之后再改
			spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, temp));
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
				{
					// TODO 自动生成的方法存根
					Map<String,String>map = new HashMap<String, String>();
					String param = (String) adapterView.getItemAtPosition(position);
					if(param.contains("-"))
					{
						String[] ar = param.split("-");
						map.put(ProductViewFragment.SELECT_LEFT, ar[0]);
						map.put(ProductViewFragment.SELECT_RIGHT, ar[1]);
					}
					else{
						map.put(ProductViewFragment.SELECT_LEFT, "600");
					}
					changeBottomLine(2);
					onProductTabClickListenter.setOnProductTabClickListenser(2, map);
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO 自动生成的方法存根
					
				}
			});
			
			//
		    //Log.d(TAG,"yes set");
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
		
		tab = new ArrayList<String>();
		tab.add("Hot");
		tab.add("New");
	}
	
	private void InitTabEvent()
	{
		Map<String,String>param = new HashMap<String,String>();
		param.put(ProductViewFragment.KEYWORD, "hot");
		param.put(ProductViewFragment.QUERY_TYPE, "0");
		hotTab.setOnClickListener(new TabOnClickEvent(param,0));
		
		param.put(ProductViewFragment.KEYWORD, "new");
		//param.put(ProductViewFragment.QUERY_TYPE, "0");
		newTab.setOnClickListener(new TabOnClickEvent(param,1));
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
	}
	
	//按钮监听
	private class TabOnClickEvent implements View.OnClickListener
	{
        private Map<String,String> params = null;
        private int curIndex = 0;
        
        public TabOnClickEvent(Map<String,String>param,int index)
        {
        	params = param;
        	//TODO 判断
        	curIndex = index;
        }
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			changeBottomLine(curIndex);
			if(onProductTabClickListenter != null)
			{
				onProductTabClickListenter.setOnProductTabClickListenser(curIndex,params);
			}
		}
		
	}
}