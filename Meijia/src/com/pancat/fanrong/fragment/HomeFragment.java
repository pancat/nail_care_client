package com.pancat.fanrong.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.j256.ormlite.dao.Dao;
import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.AdvertiseActivity;
import com.pancat.fanrong.activity.ProductViewFragmentActivity;
import com.pancat.fanrong.bean.AdBannerItem;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.db.DatabaseManager;
import com.pancat.fanrong.db.DatabaseOpenHelper;
import com.pancat.fanrong.handler.HandlerFactory;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import org.json.*;

@SuppressLint("NewApi")
public class HomeFragment extends Fragment implements OnPageChangeListener {
	private static final String TAG = "state";
	private View contextView;
	private List<ImageView> imageList;

	private Timer adBannerPlayTimer = null;
	private List<AdBannerItem> adBannerItemlist;
    
	//美甲作品及美妆作品按钮
	private ImageButton meijia;
	private ImageButton meizhuang;
	
	// 前一个被选中的点的索引位置默认设置为0
	private int preEnablePosition = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contextView = inflater
				.inflate(R.layout.fragment_home, container, false);
		
		requestAdsFromServer();
		initLeftButtonArr();
		
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

		@Override
		public void onSuccess(String content) {
			DatabaseOpenHelper helper = DatabaseManager.getHelper(getActivity()
					.getApplicationContext());
			Dao<AdBannerItem, Integer> dao = helper.getAdBannerItemDao();
			
			try {
				List<AdBannerItem> list = dao.queryForAll();
				dao.delete(list);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				JSONArray jsonArray = new JSONArray(content);
				for (int i = 0; i < jsonArray.length(); ++i)
				{
					JSONObject itemObj = jsonArray.getJSONObject(i);
					try {
						int productID = itemObj.getInt("p_id");
						String imgUrl = itemObj.getString("image_uri");
						String desc = itemObj.getString("p_describe");
						Log.d("item ", productID + " " + imgUrl + " " + desc );
						if (imgUrl != "null")
						{
							AdBannerItem item = new AdBannerItem();
							item.setProductID(productID);
							item.setImgUrl(imgUrl);
							item.setDesc(desc);
							try {
								dao.create(item);
							} catch (SQLException e) {
							//	e.printStackTrace();
							}
						}
					}catch (Exception e)
					{
					//	e.printStackTrace();
					}
				}
				
				// update from db
				setViewPagerFromDB();
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	};

	private void requestAdsFromServer() {
		String url = "product/get_product_list";
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
			// TODO Auto-generated catch block
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
		Log.e(TAG, "pause");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.e(TAG, "stop");
		super.onStop();
	}

	@Override
	public void onStart() {
		Log.e(TAG, "start");
		super.onStart(); 

		requestAdsFromServer();
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "destroy");
		System.exit(0);
		super.onDestroy();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

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
    
	//author Jogrunner
	private void initLeftButtonArr()
	{
		meijia = (ImageButton)contextView.findViewById(R.id.btn_meijia_works);
		meizhuang = (ImageButton)contextView.findViewById(R.id.btn_meizhuang_works);
		meijia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getActivity(),ProductViewFragmentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(Product.TYPE, Product.MEIJIA);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		meizhuang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getActivity(),ProductViewFragmentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(Product.TYPE, Product.MEIZHUANGE);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
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
			container.removeView(view);
			container.addView(view);

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
