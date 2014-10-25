package com.pancat.fanrong.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MoreTypeViewForGVAdapter extends BaseAdapter {
    public static final int TYPE_TOTAL = 2;
    public static final int BUSY = 0;
    public static final int IDEL = 1;
    public static final String TYPE = "type";
    public static final String LABEL = "label";
    public static final String ORDER = "order";
    
	private Context context;
	private ArrayList<HashMap<String,Object>>datas;
	private LayoutInflater inflater = null;
	
	public MoreTypeViewForGVAdapter(Context context,ArrayList<HashMap<String,Object>> datas) {
		this.context = context;
		this.datas = datas;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		if(pos >= getCount()) return null;
		return pos;
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}
    
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return Integer.parseInt(datas.get(position).get(TYPE).toString());
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return TYPE_TOTAL;
	}
	@Override
	public View getView(int pos, View courtview, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh = null;
		if(courtview == null)
		{
			vh = new ViewHolder();
			switch (getItemViewType(pos)) {
			case BUSY:
				courtview = inflater.inflate(R.layout.freetime_table_item, null);
				break;
			case IDEL:
				courtview = inflater.inflate(R.layout.freetime_table_item2, null);
				break;
			default: courtview = null;
				break;
			}
			if(courtview != null){
				vh.label = (TextView)courtview.findViewById(R.id.freetime_table_item_label);
				vh.order = (TextView)courtview.findViewById(R.id.freetime_table_item_order);
				courtview.setTag(vh);
			}
		}else{
			vh = (ViewHolder)courtview.getTag();
		}
		vh.label.setText(datas.get(pos).get(LABEL).toString());
		vh.order.setText(datas.get(pos).get(ORDER).toString());
		return courtview;
	}
   private static class ViewHolder{
	   public TextView label;
	   public TextView order;
   }
}
