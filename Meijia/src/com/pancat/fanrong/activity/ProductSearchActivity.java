package com.pancat.fanrong.activity;

import com.pancat.fanrong.R;
import com.pancat.fanrong.fragment.ProductSearchFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

public class ProductSearchActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_view);
		
		FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
		ProductSearchFragment productSearchFragment = new ProductSearchFragment();
		fragmentTransaction.add(R.id.search_view_container, productSearchFragment);
		fragmentTransaction.commit();
	}
	
}
