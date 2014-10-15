package com.pancat.fanrong.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.ProductDetailViewFragmentActivity;
import com.pancat.fanrong.activity.ProductViewFragmentActivity;
import com.pancat.fanrong.adapter.ProductInfoAdapter;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.fragment.MomentFragment.ContentTask;
import com.pancat.fanrong.fragment.MomentFragment.MyAdapter;
import com.pancat.fanrong.gridview.StaggeredGridView;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.temp.SampleData;
import com.pancat.fanrong.util.PhoneUtils;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;
import com.pancat.fanrong.waterfall.view.XListView;
import com.pancat.fanrong.waterfall.view.XListView.IXListViewListener;

public class ProductViewFragment extends Fragment implements
IXListViewListener{
	private static final String TAG = "ProductViewFragment";
	
	/*public static final String QUERY_TYPE = "querytype";
	public static final String KEYWORD = "keyword";
	public static final String QUERY_NUM = "querynum";
	public static final String QUERY_ID = "queryid";
	public static final String QUERY_TOTAL_NUM = "querytotalnum";
	public static final String SORT_ORDER_KEY = "sortorderkey";
	public static final String SORT_ORDER = "sortorder";
	public static final String SELECT_LEFT = "selectleft";
	public static final String SELECT_RIGHT = "selectright";
	public static final String SELECT_LIKE = "selectlike";
	public static final String SELECT_KEYWORDS = "selectkeywords";
	public static final String QUERY_PRODUCT_TYPE = "query_product_type";
	public static final String LAST_QUERY_ID = "last_query_id";
	public static final String AUTHOR = "author";
	public static final String STYLE = "style";
	public static final String COLOR = "color";
	public static final String HOTSEARCH = "hotsearch"; */
	
	public static final String QUERY_TYPE = "querytype";
	public static final String KEYWORD = "keyword";
	public static final String QUERY_NUM = "limit";
	public static final String QUERY_ID = "product_id";
	public static final String QUERY_TOTAL_NUM = "offset";
	public static final String SORT_ORDER_KEY = "order";
	public static final String SORT_ORDER = "desc";
	public static final String SELECT_LEFT = "selectleft";
	public static final String SELECT_RIGHT = "selectright";
	public static final String SELECT_LIKE = "selectlike";
	public static final String SELECT_KEYWORDS = "selectkeywords";
	public static final String QUERY_PRODUCT_TYPE = "query_product_type";
	public static final String LAST_QUERY_ID = "last_query_id";
	public static final String AUTHOR = "author";
	public static final String STYLE = "style";
	public static final String COLOR = "color";
	public static final String HOTSEARCH = "hotsearch";
	
	public static final String SP_ID = "p_id";
	public static final String SP_name = "name";
	public static final String SP_describe = "p_describe";
	public static final String SP_credate = "cre_date";
	public static final String SP_hit = "hit";
	public static final String SP_image_uri = "image_uri";
	public static final String SP_m_name = "m_name";
	
	public static final int Server_Query_num = 10;
	public static final int DATA_WAIT = 0;
	public static final int DATA_END = 1;
	public static final int DATA_MORE = 2;
	
	public static enum STATE{REFRESH,LOAD,IDLE};
	
	private static final String url = "product/get_product_list";
	
	private View contextView;
	private ImageFetcher mImageFetcher;
	private XListView mAdapterView = null;
	
	private int mHasRequestedMore = DATA_WAIT;//是否加载更多
	private ProductInfoAdapter mAdapter;
    private Map<String,String> pageParam;//主导布局的参数
    
	private ArrayList<Product> mData; //数据
    private OnClickListItem onClickListItem;
    
    private STATE state = STATE.IDLE;
	//新建一个实例
	public static ProductViewFragment newInstance(Map<String,String> pageParams)
	{
		ProductViewFragment instance = new ProductViewFragment();
		if(pageParams != null){
			Bundle bundle = new Bundle();
		    Iterator<String> iter = pageParams.keySet().iterator();
		    while(iter.hasNext())
		    {
		    	String key = iter.next();
		    	bundle.putString(key, pageParams.get(key));
		    }
		    instance.setArguments(bundle);
		}
		return instance;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		Bundle bundle = getArguments();
		pageParam = new HashMap<String,String>();
		if(bundle != null)
		{
			Iterator<String>  iter = bundle.keySet().iterator();
			while(iter.hasNext())
			{
				String key = iter.next();
				pageParam.put(key, bundle.getString(key));
			}
		}
		InitAllParams();
		QueryProductDataFromServer();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contextView = inflater.inflate(R.layout.act_pull_to_refresh_sample, container, false);
		mAdapterView = (XListView)contextView.findViewById(R.id.list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		
		mImageFetcher = new ImageFetcher(getActivity(), 240);
		mImageFetcher.setLoadingImage(R.drawable.defaultproduct);
		
		return contextView;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState == null) {
			final LayoutInflater layoutInflater = getActivity().getLayoutInflater();

		}

		if (mAdapter == null) {
			mAdapter = new ProductInfoAdapter(getActivity(), R.id.product_img,mImageFetcher,onClickListItem);
		}

		if (mData == null) {
			QueryProductDataFromServer();
		}
		
		mAdapterView.setAdapter(mAdapter);
	}

    
	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		onClickListItem = (OnClickListItem)activity;
	}


	private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(String content) {
			// TODO 自动生成的方法存根
			Log.d(TAG, "success---"+content);
			ArrayList<Product> res = null;
			try{
				res  = ParseToProductArr(content);
			}catch(Exception e)
			{
				e.printStackTrace();
				return ;
			}
			
		   notifyChangeParam(res);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO 自动生成的方法存根
			super.onFailure(error, content);
		}
		
	};
	private void notifyChangeParam(List<Product> product)
	{
		if(product != null)
		{
			for(Product p:product)
			{
				if(state == STATE.REFRESH)
					mAdapter.addProductToHead(p);
				else
					mAdapter.addProductToTail(p);
			}
			
			int num = product.size();
			try{
				String value = pageParam.get(QUERY_TOTAL_NUM);
				int v = ((value == null) || (value == "0") )? num:Integer.parseInt(value)+num;
				pageParam.put(QUERY_TOTAL_NUM, String.valueOf(v));

				//
				value = pageParam.get(QUERY_NUM);
				v = (value == null)?Server_Query_num:Integer.parseInt(value);
				if(num < v) mHasRequestedMore = DATA_END;
				else mHasRequestedMore = DATA_WAIT;
				
			}catch(Exception e){
				state = STATE.IDLE;
				e.printStackTrace();
			}
				
		}
		else
		{
			
		}
		state = STATE.IDLE;
	}
	private ArrayList<Product> ParseToProductArr(String content) throws Exception
	{
		ArrayList<Product> product = new ArrayList<Product>();
		
		try{
			JSONArray jsonArray = new JSONArray(content);
			for(int i=0; i<jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Map<String,String> map = new HashMap<String,String>();
				map.put(Product.ID, String.valueOf(jsonObject.getInt(SP_ID)));
				map.put(Product.TITLE, jsonObject.getString(SP_name));
				map.put(Product.DESCRIPTION, jsonObject.getString(SP_describe));
				map.put(Product.DATE, jsonObject.getString(SP_credate));
				map.put(Product.URL, jsonObject.getString(SP_image_uri));
				map.put(Product.AUTHOR, jsonObject.getString(SP_m_name));
				Product tmp = new Product(map);
				product.add(tmp);
			}
		}catch(Exception e)
		{
			throw e;
		}
		return product;
	}
	
	private void QueryProductDataFromServer()
	{
		RequestParams requestParams = new RequestParams(pageParam);
		RestClient.getInstance().get(url, requestParams, responseHandler);	
	}
	//如果参数不存在，设置为默认值
	private void setDefaultQueyParam(String key,String defaultValue)
	{
		if(!pageParam.containsKey(key) || ( pageParam.get(key) == "" ))
			 pageParam.put(key, defaultValue);
			
	}
	
	private void InitAllParams()
	{
		setDefaultQueyParam(QUERY_PRODUCT_TYPE, String.valueOf(Product.MEIJIA));
		setDefaultQueyParam(QUERY_TYPE, "0");
		setDefaultQueyParam(KEYWORD, "hot");
		setDefaultQueyParam(QUERY_NUM, "10");
		setDefaultQueyParam(QUERY_ID, "-1");
		setDefaultQueyParam(QUERY_TOTAL_NUM, "0");
		setDefaultQueyParam(SORT_ORDER_KEY, "create_date");
		setDefaultQueyParam(SORT_ORDER, "1");
		setDefaultQueyParam(SELECT_LEFT, "-1");
		setDefaultQueyParam(SELECT_RIGHT, "-1");
		setDefaultQueyParam(SELECT_KEYWORDS, "");
		setDefaultQueyParam(LAST_QUERY_ID, "-1");
	}
    private void setParam(String key,String value)
    {
    	pageParam.put(key, value);
    }
    
    public static ProductViewFragment getHotInstance(Map<String,String>map)
    {
    	if(map == null)
    	{
    		map = new HashMap<String, String>();
    		map.put(QUERY_PRODUCT_TYPE, ProductViewFragmentActivity.MEIJIA);
    	}
    	map.put(KEYWORD	, "hot");
    	return newInstance(map);
    }
    
    public static ProductViewFragment getNewInstance(Map<String,String>map)
    {
    	if(map == null)
    	{
    		map = new HashMap<String, String>();
    		map.put(QUERY_PRODUCT_TYPE, ProductViewFragmentActivity.MEIJIA);
    	}
    	map.put(KEYWORD	, "new");
    	return newInstance(map);
    } 
    
    public static ProductViewFragment getFilterInstance(Map<String,String>map)
    {
    	if(map == null)
    	{
    		map = new HashMap<String, String>();
    		map.put(QUERY_PRODUCT_TYPE, ProductViewFragmentActivity.MEIJIA);
    	}
    	map.put(QUERY_TYPE, "1");
    	map.put(SELECT_LEFT, "0");
    	map.put(SELECT_RIGHT, "200");
    	
    	return newInstance(map);
    }

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		state = STATE.REFRESH;
        QueryProductDataFromServer();
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		state = STATE.LOAD;
		QueryProductDataFromServer();
	}
	
	public interface OnClickListItem
	{
		public void setOnClickListItem(Product product);
	}
}
