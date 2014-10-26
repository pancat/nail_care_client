package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.NailTechnician;
import com.pancat.fanrong.common.CircleImageView;
import com.pancat.fanrong.waterfall.view.XListView;
import com.pancat.fanrong.waterfall.view.XListView.IXListViewListener;

public class NailTechnicianActivity extends Activity implements IXListViewListener{

	private Button mBtnBack;
	private Button mBtnSearch;
	private XListView mAdapterView;
	private NailTechAdapter mAdapter;
	private List<NailTechnician> mNailTechnicians = new ArrayList<NailTechnician>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nail_technician);
		init();
		//进入页面第一次加载数据
		onLoadMore();
	}

	/**
	 * 初始化成员变量
	 */
	public void init(){
		mBtnBack = (Button)findViewById(R.id.btn_back);
		mBtnSearch = (Button)findViewById(R.id.btn_search);
		mAdapterView = (XListView)findViewById(R.id.nail_tech_list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setPullRefreshEnable(false);
		mAdapterView.setXListViewListener(this);
		mAdapter = new NailTechAdapter(this, mAdapterView);
		mAdapterView.setAdapter(mAdapter);
	}
	
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			//点击返回按钮，finish当前页面
			finish();
			break;
		case R.id.btn_search:
			//跳转到技师搜索页面
			break;
		default:break;
		}
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			List<NailTechnician> technicians = new ArrayList<NailTechnician>();
			for(int i = 0;i < 10;i++){
				NailTechnician technician = new NailTechnician();
				technicians.add(technician);
			}
			switch(msg.what){
			case 1:
				mAdapter.addItemTop(technicians);
				mAdapter.notifyDataSetChanged();
				mAdapterView.stopRefresh();
				break;
			case 2:
				mAdapterView.stopLoadMore();
				mAdapter.addItemLast(technicians);
				mAdapter.notifyDataSetChanged();
				break;
			default:break;
			}
		}
		
	};
	
	
	@Override
	public void onRefresh() {
		Message msg = new Message();
		msg.what = 1;
		handler.sendMessage(msg);
	}

	@Override
	public void onLoadMore() {
		Message msg = new Message();
		msg.what = 2;
		handler.sendMessage(msg);
		
	}
	
	

	/**
	 * 美甲技师数据适配器
	 * @author trh
	 *
	 */
	class NailTechAdapter extends BaseAdapter{

		private Context mContext;
		private XListView mListView;
		
		public NailTechAdapter(Context context,XListView listView){
			mContext = context;
			mListView = listView;
		}
		
		@Override
		public int getCount() {
			return mNailTechnicians.size();
		}

		@Override
		public Object getItem(int position) {
			return mNailTechnicians.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			NailTechnician technician = mNailTechnicians.get(position);
			if(convertView == null){
				LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
				convertView = layoutInflater.inflate(R.layout.nail_technician_intro_item, null);
				holder = new ViewHolder();
				holder.imageView = (CircleImageView)convertView.findViewById(R.id.img);
				holder.name = (TextView)convertView.findViewById(R.id.name);
				convertView.setTag(holder);
			}
			holder = (ViewHolder)convertView.getTag();
			return convertView;
		}
		
		public void addItemTop(List<NailTechnician> technicians){
			for(NailTechnician technician : technicians){
				mNailTechnicians.add(0, technician);
			}
		}
		
		public void addItemLast(List<NailTechnician> data){
			mNailTechnicians.addAll(data);
		}
		
		class ViewHolder{
			CircleImageView imageView;
			TextView name;
			
		}
		
	}

}
