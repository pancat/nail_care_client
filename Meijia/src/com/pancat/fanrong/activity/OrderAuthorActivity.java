package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.customview.FreeTimeTableView;
import com.pancat.fanrong.customview.FreeTimeTableView.OnButtonClick;
import com.pancat.fanrong.util.LocalDateUtils;
import com.pancat.fanrong.viewpagerindicator.IconPagerAdapter;
import com.pancat.fanrong.viewpagerindicator.LinePageIndicator;
import com.pancat.fanrong.viewpagerindicator.TabPageIndicator;
import com.pancat.fanrong.viewpagerindicator.TitlePageIndicator;
import com.pancat.fanrong.viewpagerindicator.UnderlinePageIndicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class OrderAuthorActivity extends FragmentActivity implements OnButtonClick {
    private static final String TAG = "OrderAuthorActivity";
	
	private TextView mTime;
	private TextView mPosition;
	private TextView mDetail;
    private View mMoreLabel;
    private EditText mMore;
    private ImageView timeCancel;
    private ImageView positionCancel;
    private ImageView detailCancel;
    private TextView affirmOrder;
    
    private PopupWindow freeTimePopWindow = null;
    private FreeTimeTableView timeView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_author_activity);
		
		initView();
	}

	//添加响应事件
	private void initView(){
		mTime = (TextView)findViewById(R.id.order_author_activity_time);
		mPosition = (TextView)findViewById(R.id.order_author_activity_position);
		mDetail = (TextView)findViewById(R.id.order_author_activity_detail);
		timeCancel = (ImageView)findViewById(R.id.order_author_activity_cancel_time);
		positionCancel = (ImageView)findViewById(R.id.order_author_activity_cancel_position);
		detailCancel = (ImageView)findViewById(R.id.order_author_activity_cancel_detail);
		affirmOrder = (TextView)findViewById(R.id.order_author_activity_affirm);
		
		//初始化不显示取消按钮
		timeCancel.setVisibility(View.GONE);
		positionCancel.setVisibility(View.GONE);
		detailCancel.setVisibility(View.GONE);
		
		mMoreLabel = findViewById(R.id.order_author_activity_morelabel);
		mMore = (EditText)findViewById(R.id.order_author_activity_moremsg);
		
		mMore.setVisibility(View.GONE);
		
		hideSoftKeyBoard(mTime);

		setListenEvent();
		setCancelListener();
	}
	
	//添加响应事件
	private void setListenEvent()
	{   
		//设置时间组件的监听事件
		mTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				hideSoftKeyBoard(view);
				showWindow();
			}
		});
		
		//通过实现一个地理位置 跳转到可搜索页面
		mPosition.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//详细地址点击
		mDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mMoreLabel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mMore.getVisibility() == View.VISIBLE)
					mMore.setVisibility(View.GONE);
				else mMore.setVisibility(View.VISIBLE);
			}
		});
		
		final Context context = this;
		//提交按钮监听事件
		affirmOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,OrderInfoActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void setCancelListener()
	{
		timeCancel.setOnClickListener(new OnClickListener() {
			final TextView timeTmp = mTime;
			final String str = getResources().getString(R.string.order_time);
			@Override
			public void onClick(View v) {
				timeTmp.setText(str);
				v.setVisibility(View.GONE);
			}
		});
		
		positionCancel.setOnClickListener(new OnClickListener() {
			final TextView positionTmp = mPosition;
			final String str = getResources().getString(R.string.location);
			@Override
			public void onClick(View v) {
				positionTmp.setText(str);
				v.setVisibility(View.GONE);
			}
		});
		
		detailCancel.setOnClickListener(new OnClickListener() {
			final TextView detailTmp = mDetail;
			final String str = getResources().getString(R.string.detail_location);
			@Override
			public void onClick(View v) {
				detailTmp.setText(str);
				v.setVisibility(View.GONE);
			}
		});
		
	}
	public void showWindow(){
		ArrayList<Map<Integer,Integer>>m = new ArrayList<Map<Integer,Integer>>();
		Map<Integer,Integer>ma = new HashMap<Integer,Integer>();
		for(int i=9;i<18;i++)ma.put(i, FreeTimeTableView.IDLE);
		m.add(ma);
	
		if(freeTimePopWindow == null){
			timeView = (new FreeTimeTableView(this).setData(m));
			timeView.setButtonClickListener(this);
			
			freeTimePopWindow = new PopupWindow(timeView);
			freeTimePopWindow.setFocusable(true);
			freeTimePopWindow.setOutsideTouchable(true);
			freeTimePopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			freeTimePopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
			freeTimePopWindow.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.blue)));
			
		}
		freeTimePopWindow.update();
		if(freeTimePopWindow.isShowing()) dismissWindow();
		else {
			freeTimePopWindow.showAtLocation(findViewById(R.id.order_author_activity), Gravity.BOTTOM|Gravity.LEFT, 0, 0);
		}
	}
	
	public void dismissWindow(){
		if(freeTimePopWindow != null){
			freeTimePopWindow.dismiss();
		}
	}
	
	//隐藏软件键盘
	private void hideSoftKeyBoard(View view)
	{
        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	public void setOnButtonClick(String time) {
		if(time == null || time.equals("")){
			Toast.makeText(this, "你不能点击别人已经预约过的时间,请重试!", Toast.LENGTH_LONG);
			return ;
		}
		
		if(mTime != null){
			mTime.setText(time);
			timeCancel.setVisibility(View.VISIBLE);
			dismissWindow();
		}
	}
	
}
