package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.common.FilterQueryAndParse;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.fragment.ProductTabFragment;
import com.pancat.fanrong.fragment.ProductTabFragment.OnProductTabClickListenser;
import com.pancat.fanrong.fragment.ProductViewFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ProductViewFragmentActivity extends ActionBarActivity implements OnProductTabClickListenser ,FragmentCallback{
	private static final String TAG = "ProductViewFragment";

	//static title
	public static final String MEIJIA = "美甲";
	public static final String MEIZHUANG = "美妆";
    
	//Tab框架
	private ProductTabFragment productTabFragment;
	private FragmentManager fragmentManager = null;
	
    private ActionBar actionBar ;
    
    private MenuItem searchButton;

	private static ViewPager viewpage;
	private List<Fragment> lf;
	private FragmentPagerAdapter fpa;
    
	//产品类型
	private String productType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_fragmentactivity);
        
		viewpage = (ViewPager)findViewById(R.id.product_viewpage);
		fragmentManager = getSupportFragmentManager();
		productTabFragment = (ProductTabFragment)fragmentManager.findFragmentById(R.id.product_tabs_page);
		
		//解析参数
		Map<String,String> map = getMapParam(getIntent().getExtras());
		setPageTitle(map.get(FilterQueryAndParse.Q_PRODUCT_TYPE));
		
		//设置ViewPage适配器
		setFragmentAdapterAndViewPage(map);
		
		InitActionBar();
	}
	
	//设置当前页面标题
	public void setPageTitle(String type)
	{
		if(type.equals(Product.MEIJIA)){
			setTitle(MEIJIA);
			productType = type;
		}else{
			setTitle(MEIZHUANG);
			productType = type;
		}
	}
    
	//初始化ActionBar
	public void InitActionBar()
	{  
		actionBar =  getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.searchtab, menu);
		searchButton = menu.findItem(R.id.searchtab);
		return true;
	}
 
	//搜索按钮点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自动生成的方法存根
		switch(item.getItemId())
		{
		case R.id.searchtab:
			Intent intent= new Intent(this,ProductSearchActivity.class);
			intent.putExtra(Product.TYPE, productType);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	//按Tab按钮的回调函数
	@Override
	public void setOnProductTabClickListenser(int tab,Map<String,Object>map) {
		// TODO 自动生成的方法存根
       if(tab < 2 && tab >= 0){
    	   viewpage.setCurrentItem(tab);
       	   productTabFragment.changeBottomLine(tab);
       }
       else
       {
    	   lf.set(tab, ProductViewFragment.newInstance(map));
    	   viewpage.setCurrentItem(tab);
    	   productTabFragment.changeBottomLine(tab);
       }
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
				if(key.equals(Product.TYPE))
					map.put(FilterQueryAndParse.Q_PRODUCT_TYPE, bundle.getString(key));
				else map.put(key, bundle.getString(key));
			}
		}
		else
		{
			map.put(FilterQueryAndParse.Q_PRODUCT_TYPE, Product.MEIJIA);
		}
		return map;
	}
	
	private void setFragmentAdapterAndViewPage(Map<String,String> map)
	{
		Fragment a = ProductViewFragment.getHotInstance(productType);
		Fragment b = ProductViewFragment.getNewInstance(productType);
		Fragment c = ProductViewFragment.getFilterInstance(null,productType);
		
		lf = new ArrayList<Fragment>();
		lf.add(a);
		lf.add(b);
		lf.add(c);
		
		fpa = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO 自动生成的方法存根
				return lf.size();
			}

			@Override
			public Fragment getItem(int index) {
				// TODO 自动生成的方法存根
				return lf.get(index);
			}
		};

		viewpage.setAdapter(fpa);
		viewpage.setCurrentItem(0);
		//productTabFragment.changeBottomLine(0);
		viewpage.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO 自动生成的方法存根
				viewpage.setCurrentItem(arg0);
				productTabFragment.changeBottomLine(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO 自动生成的方法存根

			}
		});
      
	}
	
	//点击产品视图产生的回调函数，转到详细视图
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


