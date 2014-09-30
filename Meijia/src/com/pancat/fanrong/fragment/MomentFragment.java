package com.pancat.fanrong.fragment;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.pulltorefresh.library.PullToRefreshBase;
import com.pancat.fanrong.pulltorefresh.library.PullToRefreshBase.Mode;
import com.pancat.fanrong.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.pancat.fanrong.pulltorefresh.library.PullToRefreshListView;


@SuppressLint("NewApi")
public class MomentFragment extends Fragment{

	private PullToRefreshListView listView;
	private List<String> stringList = new ArrayList<String>();
	private MomentAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("state","createview");
		View view = inflater.inflate(R.layout.fragment_moment, container, false);
		listView = (PullToRefreshListView)view.findViewById(R.id.moment_list);
		listView.setMode(Mode.BOTH);
		adapter = new MomentAdapter(getActivity());
		listView.setAdapter(adapter);
		listView.setMode(Mode.BOTH);
		getData();
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			//下拉刷新
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				handlerRefresh.sendEmptyMessage(0);
			}

			//上拉加载
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				handlerRefresh.sendEmptyMessage(1);
			} 

			
		});
		return view;
	}

	private Handler handlerRefresh = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				stringList.clear();
				getData();
				listView.onRefreshComplete();
				break;
			case 1:
				stringList.addAll(new ArrayList<String>(Arrays.asList("123","456","789")));
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
				break;
			default:break;
			}
		}
		
	};
	
	private void getData(){
		Message msg = new Message();
		msg.what = 1;
		msg.obj = "";
		handlerRefresh.sendMessage(msg);
	}
	
	class MomentAdapter extends BaseAdapter{
		
		private LayoutInflater inflater;
		
		public MomentAdapter(Context context){
			inflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return stringList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String item = stringList.get(position);
			convertView = inflater.inflate(R.layout.moment_item, null);
			TextView tvMoment = (TextView)convertView.findViewById(R.id.tv_moment);
			tvMoment.setText(item);
			return convertView;
		}
		
	}
}
