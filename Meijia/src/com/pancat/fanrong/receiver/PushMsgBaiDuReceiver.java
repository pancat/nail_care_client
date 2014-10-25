package com.pancat.fanrong.receiver;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.pancat.fanrong.util.CommonPushMsgUtils;
import com.pancat.fanrong.util.CommonPushMsgUtils.UPLOAD_ACTION;
import com.pancat.fanrong.util.ConfigHelperUtils;

public class PushMsgBaiDuReceiver extends FrontiaPushMessageReceiver {

	@Override
	public void onBind(Context context, int errorCode, String appid,
            String userId, String channelId, String requestId) {
		// TODO Auto-generated method stub
		 String responseString = "onBind errorCode=" + errorCode + " appid="
	                + appid + " userId=" + userId + " channelId=" + channelId
	                + " requestId=" + requestId;
	        Log.d(TAG, responseString);
	   //这里要上传一些信息，上传动作由CommonPushMsgUtils中定义
	   CommonPushMsgUtils commonPushMsg = new CommonPushMsgUtils(context, ConfigHelperUtils.BAIDUMSGPUSH);
	   commonPushMsg.sendMsgPushInfoToServer(userId, "appid:"+appid+";channelid:" + channelId,
			   UPLOAD_ACTION.USERINFORMATIONUPLOAD);
	}
	
	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2,
			String arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(Context arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotificationClicked(Context arg0, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnbind(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

}
