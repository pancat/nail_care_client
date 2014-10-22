package com.pancat.fanrong.activity;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.ImageGridAdapter.TextCallback;
import com.pancat.fanrong.util.album.AlbumHelper;
import com.pancat.fanrong.util.album.ImageItem;

public class ImageGridActivity extends Activity{

	public static final String EXTRA_IMAGE_LIST = "imagelist";
	
	List<ImageItem> dataList;
	
	private GridView gridView;
	private ImageGridAdapter adapter;
	private AlbumHelper helper;
	private Button btnFinish;
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择9张图片", 400).show();
				break;
			default:
				break;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_grid);
		initData();
		initView();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
//		dataList = (List<ImageItem>)getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
		dataList = helper.getAllImage();
	}

	/**
	 * 初始化视图
	 */
	private void initView(){
		btnFinish = (Button)findViewById(R.id.bt);
		gridView = (GridView)findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this,dataList,handler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			
			@Override
			public void onAdd(int count) {
				btnFinish.setText("完成"+"("+count+")");
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_to_album:
			Intent intent = new Intent(ImageGridActivity.this,ImageBucketActivity.class);
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK){
			dataList = (List<ImageItem>)data.getSerializableExtra(EXTRA_IMAGE_LIST);
			adapter = new ImageGridAdapter(this, dataList, handler);
			gridView.setAdapter(adapter);
			adapter.setTextCallback(new TextCallback() {
				
				@Override
				public void onAdd(int count) {
					btnFinish.setText("完成"+"("+count+")");
				}
			});
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					adapter.notifyDataSetChanged();
				}
			});
		}
	}
}
