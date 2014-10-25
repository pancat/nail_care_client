package com.pancat.fanrong.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.common.FilterQueryAndParse;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.fragment.ProductViewFragment;
import com.pancat.fanrong.util.DataTransfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class SearchProductShowFragmentActivity extends FragmentActivity implements FragmentCallback{
	private static final String TAG = "SearchProductShowFragmentActivity";
	
	private ProductViewFragment productViewFragment;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO 自动生成的方法存根
		super.onCreate(arg0);
		setContentView(R.layout.search_product_fragment_layout);
		
		Map<String,Object>map = DataTransfer.getMap(getIntent().getExtras());
		
		Log.d(TAG, map.toString());
		
		productViewFragment = ProductViewFragment.newInstance(map);
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.serarch_product_fragment_activity_layout, productViewFragment);
		fragmentTransaction.commit();
	}

	@Override
	public void callback(Bundle bundle) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,ProductDetailViewFragmentActivity.class);
		intent.putExtras(bundle);

		startActivity(intent);
	}

	@Override
	public void finishActivity() {
		// TODO Auto-generated method stub
		
	}
}
