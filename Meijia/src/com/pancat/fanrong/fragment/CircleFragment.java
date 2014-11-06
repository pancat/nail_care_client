package com.pancat.fanrong.fragment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.CircleActivity;
import com.pancat.fanrong.adapter.CardsAnimationAdapter;
import com.pancat.fanrong.adapter.CircleAdapter;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.db.DatabaseManager;
import com.pancat.fanrong.util.PhoneUtils;
import com.pancat.fanrong.view.LoadingFooter;
import com.pancat.fanrong.view.LoadingFooter.OnLoadListener;
import com.pancat.fanrong.view.PageStaggeredGridView;





@SuppressLint("NewApi")
public class CircleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

	public final String TAG = "CircleFragment";
	private View contextView;
	private CircleAdapter mAdapter;
	private int mPageIndex = 0;
	private int mPageSize = 10;
	private ContentTask task;
	private FragmentCallback fragmentCallback;
	private List<Circle> mCircles = new ArrayList<Circle>();
	private SwipeRefreshLayout mSwipeLayout;
	private PageStaggeredGridView mGridView;
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "createview");
		contextView = inflater.inflate(R.layout.fragment_circles, container, false);
		task = new ContentTask(getActivity(),1);
		mSwipeLayout = (SwipeRefreshLayout)contextView.findViewById(R.id.swipe_container);	
		mGridView = (PageStaggeredGridView)contextView.findViewById(R.id.grid_view);
		mAdapter = new CircleAdapter(getActivity(), mGridView, mCircles,fragmentCallback);
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(mGridView);
        mGridView.setAdapter(animationAdapter);
        mGridView.setLoadListener(new OnLoadListener() {
			
			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				addItemToContainer(2);
			}
		});
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	
            }
        });
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
		getData();
		return contextView;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fragmentCallback = (CircleActivity)activity;
	}

	
	private void getData(){
		//首次进入页面是刷新数据
		List<Circle> result = DatabaseManager.getInstance(getActivity()).getCircles();
		addLists(result);
		mAdapter.notifyDataSetChanged();
		mSwipeLayout.setRefreshing(true);
		onRefresh();
	}
	
	
	
	/**
	 * 添加内容到列表中
	 * @param pageIndex	页数索引
	 * @param type 刷新类型 1为下拉刷新，2为上拉加载
	 */
	private void addItemToContainer(int type){
		if(task.getStatus() != Status.RUNNING){
			String url = "http://54.213.141.22/teaching/Platform/index.php/circle_service/get_circle_list?index="+mPageIndex+"&size="+mPageSize;
			ContentTask task = new ContentTask(getActivity(), type);
			task.execute(url);
		}
	}
	
	
	public void addLists(List<Circle> result){
		if(result.size() > 0){
			for(int i =0; i < result.size();i++){
				mCircles.add(result.get(i));
			}
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
				return parseCirclesJSON(param);
			} catch (Exception e) {
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
				if(result != null && result.size() > 0){
					mCircles.clear();
					addLists(result);
					mAdapter.notifyDataSetChanged();
					mSwipeLayout.setRefreshing(false);
				}
				else{
					mSwipeLayout.setRefreshing(false);
				}
			}
			else if(mType == 2){
				//执行下拉加载时
				addLists(result);
				mAdapter.notifyDataSetChanged();
				mGridView.setState(LoadingFooter.State.Idle);
			}
		}
		
		@SuppressWarnings("static-access")
		public List<Circle> parseCirclesJSON(String url) throws ClientProtocolException, IOException, SQLException{
			List<Circle> circles = new ArrayList<Circle>();
			String json = "";
			//判断是否连接网络
			if(PhoneUtils.isNetworkConnected(mContext)){
				if(mPageIndex == 0){
					DatabaseManager.getInstance(getActivity()).deleteCircleList();
				}
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
					mPageIndex += mPageSize;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return circles;
		}
	}

	@Override
	public void onRefresh() {
		//下拉刷新操作实现
//		mAdapter.notifyDataSetChanged();
//		mIndex = 0;
		mPageIndex = 0;
		addItemToContainer(1);
		
	}  
	
	

}
