package com.pancat.fanrong.fragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.CircleActivity;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.bean.Infos;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.db.DatabaseManager;
import com.pancat.fanrong.util.PhoneUtils;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;
import com.pancat.fanrong.waterfall.view.XListView;
import com.pancat.fanrong.waterfall.view.XListView.IXListViewListener;
import com.pancat.fanrong.waterfall.widget.ScaleImageView;


@SuppressLint("NewApi")
public class CircleFragment extends Fragment implements IXListViewListener{

	public final String TAG = "MomentFragment";
	private View contextView;
	private ImageFetcher mImageFetcher;
	private XListView mAdapterView = null;
	private CircleAdapter mAdapter = null;
	private int mIndex = 0;
	private ContentTask task = new ContentTask(getActivity(),2);
	private FragmentCallback fragmentCallback;
	private List<Circle> mInfos = new ArrayList<Circle>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "createview");
		contextView = inflater.inflate(R.layout.fragment_circle, container, false);
				
		mAdapterView = (XListView)contextView.findViewById(R.id.list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapter = new CircleAdapter(getActivity(), mAdapterView);
		
		mImageFetcher = new ImageFetcher(getActivity(), 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mImageFetcher.setExitTasksEarly(false);
		mAdapterView.setAdapter(mAdapter);
		getData();
		return contextView;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fragmentCallback = (CircleActivity)activity;
	}

	@Override
	public void onRefresh() {
		mIndex = 0;
		addItemToContainer(mIndex, 1);
	}

	@Override
	public void onLoadMore() {
		addItemToContainer(mIndex, 2);
	}
	
	private void getData(){
		//首次进入页面是刷新数据
		mInfos = DatabaseManager.getInstance(getActivity()).getCircles();
		mAdapter.notifyDataSetChanged();
		onRefresh();
	}
	
	
	/**
	 * 添加内容到列表中
	 * @param pageIndex	页数索引
	 * @param type 刷新类型
	 */
	private void addItemToContainer(int index,int type){
		if(task.getStatus() != Status.RUNNING){
			String url = "http://54.213.141.22/teaching/Platform/index.php/circle_service/get_circles?index="+mIndex;
			
//			String url = "http://www.duitang.com/album/1733789/masn/p/"+pageIndex+"/10/";
			Log.d("MainActivity", "current url:" + url);
			ContentTask task = new ContentTask(getActivity(), type);
			task.execute(url);
		}
	}
	
	public class ContentTask extends AsyncTask<String,Integer,List<Circle>>{
		
		private Context mContext;
		//刷新类型：1为上拉刷新，2为下拉加载
		private int mType = 1;
		
		/**
		 * 构造函数，初始化变量
		 * @param context
		 * @param type
		 */
		public ContentTask(Context context,int type){
			super();
			mContext = context;
			mType = type;
		}

		/**
		 * 在后台执行费时的操作
		 */
		@Override
		protected List<Circle> doInBackground(String... params) {
			
			try {
				String param = params[0];
				return parseNewsJSON(param);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * 接受的参数为doInBackground返回的值
		 * doInBackground执行完成后被UI线程调用
		 */
		@Override
		protected void onPostExecute(List<Circle> result) {
			if(mType == 1){
				//执行上拉刷新时
				mAdapter.addItemTop(result);
				mAdapter.notifyDataSetChanged();
				mAdapterView.stopRefresh();
			}
			else if(mType == 2){
				//执行下拉加载时
				mAdapterView.stopLoadMore();
				mAdapter.addItemLast(result);
				mAdapter.notifyDataSetChanged();
			}
		}
		
		@SuppressWarnings("static-access")
		public List<Circle> parseNewsJSON(String url) throws ClientProtocolException, IOException{
			List<Circle> circles = new ArrayList<Circle>();
			String json = "";
			//判断是否连接网络
			if(PhoneUtils.isNetworkConnected(mContext)){
				//从url中获取json字符串
				json = RestClient.getInstance().getStringFromUrl(url);
			}
			if(json != null){
				try {
					JSONArray jsonArray = new JSONArray(json);
					for(int i = 0;i < jsonArray.length();i++){
						Circle circle = new Circle();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String creTime = jsonObject.getString("cre_time");
						String description = jsonObject.getString("description");
						int width = jsonObject.getInt("width");
						int height = jsonObject.getInt("height");
						int uid = jsonObject.getInt("uid");
						String path = jsonObject.getString("path");
						int circleId = jsonObject.getInt("circle_id");
						circle.setDescription(description);
						circle.setHeight(height);
						circle.setId(circleId);
						circle.setPath(path);
						circle.setUid(uid);
						circle.setWidth(width);
						circle.setCreTime(creTime);
						circles.add(circle);
						DatabaseManager.getInstance(getActivity()).addCircle(circle);
					}
					mIndex += jsonArray.length();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return circles;
		}
	}  
	
	class CircleAdapter extends BaseAdapter{

		private Context mContext;
		private XListView mListView;
		
		public CircleAdapter(Context context,XListView xListView){
			mContext = context;
			mListView = xListView;
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
			ViewHolder holder;
			Circle circle = mInfos.get(position);
			if(convertView == null){
				LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
				convertView = layoutInflater.inflate(R.layout.item_circle, null);
				holder = new ViewHolder();
				holder.imageView = (ScaleImageView)convertView.findViewById(R.id.news_pic);
				holder.contentView = (TextView)convertView.findViewById(R.id.news_title);
				convertView.setTag(holder);
			}
			holder = (ViewHolder)convertView.getTag();
			holder.imageView.setImageWidth(circle.getWidth());
			holder.imageView.setImageHeight(circle.getHeight());
			holder.contentView.setText(circle.getDescription());
			mImageFetcher.loadImage(circle.getPath(), holder.imageView);
			final Bundle data = new Bundle();
			data.putSerializable("circle", circle);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//回调CircleActivity的接口
					fragmentCallback.callback(data);
				}
			});
			return convertView;
		}
		
		class ViewHolder{
			ScaleImageView imageView;
			TextView contentView;
			TextView timeView;
		}
		
		public void addItemTop(List<Circle> data){
			mInfos.clear();
			mInfos.addAll(data);
		}
		
		public void addItemLast(List<Circle> data){
			mInfos.addAll(data);
		}
	}

}
