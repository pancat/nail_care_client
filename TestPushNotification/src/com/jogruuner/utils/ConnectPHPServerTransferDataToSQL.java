package com.jogruuner.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.impl.io.HttpResponseWriter;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.StrictMode;


public class ConnectPHPServerTransferDataToSQL {
	public final static int HTTP_REQUEST_POST = 0;
	public final static int HTTP_REQUEST_GET = 1;
	public final static int TRANSFER_OK = 2;
	public final static int TRANSFER_CONNECT_ERROR = 4;
	public final static int TRANSFER_DATA_EXISTS = 8;
	public final static int TRANSFER_DATA_INCORRECT = 16;
	public final static int INTERNET_EXCEPTION = 32;
	
	 String url;
	 //Server
	 //Port
	 //Project folder
	 
	 int flags = HTTP_REQUEST_POST; //default Post
	 
	public ConnectPHPServerTransferDataToSQL(String url){
		this.url = url;
		
		//repair error
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
	    		   .detectDiskWrites()
	    		   .detectNetwork()
	    		   .penaltyLog()
	    		   .build());
	     StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
	    		   .detectLeakedSqlLiteObjects()
	    		   .detectLeakedClosableObjects()
	    		   .penaltyLog()
	    		   .penaltyDeath()
	    		   .build());
	}
	
	/*
	 * set Flag
	 */
	public void setFlags(int flag){
		flags = flag;
	}
	
	/*
	 * add log
	 */
	public void addLog(String header,String text){
        String logText = "" + Utils.logStringCache;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += header + ": ";
        logText += sDateFormat.format(new Date()) + ": ";
        logText += text;
        Utils.logStringCache = logText;
	}
	
	/*
	 * tranfer info to server
	 */
	public int TransferChannelIdAndUserIdToSQL(String userId,String channelId)
	{
		if((flags & 1) == 1){
			StringBuilder addDataString = new StringBuilder(url);
			addDataString.append("?userid="+userId+"&channelid="+channelId);
			
			HttpGet httpGet = new HttpGet(addDataString.toString());
			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpGet);
				
				if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					String res = EntityUtils.toString(httpResponse.getEntity());
					int code = Integer.parseInt(res);
					addLog("transfer data to my sql return code:", res);
					return code;
				}
				else{
					addLog("transfer data to my sql return code:","error");
					return INTERNET_EXCEPTION;
				}
			}catch (UnsupportedEncodingException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (ClientProtocolException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block 
	            e.printStackTrace();  
	        }  
		}
		else{
			HttpPost httpPost = new HttpPost(url);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid", userId));
			params.add(new BasicNameValuePair("channelid", channelId));
			
			try{
				HttpEntity httpentity = new UrlEncodedFormEntity(params,"utf-8");
				httpPost.setEntity(httpentity);
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpPost);
				
				if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					String res = EntityUtils.toString(httpResponse.getEntity());
					int code = Integer.parseInt(res);
					addLog("transfer data to my sql return code:", res);
					return code;
				}
				else{
					addLog("transfer data to my sql return code:","error");
					return INTERNET_EXCEPTION;
				}
			}catch (UnsupportedEncodingException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (ClientProtocolException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block 
	            e.printStackTrace();  
	        }  
		}
		
		return INTERNET_EXCEPTION;
	}
	
	
}
