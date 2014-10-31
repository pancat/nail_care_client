package com.pancat.fanrong.adapter;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.GuessLikeProduct;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;

public class GuessLikeAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<GuessLikeProduct> mProductList;
	
	public GuessLikeAdapter(Context context,List<GuessLikeProduct> productList) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mProductList = productList;
	}
	
	@Override
	public int getCount() {
		return mProductList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		GuessLikeProduct item = mProductList.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_home_list, null);
			holder.productIcon = (ImageView)convertView.findViewById(R.id.product_icon);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.distance = (TextView)convertView.findViewById(R.id.distance);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.description = (TextView)convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		ImageFetcher fetcher = new ImageFetcher(mContext, 240);
		fetcher.loadImage(item.getIconPath(), holder.productIcon);
		holder.title.setText(item.getTitle());
		holder.distance.setText(item.getDistance()+"km");
		holder.description.setText(item.getDescription());
		holder.price.setText(String.valueOf(item.getPrice()));
		return convertView;
	}

	
	class ViewHolder{
		ImageView productIcon;
		TextView title;
		TextView distance;
		TextView description;
		TextView price;
	}
}
