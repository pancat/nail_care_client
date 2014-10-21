package com.pancat.fanrong.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.CircleComment;
import com.pancat.fanrong.common.CircleImageView;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.waterfall.view.XListView;
import com.pancat.fanrong.waterfall.view.XListView.IXListViewListener;

public class CircleCommentActivity extends Activity implements IXListViewListener{

	private XListView mAdapterView;
	private CommentAdapter mAdapter;
	private Button mSendComment;
	private List<CircleComment> mComments = new ArrayList<CircleComment>();
	
	private int circleId;
	//请求数据起始索引号
	private int mIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		init();
		circleId = getIntent().getIntExtra("circleId", 0);
		onLoadMore();
	}

	/**
	 * 初始化成员变量
	 */
	private void init(){
		mSendComment = (Button)findViewById(R.id.btn_send_comment);
		mAdapterView = (XListView)findViewById(R.id.comment_list);
		mAdapterView.setPullLoadEnable(true);
		//设置列表不可刷新只可以下拉加载更多
		mAdapterView.setPullRefreshEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapter = new CommentAdapter(this,mAdapterView);
		mAdapterView.setAdapter(mAdapter);
	}
	

	
	@Override
	public void onRefresh() {
		Toast.makeText(this, "refreshing", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLoadMore() {
		String url = "http://54.213.141.22/teaching/Platform/index.php/circle_service/get_comment_list";
		RequestParams params = new RequestParams();
		params.put("circle_id", String.valueOf(circleId));
		params.put("index", String.valueOf(mIndex));
		RestClient.getInstance().postFromAbsoluteUrl(this, url, params, 
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
								mComments.add(comment);
							}
							//重新设置起始索引号
							mIndex += jsonArray.length();
							mAdapterView.stopLoadMore();
							mAdapter.notifyDataSetChanged();
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
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.to_comment_page:
			Intent intent = new Intent(CircleCommentActivity.this, CircleCommentActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	class CommentAdapter extends BaseAdapter{

		private Context mContext;
		private XListView mListView;
		
		public CommentAdapter(Context context,XListView listView){
			mContext = context;
			mListView = listView;
		}
		
		@Override
		public int getCount() {
			return mComments.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			CircleComment comment = mComments.get(position);
			if(convertView == null){
				LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
				convertView = layoutInflater.inflate(R.layout.circle_comment_item, null);
				holder = new ViewHolder();
				holder.userImg = (CircleImageView)convertView.findViewById(R.id.comment_user_img);
				
				holder.userName = (TextView)convertView.findViewById(R.id.comment_user_name);
				holder.content = (TextView)convertView.findViewById(R.id.comment_content);
				holder.commentTime = (TextView)convertView.findViewById(R.id.comment_time);
				convertView.setTag(holder);
			}
			holder = (ViewHolder)convertView.getTag();
//			holder.userImg.setImageResource(R.drawable.player);
			holder.userName.setText("Tom");
			holder.content.setText(comment.getComment().toString());
			holder.commentTime.setText(comment.getCommentTime().toString());
//			convertView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					
//				}
//			});
			return convertView;
		}
		
		class ViewHolder{
			CircleImageView userImg;
			TextView userName;
			TextView content;
			TextView commentTime;
		}
	}


}
