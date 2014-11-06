package com.pancat.fanrong.fragment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.view.LayoutInflater.Filter;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.ProductDetailViewFragmentActivity;
import com.pancat.fanrong.activity.ProductViewFragmentActivity;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.common.FilterQueryAndParse;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.db.DatabaseManager;
import com.pancat.fanrong.fragment.CircleFragment.ContentTask;
import com.pancat.fanrong.fragment.CircleFragment.CircleAdapter;
import com.pancat.fanrong.gridview.StaggeredGridView;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.temp.SampleData;
import com.pancat.fanrong.util.PhoneUtils;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;
import com.pancat.fanrong.waterfall.view.XListView;
import com.pancat.fanrong.waterfall.view.XListView.IXListViewListener;
import com.pancat.fanrong.waterfall.widget.ScaleImageView;

/*
 * 产品视图，
 * @author JogRunner
 * 于2014.10.20移除向服务器端询问细节工作，向FilterQueryAndParse类转移
 * 
 */
public class ProductViewFragment extends Fragment implements IXListViewListener{
	private static final String TAG = "ProductViewFragment";

	private static final String url = "product/get_product_list";
	
	private View contextView;
	//加载图像的工具
	private ImageFetcher mImageFetcher;
	//列表视图
	private XListView mAdapterView = null;
	
	private ProductInfoAdapter mAdapter;
    private Map<String,String> pageParam;//主导布局的参数
    
	private List<Product> mData; //数据
	// private OnClickListItem onClickListItem;
	//采用通用接口的回调函数
    private FragmentCallback onClickListItem;
    
    private int state = FilterQueryAndParse.IDLE;
	
    //新建一个实例
	public static ProductViewFragment newInstance(Map<String,Object> pageParams)
	{
		ProductViewFragment instance = new ProductViewFragment();
		instance.pageParam = FilterQueryAndParse.FilterAndRepairDefault(pageParams);
		Log.d(TAG,instance.pageParam.toString());
		return instance;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		mData = new ArrayList<Product>();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		//它对应的视图
		contextView = inflater.inflate(R.layout.fragment_product, container, false);
		mAdapterView = (XListView)contextView.findViewById(R.id.fragment_product_list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);//
		
		//设置图片预加载宽度及占位图
		mImageFetcher = new ImageFetcher(getActivity(), 240);
		mImageFetcher.setLoadingImage(R.drawable.defaultproduct);
		mImageFetcher.setExitTasksEarly(false);
		
		if (mAdapter == null) {
			mAdapter = new ProductInfoAdapter();
		}
		mAdapterView.setAdapter(mAdapter);
		
		ExecuteFirstLoad();
		
		return contextView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		onClickListItem = (FragmentCallback)activity;
	}
    
	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		state = FilterQueryAndParse.REFRESH;
        QueryProductDataFromServer();
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		state = FilterQueryAndParse.LOAD;
		QueryProductDataFromServer();
	}
	
	private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(String content) {
			ArrayList<Product> res = null;
			//TODO 以后在这提取返回的状态码执行进一步动作
			try{
				res  = FilterQueryAndParse.ParseToProductArr(content);
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
			Log.d(TAG, content+"***"+error);
		}
		
	};
	private void notifyChangeParam(List<Product> product)
	{
		if(product != null)
		{
			if(state == FilterQueryAndParse.REFRESH)
				mAdapter.addAllProductToHead(product);
			else
				mAdapter.addAllProductToTail(product);
		
			mAdapter.notifyDataSetChanged();
			addProductToDatabase(product);
		}
		//停止刷新与加载
		if(state == FilterQueryAndParse.REFRESH)
			mAdapterView.stopRefresh();
		else if(state == FilterQueryAndParse.LOAD)
			mAdapterView.stopRefresh();
	}
	
	private void ExecuteFirstLoad()
	{
		mData = DatabaseManager.getInstance().getProduct();
		mAdapter.notifyDataSetChanged();
		QueryProductDataFromServer();
	}
	private void QueryProductDataFromServer()
	{
		//首先判断是否连接网络
		
		if(PhoneUtils.isNetworkConnected(getActivity())){
			RequestParams requestParams = new RequestParams(pageParam);
			state = FilterQueryAndParse.LOAD;
			RestClient.getInstance().get(url, requestParams, responseHandler);
		}else{
			Log.d(TAG, "network isn't connected");
		}
	}
	
	private void addProductToDatabase(List<Product> products)
	{
		for (Product product : products) {
			//Log.d(TAG, product.toString());
			DatabaseManager.getInstance(getActivity()).addProduct(product);
		}
	}
	
    public static ProductViewFragment getHotInstance(String productType)
    {
    	Map<String,Object> map = new HashMap<String,Object>();
    	if(productType == null)
    		map.put(FilterQueryAndParse.Q_PRODUCT_TYPE, FilterQueryAndParse.MEIJIA);
    	else map.put(FilterQueryAndParse.Q_PRODUCT_TYPE,productType);
    	
    	map.put(FilterQueryAndParse.Q_KEYWORD, FilterQueryAndParse.HOT);
    	map.put(FilterQueryAndParse.Q_QUERY_TYPE, FilterQueryAndParse.QT_1);
    	return newInstance(map);
    }
    
    public static ProductViewFragment getNewInstance(String productType)
    {
    	Map<String,Object> map = new HashMap<String,Object>();
    	if(productType == null)
    		map.put(FilterQueryAndParse.Q_PRODUCT_TYPE, FilterQueryAndParse.MEIJIA);
    	else map.put(FilterQueryAndParse.Q_PRODUCT_TYPE,productType);

    	map.put(FilterQueryAndParse.Q_KEYWORD, FilterQueryAndParse.NEW);
    	map.put(FilterQueryAndParse.Q_QUERY_TYPE, FilterQueryAndParse.QT_2);
    	return newInstance(map);
    } 
    
    public static ProductViewFragment getFilterInstance(String content,String productType)
    {
    	Map<String,Object> map = new HashMap<String,Object>();
    	if(content == null){
			map.put(FilterQueryAndParse.Q_SELECT_RIGHT, "200");
    	}else if(content.contains("-")){
			String[] ar = content.split("-");
			map.put(FilterQueryAndParse.Q_SELECT_LEFT, ar[0]);
			map.put(FilterQueryAndParse.Q_SELECT_RIGHT, ar[1]);
		}else{
			map.put(FilterQueryAndParse.Q_SELECT_LEFT, "600");
		}
    	
    	if(productType == null)
    		map.put(FilterQueryAndParse.Q_PRODUCT_TYPE, FilterQueryAndParse.MEIJIA);
    	else map.put(FilterQueryAndParse.Q_PRODUCT_TYPE,productType);
    	
    	map.put(FilterQueryAndParse.Q_QUERY_TYPE, FilterQueryAndParse.QT_3);
    	return newInstance(map);
    }

	//产品适配器类
	private class ProductInfoAdapter extends BaseAdapter{
		
		class ViewHolder{
			ScaleImageView productImg;
			TextView productTitle;
			TextView productPrice;
			ImageView productAuthorImg;
			TextView productAuthor;
			ImageView productHot;
			TextView productHotNum;
		}
		public ProductInfoAdapter() {
		}

		@Override
		public View getView(int position, View productItemView, ViewGroup parent) {
			ViewHolder vh;
			if(productItemView == null)
			{
				LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
				productItemView = layoutInflater.inflate(R.layout.product_item_view, parent,false);
				vh = new ViewHolder();
				vh.productImg = (ScaleImageView) productItemView.findViewById(R.id.product_img);
				vh.productTitle = (TextView)productItemView.findViewById(R.id.product_title);
				vh.productPrice = (TextView)productItemView.findViewById(R.id.product_price);
				vh.productAuthorImg = (ImageView)productItemView.findViewById(R.id.product_author_img);
				vh.productAuthor = (TextView)productItemView.findViewById(R.id.product_author);
				vh.productHot = (ImageView)productItemView.findViewById(R.id.product_hot);
				vh.productHotNum = (TextView)productItemView.findViewById(R.id.product_hotnum);
				productItemView.setTag(vh);
			}
			else
			{
				vh = (ViewHolder)productItemView.getTag();
			}
			
			Product product = getItem(position);
			
			//TODO 这里之后应该改成产品图片的宽高大小
			Bitmap bmp = BitmapFactory.decodeResource(parent.getContext().getResources(), R.drawable.defaultproduct);
			vh.productImg.setImageWidth(bmp.getWidth());
			vh.productImg.setImageHeight(bmp.getHeight());
			
			try
			{
				if(product.getProductURL() != "" )
					mImageFetcher.loadImage(product.getProductURL(), vh.productImg);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
	
			vh.productAuthor.setText(product.getProductAuthor());
			
			//设置作者头像的图片
			vh.productAuthorImg.setImageResource(R.drawable.user);
			
			vh.productPrice.setText("¥"+product.getProductPrice());
			vh.productTitle.setText(product.getProductTitle());
			vh.productHotNum.setText("2");
			vh.productHot.setImageResource(R.drawable.heart_gray);
			
			final TextView textViewTemp = vh.productHotNum;
			final ImageView productHotTemp = vh.productHot;
			vh.productHot.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if(view.getId() == R.id.product_hot){

						Drawable d = getActivity().getResources().getDrawable(R.drawable.heart_red);
						if(d == null)
							Log.d(TAG, "Cao ni ma");
						productHotTemp.setImageResource(R.drawable.heart_red);
						textViewTemp.setText(""+(Integer.parseInt(textViewTemp.getText().toString())+1));
					}
				}
			});
			final Product prod = product;
			vh.productImg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Bundle bundle = new Bundle();
					bundle.putSerializable(Product.KEY, prod);
					onClickListItem.callback(bundle);
				}
			});
			//TODO 点击作者图像事件
			return productItemView;
		}
	    
		public void addAllProductToHead(List<Product> products)
		{
			mData.clear();
			mData.addAll(products);
		}
		public void addAllProductToTail(List<Product> products)
		{
			mData.addAll(products);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Product getItem(int position) {
			// TODO Auto-generated method stub
			return mData.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

}
