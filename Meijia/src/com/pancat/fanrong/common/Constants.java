/**
 * 
 */
package com.pancat.fanrong.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 常量类
 * @author trhuo
 *
 */
public class Constants {

	public static final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	
	public static final int channel =1;

	public static final String BASE_URL = "http://ec2-54-169-66-69.ap-southeast-1.compute.amazonaws.com/nail_care_svr/index.php/";
	
	
	//显示的列数
	public static final int COLUMN_count = 2;

	//每次加载的图片数
	public static final int PICTURE_COUNT_PER_LOAD = 20;
	
	//允许加载的最多图片数
	public static final int PICTURE_TOTAL_COUNT = 10000;
	
	public static final int HANDLER_WHAT = 1;
	
	public static final int MESSAGE_DELAY = 200;
}
