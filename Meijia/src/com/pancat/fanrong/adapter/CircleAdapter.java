package com.pancat.fanrong.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.common.CircleImageView;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.grid.StaggeredGridView;
import com.pancat.fanrong.waterfall.widget.ScaleImageView;

public class CircleAdapter extends BaseAdapter{

	private static final int[] COLORS = {R.color.holo_blue_light, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_purple_light, R.color.holo_red_light};
	private Drawable mDefaultImageDrawable;
	
	private Resources mResource;
	
	private StaggeredGridView mListView;
	
	private LayoutInflater mInflater;
	
	private List<Circle> circles;
	private FragmentCallback mFragmentCallback;
	private Context mContext;
	
	public CircleAdapter(Context context,StaggeredGridView listView,List<Circle> mCircles,FragmentCallback fragmentCallback){
		mContext = context;
		mResource = context.getResources();
		mInflater = LayoutInflater.from(context);
		mListView = listView;
		this.circles = mCircles;
		mFragmentCallback = fragmentCallback;
	}

	@Override
	public int getCount() {
		return circles.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Circle circle = circles.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_circle, null);
			ViewUtils.inject(holder,convertView);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		mDefaultImageDrawable = new ColorDrawable(mResource.getColor(COLORS[position % COLORS.length]));
		holder.image.setImageHeight(circle.getHeight());
		holder.image.setImageWidth(circle.getWidth());
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(mDefaultImageDrawable)
			.showImageOnFail(mDefaultImageDrawable)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		ImageLoader.getInstance().displayImage(circle.getPath(), holder.image, options);
		holder.description.setText(circle.getDescription());
		holder.username.setText("Mary");
		final Bundle data = new Bundle();
		data.putSerializable("circle", circle);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mFragmentCallback.callback(data);
			}
		});
		return convertView;
	}
	
	
	
	class ViewHolder{
		@ViewInject(R.id.news_pic)
		ScaleImageView image;
		@ViewInject(R.id.news_title)
		TextView description;
		@ViewInject(R.id.user_img)
		CircleImageView userImg;
		@ViewInject(R.id.username)
		TextView username;
	}
	
}
