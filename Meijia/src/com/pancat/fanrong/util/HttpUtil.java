package com.pancat.fanrong.util;


import java.io.ByteArrayOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;  
import java.net.URL;  

public class HttpUtil {  

  public static String URL_PATH="http://ec2-54-169-66-69.ap-southeast-1.compute.amazonaws.com/nail_care_svr";//访问网络图片的路径  
  public HttpUtil() {  
	
  }  
  /** 
   * 从网络中获取图片信息，以流的形式返回 
   * @return 
   */  
  public static InputStream getImageViewInputStream(String url0){
	  URL_PATH+=url0;
      InputStream inputStream = null;  
      try {  
          URL url = new URL(URL_PATH);  
          if(url!=null) {  
              HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();  
              httpURLConnection.setConnectTimeout(3000);  
              httpURLConnection.setRequestMethod("GET");  
              httpURLConnection.setDoInput(true) ;  
              int resonpseCode = httpURLConnection.getResponseCode();  
              if(resonpseCode == 200){  
                  inputStream = httpURLConnection.getInputStream();  
              }
          }  
      } catch (MalformedURLException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      } catch (IOException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      }  
      return inputStream;  
  }  
  /** 
   * 从网络中获取图片西悉尼，以字节数组的形式放回 
   * @return 
   */  
  public static byte[] getImageViewArray(String url0){  
	  URL_PATH+=url0;
      byte [] data = null;  
      InputStream inputStream = null;  
      //不需要关闭的输出流，直接写入内存中。  
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
      try {  
            
          URL url = new URL(URL_PATH);  
          if(url!=null) {  
              HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();  
              httpURLConnection.setConnectTimeout(3000);  
              httpURLConnection.setRequestMethod("GET");  
              httpURLConnection.setDoInput(true) ;  
              int resonpseCode = httpURLConnection.getResponseCode();  
              int len = 0;  
              byte[] b_data = new byte[1024];  
              if(resonpseCode == 200){  
                  inputStream = httpURLConnection.getInputStream();  
                  ;  
                  while ((len =inputStream.read(b_data)) !=-1){  
                      outputStream.write(b_data, 0, len);  
                  }  
                  data = outputStream.toByteArray();  
              }  
          }  
      } catch (MalformedURLException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      } catch (IOException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      }finally{  
          if(inputStream!=null){try {  
              inputStream.close();  
          } catch (IOException e) {  
              // TODO Auto-generated catch block  
              e.printStackTrace();  
          }}  
      }  
      return data;  
  }  
}  