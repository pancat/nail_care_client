package com.pancat.fanrong.fragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pancat.fanrong.R;
import com.pancat.fanrong.common.ChangeMd5;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.common.UserInter;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;

@SuppressLint("NewApi")
public class MeFragment extends Fragment {
	private View contextView;
	private MeFragment meFragment;
	private FragmentManager fragmentManager;
	private Button loginbtn;
	private EditText username, password;
	private LinearLayout log, reg;

	int colorpink = Color.parseColor("#FA8072");
	int colordefault = android.graphics.Color.WHITE;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contextView = inflater.inflate(R.layout.fragment_me, container, false);
		loginbtn = (Button) contextView.findViewById(R.id.loginbtn);
		username = (EditText) contextView.findViewById(R.id.username);
		password = (EditText) contextView.findViewById(R.id.password);
		loginbtn.setOnClickListener(logIn);

		return contextView;
	}

	private OnClickListener logIn = new OnClickListener() {
		private String name;
		private String passwd;

		@Override
		public void onClick(View v) {
			name = username.getText().toString();
			passwd = password.getText().toString();

			if (name == null) {
				Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_LONG)
						.show();
			} else {
				UserInter loguser=new UserInter(getActivity(),name,passwd);
				try {
					loguser.logmethod();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	};
	//用户登录方法
	private void logmethod(String name, String password)
			throws NoSuchAlgorithmException {
		
		String url= "user/login";
		RequestParams params = new RequestParams();
		params.put("username", name);
		// 将password转换为MD5
		MessageDigest md = MessageDigest.getInstance("MD5");

		String md5password = ChangeMd5.MD5(password, md);
		Log.i("pasword md5:", md5password);

		params.put("password", md5password);

		
		RestClient.getInstance().post(getActivity(), url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						Toast.makeText(getActivity(), "数据提交失败",
								Toast.LENGTH_LONG).show();
					}
					 @Override
				     public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
				 error)
				 {
				         // Response failed :(
				     }
					@Override
					public void onSuccess(int statusCode, String content) {
						onSuccess(content);
						Toast.makeText(getActivity(), "登录成功",
								Toast.LENGTH_LONG).show();
					}
					

				});

	}

}
