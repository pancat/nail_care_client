package com.pancat.fanrong.customview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.pancat.fanrong.bean.Product;

import android.content.Context;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class AllAuthorAllProductOrderView extends LinearLayout {
    private static final String TAG = "AllAuthorAllProductOrderView";
	
    private ArrayList<Product> products;
    protected Context context;
    
	public static AllAuthorAllProductOrderView getView(Context context,ArrayList<Product> products){
		AllAuthorAllProductOrderView view = new AllAuthorAllProductOrderView(context);
		view.setDatas(products);
		return view;
	}
	
	public AllAuthorAllProductOrderView(Context context) {
		super(context);
		this.context = context;
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
	    setLayoutParams(lp);
	    setOrientation(VERTICAL);
	}
	
	public void setDatas(ArrayList<Product> products){
		if((products == null) || (products.size() == 0)) return ;
		
		this.products = products;
		Collections.sort(this.products, new Comparator<Product>() {
			@Override
			public int compare(Product a, Product b) {
				String at = a.getProductAuthor();
				String bt = b.getProductAuthor();
				return at.compareTo(bt);
			}
		});
		
		ArrayList<Product> tmp = null;
		Product pre = null;
		Log.d(TAG, this.products.size()+"");
		
		for(int i=0; i<this.products.size(); i++){
			if(tmp == null){
				tmp = new ArrayList<Product>();
			}
			if(pre == null){
				tmp.add(this.products.get(i));
			}else if(this.products.get(i).getProductAuthor().equals(pre.getProductAuthor())){
				tmp.add(this.products.get(i));
			}else{
				if(tmp.size() > 0){
					this.addView(AllProductOrderView.getView(context, tmp));
				}
				tmp = new ArrayList<Product>();
				tmp.add(this.products.get(i));
			}
			pre = this.products.get(i);
		}
		
		if(tmp.size() > 0){
			this.addView(AllProductOrderView.getView(context, tmp));
		}	
	}
}
