package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.pancat.fanrong.R;
import com.pancat.fanrong.fragment.CommonTabSearchComponent;
import com.pancat.fanrong.fragment.CommonTabSearchComponent.OnClickSearchTabListener;
import com.pancat.fanrong.fragment.ProductSearchFragment;
import com.pancat.fanrong.fragment.ProductViewFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

public class ProductSearchActivity extends ActionBarActivity implements OnClickSearchTabListener{
    
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private static final String[] TAGLABEL ={"SEARCHTAG","STYLE1","STYLE2"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_view);
		
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction= fragmentManager.beginTransaction();
		
		for(int i=0; i< TAGLABEL.length; i++)
			if(fragmentManager.findFragmentByTag(TAGLABEL[i]) != null)
				fragmentTransaction.remove(fragmentManager.findFragmentByTag(TAGLABEL[i]));
		
		ProductSearchFragment productSearchFragment = new ProductSearchFragment();
		fragmentTransaction.add(R.id.search_view_container, productSearchFragment,TAGLABEL[0]);
		String[] ar = {"甜美","渐变","法式","彩绘","创意","纯色","糖果","日韩","新娘"};
		ArrayList<String> al = new ArrayList<String>();
		for(String e:ar){
			al.add(e);
		}
		
		CommonTabSearchComponent a = CommonTabSearchComponent.newInstance(al, "样式");
		fragmentTransaction.add(R.id.search_view_container, a,TAGLABEL[1]);
		CommonTabSearchComponent b = CommonTabSearchComponent.newInstance(al, "样式");
		fragmentTransaction.add(R.id.search_view_container, b,TAGLABEL[2]);
		
		fragmentTransaction.commit();
	}

	@Override
	public void setOnClickSearchTabListener(String label, String v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,SearchProductShowFragmentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(ProductViewFragment.SELECT_KEYWORDS, v);
		intent.putExtras(bundle);
		startActivity(intent);
	}
   
}
