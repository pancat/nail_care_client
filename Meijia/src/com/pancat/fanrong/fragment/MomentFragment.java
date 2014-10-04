package com.pancat.fanrong.fragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pancat.fanrong.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pancat.fanrong.bean.DuitangInfo;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.util.PhoneUtils;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;
import com.pancat.fanrong.waterfall.view.XListView;
import com.pancat.fanrong.waterfall.view.XListView.IXListViewListener;
import com.pancat.fanrong.waterfall.widget.ScaleImageView;


@SuppressLint("NewApi")
public class MomentFragment extends Fragment implements IXListViewListener{

	private View contextView;
	private ImageFetcher mImageFetcher;
	private XListView mAdapterView = null;
	private MyAdapter mAdapter = null;
	private int currentPage = 0;
	private ContentTask task = new ContentTask(getActivity(),2);
	//屏幕宽度
	private int mWidth;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		contextView = inflater.inflate(R.layout.act_pull_to_refresh_sample, container, false);
		//获取屏幕宽度
		WindowManager wm = (WindowManager)getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		mWidth = wm.getDefaultDisplay().getWidth();
				
		mAdapterView = (XListView)contextView.findViewById(R.id.list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapter = new MyAdapter(getActivity(), mAdapterView);
		
		mImageFetcher = new ImageFetcher(getActivity(), 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		return contextView;
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
	
	@Override
	public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        mAdapterView.setAdapter(mAdapter);
        addItemToContainer(currentPage, 2);
        Log.e("currentPage",String.valueOf(currentPage));
    }
	
	@Override
	public void onRefresh() {
		addItemToContainer(++currentPage, 1);
	}

	@Override
	public void onLoadMore() {
		addItemToContainer(++currentPage, 2);
	}
	
	public class ContentTask extends AsyncTask<String,Integer,List<DuitangInfo>>{
		
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
		protected List<DuitangInfo> doInBackground(String... params) {
			
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
		protected void onPostExecute(List<DuitangInfo> result) {
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
		
		public List<DuitangInfo> parseNewsJSON(String url) throws ClientProtocolException, IOException{
			List<DuitangInfo> duitangs = new ArrayList<DuitangInfo>();
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
                        DuitangInfo newsInfo1 = new DuitangInfo();
                        newsInfo1.setAlbid(newsInfoLeftObject.isNull("albid") ? "" : newsInfoLeftObject.getString("albid"));
                        newsInfo1.setIsrc(newsInfoLeftObject.isNull("isrc") ? "" : newsInfoLeftObject.getString("isrc"));
                        newsInfo1.setMsg(newsInfoLeftObject.isNull("msg") ? "" : newsInfoLeftObject.getString("msg"));
                        newsInfo1.setHeight(newsInfoLeftObject.getInt("iht"));
                        duitangs.add(newsInfo1);
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
		private LinkedList<DuitangInfo> mInfos;
		private XListView mListView;
		
		public MyAdapter(Context context,XListView xListView){
			mContext = context;
			mInfos = new LinkedList<DuitangInfo>();
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
			DuitangInfo duitangInfo = mInfos.get(position);
			if(convertView == null){
				LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
				convertView = layoutInflater.inflate(R.layout.infos_list, null);
				holder = new ViewHolder();
				holder.imageView = (ScaleImageView)convertView.findViewById(R.id.news_pic);
				holder.contentView = (TextView)convertView.findViewById(R.id.news_title);
				convertView.setTag(holder);
			}
			holder = (ViewHolder)convertView.getTag();
			holder.imageView.setImageWidth(mWidth);
			holder.imageView.setImageHeight(duitangInfo.getHeight());
			holder.contentView.setText(duitangInfo.getMsg());
			mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.imageView);
			return convertView;
		}
		
		class ViewHolder{
			ScaleImageView imageView;
			TextView contentView;
			TextView timeView;
		}
		
		public void addItemTop(List<DuitangInfo> data){
			for(DuitangInfo info : data){
				mInfos.addFirst(info);
			}
		}
		
		public void addItemLast(List<DuitangInfo> data){
			mInfos.addAll(data);
		}
	}

}
