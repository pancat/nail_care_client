package com.pancat.fanrong.adapter;

import java.util.List;

import com.pancat.fanrong.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HomeProductListAdapter extends BaseAdapter {

	private Context mContext;
	private ListView mListView;
	private List<Object> mInfos;
	
	public HomeProductListAdapter(Context context, ListView listView, List<Object> listItem){
		mContext = context;
		mListView = listView;
		mInfos = listItem;
	}
	
	@Override
	public int getCount() {
		return mInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_home_list, null);
		}

		ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

		TextView title = (TextView) convertView.findViewById(R.id.title);
		title.setText("这里是标题");

		return convertView;
	}

}
