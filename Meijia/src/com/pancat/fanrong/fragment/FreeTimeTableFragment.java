package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.adapter.MoreTypeViewForGVAdapter;
import com.pancat.fanrong.temp.SampleAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FreeTimeTableFragment extends Fragment {
    private static final String KEY_CONTENT = "FreeTimeTableFragment:Content";
    public static final int START_TIME = 10;
    public static final int END_TIME = 21;
    public static final String LABEL = "点";
    public static final String IDLELABEL = "可预约";
    public static final String BUSYLABEL = "已预约";
    
    private LinearLayout courtview = null;
    private GridView gridView = null;
    private MoreTypeViewForGVAdapter adapter = null;
    private onItemClickToActivity listener = null;
    
    private ArrayList<HashMap<String,Object>> datas;
    
    public static FreeTimeTableFragment newInstance(Map<Integer,Integer> map) {
    	FreeTimeTableFragment fragment = new FreeTimeTableFragment();
    	
    	ArrayList<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();
    	
    	for(int i=START_TIME; i<= END_TIME; i++){
    		HashMap<String,Object> maps = new HashMap<String,Object>();
    		int v = ((map!=null) && map.containsKey(i))?map.get(i):MoreTypeViewForGVAdapter.BUSY;
    		if(v != MoreTypeViewForGVAdapter.BUSY && v != MoreTypeViewForGVAdapter.IDEL)v = MoreTypeViewForGVAdapter.BUSY;
    		
    		maps.put(MoreTypeViewForGVAdapter.TYPE, v);
    		maps.put(MoreTypeViewForGVAdapter.LABEL, String.valueOf(i)+LABEL);
    		maps.put(MoreTypeViewForGVAdapter.ORDER, (v==MoreTypeViewForGVAdapter.BUSY)?BUSYLABEL:IDLELABEL);
    	    data.add(maps);
    	}
    	fragment.datas = data;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            datas = (ArrayList<HashMap<String,Object>>)savedInstanceState.getSerializable(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        courtview = (LinearLayout)inflater.inflate(R.layout.freetime_table_layout, null);
        gridView = (GridView)courtview.findViewById(R.id.freetime_table);
        
        if(adapter == null){
        	adapter = new MoreTypeViewForGVAdapter(getActivity(), datas);
        }
        
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Map<String,Object> map = datas.get(pos);
				if((Integer) map.get(MoreTypeViewForGVAdapter.TYPE) == MoreTypeViewForGVAdapter.BUSY ){
					if(listener != null){
						listener.setonItemClickToActivity(-1);
					}
				}else{
					if(listener != null){
						listener.setonItemClickToActivity(pos+START_TIME);
					}
				}
			}
		});
        return courtview;
    }
    
    
    @Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			listener = (onItemClickToActivity)activity;
		}catch(Exception e){
			listener = null;
		}
	}
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_CONTENT, datas);
    }
    public interface onItemClickToActivity{
    	public void setonItemClickToActivity(int time);
    }
}
