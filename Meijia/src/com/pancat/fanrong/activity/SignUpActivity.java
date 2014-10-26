package com.pancat.fanrong.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pancat.fanrong.R;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends Activity {

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
	private EditText mPasswordView, mConfirmPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	private TextView recommend;
	String  errstring = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		recommend=(TextView)findViewById(R.id.recommend);
		// Set up the login form.
		mAccount = (EditText) findViewById(R.id.account);

		mPasswordView = (EditText) findViewById(R.id.password);
		mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);

		mConfirmPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == EditorInfo.IME_ACTION_DONE) {

							attemptLogin();
							return true;
						}
						return false;
					}
				});

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
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
			//showerror(getString(R.string.error_invalid_email));
			//mAccount.setError(getString(R.string.error_invalid_email));
			focusView = mAccount;
			cancel = true;
		}else if(!password.equals(mConfirmPasswordView.getText().toString())){
			showerror("两次密码不一致");
			cancel = true;
			focusView=mPasswordView;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask(email, password);
			mAuthTask.execute((Void) null);
		}
	}

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
		//return true;
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		boolean b=false;
		
		Pattern p=Pattern.compile("[a-zA-Z0-9]{1，8}");  
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


	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

		private final String mEmail;
		private final String mPassword;

		UserLoginTask(String email, String password) {
			mEmail = email;
			mPassword = password;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return 0;
			}

			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail)) {
					// Account exists, return true if the password matches.
					if(pieces[1].equals(mPassword))
					return 0;
					
				}
			}

			// TODO: register the new account here.
		
			int code = 0;
             
			
             HttpClient httpClient = new DefaultHttpClient();  
               
             //这里是你与服务器交互的地址  
             String validateUrl = "http://ec2-54-169-66-69.ap-southeast-1.compute.amazonaws.com/nail_care_test/index.php/user/register";         
          
         //    System.out.println(validateUrl);
             //设置链接超时  
             httpClient.getParams().setParameter(CoreConnectionPNames.  
                                 CONNECTION_TIMEOUT, 5000);             
             //设置读取超时  
             httpClient.getParams().setParameter(  
                                 CoreConnectionPNames.SO_TIMEOUT, 5000);            
              
             //准备传输的数据  
             List<NameValuePair> paramsq = new ArrayList<NameValuePair>(); 
             System.out.println("mEmail="+mEmail);
             System.out.println("mPassword="+mPassword);
             paramsq.add(new BasicNameValuePair("username", mEmail));  
             paramsq.add(new BasicNameValuePair("password", mPassword));
     		
           
             HttpPost httpRequest = new HttpPost(validateUrl);  
                       
             try {
            	//发送请求  
				httpRequest.setEntity(new UrlEncodedFormEntity(paramsq, HTTP.UTF_8));
				
	            //得到响应  
	            HttpResponse response = httpClient.execute(httpRequest); 
	            if(response.getStatusLine().getStatusCode() == 200)  
                {
	            	Log.i("http", "得到了响应");
	            	 StringBuilder builder = new StringBuilder();  
                     
                     //将得到的数据进行解析  
                     BufferedReader buffer = new BufferedReader(  
                                         new InputStreamReader(response.getEntity().getContent()));  
                       
                     for(String s =buffer.readLine(); s!= null; s = buffer.readLine())  
                     {   builder.append(s);  }  
                     
                     System.out.println("hahahahah"+builder.toString());  
                     //得到Json对象  
                     Log.i("http", "json=: "+builder.toString());
                     JSONObject jsonObject   = new JSONObject(builder.toString());  
                       
                     //通过得到键值对的方式得到值  
                 	code = jsonObject.getInt("code");  
        			Log.i("http", "code=: "+code);
	
                }else{
                	
                }
	            
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
                         
           return code;
            	 
       //   return true;
         	//startSignup(mEmail, mPassword);
    		
		}

		@Override
		protected void onPostExecute(final Integer code) {
			mAuthTask = null;
			showProgress(false);
			
			if (code==1) {
				// 注册成功
				Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_LONG).show();

				Bundle bundle = new Bundle();
				bundle.putString("mAccount", mEmail);
				bundle.putString("mPassword", mPassword);
				Intent it = new Intent();
				it.putExtras(bundle);
				setResult(2, it);
				finish();
			} else {
				
				switch(code){
				case 101:
					errstring="用户名密码组合不合法";
					//Toast.makeText(SignUpActivity.this, "用户名密码组合不合法", Toast.LENGTH_LONG).show();
					Log.i("signup","code 101 用户名密码组合不合法");break;
				case 102:
					errstring="用户名已经被注册";
					//Toast.makeText(SignUpActivity.this, "用户名已经被注册", Toast.LENGTH_LONG).show();
					Log.i("signup","code 102 用户名已经被注册");break;
				case 103:
					errstring="服务器或数据库故障";
					//Toast.makeText(SignUpActivity.this, "服务器或者数据库故障", Toast.LENGTH_LONG).show();
					Log.i("signup","code 103 服务器或者数据库故障");	break;
				default:
					break;
				}
				
				showerror(errstring);
				
			//	Parcel p = Parcel.obtain();
			//	p.writeInt(Color.GREEN);
			//	p.setDataPosition(0);
			//	BackgroundColorSpan bcs = new BackgroundColorSpan(p);
				
		//		spn.setSpan(bcs, 0, errstring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				
				
				
				
				
			//	mPasswordView.setError(getString(R.string.error_incorrect_password));
			//	mPasswordView.setError("good");
			}
		}
	
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	private void showerror(String errstring){
		Spannable spn = new SpannableString(errstring);	
		recommend.setText(spn);
		recommend.setVisibility(View.VISIBLE);
		mPasswordView.requestFocus();
	}
}
