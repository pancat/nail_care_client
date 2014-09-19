package com.jogrunner.messagehandle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.jogrunner.testpushnotification.MainActivity;
import com.jogruuner.utils.ConnectPHPServerTransferDataToSQL;
import com.jogruuner.utils.ParseJsonData;
import com.jogruuner.utils.Utils;

public class MyPushMessageReceiver extends FrontiaPushMessageReceiver {

	@Override
	public void onBind(Context context, int errorCode, String appid,
            String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);
        
        //Success setFlag decrease request
        if(errorCode == 0){
        	Utils.setBind(context,true);
        	ConnectPHPServerTransferDataToSQL temp =
        			new ConnectPHPServerTransferDataToSQL("http://54.169.11.234/push_jogrunner_baidu_php_sdk/index_testandroid_connect.php");
        	if(temp.TransferChannelIdAndUserIdToSQL(userId, channelId) == ConnectPHPServerTransferDataToSQL.TRANSFER_OK){
        		Log.d(TAG,"success transfer infomation to sql");
        	}
        	else Log.d(TAG,"failed to transfer information to sql");
        }
        
        //Update View
        updateContent(context,responseString);

	}

	@Override
	public void onDelTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

   
        updateContent(context, responseString);
    }

	@Override
	 public void onListTags(Context context, int errorCode, List<String> tags,
	            String requestId) {
	        String responseString = "onListTags errorCode=" + errorCode + " tags="
	                + tags;
	        Log.d(TAG, responseString);

	      
	        updateContent(context, responseString);
	    }
    
	
	//Deal with message with msgtype= 1;
	@Override
	public void onMessage(Context context, String message,
            String customContentString){
		String messageString = "MSGTYPE=1 message=\"" + message
                + "\" customContentString=" + customContentString;
		 Log.d(TAG, messageString);
		 updateContent(context, messageString);
	}

	@Override
	public void onNotificationClicked(Context context, String title,
            String description, String customContentString) {
		String notifyString = "Notification title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);
      updateContent(context, notifyString);
	}

	@Override
	public void onSetTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
		
		  String responseString = "onSetTags errorCode=" + errorCode
	                + " sucessTags=" + sucessTags + " failTags=" + failTags
	                + " requestId=" + requestId;
	        Log.d(TAG, responseString);
	        
	        updateContent(context, responseString);   
	}

	 public void onUnbind(Context context, int errorCode, String requestId) {
	        String responseString = "onUnbind errorCode=" + errorCode
	                + " requestId = " + requestId;
	        Log.d(TAG, responseString);

	        if (errorCode == 0) {
	            Utils.setBind(context, false);
	            
	        }
	      
	        updateContent(context, responseString);
	    }

	 private void updateContent(Context context, String content) {
	        Log.d(TAG, "updateContent");
	        String logText = "" + Utils.logStringCache;

	        if (!logText.equals("")) {
	            logText += "\n";
	        }

	        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
	        logText += sDateFormat.format(new Date()) + ": ";
	        logText += content;

	        Utils.logStringCache = logText;

	        Intent intent = new Intent();
	        intent.setClass(context.getApplicationContext(), MainActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	       // intent.putExtra("data", content);
	        context.getApplicationContext().startActivity(intent);
	    }
}
