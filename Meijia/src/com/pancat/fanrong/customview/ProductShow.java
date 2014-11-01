package com.pancat.fanrong.customview;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductShow extends LinearLayout {

	private LayoutInflater inflater;
	private View view;
	private ImageView productImg;
	private TextView productNum;
	private TextView productPrice;
	private TextView productTitle;
	private Context context;
	private Product product;
	private ImageFetcher imageFecther;
	
	public static ProductShow getView(Context context,Product product){
		ProductShow  v = new ProductShow(context);
		v.setDatas(product);
		return v;
	}
	
	public ProductShow(Context context) {
		super(context);
		this.context = context;
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.view_product_show, this);
		
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);
		
		productImg = (ImageView)view.findViewById(R.id.view_product_show_img);
		productTitle = (TextView)view.findViewById(R.id.view_product_show_title);
		productNum = (TextView)view.findViewById(R.id.view_product_show_num);
		productPrice = (TextView)view.findViewById(R.id.view_product_show_price);
		
		imageFecther = new ImageFetcher(context, 60, 60);
		imageFecther.setExitTasksEarly(false);
		imageFecther.setLoadingImage(R.drawable.defaultproduct);
		
	}
	public void setDatas(Product product){
		this.product = product;
		imageFecther.loadImage(product.getProductURL(), productImg);
		productTitle.setText(product.getProductTitle());
		//TODO 数量在这里设置为1
		String numStr = context.getResources().getString(R.string.product_num);
		productNum.setText(String.format(numStr, 1));
		
		String priceStr = context.getResources().getString(R.string.product_price);
		productPrice.setText(String.format(priceStr, product.getProductPrice()));
	}
}
