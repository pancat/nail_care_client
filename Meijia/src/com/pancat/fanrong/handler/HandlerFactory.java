package com.pancat.fanrong.handler;

import java.io.IOException;

import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.util.FileUtils;

import android.widget.ImageView;

public class HandlerFactory {
	public static boolean setLoadImageHandler(String url, boolean saveToDisk,
			ImageView view) {
		String filename = FileUtils.getFilenameFromUrl(url);
		
		// 这里只可以做成异步的方式进行加载
		if (FileUtils.isFileExist(filename)) {
			ImageResponseHandler handler = new ImageResponseHandler(view);
			try {
				byte[] data = FileUtils.readDataFromFile(filename);
				handler.onSuccess(data);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ImageResponseHandler handler;
		if (saveToDisk == true) {
			handler = new ImageResponseHandler(view, filename);
		} else {
			handler = new ImageResponseHandler(view);
		}
		RestClient.getInstance().get(url, new RequestParams(), handler);
		return true;
	}
}
