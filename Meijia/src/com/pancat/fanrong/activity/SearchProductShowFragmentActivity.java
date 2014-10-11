package com.pancat.fanrong.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.fragment.ProductViewFragment;
import com.pancat.fanrong.fragment.ProductViewFragment.OnClickListItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class SearchProductShowFragmentActivity extends FragmentActivity implements OnClickListItem{
	private static final String TAG = "SearchProductShowFragmentActivity";
	
	private ProductViewFragment productViewFragment;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO 自动生成的方法存根
		super.onCreate(arg0);
		setContentView(R.layout.search_product_fragment_layout);
		
		Map<String,String>map = getMapParam(getIntent().getExtras());
		
		productViewFragment = ProductViewFragment.newInstance(map);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.serarch_product_fragment_activity_layout, productViewFragment);
		fragmentTransaction.commit();
	}

	private Map<String,String> getMapParam(Bundle bundle)
	{
		Map<String,String> map = new HashMap<String, String>();
		if(bundle != null)
		{
			Iterator<String> iter = bundle.keySet().iterator();
			while(iter.hasNext())
			{
				String key = iter.next();
				if(key == Product.TYPE)
					map.put(ProductViewFragment.QUERY_PRODUCT_TYPE, bundle.getString(key));
				else map.put(key, bundle.getString(key));
			}
		}
		else
		{
			map.put(ProductViewFragment.QUERY_PRODUCT_TYPE, Product.MEIJIA);
		}
		return map;
	}

	@Override
	public void setOnClickListItem(Product product) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,ProductDetailViewFragmentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(Product.KEY, product.toString());
		intent.putExtras(bundle);

		startActivity(intent);
	}
}
