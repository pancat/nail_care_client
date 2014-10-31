package com.pancat.fanrong;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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
			userimage.setImageResource(R.drawable.player);
			userimage.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//alertdailog ask whether want to log
					Intent it = new Intent(UserCenterActivity.this,SignInActivity.class);
					startActivity(it);
				}
				
			});
		} else {
			String nicknamestr=AuthorizeMgr.getInstance().getUser().getNickname();
			nickname.setText(nicknamestr);
			userimage.setImageResource(R.drawable.user);
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
			addPicDialog.setView(getLayoutInflater().inflate(R.layout.add_photo, null));
			addPicDialog.show();
			addPicDialog.getWindow().setLayout((int) (screenWidth*0.95), WindowManager.LayoutParams.WRAP_CONTENT);
			addPicDialog.setCanceledOnTouchOutside(true);
			
			Window addPicWindow = addPicDialog.getWindow();
			addPicWindow.setContentView(R.layout.add_photo);

			/*
			LinearLayout uploadCamera = (LinearLayout)addPicWindow.findViewById(R.id.upload_camera);
			LinearLayout uploadFile = (LinearLayout)addPicWindow.findViewById(R.id.upload_file);
			final TextView circleDescription = (TextView)addPicWindow.findViewById(R.id.circle_description);
			btnSendCircle = (ImageButton)addPicWindow.findViewById(R.id.btn_send_circle);
			btnAddLocation = (Button)addPicWindow.findViewById(R.id.btn_add_location);
			selectedImgGridView = (GridView)addPicWindow.findViewById(R.id.selected_image_gridview);
			selectedImgGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			selectedImgAdapter = new SelectedImgAdapter(this, getResources(),handler);
			selectedImgGridView.setAdapter(selectedImgAdapter);
			selectedImgGridView.setOnItemClickListener(new OnItemClickListener() {
			
						
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Intent intent = new Intent(UserCenterActivity.this,PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
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
					Intent intent = new Intent(UserCenterActivity.this,ImageGridActivity.class);
					startActivityForResult(intent,FROM_LOCAL_FILE);
				}
			});
			//圈子发送按钮点击事件
			btnSendCircle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					
				}
			});
			*/
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
