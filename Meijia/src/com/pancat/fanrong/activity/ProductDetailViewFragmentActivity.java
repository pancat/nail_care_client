package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.fragment.ProductAuthorFragment;
import com.pancat.fanrong.fragment.ProductDetailFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ProductDetailViewFragmentActivity extends FragmentActivity {
    private static final String TAG = "ProductDetailViewFragmentActivity";
    
    private FragmentManager fragmentManager = null;
    private ProductDetailFragment productDetailFragment = null;
    private ProductAuthorFragment productAuthorFragment = null;
    
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO 自动生成的方法存根
		super.onCreate(bundle);
		setContentView(R.layout.product_detail_view);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		productDetailFragment = ProductDetailFragment.newInstance(b.getString(Product.KEY));
		fragmentTransaction.add(R.id.product_detail_view_fragment, productDetailFragment);
		productAuthorFragment = ProductAuthorFragment.newInstance(b.getString(Product.KEY));
		fragmentTransaction.add(R.id.product_detail_view_fragment, productAuthorFragment);
		//fragmentTransaction.addToBackStack(TAG);
		fragmentTransaction.commit();
		
		Log.d(TAG, b.getString(Product.KEY));
	}

}
