package com.pancat.fanrong.fragment;


import com.pancat.fanrong.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pancat.fanrong.R;

@SuppressLint("NewApi")
public class MeFragment extends Fragment{
	private View contextView;
	private MeFragment meFragment;
	private FragmentManager fragmentManager;
	private Button logbtn, regbtn, loginbtn;
	private LinearLayout log,reg;
	int flag=0;
	int colorpink = Color.parseColor("#FA8072");
	int colordefault = android.graphics.Color.WHITE;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contextView = inflater.inflate(R.layout.fragment_me, container, false);
		logbtn = (Button) contextView.findViewById(R.id.logbtn);
		regbtn = (Button) contextView.findViewById(R.id.regbtn);
		loginbtn = (Button) contextView.findViewById(R.id.loginbtn);
		log = (LinearLayout) contextView.findViewById(R.id.log);
		reg = (LinearLayout) contextView.findViewById(R.id.reg);
		logbtn.setOnClickListener(showlog);
		regbtn.setOnClickListener(showreg);
		logbtn.setBackgroundColor(colorpink);
		regbtn.setBackgroundColor(colordefault);
		
		reg.setVisibility(View.GONE);
		//登录或者注册按钮，进行登录验证
		loginbtn.setOnClickListener(logOrReg);
		
		return contextView;
	}
	
	private OnClickListener showlog = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得事件的按钮设置为粉色
					logbtn.setBackgroundColor(colorpink);
					regbtn.setBackgroundColor(colordefault);
					loginbtn.setText("登录");
					flag=0;
					//显示登录页面
					log.setVisibility(View.VISIBLE);
					reg.setVisibility(View.GONE);

		}

	};
	
	private OnClickListener showreg = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得事件的按钮设置为粉色

   			regbtn.setBackgroundColor(colorpink);
			logbtn.setBackgroundColor(colordefault);
			loginbtn.setText("注册");
			flag=1;
			//显示注册页面
			reg.setVisibility(View.VISIBLE);
			log.setVisibility(View.GONE);

		}

	};
	
	private OnClickListener logOrReg=new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			//转入登录验证
			if(flag==0){
				
			}else if(flag==1)
			{
			//或者转入注册系统
				
			}
			
			
		}
		
	};
	
}
