package com.pancat.fanrong.fragment;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.User;
import com.pancat.fanrong.mgr.AuthorizeMgr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderUserFragment extends Fragment {
	public static final String TAG = "OrderUserFragment";
	
	public static final String ULOGIN = "未登录";
	public static final String UNAME = "[登录]";
	public static final String SAFELOGINOUT = "[安全退出]";
	
	private User user;
	private TextView userName;
	private TextView userInfo;
	private ImageView leftButton;
	
	public static OrderUserFragment newInstance(User user){
		OrderUserFragment instance = new OrderUserFragment();
		if(user == null){
			instance.user = new User();
			instance.user.setUsername("Unkown");
			//instance.user.setAddress("SYSU");
		}else
			instance.user = user;
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_order_user, container,false);
		userName = (TextView)v.findViewById(R.id.fragment_order_user_name);
		userInfo = (TextView)v.findViewById(R.id.fragment_order_user_info);
		leftButton = (ImageView)v.findViewById(R.id.fragment_order_user_button);
		
		userName.setText(user.getUsername());
		
		userInfo.setText(AuthorizeMgr.getInstance().hasLogined()?SAFELOGINOUT:UNAME);
		
		//安全退出，目前并没有实际退出
		userInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = ((TextView)v).getText().toString();
				if(text.equals(SAFELOGINOUT)){
					((TextView)v).setText(UNAME);
					//
					//AuthorizeMgr.getInstance().setLogout();
				}else{
					((TextView)v).setText(SAFELOGINOUT);
					//Intent intent = new Intent(getActivity(),);
				}
			}
		});
		
		leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//跳转到个人中心
			}
		});
		return v;
	}
	
}
