package com.pancat.fanrong.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.AdvertiseActivity;
import com.pancat.fanrong.bean.AdBannerItem;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.db.DatabaseManager;
import com.pancat.fanrong.db.DatabaseOpenHelper;
import com.pancat.fanrong.handler.HandlerFactory;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

@SuppressLint("NewApi") 
public class AdBannerFragment extends Fragment implements OnPageChangeListener {
	private View contextView;
	
	private List<ImageView> imageList;
	private List<AdBannerItem> adBannerItemlist;
	
	private Timer adBannerPlayTimer = null;
	
	// 前一个被选中的点的索引位置默认设置为0
	private int preEnablePosition = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contextView = inflater
				.inflate(R.layout.fragment_ad_banner, container, false);
		
		return contextView;
	}
	
	final Handler adBannerPlayHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ViewPager viewPager = (ViewPager) contextView
					.findViewById(R.id.view_pager);
			int nextAdIdx = viewPager.getCurrentItem() + 1;
			viewPager.setCurrentItem(nextAdIdx);
			super.handleMessage(msg);
		}
	};

	private Timer startAdBannerPlayTimer() {
		if (adBannerPlayTimer != null) {
			return adBannerPlayTimer;
		}
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				adBannerPlayHandler.sendEmptyMessage(0);
			}
		};

		adBannerPlayTimer = new Timer();
		adBannerPlayTimer.schedule(task, 1, 3000);

		return adBannerPlayTimer;
	}
	
	final AsyncHttpResponseHandler adBannerReadyHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
		}
		
		private List<AdBannerItem> parseJsonString(String content) 
		{
			Log.d("AdBannerItem", content);
			List<AdBannerItem> list = new ArrayList<AdBannerItem>();
			try {
				JSONArray jsonArray = new JSONArray(content);
				for (int i = 0; i < jsonArray.length(); ++i)
				{
					JSONObject itemObj = jsonArray.getJSONObject(i);
					try {
						int productID = itemObj.getInt("p_id");
						String imgUrl = itemObj.getString("image_uri");
						String desc = itemObj.getString("p_describe");
						if (imgUrl == "null")
						{
							throw new Exception("image url is null.");
						}
						AdBannerItem item = new AdBannerItem();
						item.setProductID(productID);
						item.setImgUrl(imgUrl);
						item.setDesc(desc);
						list.add(item);
					}catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return list;
		}

		@Override
		public void onSuccess(String content) {
			DatabaseOpenHelper helper = DatabaseManager.getInstance().getHelper();
			Dao<AdBannerItem, Integer> dao = helper.getAdBannerItemDao();
			
			try {
				List<AdBannerItem> list = dao.queryForAll();
				dao.delete(list);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			List<AdBannerItem> list = parseJsonString(content);
			for (AdBannerItem item : list)
			{
				try {
					dao.create(item);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			setViewPagerFromDB();
		}
	};

	private void requestAdsFromServer() {
		String url = "product/get_home_ad_list";
		RestClient.getInstance().get(url, new RequestParams(), adBannerReadyHandler);
	}
	
	private void setViewPagerFromDB()
	{
		DatabaseOpenHelper helper = DatabaseManager.getHelper(getActivity()
				.getApplicationContext());
		Dao<AdBannerItem, Integer> dao = helper.getAdBannerItemDao();
		try {
			List<AdBannerItem> list = dao.queryForAll();
			initViewPager(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		startAdBannerPlayTimer();
	}

	private void initViewPager(List<AdBannerItem> list) {
		this.adBannerItemlist = list;
		ViewPager viewPager = (ViewPager) contextView
				.findViewById(R.id.view_pager);
		viewPager.removeAllViews();
		LinearLayout pointGroup = (LinearLayout) contextView
				.findViewById(R.id.ll_point_group);
		pointGroup.removeAllViews();
		TextView tvDescription = (TextView) contextView
				.findViewById(R.id.tv_description);
		imageList = new ArrayList<ImageView>();

		for (AdBannerItem item : list) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setBackgroundResource(R.drawable.default_banner_ad);
			imageList.add(imageView);
			
			String url = item.getImgUrl();
			
			HandlerFactory.setLoadImageHandler(url, true, imageView);

			// 循环一次添加一个圆点
			View point = new View(getActivity());
			point.setBackgroundResource(R.drawable.point_background);
			LayoutParams params = new LayoutParams(15, 15);
			params.leftMargin = 5;
			point.setEnabled(false);
			point.setLayoutParams(params);
			// 向线性布局中添加点
			pointGroup.addView(point);
		}

		viewPager.setAdapter(new AdBannerViewPagerAdapter(imageList));
		viewPager.setOnPageChangeListener(this);
		// 初始化图片和选中的点
		if (this.adBannerItemlist.size() > 0) {
			tvDescription.setText(this.adBannerItemlist.get(0).getDesc());
			pointGroup.getChildAt(0).setEnabled(true);
			viewPager.setCurrentItem(0);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		requestAdsFromServer();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onStart() {
		super.onStart(); 
	}

	@Override
	public void onDestroy() {
		System.exit(0);
		super.onDestroy();
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		TextView tvDescription = (TextView) contextView
				.findViewById(R.id.tv_description);
		LinearLayout pointGroup = (LinearLayout) contextView
				.findViewById(R.id.ll_point_group);

		if (this.adBannerItemlist.size() > 0) {
			int idx = position % this.adBannerItemlist.size();
			tvDescription.setText(this.adBannerItemlist.get(idx).getDesc());

			pointGroup.getChildAt(preEnablePosition).setEnabled(false);
			pointGroup.getChildAt(idx).setEnabled(true);
			preEnablePosition = idx;
		}
	}
	
	public class AdBannerViewPagerAdapter extends PagerAdapter {
		private List<ImageView> mListViews;

		public AdBannerViewPagerAdapter(List<ImageView> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			int idx = position % this.mListViews.size();
			container.removeView(mListViews.get(idx));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int idx = position % this.mListViews.size();
			ImageView view = mListViews.get(idx);
			view.setScaleType(ScaleType.CENTER_CROP);
			container.removeView(view);
			container.addView(view, new LinearLayout.LayoutParams(container.getWidth(), container.getHeight()));
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							AdvertiseActivity.class);
					startActivity(intent);
				}
			});
			return view;
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
