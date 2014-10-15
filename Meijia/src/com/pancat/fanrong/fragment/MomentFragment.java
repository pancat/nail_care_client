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
import com.pancat.fanrong.activity.MomentActivity;
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
public class MomentFragment extends Fragment implements IXListViewListener{

	public final String TAG = "MomentFragment";
	private View contextView;
	private ImageFetcher mImageFetcher;
	private XListView mAdapterView = null;
	private MyAdapter mAdapter = null;
	private int currentPage = 0;
	private ContentTask task = new ContentTask(getActivity(),2);
	private FragmentCallback fragmentCallback;
	private List<Circle> mInfos = new ArrayList<Circle>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "createview");
		contextView = inflater.inflate(R.layout.act_pull_to_refresh_sample, container, false);
				
		mAdapterView = (XListView)contextView.findViewById(R.id.list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapter = new MyAdapter(getActivity(), mAdapterView);
		
		mImageFetcher = new ImageFetcher(getActivity(), 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mImageFetcher.setExitTasksEarly(false);
		mAdapterView.setAdapter(mAdapter);
		
		return contextView;
	}
	
	
	@Override
	public void onResume() {
		getData();
		Log.e(TAG, "resume");
        super.onResume();
    }
	
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fragmentCallback = (MomentActivity)activity;
	}

	@Override
	public void onRefresh() {
		addItemToContainer(++currentPage, 1);
	}

	@Override
	public void onLoadMore() {
		addItemToContainer(++currentPage, 2);
	}
	
	private void getData(){
		//进入页面第一次加载数据
		mInfos = DatabaseManager.getInstance(getActivity()).getCircles();
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 添加内容到列表中
	 * @param pageIndex	页数索引
	 * @param type 刷新类型
	 */
	private void addItemToContainer(int pageIndex,int type){
		if(task.getStatus() != Status.RUNNING){
			String url = "http://www.duitang.com/album/1733789/masn/p/"+pageIndex+"/10/";
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
				return parseNewsJSON(params[0]);
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
		
		public List<Circle> parseNewsJSON(String url) throws ClientProtocolException, IOException{
			List<Circle> duitangs = new ArrayList<Circle>();
			String json = "";
			//判断是否连接网络
			if(PhoneUtils.isNetworkConnected(mContext)){
				//从url中获取json字符串
				json = RestClient.getInstance().getStringFromUrl(url);
			}
			if(json != null){
				try {
					JSONObject newsObject = new JSONObject(json);
					JSONObject jsonObject = newsObject.getJSONObject("data");
					JSONArray blogsJson = jsonObject.getJSONArray("blogs");
					for (int i = 0; i < blogsJson.length(); i++) {
                        JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
                        Circle circle = new Circle();
                        circle.setIsrc(newsInfoLeftObject.isNull("isrc") ? "" : newsInfoLeftObject.getString("isrc"));
                        circle.setMsg(newsInfoLeftObject.isNull("msg") ? "" : newsInfoLeftObject.getString("msg"));
                        circle.setHeight(newsInfoLeftObject.getInt("iht"));
                        circle.setWidth(newsInfoLeftObject.getInt("iwd"));
                        duitangs.add(circle);
                        DatabaseManager.getInstance(getActivity()).addCircle(circle);
                    }
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return duitangs;
		}
	}  
	
	class MyAdapter extends BaseAdapter{

		private Context mContext;
		private XListView mListView;
		
		public MyAdapter(Context context,XListView xListView){
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
			Circle duitangInfo = mInfos.get(position);
			if(convertView == null){
				LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
				convertView = layoutInflater.inflate(R.layout.infos_list, null);
				holder = new ViewHolder();
				holder.imageView = (ScaleImageView)convertView.findViewById(R.id.news_pic);
				holder.contentView = (TextView)convertView.findViewById(R.id.news_title);
				convertView.setTag(holder);
			}
			holder = (ViewHolder)convertView.getTag();
			holder.imageView.setImageWidth(duitangInfo.getWidth());
			holder.imageView.setImageHeight(duitangInfo.getHeight());
			holder.contentView.setText(duitangInfo.getMsg());
			mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.imageView);
			final Bundle data = new Bundle();
			data.putSerializable("duitangInfo", duitangInfo);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//回调MomentActivity的接口
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
			for(Circle info : data){
				mInfos.add(0, info);
			}
		}
		
		public void addItemLast(List<Circle> data){
			mInfos.addAll(data);
		}
	}

}
