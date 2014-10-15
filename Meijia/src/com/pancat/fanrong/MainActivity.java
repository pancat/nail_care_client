package com.pancat.fanrong;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.igexin.sdk.PushManager;
import com.pancat.fanrong.activity.HomeActivity;
import com.pancat.fanrong.activity.LoginActivity;
import com.pancat.fanrong.activity.MeActivity;
import com.pancat.fanrong.activity.MomentActivity;
import com.pancat.fanrong.activity.OrderActivity;
import com.pancat.fanrong.common.User;
import com.pancat.fanrong.util.CommonPushMsgUtils;
import com.pancat.fanrong.util.ConfigHelperUtils;
import com.pancat.fanrong.waterfall.bitmaputil.ImageResizer;


@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements OnClickListener{
    //TAG 调试所用
	private static final String TAG = "MainActivity";
	
	private LinearLayout container;
	//底部四个tab
	private LinearLayout tabHome,tabOrder,tabMe,tabMoment;
	private Window subActivity;
	private int segment = 1;
	
	private Resources resource;
	
	//发送圈子按钮
	private Button btnSendMoment;
	
	private final int FROM_CAMERA = 1;
	private final int FROM_LOCAL_FILE = 2;
	
	private AlertDialog addPicDialog;
	
	//手机屏幕的宽度，用来设置dialog的大小
	private int screenWidth;
	
	//图片存储路径
	private final String BASE_FILE_PATH = Environment.getExternalStorageDirectory() + "/rongmeme/";
	//拍照上传照片临时存放文件
	private final String TEMP_PHOTO_PATH = BASE_FILE_PATH + "temp.jpg";
	
	//消息推送的配置选项
	private int msgPushType = ConfigHelperUtils.BAIDUMSGPUSH;
	//个推消息推送密钥  必须在主Activity中或服务中起动推送服务
	private String appkey = "";
	private String appsecret = "";
	private String appid = "";
	//百度消息推送密钥
	private String api_key = "";
	
	/**
	 * 第三方应用Master Secret，修改为正确的值，为了安全起见，请勿移动
	 */
	private static final String MASTERSECRET = "WL5D8yz7sZAR7h6P3Ufe65";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WindowManager wm = this.getWindowManager();
		screenWidth = wm.getDefaultDisplay().getWidth();
		container = (LinearLayout)findViewById(R.id.container);
		init();
		segment = getIntent().getIntExtra("segment", 1);
		tabHome.setClickable(false);
		initMsgPush();//消息推送初始化
	}
	
	private void addView(){
		container.removeAllViews();
		Intent intent = new Intent();
		if(segment == 1){
			intent.setClass(MainActivity.this, HomeActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity1", intent);
		}
		else if(segment == 2){
			intent.setClass(MainActivity.this, OrderActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity2", intent);
		}
		else if(segment == 3){
			intent.setClass(MainActivity.this, MeActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity3", intent);
		}
		else if(segment == 4){
			intent.setClass(MainActivity.this, MomentActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity4", intent);
		}
		container.addView(subActivity.getDecorView());
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		addView();
	}

	private void init(){
		btnSendMoment = (Button)findViewById(R.id.add_to_moment);
		tabHome = (LinearLayout)findViewById(R.id.tab_home);
		tabOrder = (LinearLayout)findViewById(R.id.tab_order);
		tabMe = (LinearLayout)findViewById(R.id.tab_me);
		tabMoment = (LinearLayout)findViewById(R.id.tab_moment);
		tabHome.setOnClickListener(this);
		tabOrder.setOnClickListener(this);
		tabMe.setOnClickListener(this);
		tabMoment.setOnClickListener(this);
		btnSendMoment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!User.getInstance().isUserLogined())
				{
					Intent it=new Intent(MainActivity.this,LoginActivity.class);
					startActivity(it);
				}
				else{
				openAddDialog();
				}
			}
		});
	}
	
	/**
	 * 弹出添加圈子图片对话框
	 */
	private void openAddDialog(){
		
		// 创建AlertDialog
		addPicDialog = new AlertDialog.Builder(this).create();
		
		addPicDialog.show();
		Window addPicWindow = addPicDialog.getWindow();
		addPicWindow.setContentView(R.layout.add_photo);
		RelativeLayout uploadCamera = (RelativeLayout)addPicWindow.findViewById(R.id.upload_camera);
		RelativeLayout uploadFile = (RelativeLayout)addPicWindow.findViewById(R.id.upload_file);
		//照相上传图片点击事件
		uploadCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(TEMP_PHOTO_PATH)));
				startActivityForResult(intent, FROM_CAMERA);
			}
		});
		
		//本地图片上传点击事件
		uploadFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//到系统相册选择图片
				Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				
				startActivityForResult(intent, FROM_LOCAL_FILE);
			}
		});
	}
	
	/**
	 * 弹出发送圈子内容对话框
	 * @param path 图片存储路径
	 */
	private void openSendDialog(String path){
		AlertDialog sendDialog = new AlertDialog.Builder(this).create();
		//解决唤不出键盘问题
		sendDialog.setView(getLayoutInflater().inflate(R.layout.edit_moment, null));
		sendDialog.show();
		Window sendWindow = sendDialog.getWindow();
		sendWindow.setContentView(R.layout.edit_moment);
		EditText description = (EditText)sendWindow.findViewById(R.id.moment_des);			//对话框中的描述控件
		ImageView selectedImg = (ImageView)sendWindow.findViewById(R.id.selected_image);	//对话框中的图片控件
		Button btnSendMoment = (Button)sendWindow.findViewById(R.id.btn_send_moment);
		//设置图片控件参数
		android.view.ViewGroup.LayoutParams params = selectedImg.getLayoutParams();
		
		selectedImg.setScaleType(ScaleType.CENTER_CROP);
		
		//获取图片的原始宽和高
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int originalHeight = options.outHeight;
		int originalWidth = options.outWidth;
		//按比例缩放图片
		int iWidth = (int) (screenWidth*0.9);
		int iHeight = iWidth*originalHeight/originalWidth;
		params.height = iHeight;
		params.width = iWidth;
		selectedImg.setLayoutParams(params);
		//得到缩略图
		Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFile(path, iWidth, iHeight);
		selectedImg.setImageBitmap(bitmap);
		btnSendMoment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//这里实现点击发送圈子按钮操作(上传图文到服务器)
				Toast.makeText(MainActivity.this, "点击发送按钮", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK){
			if(addPicDialog.isShowing()){
				addPicDialog.dismiss();
			}
			if(requestCode == FROM_CAMERA){
				//图片从相机获取
				File file = new File(BASE_FILE_PATH);
				if(!file.exists()){
					file.mkdirs();
				}
				//打开发送对话框
				openSendDialog(TEMP_PHOTO_PATH);
			}
			else if(requestCode == FROM_LOCAL_FILE){
				//图片是从本地文件获取
				
				final Uri uri = data.getData();
				//以下操作获取本地图片存储路径
				String[] proj = {MediaStore.Images.Media.DATA};
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				final String path = cursor.getString(columnIndex);//本地图片存储路径
				//打开发送对话框
				openSendDialog(path);
			}
		}
	}

	/**
	 * 根据路径获取本地本地图片bitmap对象
	 * @param url 本地图片路径
	 * @return
	 */
	private Bitmap getLocalBitmap(String url){
		 try {
	          FileInputStream fis = new FileInputStream(url);
	          return BitmapFactory.decodeStream(fis);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	
	
	public void onClick(View v){
		
		Intent intent = new Intent();
		//改变点击的tab的图片和文字颜色并设置点击的tab为不可点击状态
		switch(v.getId()){
		case R.id.tab_home:
			resetBtn();
			ImageButton btnTabHome = (ImageButton)tabHome.findViewById(R.id.btn_tab_home);
			btnTabHome.setImageResource(R.drawable.icon_tab_home_unfold);
			tabHome.setClickable(false);
			intent.setClass(MainActivity.this, HomeActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity1", intent);
			container.addView(subActivity.getDecorView());
			segment = 1;
			break;
		case R.id.tab_order:
			resetBtn();
			ImageButton btnTabOrder = (ImageButton)tabOrder.findViewById(R.id.btn_tab_order);
			btnTabOrder.setImageResource(R.drawable.icon_tab_product_unfold);
			tabOrder.setClickable(false);
			intent.setClass(MainActivity.this, OrderActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity2", intent);
			container.addView(subActivity.getDecorView());
			segment = 2;
			break;
		case R.id.tab_me:
			resetBtn();
			ImageButton btnTabMe = (ImageButton)tabMe.findViewById(R.id.btn_tab_me);
			btnTabMe.setImageResource(R.drawable.icon_tab_me_unfold);
			tabMe.setClickable(false);
			
			intent.setClass(MainActivity.this,MeActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity3", intent);
			container.addView(subActivity.getDecorView());
			segment = 3;
			break;
		case R.id.tab_moment:
			resetBtn();
			ImageButton btnTabMoment = (ImageButton)tabMoment.findViewById(R.id.btn_tab_moment);
			btnTabMoment.setImageResource(R.drawable.icon_tab_mass_unfold);
			
			intent.setClass(MainActivity.this, MomentActivity.class);
			subActivity = getLocalActivityManager().startActivity(
					"subActivity4", intent);
			container.addView(subActivity.getDecorView());
			segment = 4;
			break;
		default:break;
		}
	}
	
	/**
	 * 清除按钮选中状态并设置所有按钮为可点击
	 */
	private void resetBtn() {
		container.removeAllViews();
		((ImageButton)tabHome.findViewById(R.id.btn_tab_home))
			.setImageResource(R.drawable.icon_tab_home_fold);
		((ImageButton)tabOrder.findViewById(R.id.btn_tab_order))
			.setImageResource(R.drawable.icon_tab_product_fold);
		((ImageButton)tabMe.findViewById(R.id.btn_tab_me))
			.setImageResource(R.drawable.icon_tab_me_fold);
		((ImageButton)tabMoment.findViewById(R.id.btn_tab_moment))
			.setImageResource(R.drawable.icon_tab_mass_fold);
		tabHome.setClickable(true);
		tabOrder.setClickable(true);
		tabMe.setClickable(true);
		tabMoment.setClickable(true);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		getLocalActivityManager().getCurrentActivity().onTouchEvent(event);
		return true;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}
	
	private void initMsgPush(){
		
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
	    		   .detectDiskWrites()
	    		   .detectNetwork()
	    		   .penaltyLog()
	    		   .build());
	     StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
	    		   .detectLeakedSqlLiteObjects()
	    		   .penaltyLog()
	    		   .penaltyDeath()
	    		   .build());
	     
		try{
			if(msgPushType == ConfigHelperUtils.GETUIMSGPUSH){
				appid = ConfigHelperUtils.getMetaValue(this, "PUSH_APPID");
				appsecret = ConfigHelperUtils.getMetaValue(this,"PUSH_APPSECRET");
				appkey = ConfigHelperUtils.getMetaValue(this, "PUSH_APPKEY");
				Log.d(TAG, "appid:"+appid+" appsecret:"+appsecret+" appkey:"+appkey);
			}else if(msgPushType == ConfigHelperUtils.BAIDUMSGPUSH){
				api_key = ConfigHelperUtils.getMetaValue(this, "api_key");
				Log.v(TAG,"api_key:"+api_key);
			}
		}catch(NameNotFoundException e){
			Log.d(TAG,"don't found config key");
		}
		if(msgPushType == ConfigHelperUtils.GETUIMSGPUSH){
			//个推消息推送初始化
			PushManager.getInstance().initialize(getApplicationContext());
			Log.d(TAG,"jogrunner initialize success");
			
		}else if(msgPushType == ConfigHelperUtils.BAIDUMSGPUSH){
			//TODO 暂未实现百度云推送服务
			com.baidu.android.pushservice.PushManager.startWork(getApplicationContext(), 
					PushConstants.LOGIN_TYPE_API_KEY, api_key);
			
			//设置百度云推送通知格式
			CommonPushMsgUtils temp = new CommonPushMsgUtils(this, msgPushType);
			temp.setBaiDuNotificationLayoutId(R.layout.notification_custom_builder, 
					R.id.notification_icon, R.id.notification_title,R.id.notification_text, android.R.drawable.ic_media_play);
			temp.customNotificationStyle();
			Log.d(TAG,"hello world! jogrunner");
		}
	}
}
