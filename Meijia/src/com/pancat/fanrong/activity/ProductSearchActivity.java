package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.bean.SearchLabelAndTab;
import com.pancat.fanrong.common.FilterQueryAndParse;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.fragment.CommonTabSearchComponent;
import com.pancat.fanrong.fragment.CommonTabSearchComponent.OnClickSearchTabListener;
import com.pancat.fanrong.fragment.ProductSearchFragment;
import com.pancat.fanrong.fragment.ProductViewFragment;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.util.PhoneUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class ProductSearchActivity extends ActionBarActivity implements OnClickSearchTabListener{
    public static final String TAG = "ProductSearchActivity";
    
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private static final String[] TAGLABEL ={"SEARCHTAG","STYLE1","STYLE2"};
	private String productType = Product.MEIJIA;
	ProductSearchFragment productSearchFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_view);
		
		productType = getIntent().getStringExtra(Product.TYPE);
		productType = (productType == null)? Product.MEIJIA:productType;
		
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction= fragmentManager.beginTransaction();
		
		for(int i=0; i< TAGLABEL.length; i++)
			if(fragmentManager.findFragmentByTag(TAGLABEL[i]) != null)
				fragmentTransaction.remove(fragmentManager.findFragmentByTag(TAGLABEL[i]));
		
		productSearchFragment = ProductSearchFragment.newInstance(productType);//new ProductSearchFragment();
		fragmentTransaction.add(R.id.search_view_container, productSearchFragment,TAGLABEL[0]);
		
		String[] ar = {"甜美","渐变","法式","彩绘","创意","纯色","糖果","日韩","新娘"};
		ArrayList<SearchLabelAndTab> al = new ArrayList<SearchLabelAndTab>();
		for(int i=0; i<ar.length; i++){
			al.add(new SearchLabelAndTab("1", ar[i], "样式"));
		}
		
		CommonTabSearchComponent a = CommonTabSearchComponent.newInstance(al, "样式");
		fragmentTransaction.add(R.id.search_view_container, a,TAGLABEL[1]);
		
		//CommonTabSearchComponent b = CommonTabSearchComponent.newInstance(al, "样式");
		//fragmentTransaction.add(R.id.search_view_container, b,TAGLABEL[2]);
		
		fragmentTransaction.commit();
		
		loadData();
	}

	private void loadData(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(FilterQueryAndParse.Q_QUERY_TYPE, FilterQueryAndParse.QT_6);
		//map.put(FilterQueryAndParse., arg1)
		Map<String,String> tmap = FilterQueryAndParse.FilterAndRepairDefault(map);
		String url = FilterQueryAndParse.getRelativeURL(map);
		
		if(PhoneUtils.isNetworkConnected(this)){
			RequestParams requestParams = new RequestParams(tmap);
			Log.d(TAG, tmap.toString()+"**************"+url);
			RestClient.getInstance().get(url, requestParams, responseHandler);
		}else{
			Log.d(TAG, "network isn't connected");
		}
	}
	
	private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(String content) {
			ArrayList<SearchLabelAndTab> res = null;
			Log.d(TAG, content+"cccc");
			//TODO 以后在这提取返回的状态码执行进一步动作
			try{
				res  = FilterQueryAndParse.ParseToSearchLabelAndTab(content);
			}catch(Exception e)
			{
				e.printStackTrace();
				return ;
			}
			addToView(res);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO 自动生成的方法存根
			super.onFailure(error, content);
			Log.d(TAG, content+"*******"+error);
		}
		
	};
	
	private void addToView(ArrayList<SearchLabelAndTab> res){
		fragmentTransaction= fragmentManager.beginTransaction();
		CommonTabSearchComponent b = CommonTabSearchComponent.newInstance(res, "hot");
		fragmentTransaction.add(R.id.search_view_container, b,TAGLABEL[2]);
		fragmentTransaction.commit();
	}
	
	@Override
	public void setOnClickSearchTabListener(String label, SearchLabelAndTab v,boolean flag) {
		// TODO Auto-generated method stub
		/*Intent intent = new Intent(this,SearchProductShowFragmentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(FilterQueryAndParse.Q_SELECT_KEYWORDS, v);
		intent.putExtras(bundle);
		startActivity(intent);*/
		
		if(productSearchFragment != null){
			if(flag) productSearchFragment.add(v);
			else productSearchFragment.del(v);
		}
	}
   
}
