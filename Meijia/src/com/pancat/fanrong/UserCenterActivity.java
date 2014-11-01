package com.pancat.fanrong;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.pancat.fanrong.activity.ImageGridActivity;
import com.pancat.fanrong.activity.PhotoActivity;
import com.pancat.fanrong.activity.SignInActivity;
import com.pancat.fanrong.activity.TestLocation;
import com.pancat.fanrong.adapter.SelectedImgAdapter;
import com.pancat.fanrong.common.CircleImageView;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.mgr.AuthorizeMgr;
import com.pancat.fanrong.util.MapUtil;
import com.pancat.fanrong.util.album.Bimp;
import com.pancat.fanrong.util.album.FileUtils;

public class UserCenterActivity extends Activity {

	private Button btnSendMoment;
	private ImageButton btnSendCircle;
	private static Button btnAddLocation;
	
	private final int FROM_CAMERA = 1;
	private final int FROM_LOCAL_FILE = 2;

	private AlertDialog addPicDialog;
	private AlertDialog sendDialog;
	private ProgressDialog progressDialog;
	
	private int finishedUploadNum = 0;
	private static String address=null;
	
	private List<String> pathList;
	
	//手机屏幕的宽度，用来设置dialog的大小
	private int screenWidth;
	
	//选中图片的数据适配器
	private SelectedImgAdapter selectedImgAdapter; 
	private GridView selectedImgGridView;
	
	//from net
	 /*头像名称*/   
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";   
      
    /* 请求码*/   
    private static final int IMAGE_REQUEST_CODE = 0;    
    private static final int CAMERA_REQUEST_CODE = 1;    
    private static final int RESULT_REQUEST_CODE = 2;    
      //from net
	
	//图片存储路径
	private final String BASE_FILE_PATH = Environment.getExternalStorageDirectory() + "/rongmeme/";
	//拍照上传照片临时存放文件
//	private final String TEMP_PHOTO_PATH = BASE_FILE_PATH + "temp.jpg";
	private String cameraPicPath;
	
	
	private TextView nickname;
	private CircleImageView userimage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
		nickname = (TextView) findViewById(R.id.nick_name);
		userimage=( CircleImageView )findViewById(R.id.usercircleimage);
		setButtonListener();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUserInfomation();
		setLoginBtn();

	}
	

	
	private void setUserInfomation() {
		// TODO Auto-generated method stub
		if (AuthorizeMgr.getInstance().hasLogined() == false) {
			// set userimage and nickname = default
			nickname.setText(R.string.user_center_welcomepassager);
			//Drawable drawable=new Drawable(findViewById(R.drawable.player));
		//	userimage.setImageResource(R.drawable.player);
			userimage.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//alertdailog ask whether want to log
				 	 new AlertDialog.Builder(UserCenterActivity.this) 
				  	.setTitle("尚未登录")
				  	.setMessage("请问您现在要登录吗")
				  	.setPositiveButton("是",  new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							Intent it = new Intent(UserCenterActivity.this,SignInActivity.class);
							startActivity(it);
						}
				  		
				  	})
				  	.setNegativeButton("否", null)
				  	.show();
	
				}
				
			});
		} else {
			String nicknamestr=AuthorizeMgr.getInstance().getUser().getNickname();
			nickname.setText(nicknamestr);
		//	userimage.setImageResource(R.drawable.user);
			/*
			Resources res=getResources();
			Drawable drawable= res.getDrawable(R.drawable.user);
			userimage.setImageDrawable(drawable);
			*/
			
			userimage.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//upload user image
					showupload();
				}		
			});
		}
}
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
			//	selectedImgAdapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

public void showupload(){
	DisplayMetrics dm = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(dm);
	screenWidth = dm.widthPixels;
	// screenHeigh = dm.heightPixels;
			// 创建AlertDialog
			
			Bimp.bmp.clear();
			Bimp.drr.clear();
			Bimp.max = 0;
		//	handler.sendEmptyMessage(1);
			addPicDialog = new AlertDialog.Builder(this).create();
			//addPicDialog.setView(getLayoutInflater().inflate(R.layout.add_photo, null));
			addPicDialog.show();
			//addPicDialog.getWindow().setLayout((int) (screenWidth*0.95), WindowManager.LayoutParams.WRAP_CONTENT);
			addPicDialog.setCanceledOnTouchOutside(true);
			
			Window addPicWindow = addPicDialog.getWindow();
			addPicWindow.setContentView(R.layout.add_photo);
			RelativeLayout uploadCamera = (RelativeLayout)addPicWindow.findViewById(R.id.upload_camera);
			RelativeLayout uploadFile = (RelativeLayout)addPicWindow.findViewById(R.id.upload_file);
			
			//照相上传图片点击事件
			uploadCamera.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/*
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(BASE_FILE_PATH,String.valueOf(System.currentTimeMillis())+".jpg");
					cameraPicPath = file.getPath();
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					
					startActivityForResult(intent, CAMERA_REQUEST_CODE);
					*/
					
					 Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
                     // 判断存储卡是否可以用，可用进行存储    
                     if (hasSdcard()) {    
                         intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,  
                                 Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));  
                     }    
                     startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);  
				}
			});
			
			//本地图片上传点击事件
			uploadFile.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//到相册选择图片
				//	Intent intent = new Intent(UserCenterActivity.this,ImageGridActivity.class);
				//	startActivityForResult(intent,IMAGE_REQUEST_CODE);
					
					 Intent intentFromGallery = new Intent();    
                     intentFromGallery.setType("image/*"); // 设置文件类型    
                     intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);  
                   
                  //   Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                     startActivityForResult(intentFromGallery,IMAGE_REQUEST_CODE);
				}
			});
			
	}

@Override  
protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    // 结果码不等于取消时候  
    if (resultCode != RESULT_CANCELED) {  
        switch (requestCode) {  
        case IMAGE_REQUEST_CODE:  
        	Log.i("uri", " "+data);
            startPhotoZoom(data.getData());  
            break;  
        case CAMERA_REQUEST_CODE:  
            if (hasSdcard()) {  
                File tempFile = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);  
                startPhotoZoom(Uri.fromFile(tempFile));  
            } 

            break;  
        case RESULT_REQUEST_CODE:  
            if (data != null) {  
                setImageToView(data);  
            }  
            break;  
        }  
    }  
    super.onActivityResult(requestCode, resultCode, data);  
}  

private boolean hasSdcard() {
	// TODO Auto-generated method stub
	boolean sdCardExist = Environment.getExternalStorageState()
			.equals(android.os.Environment.MEDIA_MOUNTED);
	return sdCardExist;
}

public void startPhotoZoom(Uri uri) {    
    if(uri==null){  
        Log.i("tag", "The uri is not exist.");  
    }  
    Intent intent = new Intent("com.android.camera.action.CROP");    
    intent.setDataAndType(uri, "image/*");    
    // 设置裁剪    
    intent.putExtra("crop", "true");    
    // aspectX aspectY 是宽高的比例    
    intent.putExtra("aspectX", 1);    
    intent.putExtra("aspectY", 1);    
    // outputX outputY 是裁剪图片宽高    
    intent.putExtra("outputX", 320);    
    intent.putExtra("outputY", 320);    
    intent.putExtra("return-data", true);    
    startActivityForResult(intent, 2);    
}   

@SuppressLint("NewApi") public void setImageToView(Intent data) {    
    Bundle extras = data.getExtras();    
    if (extras != null) {    
        Bitmap photo = extras.getParcelable("data");    
        @SuppressWarnings("deprecation")
		Drawable drawable = new BitmapDrawable(photo);    
          
        //Drawable dra=new Drawable(photo);
        Toast.makeText(getApplication(), "成功设置了头像", Toast.LENGTH_LONG).show();
      //  userimage.setBackground(drawable);
        //userimage.setImageDrawable(drawable);
        userimage.setImageBitmap(photo);
  //      userimage.refreshDrawableState();
       // btn_crop.setBackgroundDrawable(drawable);  
       
    }    
}    
//发送头像结束


	private void setButtonListener() {
		@SuppressWarnings("rawtypes")
		Map<Button, Class> map = new HashMap<Button, Class>() {
			{
				put((Button) findViewById(R.id.address_btn), TestLocation.class);
			}
		};

		Iterator<Button> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			Button btn = (Button) iter.next();
			final Object val = map.get(btn);

			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent();
					intent.setClass(UserCenterActivity.this, (Class) val);
					startActivity(intent);
				}
			});
			
			
		}

		// 客服电话
		Button callBtn = (Button) findViewById(R.id.service_btn);
		callBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ getString(R.string.service_call_no)));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	protected void setLoginBtn() {

		Button loginBtn = (Button) findViewById(R.id.login_btn);

		if (AuthorizeMgr.getInstance().hasLogined() == false) {
			loginBtn.setText(R.string.action_sign_in_short);
			loginBtn.setBackgroundResource(R.drawable.btn_green_bg_selector);
			loginBtn.setTextColor(R.drawable.btn_green_bg_text_selector);

			loginBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent();
					intent.setClass(UserCenterActivity.this,
							SignInActivity.class);
					startActivity(intent);
				}
			});
		} else {
			loginBtn.setText(R.string.action_sign_out_short);
			loginBtn.setBackgroundResource(R.drawable.btn_red_bg_selector);
			loginBtn.setTextColor(R.drawable.btn_green_bg_text_selector);

			loginBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					AuthorizeMgr.getInstance().setLogout();
					
					setLoginBtn();
					UserCenterActivity.this.onResume();
				}
			});

		}
	}
}
