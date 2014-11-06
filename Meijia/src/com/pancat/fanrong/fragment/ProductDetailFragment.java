package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.List;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.OrderAuthorActivity;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.handler.HandlerFactory;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailFragment extends Fragment {
   private static final String TAG = "ProductDetailFragment";
	
   
   private Product product;
   private ImageView productDFImg;

   private TextView productDFDescription;
   private TextView productDFPrice;
   private Button productDFOrder;
   
   private ImageFetcher imageFetcher;
   
   public static ProductDetailFragment newInstance(Product product)
   {
	   ProductDetailFragment productDetailFragment = new ProductDetailFragment();
	   productDetailFragment.product = product;
	   return productDetailFragment;
   }
   
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
	   // TODO 自动生成的方法存根
	   super.onCreate(savedInstanceState);
   }

   
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.product_detail_fragment, container, false);
	}

   
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//获取视图中组件
		initView();
		
		//加载产品图像
        imageFetcher = new ImageFetcher(getActivity(), 600);
        imageFetcher.setExitTasksEarly(false);
        imageFetcher.setLoadingImage(R.drawable.defaultproduct);
        
		//productDFImg.setImageResource(R.drawable.defaultproduct);
		
        //TODO 产品其它相关设置
		
		if(product != null)
		{
			productDFDescription.setText(product.getProductDescription());
			productDFPrice.setText(product.getProductPrice()+"");
			imageFetcher.loadImage(product.getProductURL(), productDFImg);
			 
			//TODO 需要进一步更改
			//HandlerFactory.setLoadImageHandler(product.getProductURL(), true, productDFImg);
		}
		
	}
	
	//初始化视图
	private void initView(){
		
		View view = getView();
		productDFDescription = (TextView)view.findViewById(R.id.product_detail_fragment_description);
		productDFImg = (ImageView)view.findViewById(R.id.product_detail_fragment_img);
      
        //productDFOrder = (Button)view.findViewById(R.id.product_detail_fragment_order);
		productDFPrice = (TextView)view.findViewById(R.id.product_detail_fragment_price);

	}
	
}
