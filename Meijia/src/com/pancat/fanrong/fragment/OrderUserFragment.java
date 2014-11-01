package com.pancat.fanrong.fragment;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OrderUserFragment extends Fragment {
	public static final String TAG = "OrderUserFragment";
	
	private User user;
	private TextView userName;
	private TextView userInfo;
	private TextView leftButton;
	
	public static OrderUserFragment newInstance(User user){
		OrderUserFragment instance = new OrderUserFragment();
		if(user == null){
			instance.user = new User();
			instance.user.setUsername("JogRunner");
			instance.user.setAddress("SYSU");
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
		leftButton = (TextView)v.findViewById(R.id.fragment_order_user_button);
		
		userName.setText(user.getUsername());
		userInfo.setText(user.getAddress());
		
		return v;
	}
	
}
