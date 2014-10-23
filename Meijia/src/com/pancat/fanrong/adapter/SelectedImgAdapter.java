package com.pancat.fanrong.adapter;

import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.util.album.Bimp;
import com.pancat.fanrong.util.album.FileUtils;

public class SelectedImgAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;
	private Resources resource;
	private Handler handler;
	
	
	public SelectedImgAdapter(Context context,Resources resource,Handler handler){
		inflater = LayoutInflater.from(context);
		this.resource = resource;
		this.handler = handler;
	}
	
	
	public boolean isShape(){
		return shape;
	}
	
	public void setShape(boolean shape){
		this.shape = shape;
	}
	
	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.drr.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						try {
							String path = Bimp.drr.get(Bimp.max);
							System.out.println(path);
							Bitmap bm = Bimp.revitionImageSize(path);
							Bimp.bmp.add(bm);
							String newStr = path.substring(
									path.lastIndexOf("/") + 1,
									path.lastIndexOf("."));
							FileUtils.saveBitmap(bm, "" + newStr);
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
	
	@Override
	public int getCount() {
		return (Bimp.bmp.size() + 1);
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	
	public void setSelectedPosition(int position){
		selectedPosition = position;
	}
	
	public int getSelectedPosition(){
		return selectedPosition;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_published_grid, parent,false);
			holder = new ViewHolder();
			holder.imageView = (ImageView)convertView.findViewById(R.id.item_grid_image);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		if (position == Bimp.bmp.size()) {
			holder.imageView.setImageBitmap(BitmapFactory.decodeResource(
					resource, R.drawable.icon_addpic_unfocused));
			if (position == 9) {
				holder.imageView.setVisibility(View.GONE);
			}
		} else {
			holder.imageView.setImageBitmap(Bimp.bmp.get(position));
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView imageView;
	}

}
