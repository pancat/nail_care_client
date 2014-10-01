/**
 * 
 */
package com.pancat.fanrong.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 
 * @author trhuo
 *
 */
public class Constants {

	public static final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	
	public static final int channel =1;
	
	public static final String BASE_URL = "http://ec2-54-169-66-69.ap-southeast-1.compute.amazonaws.com/nail_care_svr/index.php/";
}
