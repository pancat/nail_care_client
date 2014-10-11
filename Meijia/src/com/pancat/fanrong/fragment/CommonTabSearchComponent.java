package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.List;

import com.pancat.fanrong.R;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class CommonTabSearchComponent extends Fragment{
	private static final String TAG = "CommonTabSearchComponent";
	
	private static final String TABKEY = "tabOption";
	private static final String TABLABEL = "tabLable";
	private View contextView;
    private ArrayList<String> tabOption;
    private List<Button> tabButton;
    private TableLayout tableLayout;
    private String label;
    private TextView textLabel;
    private OnClickSearchTabListener onClickSearchTabListener;
    private static final int TABLECOLNUM = 4;
    
    public static CommonTabSearchComponent newInstance(ArrayList<String> strArr,String label)
    {
    	CommonTabSearchComponent instance = new CommonTabSearchComponent();
    	Bundle bundle = new Bundle();
    	bundle.putStringArrayList(TABKEY, strArr);
    	bundle.putString(TABLABEL, label);
    	instance.setArguments(bundle);
    	return instance;
    }
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
        tabOption = bundle.getStringArrayList(TABKEY);
        label = bundle.getString(TABLABEL);
        tabButton = new ArrayList<Button>();
        InitTabButton();
	}

    public void InitTabButton()
    {
    	for(String str:tabOption)
    	{
    		Button b= new Button(getActivity());
    		b.setText(str);
    		b.setGravity(Gravity.CENTER);
    	    b.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
    		tabButton.add(b);
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
	    int w = (dm.widthPixels - 50) / TABLECOLNUM;
	    LayoutParams vlp = new LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
		vlp.setMargins(10, 10, 10, 10);
		
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
	    
		for(int i=0;i<tabButton.size();i++)
		{
			final String v = tabOption.get(i);
			tabButton.get(i).setOnClickListener(new OnClickListener() {
				
				@Override
			
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					onClickSearchTabListener.setOnClickSearchTabListener(label,v);
				}
			});
		}
		
		return contextView;
	}
	
	public interface OnClickSearchTabListener{
		void setOnClickSearchTabListener(String label,String v);
	}
}
