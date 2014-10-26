
package com.pancat.fanrong.activity;

import java.util.HashMap;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.fragment.ProductAuthorFragment;
import com.pancat.fanrong.fragment.ProductViewFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NailTechnicianDetailActivity extends FragmentActivity implements FragmentCallback{

	private ImageView backImage = null;
	private TextView titleAuthor = null;
	private TextView authorShare = null;
	private TextView authorCollection = null;
	private TextView authorHonor = null;
	private ProductAuthorFragment productAuthorFragment = null;
	private ProductViewFragment productViewFragment = null;
	
	//三类按钮
	private LinearLayout btnShare = null;
	private LinearLayout btnCollect = null;
	private LinearLayout btnHonor = null;
	
	//三类标签
	private TextView labelShare = null;
	private TextView labelCollect = null;
	private TextView labelHonor = null;
	
	private Product product = null;
	private int red = 0;
	private int black = 0;
	
	//当前标签
	private int curIndex = -1;
	
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTranscation;
	
	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		setContentView(R.layout.activity_nail_technician_detail);
		
		product = (Product)getIntent().getExtras().getSerializable(Product.KEY);
		initView();
	}
	
	private void initView(){
		backImage = (ImageView)findViewById(R.id.activity_nail_technicial_detail_back);
		titleAuthor = (TextView)findViewById(R.id.activity_nail_technicial_detail_title);
		authorShare = (TextView)findViewById(R.id.activity_nail_technicial_detail_share);
		authorCollection = (TextView)findViewById(R.id.activity_nail_technicial_detail_collect);
		authorHonor = (TextView)findViewById(R.id.activity_nail_technicial_detail_honor);
		
		btnShare = (LinearLayout)findViewById(R.id.activity_nail_technicial_detail_share_btn);
		btnCollect = (LinearLayout)findViewById(R.id.activity_nail_technicial_detail_collect_btn);
		btnHonor = (LinearLayout)findViewById(R.id.activity_nail_technicial_detail_honor_btn);
		
		labelShare = (TextView)findViewById(R.id.activity_nail_technicial_detail_share_label);
		labelCollect = (TextView)findViewById(R.id.activity_nail_technicial_detail_collect_label);
		labelHonor = (TextView)findViewById(R.id.activity_nail_technicial_detail_honor_label);
		
		red = getResources().getColor(R.color.red);
		black = getResources().getColor(R.color.blue);
		
		titleAuthor.setText(product.getProductAuthor());
		setIndex(0);
		
		btnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIndex(0);
			}
		});
		
		btnCollect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIndex(1);
				removeProductFragment();
			}
		});
		
		btnHonor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIndex(2);
				removeProductFragment();
			}
		});
		
		productAuthorFragment = ProductAuthorFragment.newInstance(product);
		productAuthorFragment.setForbitClick();
		//Map<String,Object>pageParams = new HashMap<String,Object>();
		
		//pageParams.put(P, arg1)
		productViewFragment = ProductViewFragment.newInstance(null);
		
		fragmentManager = getSupportFragmentManager();
		fragmentTranscation = fragmentManager.beginTransaction();
		
		removeAllFragment();
		
		fragmentTranscation.add(R.id.activity_nail_technicial_detail_author, productAuthorFragment, "A");
		fragmentTranscation.add(R.id.activity_nail_technicial_detail_fromauthor, productViewFragment,"B");
		
		fragmentTranscation.commit();
	}

	//设置颜色
	private void setIndex(int index){
		TextView []a = new TextView[]{authorShare,authorCollection,authorHonor};
		TextView []b = new TextView[]{labelShare,labelCollect,labelHonor};
		
		if(curIndex != -1){
			a[curIndex].setTextColor(black);
			b[curIndex].setTextColor(black);
		}
		if(index != curIndex && index <3 && index >= 0){
			curIndex = index;
			a[index].setTextColor(red);
			b[index].setTextColor(red);
		}
		
	}
	
	private void removeAllFragment(){
		if(fragmentManager == null){
			fragmentManager = getSupportFragmentManager();
			fragmentTranscation = fragmentManager.beginTransaction();
		}
		
		if(fragmentManager.findFragmentByTag("A")!=null){
			fragmentTranscation.remove(fragmentManager.findFragmentByTag("A"));
		}
		if(fragmentManager.findFragmentByTag("B")!=null){
			fragmentTranscation.remove(fragmentManager.findFragmentByTag("B"));
		}
	}
	private void removeProductFragment(){
		if(fragmentManager == null){
			fragmentManager = getSupportFragmentManager();
			fragmentTranscation = fragmentManager.beginTransaction();
		}
		
		if(fragmentManager.findFragmentByTag("B")!=null){
			fragmentTranscation.remove(fragmentManager.findFragmentByTag("B"));
		}
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
=======
package com.pancat.fanrong.activity;

import java.util.HashMap;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.fragment.ProductAuthorFragment;
import com.pancat.fanrong.fragment.ProductViewFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NailTechnicianDetailActivity extends FragmentActivity implements FragmentCallback{

	private ImageView backImage = null;
	private TextView titleAuthor = null;
	private TextView authorShare = null;
	private TextView authorCollection = null;
	private TextView authorHonor = null;
	private ProductAuthorFragment productAuthorFragment = null;
	private ProductViewFragment productViewFragment = null;
	
	//三类按钮
	private LinearLayout btnShare = null;
	private LinearLayout btnCollect = null;
	private LinearLayout btnHonor = null;
	
	//三类标签
	private TextView labelShare = null;
	private TextView labelCollect = null;
	private TextView labelHonor = null;
	
	private Product product = null;
	private int red = 0;
	private int black = 0;
	
	//当前标签
	private int curIndex = -1;
	
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTranscation;
	
	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		setContentView(R.layout.activity_nail_technician_detail);
		
		product = (Product)getIntent().getExtras().getSerializable(Product.KEY);
		initView();
	}
	
	private void initView(){
		backImage = (ImageView)findViewById(R.id.activity_nail_technicial_detail_back);
		titleAuthor = (TextView)findViewById(R.id.activity_nail_technicial_detail_title);
		authorShare = (TextView)findViewById(R.id.activity_nail_technicial_detail_share);
		authorCollection = (TextView)findViewById(R.id.activity_nail_technicial_detail_collect);
		authorHonor = (TextView)findViewById(R.id.activity_nail_technicial_detail_honor);
		
		btnShare = (LinearLayout)findViewById(R.id.activity_nail_technicial_detail_share_btn);
		btnCollect = (LinearLayout)findViewById(R.id.activity_nail_technicial_detail_collect_btn);
		btnHonor = (LinearLayout)findViewById(R.id.activity_nail_technicial_detail_honor_btn);
		
		labelShare = (TextView)findViewById(R.id.activity_nail_technicial_detail_share_label);
		labelCollect = (TextView)findViewById(R.id.activity_nail_technicial_detail_collect_label);
		labelHonor = (TextView)findViewById(R.id.activity_nail_technicial_detail_honor_label);
		
		red = getResources().getColor(R.color.red);
		black = getResources().getColor(R.color.blue);
		
		titleAuthor.setText(product.getProductAuthor());
		setIndex(0);
		
		btnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIndex(0);
			}
		});
		
		btnCollect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIndex(1);
				removeProductFragment();
			}
		});
		
		btnHonor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIndex(2);
				removeProductFragment();
			}
		});
		
		productAuthorFragment = ProductAuthorFragment.newInstance(product);
		productAuthorFragment.setForbitClick();
		//Map<String,Object>pageParams = new HashMap<String,Object>();
		
		//pageParams.put(P, arg1)
		productViewFragment = ProductViewFragment.newInstance(null);
		
		fragmentManager = getSupportFragmentManager();
		fragmentTranscation = fragmentManager.beginTransaction();
		
		removeAllFragment();
		
		fragmentTranscation.add(R.id.activity_nail_technicial_detail_author, productAuthorFragment, "A");
		fragmentTranscation.add(R.id.activity_nail_technicial_detail_fromauthor, productViewFragment,"B");
		
		fragmentTranscation.commit();
	}

	//设置颜色
	private void setIndex(int index){
		TextView []a = new TextView[]{authorShare,authorCollection,authorHonor};
		TextView []b = new TextView[]{labelShare,labelCollect,labelHonor};
		
		if(curIndex != -1){
			a[curIndex].setTextColor(black);
			b[curIndex].setTextColor(black);
		}
		if(index != curIndex && index <3 && index >= 0){
			curIndex = index;
			a[index].setTextColor(red);
			b[index].setTextColor(red);
		}
		
	}
	
	private void removeAllFragment(){
		if(fragmentManager == null){
			fragmentManager = getSupportFragmentManager();
			fragmentTranscation = fragmentManager.beginTransaction();
		}
		
		if(fragmentManager.findFragmentByTag("A")!=null){
			fragmentTranscation.remove(fragmentManager.findFragmentByTag("A"));
		}
		if(fragmentManager.findFragmentByTag("B")!=null){
			fragmentTranscation.remove(fragmentManager.findFragmentByTag("B"));
		}
	}
	private void removeProductFragment(){
		if(fragmentManager == null){
			fragmentManager = getSupportFragmentManager();
			fragmentTranscation = fragmentManager.beginTransaction();
		}
		
		if(fragmentManager.findFragmentByTag("B")!=null){
			fragmentTranscation.remove(fragmentManager.findFragmentByTag("B"));
		}
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

