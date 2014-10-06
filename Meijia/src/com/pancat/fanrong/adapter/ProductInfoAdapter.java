package com.pancat.fanrong.adapter;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.gridview.util.DynamicHeightImageView;
import com.pancat.fanrong.handler.HandlerFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductInfoAdapter extends ArrayAdapter<Product> {

	private static final String TAG = "ProductInfoAdapter";
	
	static class ViewHolder{
		DynamicHeightImageView productImg;
		TextView productTitle;
		TextView productPrice;
		ImageView productAuthorImg;
		TextView productAuthor;
		ImageView productHot;
	}
	
	private LayoutInflater layoutInflater;
	
	public ProductInfoAdapter(Context context, int resource) {
		super(context, resource);
		// TODO �Զ����ɵĹ��캯�����
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View productItemView, ViewGroup parent) {
		// TODO �Զ����ɵķ������
		
		ViewHolder vh;
		if(productItemView == null)
		{
			productItemView = layoutInflater.inflate(R.layout.product_item_view, parent,false);
			vh = new ViewHolder();
			vh.productImg = (DynamicHeightImageView) productItemView.findViewById(R.id.product_img);
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
		vh.productImg.setHeightRatio(bmp.getHeight() / bmp.getWidth());
		vh.productImg.setImageResource(R.drawable.defaultproduct);
		vh.productImg.setBackgroundResource(R.color.grey);
		Log.d(TAG, "nima" + product.getProductURL());
		HandlerFactory.setLoadImageHandler(product.getProductURL(), true, vh.productImg);
		
		vh.productAuthor.setText(product.getProductAuthor());
		vh.productAuthorImg.setImageResource(R.drawable.user);
		//vh.productHot.setText(product.getProductHot());
		vh.productPrice.setText(product.getProductPrice());
		vh.productTitle.setText(product.getProductTitle());
		return productItemView;
	}
    
	
}
