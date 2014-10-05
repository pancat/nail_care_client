package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ProductDetailViewFragmentActivity extends FragmentActivity {
    private static final String TAG = "ProductDetailViewFragmentActivity";
    
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO 自动生成的方法存根
		super.onCreate(bundle);
		setContentView(R.layout.product_detail_view);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		Log.d(TAG, b.getString(Product.KEY));
	}

}
