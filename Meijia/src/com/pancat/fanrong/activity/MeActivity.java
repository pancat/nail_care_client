package com.pancat.fanrong.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pancat.fanrong.MainActivity;
import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.R;
import com.pancat.fanrong.common.FragmentCallback;
import com.pancat.fanrong.common.User;

public class MeActivity extends Activity implements FragmentCallback {

	int age;
	String username, nick_name, email, avatar_uri;
	int id;
	String token;
	private Button loginOrout;
	private ListView listview;
	private TextView nickname;
	int colorpink = Color.parseColor("#FA8072");
	int colordefault = android.graphics.Color.WHITE;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		
		// user.setUserDate();
		id = User.getInstance().getId();
		token = User.getInstance().getToken();
		// Log.i("User", "id= " + id);
		// Log.i("User", "token= " + token);
		Log.i("iflogin", "" + User.getInstance().isUserLogined());
		if (User.getInstance().isUserLogined() == false) {

			// 未登录
			loginOrout.setText("登录");
			loginOrout.setOnClickListener(login);
			// Intent intent = new Intent(MeActivity.this, LogActivity.class);
			// startActivity(intent);

		} else {
			// 已登录，显示登录信息
			loginOrout.setText("注销");
			loginOrout.setOnClickListener(logout);
			// 通过网络请求获取用户信息

			// app.getUserDateFromServer();

			User.getInstance().getUserDateFromServer();
			// 读取sharedperference,然后显示
			SharedPreferences userInfo = getSharedPreferences("userinfo",
					Activity.MODE_PRIVATE);
			username = userInfo.getString("username", "null");
			nick_name = userInfo.getString("nick_name", "null");
			age = userInfo.getInt("age", 0);
			email = userInfo.getString("email", "null");
			avatar_uri = userInfo.getString("avatar_uri", "null");

			nickname.setText(nick_name);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

		super.onStart();

	}

	PopupWindow pw;
	Button btncancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_me);

		loginOrout = (Button) findViewById(R.id.loginOrout);
		Log.i("context", "Me context   " + MainApplication.getAppContext());
		nickname = (TextView) findViewById(R.id.nick_name);

	}

	private OnClickListener what = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			pw.dismiss();
		}

	};

	public void showUserloginPopwindows() {

		View contentView = LayoutInflater.from(this).inflate(
				R.layout.popupwindow, null);
		pw = new PopupWindow(contentView, 500, 400);
		pw.setOutsideTouchable(true);
		pw.setFocusable(true);
		pw.update();
		pw.showAtLocation(findViewById(R.id.scrollview), Gravity.CENTER, 0, 0);

		btncancle = (Button) findViewById(R.id.BtnOK);
		btncancle.setText("hahahaha");
		// btncancle.setOnClickListener(what);
	}

	public void uploadimage() {
		View imagalertDialog  = LayoutInflater.from(this).inflate(R.layout.activity_me_userimage, null);
		Builder builder =new AlertDialog.Builder(this);
		builder.setView(imagalertDialog);
		builder.show();
		
	
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.redpacket:
			// showUserloginPopwindows
			break;
		case R.id.commonaddress:
			Intent it = new Intent(this, TestLocation.class);
			startActivity(it);
			this.getParent().overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			break;
		case R.id.discount:
			Intent intent = new Intent(this, LoginActivity.class);

			startActivity(intent);
			this.getParent().overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			break;
		case R.id.collection:
			break;
		case R.id.messagecenter:
			break;
		case R.id.servicephone:
			break;

		case R.id.usercircleimage:
			if (id == -1) {
				Toast.makeText(this, "您还没登录", Toast.LENGTH_LONG).show();
				Intent intent2 = new Intent(this, LoginActivity.class);
				startActivity(intent2);
			} else {
				uploadimage();
			}
			break;

		default:
			break;
		}
	}

	private OnClickListener logout = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得事件的按钮设置为粉色
			SharedPreferences userInfo = getSharedPreferences("userinfo",
					Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = userInfo.edit();
			editor.clear();
			editor.commit();
			User.getInstance().setUserDate();
			// MainApplication app=(MainApplication)getApplicationContext();
			// app.setUserDate();
			Intent intent = new Intent(MeActivity.this, MainActivity.class);
			startActivity(intent);
		}

	};
	private OnClickListener login = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MeActivity.this, SignInActivity.class);

			startActivity(intent);
			MeActivity.this.getParent().overridePendingTransition(
					android.R.anim.fade_in, android.R.anim.fade_out);
		}

	};

	@Override
	public void callback(Bundle arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishActivity() {
		// TODO Auto-generated method stub

	}

}