package com.pancat.fanrong.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.edmodo.cropper.CropImageView;
import com.pancat.fanrong.R;

public class CropImageActivity extends Activity {


	// Static final constants
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final int ROTATE_NINETY_DEGREES = 90;
	private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
	private static final int ON_TOUCH = 1;
	private byte[] mContent;
	// Instance variables
	private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
	private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
	private CropImageView cropImageView;
	Bitmap croppedImage;
	private Uri imageUri;




	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Intent it = getIntent();
		// it.getExtras();

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cropimage);
		cropImageView = (CropImageView) findViewById(R.id.CropImageView);
		final Button cropButton = (Button) findViewById(R.id.Button_crop);
		final Button rotateButton = (Button) findViewById(R.id.Button_rotate);
		
		// 接受传入的uri，通过uri显示获取图片，显示图片，对现实的图片切割，然后再返回去
	    imageUri = it.getData();
		Log.i("imageuri", imageUri.toString());
		ContentResolver resolver = getContentResolver();
		try {
			mContent = readStream(resolver.openInputStream(Uri.parse(imageUri
					.toString())));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 将字节数组转换为ImageView可调用的Bitmap对象
		Bitmap myBitmap = getPicFromBytes(mContent, null);
		// 图片进行拉伸
		myBitmap = getBitmap(myBitmap);

		// //把得到的图片绑定在控件上显示
		cropImageView.setImageBitmap(myBitmap);
		// Sets initial aspect ratio to 10/10, for demonstration purposes
		cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES,
				DEFAULT_ASPECT_RATIO_VALUES);
		
		cropImageView.setFixedAspectRatio(true);

		// Initialize components of the app

		// Sets the rotate button

		rotateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
			}
		});
		
		cropButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				croppedImage = cropImageView.getCroppedImage();
				
				// bitmap croppedImage 写入 头像文件	 
			    Uri uri = imageUri;		    
			    int imagepathnum=uri.toString().indexOf("/storage");		   
			    String img_path = uri.toString().substring(imagepathnum);
			    Log.i("crop uri", uri.toString());
			    Log.i("crop filepathnum", ""+imagepathnum);
			    Log.i("crop filepath", img_path);
			    File filex = new File(img_path);		    
				if (filex.exists()) {
					filex.delete();
				}
				try {
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(filex));
					croppedImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					Log.i("save crop", "把剪切后的头像保存成功成功");
					Log.i("save", "success");
					bos.flush();
					bos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setResult(2,null);
				//startActivityForResult(intent, RESULT_REQUEST_CODE);
			    finish();
			}
		});

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	/**
	 * @param bitmap
	 * @return 如果图片太小，那么就拉伸
	 */
	public Bitmap getBitmap(Bitmap bitmap) {
		WindowManager wm = (WindowManager) getBaseContext().getSystemService(
				Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		float scaleWidth = 1, scaleHeight = 1;
		if (bitmap.getWidth() < width) {
			Log.i("lashen", "图片宽度小于屏幕");
			scaleWidth = (float) width / bitmap.getWidth();
			Log.i("lashen", "比例" + scaleWidth);
			scaleHeight = scaleWidth;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		Log.i("lashen", "对小图片进行了拉伸");
		return bitmap;
	}

}
