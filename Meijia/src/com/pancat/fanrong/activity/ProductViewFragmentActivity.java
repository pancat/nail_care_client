package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.List;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
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
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ProductViewFragmentActivity extends ActionBarActivity implements OnProductTabClickListenser {

	//debug
	private static final String TAG = "ProductViewFragment";

	//static title
	private static final String MEIJIA = "美甲";
	private static final String MEIZHUANG = "美妆";
    
	private ProductTabFragment productTabFragment;
    private ActionBar actionBar ;
    
    private MenuItem searchButton;
	@Override
	public void setOnProductTabClickListenser(String tab) {
		// TODO 自动生成的方法存根

	}
	private static ViewPager viewpage;
	private TextView hotview,newview;
	private List<Fragment> lf;
	private FragmentPagerAdapter fpa;
	//private ProductViewpager productViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_fragmentactivity);
        
		//hotview = (TextView)findViewById(R.id.product_hot_tab);
		//newview = (TextView)findViewById(R.id.product_new_tab);
		viewpage = (ViewPager)findViewById(R.id.product_viewpage);
		// productViewPager = new ProductViewpager(viewpage, this);
           
		//hotview.setOnClickListener(new myOnClickListener(0));
		//newview.setOnClickListener(new myOnClickListener(1));

		Fragment a = ProductViewFragment.newInstance(null);
		Fragment b = ProductViewFragment.newInstance(null);

		lf = new ArrayList<Fragment>();
		lf.add(a);
		lf.add(b);

		fpa = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO 自动生成的方法存根
				return 2;
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO 自动生成的方法存根
				return lf.get(arg0);
			}
		};

		viewpage.setAdapter(fpa);
		viewpage.setCurrentItem(0);
		viewpage.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO 自动生成的方法存根
				if(arg0 == 0){
					viewpage.setCurrentItem(0);
				}
				else viewpage.setCurrentItem(1);
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
      
		InitActionBar();
	}
	
	//设置当前页面标题
	public void setPageTitle(int type)
	{
		if(type == Product.MEIJIA)
			setTitle(MEIJIA);
		else
			setTitle(MEIZHUANG);
	}
    
	//初始化ActionBar
	public void InitActionBar()
	{  
		setPageTitle(getIntent().getIntExtra(Product.TYPE ,Product.MEIJIA));
		actionBar =  getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		//actionBar.hide();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自动生成的方法存根
		//return super.onCreateOptionsMenu(menu);
		
		getMenuInflater().inflate(R.menu.searchtab, menu);
		searchButton = menu.findItem(R.id.searchtab);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自动生成的方法存根
		switch(item.getItemId())
		{
		case R.id.searchtab:
			Intent intent= new Intent(this,ProductSearchActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	public static class myOnClickListener implements View.OnClickListener{
		private int index;
		public myOnClickListener(int i) {
			// TODO 自动生成的构造函数存根
			index = i;
		}
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			viewpage.setCurrentItem(index);
		}

	}
}
