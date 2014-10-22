package com.pancat.fanrong.activity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.baidu.platform.comapi.map.m;
import com.pancat.fanrong.MainActivity;
import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.bean.CircleComment;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.waterfall.bitmaputil.ImageFetcher;

/**
 * 圈子item的具体内容
 * @author trh
 *
 */
public class CircleItemActivity extends Activity{

	private LinearLayout mImgParent;
	private TextView mImgPath;
	//评论内容
	private EditText mCommentContent;
	private TextView mCommentCount;
	
	private TextView mDescription;
	private TextView mCreTime;
	
	private int circleId;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				String url = "http://54.213.141.22/teaching/Platform/index.php/circle_service/get_comments_count?circle_id="+circleId;
				try {
					String json = RestClient.getInstance().getStringFromUrl(url);
					mCommentCount.setText(json);
					Log.e("json", json);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_item);
		
		//初始化控件
		mCommentContent = (EditText)findViewById(R.id.comment_content);
		mCommentCount = (TextView)findViewById(R.id.tv_comment_count);
		mDescription = (TextView)findViewById(R.id.description);
		mCreTime = (TextView)findViewById(R.id.added_time);
		handler.sendEmptyMessage(1);
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		//获取从上一个页面传过来的本条圈子信息
		final Circle info = (Circle)data.getSerializable("circle");
		final int originalWidth = info.getWidth();
		final int originalHeight = info.getHeight();
		//获取该条圈子的id
		circleId = info.getId();
		
		//设置页面控件显示内容
		mDescription.setText(info.getDescription().toString());
		mCreTime.setText("added in "+info.getCreTime().toString());
		
		mImgParent = (LinearLayout)findViewById(R.id.img_parent_ll);
		mImgPath = (TextView)findViewById(R.id.img_path);
		//设置ImageView的显示宽度和高度
		ViewTreeObserver observer = mImgParent.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				mImgParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mImgPath.setText(info.getPath());
				int  parentWidth = mImgParent.getWidth();
				//按比例缩放图片
				int iWidth = parentWidth;
				int iHeight = iWidth*originalHeight/originalWidth;
				mImgParent.addView(addImageView(iWidth, iHeight,info.getPath()));
			}
		});
	}

	private final View addImageView(int width,int height,String src){
		LinearLayout view = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		//设置LinearLayout布局参数
		view.setLayoutParams(lp);
		view.setOrientation(LinearLayout.VERTICAL);
		view.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		//定义ImageView的布局
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(width, height);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(vlp);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		ImageFetcher fetcher = new ImageFetcher(this, 720);
		fetcher.loadImage(src, imageView);
		view.addView(imageView);
		return view;
	}
	
	/**
	 * 页面控件点击事件
	 * @param v
	 */
	public void onClick(View v){
		Intent intent;
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_send_comment:
			final ProgressDialog progressDialog = ProgressDialog.show(CircleItemActivity.this, "uploading", "Please wait...");
			String url = "http://54.213.141.22/teaching/Platform/index.php/circle_service/add_comment";
			//获取评论内容
			String content = mCommentContent.getText().toString();
			//同步评论到服务器并且跳转到评论页面
			RequestParams params = new RequestParams();
			params.put("circle_id", String.valueOf(circleId));
			params.put("comment", content);
			params.put("uid", "1");
			RestClient.getInstance().postFromAbsoluteUrl(this, url, params, 
					new AsyncHttpResponseHandler(){
				
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					mCommentContent.setText("");
//					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					progressDialog.dismiss();
					Toast.makeText(CircleItemActivity.this, "评论成功", Toast.LENGTH_LONG).show();
					try {
						
						JSONObject jsonObject = new JSONObject(content);
						int commentsCount = jsonObject.getInt("comments_count");
						mCommentCount.setText(String.valueOf(commentsCount));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}

				@Override
				public void onFailure(Throwable error,
						String content) {
					
					super.onFailure(error, content);
					progressDialog.dismiss();
					Toast.makeText(CircleItemActivity.this, "评论失败", Toast.LENGTH_LONG).show();
				}

			});
			break;
		case R.id.to_comment_page:
			intent = new Intent(CircleItemActivity.this, CircleCommentActivity.class);
			//携带圈子id跳转到评论页面
			intent.putExtra("circleId", circleId);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
