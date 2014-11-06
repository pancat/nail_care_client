package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class CropImageActivity extends Activity {
	//接受传入的uri，通过uri显示获取图片，显示图片，对现实的图片切割，然后再返回去
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent it=getIntent();
	//	it.getExtras();
		setContentView(R.layout.activity_cropimage);
		Uri imageUri=it.getData();
		Log.i("imageuri",imageUri.toString());
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}


	
	
}
