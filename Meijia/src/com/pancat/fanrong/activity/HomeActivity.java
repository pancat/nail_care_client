package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.adapter.GuessLikeAdapter;
import com.pancat.fanrong.bean.CircleComment;
import com.pancat.fanrong.bean.GuessLikeProduct;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.view.ListLinearLayout;
import com.pancat.fanrong.view.ListViewWithLoad;
import com.pancat.fanrong.view.ListViewWithLoad.OnLoadListener;

@SuppressLint("ResourceAsColor")
public class HomeActivity extends Activity implements OnLoadListener{

	protected ScrollView scrollView;
	private ListViewWithLoad homeList;
	private GuessLikeAdapter guessLikeAdapter;
	private List<GuessLikeProduct> productList;
	private int mPageIndex = 0;
	private int mPageSize = 10;
	
	List<GuessLikeProduct> list;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				ArrayList<GuessLikeProduct> products = (ArrayList<GuessLikeProduct>)msg.obj;
				if(products.size() > 0 && products!=null){
					for(int i = 0;i<products.size();i++){
						GuessLikeProduct product = products.get(i);
						productList.add(product);
					}
					guessLikeAdapter.notifyDataSetChanged();
					homeList.setResultSize(products.size());
					homeList.onLoadComplete();
				}
				else{
					homeList.onLoadComplete();
				}
				break;
			default:
				break;
			}
		}
	};
	
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			String url = "http://54.213.141.22/teaching/Platform/index.php/guesslike_service/get_product_list";
			RequestParams params = new RequestParams();
			params.put("index", String.valueOf(mPageIndex));
			params.put("size", String.valueOf(mPageSize));
			RestClient.getInstance().postFromAbsoluteUrl(HomeActivity.this, url, params, new AsyncHttpResponseHandler(){

				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					List<GuessLikeProduct> products = new ArrayList<GuessLikeProduct>();
					try {
						JSONArray jsonArray = new JSONArray(content);
						for(int i = 0;i<jsonArray.length();i++){
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							GuessLikeProduct product = new GuessLikeProduct();
							product.setId(jsonObject.getInt("id"));
							product.setIconPath(jsonObject.getString("iconPath"));
							product.setTitle(jsonObject.getString("title"));
							product.setDescription(jsonObject.getString("description"));
							product.setPrice(Float.valueOf(jsonObject.getString("price")));
							product.setDistance(Float.valueOf(jsonObject.getString("distance")));
							products.add(product);
						}
						mPageIndex+=mPageSize;
						handler.obtainMessage(1, products).sendToTarget();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
				}
				
			});
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		LayoutInflater inflater = LayoutInflater.from(this);
		View headView = inflater.inflate(R.layout.headerview_home, null);
		productList = new ArrayList<GuessLikeProduct>();
		list = new ArrayList<GuessLikeProduct>();
		homeList = (ListViewWithLoad)findViewById(R.id.home_list);
		homeList.addHeaderView(headView);
		homeList.setPageSize(mPageSize);
		guessLikeAdapter = new GuessLikeAdapter(this, productList);
		homeList.setAdapter(guessLikeAdapter);
		homeList.setOnLoadListener(this);
		onLoad();
		
	}

	@Override
	public void onLoad() {
//		handler.sendEmptyMessage(1);
		Thread thread = new Thread(runnable);
		thread.start();
	}
	


}
