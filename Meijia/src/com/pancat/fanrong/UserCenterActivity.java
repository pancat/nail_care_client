package com.pancat.fanrong;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pancat.fanrong.activity.CropImageActivity;
import com.pancat.fanrong.activity.SignInActivity;
import com.pancat.fanrong.activity.TestLocation;
import com.pancat.fanrong.adapter.SelectedImgAdapter;
import com.pancat.fanrong.common.CircleImageView;
import com.pancat.fanrong.common.Constants;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.mgr.AuthorizeMgr;
import com.pancat.fanrong.util.album.Bimp;
import com.pancat.fanrong.waterfall.bitmaputil.ImageResizer;

public class UserCenterActivity extends Activity {

	private final int FROM_CAMERA = 1;
	private final int FROM_LOCAL_FILE = 2;

	private AlertDialog addPicDialog;
	private int buttonHeight;
	private Button callBtn, loginBtn;
	private static boolean hasuserimage = false;

	// 手机屏幕的宽度，用来设置dialog的大小
	private int screenWidth;

	// 选中图片的数据适配器
	private SelectedImgAdapter selectedImgAdapter;
	private GridView selectedImgGridView;
	private File userimagefile;
	// from net
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	// from net
	
	private String cameraPicPath;
 	private String nick=null;
	private TextView nickname;
	private CircleImageView userimage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
		nickname = (TextView) findViewById(R.id.nick_name);
		userimage = (CircleImageView) findViewById(R.id.usercircleimage);
		callBtn = (Button) findViewById(R.id.service_btn);
		loginBtn = (Button) findViewById(R.id.login_btn);
		Log.i("user-image-path",Constants.USER_IMAGE_PATH);
		setButtonListener();
		userimagefile=new File(MainApplication.getAppContext().getExternalFilesDir("userimage"),IMAGE_FILE_NAME);
		 ViewTreeObserver vto2 = callBtn.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				callBtn.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				buttonHeight = callBtn.getHeight();
				loginBtn.setHeight(buttonHeight);
			}
		});
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
			Drawable userdefaultdr = getResources().getDrawable(
					R.drawable.user_default_unsigned_avater);
			userimage.setImageDrawable(userdefaultdr);
			userimage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// alertdailog ask whether want to log
					new AlertDialog.Builder(UserCenterActivity.this)
							.setTitle("尚未登录")
							.setMessage("请问您现在要登录吗")
							.setPositiveButton(
									"是",
									new android.content.DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											Intent it = new Intent(
													UserCenterActivity.this,
													SignInActivity.class);
											startActivity(it);
										}

									}).setNegativeButton("否", null).show();

				}

			});
		} else {
			String nicknamestr = AuthorizeMgr.getInstance().getUser()
					.getNickname();

			// AuthorizeMgr.setLastUserInfomationFromSer();
			
			if(nicknamestr.isEmpty())
			{
				nickname.setText("Click to set nickname");
			}else{
			nickname.setText(nicknamestr);
			}
	
			Log.i("userimage", AuthorizeMgr.getInstance().getUser()
					.getAvatarUri());
			if (!hasuserimage)
			// 显示本地默认头像
			{
				Drawable userdefaultdr = getResources().getDrawable(
						R.drawable.user_default_avater);
				userimage.setImageDrawable(userdefaultdr);
			}
			// 从网络下载头像
			// InputStream inputStream =
			// HttpUtil.getImageViewInputStream(AuthorizeMgr.getInstance().getUser().getAvatarUri());
			// Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			// userimage.setImageBitmap(bitmap);

			// 如果本地缓存了
			// byte[] data =
			// HttpUtil.getImageViewArray(AuthorizeMgr.getInstance().getUser().getAvatarUri());
			// Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
			// data.length);
			// userimage.setImageBitmap(bitmap);

			//File file = new File(MainApplication.getAppContext()
				//	.getExternalFilesDir("userimage"), IMAGE_FILE_NAME);
			// Log.i("externailfilesdir",
			// MainApplication.getAppContext().getExternalFilesDir("good").toString());
			if (userimagefile.exists()) {
				// Drawable
				// userfaceimage=Drawable.createFromPath(MainApplication.getAppContext().getExternalFilesDir("image")+"/"+IMAGE_FILE_NAME);
				Bitmap imageBitmap = BitmapFactory.decodeFile(userimagefile.getPath());
				userimage.setImageBitmap(imageBitmap);
			}
			//设置昵称事件
			nickname.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// upload user image
					showSetNickname();
				}
			});
			//设置头像事件
			userimage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// upload user image
					showupload();
				}
			});
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// selectedImgAdapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};
	public void showSetNickname() {
		// TODO Auto-generated method stub


	    final EditText et = new EditText(this);  
	    new AlertDialog.Builder(this)  
	    .setIcon(android.R.drawable.btn_star)
	    .setInverseBackgroundForced(true)
	    .setView(et)  
	    .setTitle("设置昵称")
	    .setNegativeButton("确定", new DialogInterface.OnClickListener() {  
	        public void onClick(DialogInterface dialog, int which) {  
	        		//设置昵称
	        	 nick=et.getText().toString();
	        	
	        	String url = "user/update_nickname";
	    		RequestParams params = new RequestParams();
	    		params.put("id",String.valueOf( AuthorizeMgr.getInstance().getUser().getId()));
	    		params.put("sessionid",  AuthorizeMgr.getInstance().getUser().getSessionid());
	    		params.put("nickname", nick);// 不用md5登录

	    		RestClient.getInstance().post(MainApplication.getAppContext(), url,
	    				params, adBannerReadyHandler);
	        }  
	        })  
	    .setPositiveButton("取消", null)  
	    .show();
	}

	final AsyncHttpResponseHandler adBannerReadyHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			Log.i("change nickname", "change nickname fail");
		
		}

		@Override
		public void onSuccess(String content) {
			Log.i("change nickname", "change nickname success");
			Log.i("change nickname", content);
			super.onSuccess(content);
			
			Message msg = new Message();
			msg.what =  1;
			handlers.handleMessage(msg);
		
		}
	};
	Handler handlers=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==1){
				AuthorizeMgr.getInstance().getUser().setNickname(nick);
				nickname.setText(AuthorizeMgr.getInstance().getUser().getNickname());
			}
			
			super.handleMessage(msg);
			
		}
		
	};
	public void showupload() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		// screenHeigh = dm.heightPixels;
		// 创建AlertDialog

		Bimp.bmp.clear();
		Bimp.drr.clear();
		Bimp.max = 0;
		// handler.sendEmptyMessage(1);
		addPicDialog = new AlertDialog.Builder(this).create();
		// addPicDialog.setView(getLayoutInflater().inflate(R.layout.add_photo,
		// null));
		addPicDialog.show();
		// addPicDialog.getWindow().setLayout((int) (screenWidth*0.95),
		// WindowManager.LayoutParams.WRAP_CONTENT);
		addPicDialog.setCanceledOnTouchOutside(true);

		Window addPicWindow = addPicDialog.getWindow();
		addPicWindow.setContentView(R.layout.add_photo);
		RelativeLayout uploadCamera = (RelativeLayout) addPicWindow
				.findViewById(R.id.upload_camera);
		RelativeLayout uploadFile = (RelativeLayout) addPicWindow
				.findViewById(R.id.upload_file);

		// 照相上传图片点击事件
		uploadCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * File file=new
				 * File(MainApplication.getAppContext().getExternalFilesDir
				 * ("good"),"faceimage.jpg"); Log.i("externailfilesdir",
				 * MainApplication
				 * .getAppContext().getExternalFilesDir("good").toString()); try
				 * { file.createNewFile(); } catch (IOException e) {
				 * 
				 * e.printStackTrace(); }
				 */
				Intent intentFromCapture = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				Log.i("this getfile", MainApplication.getAppContext()
						.getFilesDir().toString());
				// 判断存储卡是否可以用，可用进行存储
			//	File file=new File(MainApplication.getAppContext().getExternalFilesDir("userimage"),IMAGE_FILE_NAME);
			//	cameraPicPath = userimagefile.getPath();
				if (hasSdcard()) {
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(userimagefile));
				}

				startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
			}
		});

		// 本地图片上传点击事件
		uploadFile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intentFromGallery = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				// Intent i = new Intent( Intent.ACTION_PICK,
				// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				Log.i("uri", " " + data);
				Intent ittocrop = new Intent(this, CropImageActivity.class);

				final BitmapFactory.Options options = new BitmapFactory.Options();  
			    options.inJustDecodeBounds = true;  
			    
			    Uri uri = data.getData();
			    String[] proj = { MediaStore.Images.Media.DATA }; 
			    Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);	     
			    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);	     
			    actualimagecursor.moveToFirst();	     
			    String img_path = actualimagecursor.getString(actual_image_column_index);    
			    File fileb = new File(img_path);
			    Log.i("uri to filepath",fileb.getPath());
			    BitmapFactory.decodeFile(fileb.getPath(), options);  
			 // Calculate inSampleSize  
			    options.inSampleSize =  ImageResizer.calculateInSampleSize(options,300,300);
			  
			    // Decode bitmap with inSampleSize set  
			    options.inJustDecodeBounds = false;  			      
			    Bitmap bm = BitmapFactory.decodeFile(fileb.getPath(), options);  
			    
			    
			 //   File filex=new File(MainApplication.getAppContext().getExternalFilesDir("userimage"),IMAGE_FILE_NAME);
				if (userimagefile.exists()) {
					userimagefile.delete();
				}
				try {
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(userimagefile));
					bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					Log.i("resize", "压缩成功");
					bos.flush();
					bos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//然后把压缩后的文件的path转换成uri传给剪裁activiy
				Uri uri2=Uri.fromFile(userimagefile);
				ittocrop.setData(uri2);
				// 使用自定义剪裁，尚未完成
				 startActivityForResult(ittocrop,RESULT_REQUEST_CODE);
				// 使用系统自带剪裁
				//startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				// 图片从相机获取
		//	File filea = new File(cameraPicPath);
			
				
				Bitmap resizeBitmap = ImageResizer.decodeSampledBitmapFromFile(userimagefile.getPath(), 300, 300);		
				if (userimagefile.exists()) {
					userimagefile.delete();
				}
				try {
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(userimagefile));
					resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					Log.i("resize", "压缩成功");
					bos.flush();
					bos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			if (hasSdcard()) {
				File tempFile = new File(MainApplication.getAppContext()
							.getExternalFilesDir("userimage"), IMAGE_FILE_NAME);
			
				if (!tempFile.exists()) {
						try {
						tempFile.createNewFile();
					} catch (IOException e) {
					
						e.printStackTrace();
					}
					
				}
					
					Intent ittocrop2 = new Intent(this, CropImageActivity.class);
					ittocrop2.setData(Uri.fromFile(userimagefile));
					// 使用自定义剪裁，尚未完成
					 startActivityForResult(ittocrop2,RESULT_REQUEST_CODE);
					// 使用系统自带剪裁
					//startPhotoZoom(Uri.fromFile(tempFile));
				}

				break;
			case RESULT_REQUEST_CODE:
				addPicDialog.dismiss();
				/**
				 * 开启一个后台程序把头像上传到服务器
				 */
				
				
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	private boolean hasSdcard() {
		// TODO Auto-generated method stub
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		return sdCardExist;
	}

	public void startPhotoZoom(Uri uri) {
		if (uri == null) {
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
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	@SuppressLint("NewApi")
	public void setImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(photo);

			// Drawable dra=new Drawable(photo);
			//Toast.makeText(getApplication(), "成功设置了头像", Toast.LENGTH_LONG).show();
			// userimage.setBackground(drawable);
			// userimage.setImageDrawable(drawable);
			userimage.setImageBitmap(photo);
			hasuserimage = true;
			// userimage.refreshDrawableState();
			// btn_crop.setBackgroundDrawable(drawable);

		}
	}

	// 发送头像结束

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

		callBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ getString(R.string.service_call_no)));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		// 用户订单
		Button orderBtn = (Button) findViewById(R.id.order_btn);
		orderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(UserCenterActivity.this,
						MainActivity.class);
				it.putExtra("segment", 2);
				startActivity(it);
			}

		});
	}

	protected void setLoginBtn() {

		if (AuthorizeMgr.getInstance().hasLogined() == false) {
			loginBtn.setText(R.string.action_sign_in_short);
			loginBtn.setBackgroundResource(R.drawable.btn_green_bg_selector);
			loginBtn.setTextColor(R.drawable.btn_green_bg_text_selector);
			setUserInfomation();
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
