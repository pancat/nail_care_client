package com.pancat.fanrong.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.GuessLikeProduct;

public class GuessLikeAdapter extends BaseAdapter{
	private static final int[] COLORS = {R.color.holo_blue_light, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_purple_light, R.color.holo_red_light};
	private Context mContext;
	private LayoutInflater mInflater;
	private List<GuessLikeProduct> mProductList;
	private Drawable mDefaultImageDrawable;
	private Resources mResource;
	
	public GuessLikeAdapter(Context context,List<GuessLikeProduct> productList) {
		mContext = context;
		mResource = context.getResources();
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
		mDefaultImageDrawable = new ColorDrawable(mResource.getColor(COLORS[position % COLORS.length]));
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(mDefaultImageDrawable)
		.showImageOnFail(mDefaultImageDrawable)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		ImageLoader.getInstance().displayImage(item.getIconPath(), holder.productIcon, options);
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
