package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.List;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.OrderAuthorActivity;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.handler.HandlerFactory;

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
 //  private ImageView productDFImg;
   private ViewPager productDFImgListViewpager;
   private ImageView leftArrow = null;
   private ImageView rightArrow = null;
   
   private TextView productDFDescription;
   private TextView productDFPrice;
   private Button productDFOrder;
   
   
   public static ProductDetailFragment newInstance(String product)
   {
	   ProductDetailFragment productDetailFragment = new ProductDetailFragment();
	   Bundle  bundle = new Bundle();
	   bundle.putString(Product.KEY, product);
	   productDetailFragment.setArguments(bundle);
	   return productDetailFragment;
   }
   
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
	   // TODO 自动生成的方法存根
	   super.onCreate(savedInstanceState);
	   Bundle args = getArguments();
	   product = Product.ParseFromString(args.getString(Product.KEY));
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

		//productDFImg.setImageResource(R.drawable.defaultproduct);
		initView();
		if(product != null)
		{
			productDFDescription.setText(product.getProductDescription());
			productDFPrice.setText(product.getProductPrice());
			
			//TODO 需要进一步更改
			//HandlerFactory.setLoadImageHandler(product.getProductURL(), true, productDFImg);
			Log.d(TAG, product.getProductURL());
		}
		
		//预约按钮监听事件
		productDFOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//TODO 这里需要判断是否登录 @empark
				// Class.getInstance().isLogin()
				// do something and if not login ,return ;
				
				Intent intent = new Intent(getActivity(),OrderAuthorActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Product.KEY, product);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
		});
	}
	
	//初始化视图
	private void initView(){
		
		View view = getView();
		productDFDescription = (TextView)view.findViewById(R.id.product_detail_fragment_description);
		//productDFImg = (ImageView)view.findViewById(R.id.product_detail_fragment_img);
        productDFImgListViewpager = (ViewPager)view.findViewById(R.id.product_detail_fragment_viewpage);
		leftArrow = (ImageView)view.findViewById(R.id.product_detail_fragment_left);
		rightArrow = (ImageView)view.findViewById(R.id.product_detail_fragment_right);
        productDFOrder = (Button)view.findViewById(R.id.product_detail_fragment_order);
		productDFPrice = (TextView)view.findViewById(R.id.product_detail_fragment_price);
		
		mapList = new ArrayList<View>();
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.defaultproduct);
		for(int i=0;i<20; i++){
			View v = LayoutInflater.from(getActivity()).inflate(R.layout.single_img, null);
			ImageView iv = (ImageView)v.findViewById(R.id.single_img);
			iv.setImageBitmap(bmp);
			mapList.add(v);
		}
		productDFImgListViewpager.setAdapter(new ImagePagerAdapter());
		
	}
	
	private List<View> mapList ;
	private int index = 0;
	public class ImagePagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mapList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(mapList.get(position));
			//super.destroyItem(container, position, object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(mapList.get(position));
			return mapList.get(position);
			//return super.instantiateItem(container, position);
		}
	}
    private void set(){
    	leftArrow.setVisibility(View.GONE);
    	rightArrow.setVisibility(View.GONE);
    	productDFImgListViewpager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction() == MotionEvent.ACTION_DOWN){
					setvis();
				}
				return true;
			}
		});
    }
    private void setvis()
    {
    	if(index != 0) leftArrow.setVisibility(View.VISIBLE);
    	else leftArrow.setVisibility(View.GONE);
    	
    	if(index != mapList.size()) rightArrow.setVisibility(View.VISIBLE);
    	else rightArrow.setVisibility(View.GONE);
    }
	private void setListener(){
		leftArrow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(index == 0){leftArrow.setVisibility(View.GONE);}
				else{
					index--;
					productDFImgListViewpager.setCurrentItem(index);
				}
				
			}
		});
		rightArrow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(index == mapList.size()){
					rightArrow.setVisibility(View.GONE);
				}
				else index++;
				productDFImgListViewpager.setCurrentItem(index);
			}
		});
	}
}
