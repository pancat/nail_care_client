package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.List;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.SearchLabelAndTab;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class CommonTabSearchComponent extends Fragment{
	private static final String TAG = "CommonTabSearchComponent";

	private View contextView;
    private ArrayList<SearchLabelAndTab> tabOption;
    private List<View> tabButton;
    private TableLayout tableLayout;
    private String label;
    private TextView textLabel;
    private OnClickSearchTabListener onClickSearchTabListener;
    private static final int TABLECOLNUM = 4;
    private int labelnum = 0;
    private boolean[] flags;
    private int normalColor ;
    private int selColor;
    
    public static CommonTabSearchComponent newInstance(ArrayList<SearchLabelAndTab> labelTabList,String label)
    {
    	CommonTabSearchComponent instance = new CommonTabSearchComponent();
    	instance.tabOption = labelTabList;
    	instance.label = label;
    	return instance;
    }
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        tabButton = new ArrayList<View>();
        InitTabButton();
	}

    public void InitTabButton()
    {
    	normalColor = getActivity().getResources().getColor(R.color.depthred);
    	selColor = getActivity().getResources().getColor(R.color.blue);
    	
    	for(SearchLabelAndTab tab:tabOption)
    	{
    		Button b= new Button(getActivity());
    		b.setText(tab.getName());
    		b.setTextColor(getActivity().getResources().getColor(R.color.depthred));
    		b.setGravity(Gravity.CENTER);
    	    b.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stylelabel));
    		tabButton.add(b);
    	}
    	labelnum = tabOption.size();
    	
    	int left = TABLECOLNUM-tabOption.size()%TABLECOLNUM;
    	if(left != TABLECOLNUM){
    		for(int i=0;i<left;i++){
    			TextView t = new TextView(getActivity());
    			t.setText("");
    			t.setGravity(Gravity.CENTER);
    			t.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
    			t.setFocusable(false);
    			tabButton.add(t);
    		}
    	}
    	flags = new boolean[labelnum];
    	for(int i=0; i<labelnum; i++){
    		flags[i] = false;
    	}
    }
    
    
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		onClickSearchTabListener = (OnClickSearchTabListener)activity;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		contextView = inflater.inflate(R.layout.tab_search_component_fragment, container,false);
		tableLayout = (TableLayout)contextView.findViewById(R.id.tab_search_component_fragment_tabtable);
	    textLabel = (TextView)contextView.findViewById(R.id.tab_search_component_fragment_label);
	    textLabel.setText(label);

	    DisplayMetrics dm = new DisplayMetrics();
	    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
	    
	    //set button params
	    //int w = (dm.widthPixels - 50) / TABLECOLNUM;
	    LayoutParams vlp = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
		vlp.setMargins(10, 10, 10, 10);
		vlp.gravity = Gravity.CENTER;
		vlp.weight = 1;
		
	    TableRow tableRow = null;
	    for(int i=0;i<tabButton.size();i++)
	    {
	    	if(i%TABLECOLNUM == 0)
	    	{
	    		if(tableRow != null)
	    		{
	    			tableLayout.addView(tableRow);
	    		}
	    		tableRow = new TableRow(getActivity());
	    	}
	    	tabButton.get(i).setLayoutParams(vlp);
	    	tableRow.addView(tabButton.get(i));
	    }
	    tableLayout.addView(tableRow);
	    
		for(int i=0;i<labelnum;i++)
		{
			final SearchLabelAndTab v = tabOption.get(i);
			final int sel = i;
			
			tabButton.get(i).setOnClickListener(new OnClickListener() {
				
				@Override
			
				public void onClick(View view) {
					// TODO Auto-generated method stub
					onClickSearchTabListener.setOnClickSearchTabListener(label,v,!flags[sel]);
					flags[sel] = !flags[sel];
					if(flags[sel]) ((Button)view).setTextColor(selColor);
					else ((Button)view).setTextColor(normalColor);
				}
			});
		}
		
		return contextView;
	}
	
	public interface OnClickSearchTabListener{
		void setOnClickSearchTabListener(String label,SearchLabelAndTab v,boolean flag);
	}
}
