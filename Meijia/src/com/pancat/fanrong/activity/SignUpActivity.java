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
			showerror(getString(R.string.confirm_password_faile));
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
			//�ж�email�Ƿ�Ϸ�

			Pattern p=Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
		    Matcher m=p.matcher(email);
		    b=m.matches();
		    if(b)Log.i("emial","email�Ϸ�");
		    else
		    	showerror(getString(R.string.error_format));
		    	Log.i("emial","email���Ϸ�");
		}else if(email.length()==11){
			Log.i("emial","�ֻ����ȺϷ�����ʼ��֤");
			//��֤�ֻ��źϷ�
			Pattern p=Pattern.compile("[0-9]+");
		    Matcher m=p.matcher(email);
		    b=m.matches();
		    if(b)Log.i("emial","�ֻ��Ϸ�");
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

		Pattern p=Pattern.compile("[a-zA-Z0-9]{1,8}");
	    Matcher m=p.matcher(password);
	    b=m.matches();
		if(b){
			Log.i("emial","����Ϸ�");
			return true;
		}
		else{
			Log.i("emial","���벻�Ϸ�");
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

             //��������������������ĵ�ַ
             String validateUrl = "http://ec2-54-169-66-69.ap-southeast-1.compute.amazonaws.com/nail_care_test/index.php/user/register";

         //    System.out.println(validateUrl);
             //�������ӳ�ʱ
             httpClient.getParams().setParameter(CoreConnectionPNames.
                                 CONNECTION_TIMEOUT, 5000);
             //���ö�ȡ��ʱ
             httpClient.getParams().setParameter(
                                 CoreConnectionPNames.SO_TIMEOUT, 5000);

             //׼�����������
             List<NameValuePair> paramsq = new ArrayList<NameValuePair>();
             System.out.println("mEmail="+mEmail);
             System.out.println("mPassword="+mPassword);
             paramsq.add(new BasicNameValuePair("username", mEmail));
             paramsq.add(new BasicNameValuePair("password", mPassword));


             HttpPost httpRequest = new HttpPost(validateUrl);

             try {
            	//��������
				httpRequest.setEntity(new UrlEncodedFormEntity(paramsq, HTTP.UTF_8));

	            //�õ���Ӧ
	            HttpResponse response = httpClient.execute(httpRequest);
	            if(response.getStatusLine().getStatusCode() == 200)
                {
	            	Log.i("http", "�õ�����Ӧ");
	            	 StringBuilder builder = new StringBuilder();

                     //���õ������ݽ��н���
                     BufferedReader buffer = new BufferedReader(
                                         new InputStreamReader(response.getEntity().getContent()));

                     for(String s =buffer.readLine(); s!= null; s = buffer.readLine())
                     {   builder.append(s);  }

                     System.out.println("hahahahah"+builder.toString());
                     //�õ�Json����
                     Log.i("http", "json=: "+builder.toString());
                     JSONObject jsonObject   = new JSONObject(builder.toString());

                     //ͨ���õ���ֵ�Եķ�ʽ�õ�ֵ
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
				// ע��ɹ�
				Toast.makeText(SignUpActivity.this, getString(R.string.signup_success), Toast.LENGTH_LONG).show();

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
					errstring="�û���������ϲ��Ϸ�";
					//Toast.makeText(SignUpActivity.this, "�û���������ϲ��Ϸ�", Toast.LENGTH_LONG).show();
					Log.i("signup","code 101 �û���������ϲ��Ϸ�");break;
				case 102:
					errstring="�û����Ѿ���ע��";
					//Toast.makeText(SignUpActivity.this, "�û����Ѿ���ע��", Toast.LENGTH_LONG).show();
					Log.i("signup","code 102 �û����Ѿ���ע��");break;
				case 103:
					errstring="�����������ݿ����";
					//Toast.makeText(SignUpActivity.this, "�������������ݿ����", Toast.LENGTH_LONG).show();
					Log.i("signup","code 103 �������������ݿ����");	break;
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
