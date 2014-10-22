package com.pancat.fanrong.activity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.util.album.Bimp;
import com.pancat.fanrong.util.album.BitmapCache;
import com.pancat.fanrong.util.album.BitmapCache.ImageCallback;
import com.pancat.fanrong.util.album.ImageItem;

public class ImageGridAdapter extends BaseAdapter{

	public final String TAG = getClass().getSimpleName();
	private TextCallback textCallback = null;
	Activity activity;
	List<ImageItem> dataList;
	Map<String,String> map = new HashMap<String,String>();
	BitmapCache cache;
	private Handler handler;
	private int selectTotal = 0;
	
	private ImageCallback callback = new ImageCallback(){

		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if(imageView != null && bitmap != null){
				String url = params[0].toString();
				if(url != null && url.equals((String)imageView.getTag())){
					imageView.setImageBitmap(bitmap);
				}
				else{
					Log.e(TAG, "callback, bmp not match");
				}
			}
			else{
				Log.e(TAG, "callback, bmp null");
			}
		}
		
	};
	
	public static interface TextCallback{
		public void onAdd(int count);
	}
	
	
	public void setTextCallback(TextCallback listener){
		this.textCallback = listener;
	}
	
	public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler) {
		this.activity = act;
		dataList = list;
		cache = new BitmapCache();
		this.handler = mHandler;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(activity, R.layout.item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView
					.findViewById(R.id.isselected);
			holder.text = (TextView) convertView
					.findViewById(R.id.item_image_grid_text);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		final ImageItem item = dataList.get(position);
		holder.iv.setTag(item.imagePath);
		cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
				callback);
		if (item.isSelected) {
			holder.selected.setImageResource(R.drawable.icon_data_select);  
			holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
		} else {
			holder.selected.setImageResource(-1);
			holder.text.setBackgroundColor(0x00000000);
		}
		
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String path = dataList.get(position).imagePath;

				if ((Bimp.drr.size() + selectTotal) < 9) {
					item.isSelected = !item.isSelected;
					if (item.isSelected) {
						holder.selected
								.setImageResource(R.drawable.icon_data_select);
						holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
						selectTotal++;
						if (textCallback != null)
							textCallback.onAdd(selectTotal);
						map.put(path, path);

					} else if (!item.isSelected) {
						holder.selected.setImageResource(-1);
						holder.text.setBackgroundColor(0x00000000);
						selectTotal--;
						if (textCallback != null)
							textCallback.onAdd(selectTotal);
						map.remove(path);
					}
				} else if ((Bimp.drr.size() + selectTotal) >= 9) {
					if (item.isSelected == true) {
						item.isSelected = !item.isSelected;
						holder.selected.setImageResource(-1);
						selectTotal--;
						map.remove(path);

					} else {
						Message message = Message.obtain(handler, 0);
						message.sendToTarget();
					}
				}
			}

		});
		return convertView;
	}

	class ViewHolder{
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}
}
