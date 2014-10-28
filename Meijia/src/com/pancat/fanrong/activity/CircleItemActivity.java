package com.pancat.fanrong.activity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Circle;
import com.pancat.fanrong.bean.CircleComment;
import com.pancat.fanrong.common.CircleImageView;
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
	
	private LinearLayout mCommentInfo;
	
	private Button mBtnAllComments;
	//当前页面显示的评论数，已有四条则不再添加
	private int mCurrentCommentNum = 0;
	
	/**
	 * 获取评论条数并显示
	 */
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				String url = "http://54.213.141.22/teaching/Platform/index.php/circle_service/get_comments_count?circle_id="+circleId;
				try {
					String json = RestClient.getInstance().getStringFromUrl(url);
					if(json != null){
						
						mCommentCount.setText(json);
						mBtnAllComments.setText("全部评论("+json+"条)");
						int commentCount = Integer.valueOf(json);//评论总数
						if(commentCount>0){
							if(mCurrentCommentNum < 4){
								String requestUrl = "http://54.213.141.22/teaching/Platform/index.php/circle_service/get_comment_list";
								RequestParams params = new RequestParams();
								params.put("circle_id", String.valueOf(circleId));
								params.put("index", String.valueOf(mCurrentCommentNum));
								params.put("size",String.valueOf(4-mCurrentCommentNum));
								RestClient.getInstance().postFromAbsoluteUrl(CircleItemActivity.this, requestUrl, params, 
									new AsyncHttpResponseHandler(){

										@Override
										public void onSuccess(String content) {
											super.onSuccess(content);
											try {
												
												JSONArray jsonArray = new JSONArray(content);
												for(int i = 0; i < jsonArray.length();i++){
													JSONObject jsonObject = jsonArray.getJSONObject(i);
													CircleComment comment = new CircleComment();
													comment.setId(jsonObject.getInt("comment_id"));
													comment.setCircleId(jsonObject.getInt("circle_id"));
													comment.setComment(jsonObject.getString("comment"));
													comment.setCommentTime(jsonObject.getString("comment_time"));
													comment.setUid(jsonObject.getInt("uid"));
													mCurrentCommentNum++;
													mCommentInfo.addView(addCommentInfo(comment));
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}

										@Override
										public void onFailure(Throwable error, String content) {
											super.onFailure(error, content);
										}
								});
							}
						}
						else{
							
						}
					}
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
		mCommentInfo = (LinearLayout)findViewById(R.id.circle_comment_ll);
		mBtnAllComments = (Button)findViewById(R.id.btn_all_comments);
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
		//设置ImageView的显示宽度和高度
		ViewTreeObserver observer = mImgParent.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				mImgParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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
	 * 添加显示最多前四条评论
	 * @return
	 */
	private View addCommentInfo(CircleComment comment){
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = (View)inflater.inflate(R.layout.item_circle_comment,null);
		CircleImageView userImg = (CircleImageView)view.findViewById(R.id.comment_user_img);
		TextView username = (TextView)view.findViewById(R.id.comment_user_name);
		TextView commentContent = (TextView)view.findViewById(R.id.comment_content);
		TextView commentTime = (TextView)view.findViewById(R.id.comment_time);
//		userImg.setImageBitmap();
		username.setText("Mary");
		commentContent.setText(comment.getComment());
		commentTime.setText(comment.getCommentTime());
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
			//同步评论到服务器并刷新本页面
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
					handler.sendEmptyMessage(1);
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
		case R.id.btn_all_comments:
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
