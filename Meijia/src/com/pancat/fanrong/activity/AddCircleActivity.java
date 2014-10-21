package com.pancat.fanrong.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.pancat.fanrong.R;

public class AddCircleActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_circle);
	}

	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_add_location:
			break;
		default:
			break;
		}
	}
}
