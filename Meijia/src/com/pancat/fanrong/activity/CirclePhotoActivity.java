package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
	private int circleId;
	//当前图片号
	private TextView mCurImgNum;
	//图片总数
	private TextView mImgCount;
	
	DisplayImageOptions options = new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.build();
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				mCurImgNum.setText(String.valueOf(msg.obj));
				break;
			default:
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_photo);
		RelativeLayout bottomBg = (RelativeLayout)findViewById(R.id.bottom_bg);
		bottomBg.getBackground().setAlpha(50);
		mCurImgNum = (TextView)findViewById(R.id.cur_img_num);
		mImgCount = (TextView)findViewById(R.id.img_count);
		viewPager = (ViewPager)findViewById(R.id.circle_photo_viewpager);
		circlePhotoAdapter = new CirclePhotoAdapter();
		viewPager.setAdapter(circlePhotoAdapter);
		//获取从上一个Activity中传来的圈子id
		circleId = getIntent().getIntExtra("circleId", 0);
		int imgCount = getIntent().getIntExtra("imgCount", 0);
		mImgCount.setText(" / "+imgCount);
		viewPager.setOnPageChangeListener(onPageChangeListener);
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
								ImageLoader.getInstance().displayImage(path, imageView);
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
	
	OnPageChangeListener onPageChangeListener = new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		//页面跳转完成后调用，arg0当前页面的索引号
		@Override
		public void onPageSelected(int arg0) {
			Message msg = new Message();
			msg.obj = arg0 + 1;
			msg.what = 1;
			handler.sendMessage(msg);
		}
		
	};
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}
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
