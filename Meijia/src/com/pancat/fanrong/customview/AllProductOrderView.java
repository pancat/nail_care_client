package com.pancat.fanrong.customview;

import java.util.ArrayList;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AllProductOrderView extends LinearLayout{
	public static final String TAG = "AllProductOrderView";
	
	private Context context;
	private String author;
	private TextView viewAuthor;
	private TextView viewTotalnum;
	private ArrayList<Product> products;
	private LinearLayout productView;
	private LayoutInflater inflater;
	
	public static AllProductOrderView getView(Context context,String author,ArrayList<Product>products){
		AllProductOrderView view = new AllProductOrderView(context);
		view.setDatas(author, products);
		return view;
	}
	
	public AllProductOrderView(Context context) {
		super(context);
		this.context = context;
		inflater = LayoutInflater.from(context);
		View tv = inflater.inflate(R.layout.view_allproduct_order, this);
		
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);
		
		viewAuthor = (TextView)tv.findViewById(R.id.view_allproduct_order_author);
		viewTotalnum = (TextView)tv.findViewById(R.id.view_allproduct_order_totalnum);
		productView = (LinearLayout)tv.findViewById(R.id.view_allproduct_order_products);
	}
	
	public void setDatas(String author,ArrayList<Product>products){
		if(products.size() ==0 ) return ;
		this.author = author;
		this.products = products;
		
		viewAuthor.setText(author);
		
		String totalnum = context.getResources().getString(R.string.product_totalnum);
		viewTotalnum.setText(String.format(totalnum, products.size()));
		
		int len = products.size();
		for(int i=0; i<len; i++){
			ProductShow view = ProductShow.getView(context, products.get(i));
			productView.addView(view);
			if(i != len-1){
				productView.addView(getHorizontalLine());
			}
		}
	}
	private View getHorizontalLine(){
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2);
		View v = new View(context);
		v.setBackgroundColor(context.getResources().getColor(R.color.border_great));
		v.setLayoutParams(vlp);
		return v;
	}
}
