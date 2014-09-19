package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MeActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me);
	}
	
//	public void onClick(View v){
//		Intent intent = null;
//		switch(v.getId()){
//		case R.id.btn_home:
//			intent = new Intent(this,HomeActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.btn_order:
//			intent = new Intent(this,OrderActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.btn_me:
//			
//			break;
//		case R.id.btn_service:
//			intent = new Intent(this,ServiceActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		default:break;
//		}
//	}

}
