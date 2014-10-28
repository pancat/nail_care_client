package com.pancat.fanrong;



import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.igexin.sdk.PushManager;
import com.pancat.fanrong.activity.CircleActivity;
import com.pancat.fanrong.activity.HomeActivity;
import com.pancat.fanrong.activity.ImageGridActivity;
import com.pancat.fanrong.activity.OrderActivity;
import com.pancat.fanrong.activity.PhotoActivity;
import com.pancat.fanrong.activity.SignInActivity;
import com.pancat.fanrong.adapter.SelectedImgAdapter;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.mgr.AuthorizeMgr;
import com.pancat.fanrong.util.CommonPushMsgUtils;
import com.pancat.fanrong.util.ConfigHelperUtils;
import com.pancat.fanrong.util.album.Bimp;
import com.pancat.fanrong.util.album.FileUtils;


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
	private ImageButton btnSendCircle;
	
	private final int FROM_CAMERA = 1;
	private final int FROM_LOCAL_FILE = 2;
	
	private AlertDialog addPicDialog;
	private AlertDialog sendDialog;
	private ProgressDialog progressDialog;
	
	private int finishedUploadNum = 0;
	
	private List<String> pathList;
	
	//手机屏幕的宽度，用来设置dialog的大小
	private int screenWidth;
	
	//选中图片的数据适配器
	private SelectedImgAdapter selectedImgAdapter; 
	private GridView selectedImgGridView;
	
	//图片存储路径
	private final String BASE_FILE_PATH = Environment.getExternalStorageDirectory() + "/rongmeme/";
	//拍照上传照片临时存放文件
//	private final String TEMP_PHOTO_PATH = BASE_FILE_PATH + "temp.jpg";
	private String cameraPicPath;
	
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
			intent.setClass(MainActivity.this, UserCenterActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity3", intent);
		}
		else if(segment == 4){
			intent.setClass(MainActivity.this, CircleActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity4", intent);
		}
		container.addView(subActivity.getDecorView());
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				selectedImgAdapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	protected void onResume() {
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
				
				showpopwindow();
					//openAddDialog();

			}
		});
	}
	private void showpopwindow(){
		LayoutInflater inflater=getLayoutInflater();
		View dropDownList=inflater.inflate(R.layout.dropdownlist, null);
		final PopupWindow menuWindow = new PopupWindow(dropDownList,
				LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		menuWindow.setFocusable(true);
		menuWindow.setOutsideTouchable(true); 
		menuWindow.update(); 
		menuWindow.setBackgroundDrawable(new BitmapDrawable()); 
		menuWindow.showAsDropDown(btnSendMoment, -100, 12);
		Button sendmoment=(Button)dropDownList.findViewById(R.id.sendmomentbtn);
		Button usersetting=(Button)dropDownList.findViewById(R.id.usersettingbtn);
		sendmoment.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openAddDialog();
				menuWindow.dismiss();
			}
		});
		usersetting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {	
				if (AuthorizeMgr.getInstance().hasLogined() == false)
				{
					menuWindow.dismiss();
					Intent signInIntent = new Intent();
					signInIntent.setClass(MainActivity.this, SignInActivity.class);
					startActivity(signInIntent);	
				}
				// TODO 跳转到个人设置activity
				
				
			}	
		});
	}
	
	/**
	 * 弹出添加圈子图片对话框
	 */
	private void openAddDialog(){
		// 创建AlertDialog
		
		Bimp.bmp.clear();
		Bimp.drr.clear();
		Bimp.max = 0;
		handler.sendEmptyMessage(1);
		addPicDialog = new AlertDialog.Builder(this).create();
		addPicDialog.setView(getLayoutInflater().inflate(R.layout.dialog_add_circle, null));
		addPicDialog.show();
		addPicDialog.getWindow().setLayout((int) (screenWidth*0.95), WindowManager.LayoutParams.WRAP_CONTENT);
		addPicDialog.setCanceledOnTouchOutside(true);
		Window addPicWindow = addPicDialog.getWindow();
		addPicWindow.setContentView(R.layout.dialog_add_circle);
		LinearLayout uploadCamera = (LinearLayout)addPicWindow.findViewById(R.id.upload_camera);
		LinearLayout uploadFile = (LinearLayout)addPicWindow.findViewById(R.id.upload_file);
		final TextView circleDescription = (TextView)addPicWindow.findViewById(R.id.circle_description);
		btnSendCircle = (ImageButton)addPicWindow.findViewById(R.id.btn_send_circle);
		Button btnAddLocation = (Button)addPicWindow.findViewById(R.id.btn_add_location);
		selectedImgGridView = (GridView)addPicWindow.findViewById(R.id.selected_image_gridview);
		selectedImgGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		selectedImgAdapter = new SelectedImgAdapter(this, getResources(),handler);
		selectedImgGridView.setAdapter(selectedImgAdapter);
		selectedImgGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MainActivity.this,
						PhotoActivity.class);
				intent.putExtra("ID", arg2);
				startActivity(intent);
			}
			
		});
		//添加位置按钮点击事件
		btnAddLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		//照相上传图片点击事件
		uploadCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(BASE_FILE_PATH,String.valueOf(System.currentTimeMillis())+".jpg");
				cameraPicPath = file.getPath();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				
				startActivityForResult(intent, FROM_CAMERA);
			}
		});
		
		//本地图片上传点击事件
		uploadFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//到相册选择图片
				Intent intent = new Intent(MainActivity.this,ImageGridActivity.class);
				startActivityForResult(intent,FROM_LOCAL_FILE);
			}
		});
		//圈子发送按钮点击事件
		btnSendCircle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String requestUrl = "http://54.213.141.22/teaching/Platform/index.php/circle_service/save_circle";
				finishedUploadNum = 0;
				RequestParams params = new RequestParams();
				pathList = Bimp.drr;
				Log.i("pathlist",String.valueOf(pathList));
				String description = circleDescription.getText().toString();
				if(pathList.size() > 0){
					String path = pathList.get(0);
					File file = new File(path);
					List<Integer> sizeList = FileUtils.getImageSize(path);
					int width = sizeList.get(0);
					int height = sizeList.get(1);
					try {
						params.put("uid", String.valueOf(1));
						params.put("img", file);
						params.put("description", description);
						params.put("width", String.valueOf(width));
						params.put("height",String.valueOf(height));
						params.put("cre_time",String.valueOf(System.currentTimeMillis()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					//设置ProgressDialog参数
					progressDialog = new ProgressDialog(MainActivity.this);
					progressDialog.setTitle("uploading...");
					progressDialog.setMessage(finishedUploadNum+" / "+pathList.size());
					progressDialog.setCancelable(false);
					progressDialog.show();
					RestClient.getInstance().postFromAbsoluteUrl(MainActivity.this, requestUrl, params,
							new AsyncHttpResponseHandler(){
								
								@Override
								public void onSuccess(String content) {
									//圈子创建成功，继续通过圈子id上传图片
									super.onSuccess(content);
									finishedUploadNum++;
									progressDialog.setMessage(finishedUploadNum+" / "+pathList.size());
									int circleId = Integer.valueOf(content);
									if(pathList.size() > 1){
										for(int i = 1;i < pathList.size();i++){
											String imgPath = pathList.get(i);
											uploadCircleImg(circleId, imgPath); 
										}
									}
									else{
										progressDialog.dismiss();
										Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_LONG).show();
										addPicDialog.dismiss();
									}
								}

								@Override
								public void onFailure(Throwable error,
										String content) {
									super.onFailure(error, content);
									progressDialog.dismiss();
									Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_LONG).show();
								}
					});
				}
			}
		});
	}
	
	public void uploadCircleImg(int circleId,String path){
		String url = "http://54.213.141.22/teaching/Platform/index.php/circle_service/save_circle_img";
		RequestParams params = new RequestParams();
		File file = new File(path);
		List<Integer> imageSize = FileUtils.getImageSize(path);
		int width = imageSize.get(0);
		int height = imageSize.get(1);
		
		try {
			params.put("circle_id", String.valueOf(circleId));
			params.put("height", String.valueOf(height));
			params.put("width", String.valueOf(width));
			params.put("img",file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RestClient.getInstance().postFromAbsoluteUrl(this, url, params, 
				new AsyncHttpResponseHandler(){

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						finishedUploadNum++;
						progressDialog.setMessage(finishedUploadNum+" / "+pathList.size());
						if(finishedUploadNum == pathList.size()){
							progressDialog.dismiss();
							Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_LONG).show();
							addPicDialog.dismiss();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Toast.makeText(MainActivity.this, "上传中断，上传成功"+finishedUploadNum+"，上传失败"+(pathList.size()-finishedUploadNum), Toast.LENGTH_LONG).show();
						addPicDialog.dismiss();
					}
			
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == FROM_CAMERA){
				//图片从相机获取
				selectedImgGridView.setVisibility(View.VISIBLE);
				if (Bimp.drr.size() < 9) {
					Bimp.drr.add(cameraPicPath);
				}
			}
			else if(requestCode == FROM_LOCAL_FILE){
				//图片是从本地文件获取
				
				//得到选择的图片路径列表
				selectedImgGridView.setVisibility(View.VISIBLE);
				ArrayList<String> list = (ArrayList<String>)data.getSerializableExtra(ImageGridActivity.SELECTED_IMAGE_LIST);
				Log.i(ImageGridActivity.SELECTED_IMAGE_LIST,String.valueOf(list));
			}
		}
	}

	
	
	@Override
	protected void onRestart() {
		if(selectedImgAdapter != null){
			selectedImgAdapter.loading();
		}
		super.onRestart();
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
			
			// 验证是否已登录
			if (AuthorizeMgr.getInstance().hasLogined() == false)
			{
				Intent signInIntent = new Intent();
				signInIntent.setClass(MainActivity.this, SignInActivity.class);
				startActivity(signInIntent);
				break;
			}
			
			resetBtn();
			ImageButton btnTabMe = (ImageButton)tabMe.findViewById(R.id.btn_tab_me);
			btnTabMe.setImageResource(R.drawable.icon_tab_me_unfold);
			tabMe.setClickable(false);
			
			intent.setClass(MainActivity.this,UserCenterActivity.class);
			subActivity = getLocalActivityManager().startActivity("subActivity3", intent);
			container.addView(subActivity.getDecorView());
			segment = 3;
			break;
		case R.id.tab_moment:
			resetBtn();
			ImageButton btnTabMoment = (ImageButton)tabMoment.findViewById(R.id.btn_tab_moment);
			btnTabMoment.setImageResource(R.drawable.icon_tab_mass_unfold);
			
			intent.setClass(MainActivity.this, CircleActivity.class);
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