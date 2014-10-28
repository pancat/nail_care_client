package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.pancat.fanrong.R;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;
import com.pancat.fanrong.waterfall.widget.ScaleImageView;

public class CirclePhotoActivity extends Activity{

	private ViewPager viewPager;
	private CirclePhotoAdapter circlePhotoAdapter;
	private List<ScaleImageView> images = new ArrayList<ScaleImageView>();
	private int imgCount = 0;
	private int circleId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_photo);
		viewPager = (ViewPager)findViewById(R.id.circle_photo_viewpager);
		circlePhotoAdapter = new CirclePhotoAdapter();
		viewPager.setAdapter(circlePhotoAdapter);
		//获取从上一个Activity中传来的圈子id
		circleId = getIntent().getIntExtra("circleId", 0);
		requestData();
	}

	private void requestData(){
		String url = "http://54.213.141.22/teaching/Platform/index.php/circle_service/get_circle_img_list";
		RequestParams params = new RequestParams();
		params.put("circle_id", String.valueOf(circleId));
		RestClient.getInstance().postFromAbsoluteUrl(this, url, params, 
				new AsyncHttpResponseHandler(){

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONArray jsonArray = new JSONArray(content);
							for(int i = 0;i < jsonArray.length();i++){
								ScaleImageView imageView = new ScaleImageView(CirclePhotoActivity.this);
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								Log.i("img info", jsonObject.toString());
								String path = jsonObject.getString("path");
								imageView.setScaleType(ScaleType.FIT_CENTER);
								ImageFetcher fetcher = new ImageFetcher(CirclePhotoActivity.this, 720);
								fetcher.loadImage(path, imageView);
								images.add(imageView);
							}
							circlePhotoAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}
		});
	}
	
	class CirclePhotoAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(images.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(images.get(position),0);
			return images.get(position);
		}
		
		
	}
}
