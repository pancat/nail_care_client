package com.pancat.fanrong.common;

import android.os.Bundle;

/**
 *Fragment和Activity之间传递数据回调接口
 *@author trh 
 *
 */
public interface FragmentCallback {
	public void callback(Bundle arg);
	

	public void finishActivity();

}
