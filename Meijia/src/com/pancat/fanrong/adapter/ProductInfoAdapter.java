package com.pancat.fanrong.adapter;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.ProductDetailViewFragmentActivity;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.fragment.ProductViewFragment.OnClickListItem;
import com.pancat.fanrong.gridview.util.DynamicHeightImageView;
import com.pancat.fanrong.handler.HandlerFactory;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;
import com.pancat.fanrong.waterfall.widget.ScaleImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductInfoAdapter extends ArrayAdapter<Product> {

	private static final String TAG = "ProductInfoAdapter";
	
	static class ViewHolder{
		ScaleImageView productImg;
		TextView productTitle;
		TextView productPrice;
		ImageView productAuthorImg;
		TextView productAuthor;
		ImageView productHot;
	}
	
	private LayoutInflater layoutInflater;
	private ImageFetcher imageFetcher;
	private Context context;
	private OnClickListItem onClickListItem;
	
	public ProductInfoAdapter(Context context, int resource,ImageFetcher imageFetcher,OnClickListItem onClickListItem) {
		super(context, resource);
		// TODO �Զ����ɵĹ��캯�����
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		
		this.imageFetcher = imageFetcher;
		this.onClickListItem = onClickListItem;
	}

	@Override
	public View getView(int position, View productItemView, ViewGroup parent) {
		// TODO �Զ����ɵķ������
		
		ViewHolder vh;
		if(productItemView == null)
		{
			productItemView = layoutInflater.inflate(R.layout.product_item_view, parent,false);
			vh = new ViewHolder();
			vh.productImg = (ScaleImageView) productItemView.findViewById(R.id.product_img);
			vh.productTitle = (TextView)productItemView.findViewById(R.id.product_title);
			vh.productPrice = (TextView)productItemView.findViewById(R.id.product_price);
			vh.productAuthorImg = (ImageView)productItemView.findViewById(R.id.product_author_img);
			vh.productAuthor = (TextView)productItemView.findViewById(R.id.product_author);
			vh.productHot = (ImageView)productItemView.findViewById(R.id.product_hot);
			
			productItemView.setTag(vh);
		}
		else
		{
			vh = (ViewHolder)productItemView.getTag();
		}
		
		Product product = getItem(position);
		
		Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.defaultproduct);
		vh.productImg.setImageWidth(bmp.getWidth());
		vh.productImg.setImageHeight(bmp.getHeight());
		try
		{
			imageFetcher.loadImage(product.getProductURL(), vh.productImg);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		//vh.productImg.setImageResource(R.drawable.defaultproduct);
		//vh.productImg.setBackgroundResource(R.color.grey);
		//Log.d(TAG, "nima" + product.getProductURL());
		//HandlerFactory.setLoadImageHandler(product.getProductURL(), true, vh.productImg);
		
		vh.productAuthor.setText(product.getProductAuthor());
		vh.productAuthorImg.setImageResource(R.drawable.user);
		//vh.productHot.setText(product.getProductHot());
		vh.productPrice.setText(product.getProductPrice());
		vh.productTitle.setText(product.getProductTitle());
		final Product prod = product;
		productItemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				onClickListItem.setOnClickListItem(prod);
			}
		});
		return productItemView;
	}
    
	
}
