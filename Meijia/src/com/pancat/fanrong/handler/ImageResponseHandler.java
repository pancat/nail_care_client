package com.pancat.fanrong.handler;

import java.io.IOException;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.http.BinaryHttpResponseHandler;
import com.pancat.fanrong.util.FileUtils;

public class ImageResponseHandler extends BinaryHttpResponseHandler {
	private ImageView imageView;
	private boolean saveToDisk = false;
	private Context context;
	private String filename;

	public ImageResponseHandler(ImageView imageView,
			String filename) {
		super();
		
		this.context = MainApplication.getAppContext();
		this.imageView = imageView;
		this.saveToDisk = true;
		this.filename = filename;
		if (filename == null || filename.length() == 0) {
			this.saveToDisk = true;
		}
	}

	public ImageResponseHandler(ImageView imageView) {
		super();
		this.context = MainApplication.getAppContext();
		this.imageView = imageView;
		this.saveToDisk = false;
	}

	@Override
	public void onSuccess(byte[] data) {
		this.imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0,
				data.length));
		if (this.saveToDisk)
		{
			try {
				FileUtils.writeDataToFile(this.filename, data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onFailure(Throwable e, String data) {
		Log.i("image", "download failed");
	}
}
