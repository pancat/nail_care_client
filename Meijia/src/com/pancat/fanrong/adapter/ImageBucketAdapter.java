package com.pancat.fanrong.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.util.album.BitmapCache.ImageCallback;
import com.pancat.fanrong.util.album.BitmapCache;
import com.pancat.fanrong.util.album.ImageBucket;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;

public class ImageBucketAdapter extends BaseAdapter{

	final String TAG = getClass().getSimpleName();
	
	private ImageFetcher imageFetcher;
	
	Activity activity;
	//图片集列表
	List<ImageBucket> dataList;
	
	BitmapCache cache;
	
	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};
	
	public ImageBucketAdapter(Activity activity,List<ImageBucket> list){
		this.activity = activity;
		dataList = list;
		cache = new BitmapCache();
		imageFetcher = new ImageFetcher(activity, 240);
	}
	
	@Override
	public int getCount() {
		int count = 0;
		if(dataList != null){
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(activity, R.layout.item_image_bucket, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			convertView.setTag(holder);
		}
		holder = (ViewHolder)convertView.getTag();
		ImageBucket item = dataList.get(position);
		holder.count.setText("" + item.count+"张");
		holder.name.setText(item.bucketName);
		holder.selected.setVisibility(View.GONE);
		if (item.imageList != null && item.imageList.size() > 0) {
			String thumbPath = item.imageList.get(0).thumbnailPath;
			String sourcePath = item.imageList.get(0).imagePath;
			holder.iv.setTag(sourcePath);
			cache.displayBmp(holder.iv, thumbPath, sourcePath, callback);
		} else {
			holder.iv.setImageBitmap(null);
			Log.e(TAG, "no images in bucket " + item.bucketName);
		}
		return convertView;
	}
	
	class ViewHolder{
		private ImageView iv;
		private ImageView selected;
		private TextView name;
		private TextView count;
	}
	
}
