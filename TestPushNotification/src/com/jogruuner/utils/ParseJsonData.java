package com.jogruuner.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;


public class ParseJsonData {
	 public static final String DATA_TYPE_MSG = "msg";
	 public static final String DATA_TYPE_NOTIFICATION = "notification";
	 public static final String DATA_TYPE_DIALOG = "dialog";
	 public static final String DATA_TYPE_USERINFO = "userinfo";
	 public static final String DATA_TYPE_UNKOWN = "unknow";
	 static List<String> keys ;
	 
	  public static void InitKeys()
	  {  
		  keys.clear();
		  keys.add("type");
		  keys.add("title");
		  keys.add("message");
		  keys.add("time");
	  }
	  public static void AddKeys(String key){
		  keys.add(key);
	  }
	  
	  /*
	   * PushMsg
	   */
      public static Map<String,String> ParseMessage(String customContentString){
    	  InitKeys();
    	  Map<String,String> res = new HashMap<String,String>();
    	  
    	  if(!TextUtils.isEmpty(customContentString)){
 			 JSONObject customJson = null;
 			try{
 				customJson = new JSONObject(customContentString);
 				for(String mykey:keys){
 					String myvalue = null;
 				  if (customJson.isNull(mykey)) {
 	                    myvalue = customJson.getString(mykey);
 	                    if((mykey == "type")) myvalue = myvalue.toLowerCase();
 	                    res.put(mykey, myvalue);
 				  }
 				}
 			}catch(JSONException e){
 				e.printStackTrace();
 			}
    	  }
    	  return res;
      }
}
