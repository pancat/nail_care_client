package com.pancat.fanrong.util;

import android.app.Notification;
import android.content.Context;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;

public class CommonPushMsgUtils {
	
	//上传各类信息时的行为动作，与服务器端确定共同的数据处理方式
	//分别为：初始化用户上传，用户绑定信息上传，用户反馈上传，用户标签设置上传，用户删除标签上传，用户点击资源获
	public enum UPLOAD_ACTION{INITUSERUPULOAD,USERINFORMATIONUPLOAD,USERRETURNUPLOAD,USERSETTAGUPLOAD,USERDELTAGUPLOAD,USERCLICKOBTAININFO};

      private int msgPushType;
      private Context context;
      private int notificationLayout;
      private int notificationIcon;
      private int notificationTitle;
      private int notificationText;
      private int notificationSimpleIcon;
      
      public CommonPushMsgUtils(Context context,int msgPushType) {
    	  this.context = context;
    	  this.msgPushType = msgPushType;
    	  notificationLayout = -1;
    	  notificationIcon = -1;
    	  notificationSimpleIcon = -1;
    	  notificationText = -1;
    	  notificationTitle = -1;
      }
      
      //设置百度云推送通知组件的ID
      public void setBaiDuNotificationLayoutId(int layout,int icon,int title,int text,int simpleIcon){
    	  notificationLayout = layout;
    	  notificationIcon = icon;
    	  notificationSimpleIcon = simpleIcon;
    	  notificationText = text;
    	  notificationTitle = title;
      }
      
      private boolean isSetBaiDuNotification()
      {
    	  if(notificationIcon == -1) return false;
    	  if(notificationLayout == -1) return false;
    	  if(notificationSimpleIcon == -1) return false;
    	  if(notificationText == -1 ) return false;
    	  if(notificationTitle == -1) return false;
    	  return true;
      }
      
      //通知类消息定制显示样式
      public void customNotificationStyle(){
    	  if(msgPushType == ConfigHelperUtils.BAIDUMSGPUSH){
    		  if(isSetBaiDuNotification()){
	    		  CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
	    	                context, notificationLayout, notificationIcon,
	    	               notificationTitle,notificationText);
	    	        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
	    	        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
	    	                | Notification.DEFAULT_VIBRATE);
	    	        cBuilder.setStatusbarIcon(context.getApplicationInfo().icon);
	    	        cBuilder.setLayoutDrawable(notificationSimpleIcon);
	    	        com.baidu.android.pushservice.PushManager.setNotificationBuilder(context, 1, cBuilder);
    		  }
    		}else if(msgPushType == ConfigHelperUtils.GETUIMSGPUSH){
    		  //TODO 未实现
    	  }
      }
      
      //TODO 将一些重要的信息发送到服务器端，接下来还得继续确定实现
      //@param  other 为"key:value;key2:value; key必须为字符串，value可以是数组形式以[]标注，其中元素用，隔开
      public void sendMsgPushInfoToServer(String userId,String other,UPLOAD_ACTION action){
    	 // if(msgPushType == ConfigHelperUtils.BAIDUMSGPUSH){
    	 //推送选用类型放到服务器端解析
    		  switch (action) {
			case INITUSERUPULOAD:
				//TODO 用户信息上传,uid,username,phonetype
				initUserUpload(userId, other);
				break;
			case USERINFORMATIONUPLOAD:
				//TODO 用户信息上传,etc: channelid,requestid,logtime,sendtime
				userInfomationUpload(userId, other);
				break;
			case USERSETTAGUPLOAD:
				//TODO 用户设置标签上传
				
				break;
			case USERRETURNUPLOAD:
				//TODO 用户反馈信息上传
				
				break;
			case USERCLICKOBTAININFO:
				//TODO 用户点击上传
				
				break;
			case USERDELTAGUPLOAD:
				//TODO 用户删除标签上传
				
				break;
			default:
				break;
			}
    	  //}else if(msgPushType == ConfigHelperUtils.GETUIMSGPUSH){
    		 //个推解析  
    	 // }
      }
      
      //TODO 需要去确定是不是需要返回码
      private int initUserUpload(String userId,String other){
    	  
    	  return 0;//成功
      }
      
      //TODO 
      private int userInfomationUpload(String userId,String other){
    	  
    	  return 0; //success
      }
}
