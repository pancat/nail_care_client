package com.pancat.fanrong.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pancat.fanrong.MainApplication;
import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.SignUpActivity.UserLoginTask;
import com.pancat.fanrong.bean.User;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.http.AsyncHttpResponseHandler;
import com.pancat.fanrong.http.RequestParams;
import com.pancat.fanrong.mgr.AuthorizeMgr;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends Activity {

	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// UI references.
	private EditText mAccount;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	private TextView recommend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		// Set up the login form.
		mAccount = (EditText) findViewById(R.id.account);
		recommend=(TextView)findViewById(R.id.recommend);
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
		//不弹出输入框
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		TextView registerBtn = (TextView) findViewById(R.id.register_btn);
		registerBtn.setClickable(true);
		//registerBtn.setFocusable(true);
		//registerBtn.setFocusableInTouchMode(true);
		registerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(),
						SignUpActivity.class);
				// 带返回值的启动activity
				startActivityForResult(intent, 1);
			}
		});
		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);

		//
		setForgetPasswordBtn();
	}

	private void setForgetPasswordBtn() {
		TextView forgetPasswrodBtn = (TextView) findViewById(R.id.forget_password_btn);
		forgetPasswrodBtn.setClickable(true);
		forgetPasswrodBtn.setFocusable(true);
		forgetPasswrodBtn.setFocusableInTouchMode(true);
		forgetPasswrodBtn.setOnClickListener(new OnClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View view) {
				Log.i("forget_pass", "forget_pass");
			}
		});


	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
				switch (resultCode) {
					case 2:
						String email=data.getStringExtra("mAccount");
						String password=data.getStringExtra("mPassword");
						mAccount.setText(email);
						mPasswordView.setText(password);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mAccount.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email = mAccount.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			showerror(getString(R.string.error_invalid_password));
			//mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			showerror(getString(R.string.error_field_required));
			//mAccount.setError(getString(R.string.error_field_required));
			focusView = mAccount;
			cancel = true;
		} else if (TextUtils.isEmpty(password)) {
			showerror(getString(R.string.error_field_required));
			//mAccount.setError(getString(R.string.error_invalid_email));
			focusView = mPasswordView;
			cancel = true;
		}else if (!isEmailValid(email)) {
		//	mAccount.setError(getString(R.string.error_invalid_email));
			focusView = mAccount;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			startLogin(email, password);
		}
	}

	private void startLogin(String username, String password) {
		String url = "user/login";
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", password);// 不用md5登录

		//进行登录时隐藏输入法框
		InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		imm.hideSoftInputFromWindow(mAccount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


		RestClient.getInstance().post(MainApplication.getAppContext(), url,
				params, adBannerReadyHandler);
		Log.i("start_login", "fff");
	}

	@SuppressLint("HandlerLeak")
	private Handler mSignInHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			showProgress(false);
			if (msg.what == 0) {
				finish();

				//持久化保存用户登录数据
				AuthorizeMgr.getInstance().setUser((User) msg.obj);
				AuthorizeMgr.getInstance().persistUser((User) msg.obj);
			} else {

				String  errstring=getString(R.string.error_incorrect_password);
				Spannable spn = new SpannableString(errstring);


			//	Parcel p = Parcel.obtain();
			//	p.writeInt(Color.GREEN);
			//	p.setDataPosition(0);
			//	BackgroundColorSpan bcs = new BackgroundColorSpan(p);

			//	spn.setSpan(bcs, 0, errstring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


				TextView recommend=(TextView)findViewById(R.id.recommend);
				recommend.setText(spn);
				recommend.setVisibility(View.VISIBLE);
				//mPasswordView.setError(spn);
				mPasswordView.requestFocus();
			}
			super.handleMessage(msg);
		}
	};

	final AsyncHttpResponseHandler adBannerReadyHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			Log.i("login fail", "login fail");
			Message msg = new Message();
			msg.what = 1;
			mSignInHandler.sendMessage(msg);
		}

		@Override
		public void onSuccess(String content) {
			Log.i("login json", content);
			super.onSuccess(content);
			User user = AuthorizeMgr.parseUserFromJsonText(content);
			Message msg = new Message();
			msg.what = (user == null ? 1 : 0);
			msg.obj = user;
			mSignInHandler.sendMessage(msg);
		}
	};

	private boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		// return email.contains("@");
		boolean b=false;
		if(email.contains("@")){
			//判断email是否合法

			Pattern p=Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
		    Matcher m=p.matcher(email);
		    b=m.matches();
		    if(b)Log.i("emial","email合法");
		    else
		    	showerror(getString(R.string.error_format));
		    	Log.i("emial","email不合法");
		}else if(email.length()==11){
			Log.i("emial","手机长度合法，开始验证");
			//验证手机号合法
			Pattern p=Pattern.compile("[0-9]+");
		    Matcher m=p.matcher(email);
		    b=m.matches();
		    if(b)Log.i("emial","手机合法");
		    else
		    	showerror(getString(R.string.error_format));
		}else{
			showerror(getString(R.string.error_format));
		}
		 if(b){
		    	return true;
		    }else{
		        return false;
		    }

	}
	private void showerror(String errstring){
		Spannable spn = new SpannableString(errstring);
		recommend.setText(spn);
		recommend.setVisibility(View.VISIBLE);
		mPasswordView.requestFocus();
	}
	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		boolean b=false;

		Pattern p=Pattern.compile("[a-zA-Z0-9]{5}");
	    Matcher m=p.matcher(password);
	    b=m.matches();
		if(b){
			Log.i("emial","密码合法");
			return true;
		}
		else{
			Log.i("emial","密码不合法");
		return false;
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
