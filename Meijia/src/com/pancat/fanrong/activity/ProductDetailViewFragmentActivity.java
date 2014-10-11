package com.pancat.fanrong.activity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.fragment.ProductAuthorFragment;
import com.pancat.fanrong.fragment.ProductDetailFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ProductDetailViewFragmentActivity extends FragmentActivity {
    private static final String TAG = "ProductDetailViewFragmentActivity";
    
    private static final String DETAILTAG = "detail";
    private static final String AUTHORTAG = "author";
    
    private FragmentManager fragmentManager = null;

    private ProductDetailFragment productDetailFragment = null;
    private ProductAuthorFragment productAuthorFragment = null;
    private FragmentTransaction fragmentTransaction = null;
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO 自动生成的方法存根
		super.onCreate(bundle);
		setContentView(R.layout.product_detail_view);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		
		if(fragmentTransaction == null ){
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
		}
		
		//repair bug
        if(fragmentManager.findFragmentByTag(DETAILTAG)!=null)
        	fragmentTransaction.remove(fragmentManager.findFragmentByTag(DETAILTAG));
	    if(fragmentManager.findFragmentByTag(AUTHORTAG) != null)
	    	fragmentTransaction.remove(fragmentManager.findFragmentByTag(AUTHORTAG));
		
        
		productDetailFragment = ProductDetailFragment.newInstance(b.getString(Product.KEY));
		fragmentTransaction.add(R.id.product_detail_view_fragment, productDetailFragment,DETAILTAG);
		
		productAuthorFragment = ProductAuthorFragment.newInstance(b.getString(Product.KEY));
		fragmentTransaction.add(R.id.product_detail_view_fragment, productAuthorFragment,AUTHORTAG);

	
		//fragmentTransaction.addToBackStack(TAG);
		fragmentTransaction.commit();
		
		Log.d(TAG, b.getString(Product.KEY));
	}

}
