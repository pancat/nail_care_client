package com.pancat.fanrong.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;

/**
 * 圈子item的具体内容
 * @author trh
 *
 */
public class MomentItemActivity extends Activity{

	private LinearLayout mImgParent;
	private TextView mImgPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moment_item);
		
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		final Circle info = (Circle)data.getSerializable("duitangInfo");
		final int originalWidth = info.getWidth();
		final int originalHeight = info.getHeight();
		mImgParent = (LinearLayout)findViewById(R.id.img_parent_ll);
		mImgPath = (TextView)findViewById(R.id.img_path);
		//设置ImageView的显示宽度和高度
		ViewTreeObserver observer = mImgParent.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				mImgParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mImgPath.setText(info.getPath());
				int  parentWidth = mImgParent.getWidth();
				//按比例缩放图片
				int iWidth = parentWidth;
				int iHeight = iWidth*originalHeight/originalWidth;
				mImgParent.addView(addImageView(iWidth, iHeight,info.getPath()));
			}
		});
	}

	private final View addImageView(int width,int height,String src){
		LinearLayout view = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		//设置LinearLayout布局参数
		view.setLayoutParams(lp);
		view.setOrientation(LinearLayout.VERTICAL);
		view.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		//定义ImageView的布局
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(width, height);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(vlp);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		ImageFetcher fetcher = new ImageFetcher(this, 720);
		fetcher.loadImage(src, imageView);
		view.addView(imageView);
		return view;
	}
}
