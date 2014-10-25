package com.pancat.fanrong;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pancat.fanrong.activity.SignInActivity;
import com.pancat.fanrong.activity.TestLocation;
import com.pancat.fanrong.mgr.AuthorizeMgr;

public class UserCenterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);

		setButtonListener();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setLoginBtn();
	}

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
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.service_call_no)));
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
				}
			});

		}
	}
}
