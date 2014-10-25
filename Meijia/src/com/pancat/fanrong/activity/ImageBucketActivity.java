package com.pancat.fanrong.activity;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.adapter.ImageBucketAdapter;
import com.pancat.fanrong.util.album.AlbumHelper;
import com.pancat.fanrong.util.album.ImageBucket;

public class ImageBucketActivity extends Activity{

	List<ImageBucket> mDataList;
	GridView mAlbumGridView;
	//数据适配器
	ImageBucketAdapter mAdapter;
	
	AlbumHelper mHelper;
	
	public static Bitmap mBitmap;
	
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);
		initData();
		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		mAlbumGridView = (GridView)findViewById(R.id.album_gridview);
		mAdapter = new ImageBucketAdapter(ImageBucketActivity.this,mDataList);
		mAlbumGridView.setAdapter(mAdapter);
		
		mAlbumGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(ImageBucketActivity.this,ImageGridActivity.class);
				intent.putExtra(ImageBucketActivity.EXTRA_IMAGE_LIST, (Serializable)mDataList.get(position).imageList);
//				startActivity(intent);
				setResult(RESULT_OK,intent);
				finish();
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mHelper = AlbumHelper.getHelper();
		mHelper.init(getApplicationContext());
		mDataList = mHelper.getImagesBucketList(false);
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
	}

	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			finish();
		default:
			break;
		}
	}
	
}
