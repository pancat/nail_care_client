package com.jogrunner.testpushnotification;

import java.util.List;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.jogruuner.utils.Utils;
import com.jogrunner.messagehandle.*;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
		int      idinfoshow = 0;
		int      id_btn_settag =0;
		int      id_btn_deltag = 0;
		
		TextView infoshow = null;
		Button   btn_settag = null;
		Button   btn_deltag = null;
		ScrollView scrollView = null;
		
		Resources resource = null;
		String packageName ;
		private final static String TAG = MainActivity.class.getSimpleName();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.logStringCache = Utils.getLogText(getApplicationContext());
		
		setContentView(R.layout.activity_main);
		
		resource = getResources();
		packageName = getPackageName();
		idinfoshow = resource.getIdentifier("infoshow", "id", packageName);
		id_btn_settag = resource.getIdentifier("btn_setTags", "id", packageName);
		id_btn_deltag = resource.getIdentifier("btn_delTags", "id", packageName);
		
		
		infoshow = (TextView)findViewById(idinfoshow);
		btn_settag = (Button)findViewById(id_btn_settag);
		btn_deltag = (Button)findViewById(id_btn_deltag);
		 scrollView = (ScrollView) findViewById(resource.getIdentifier(
	                "stroll_text", "id", packageName));
		 
		btn_settag.setOnClickListener(this);
		//btn_deltag.setOnClickListener();
		
		if(!Utils.hasBind(getApplicationContext())){
			PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,  Utils.getMetaValue(MainActivity.this, "api_key"));
		}
		
		SetCloudPushNotificationStyle();
	}
    
	 public void onClick(View view) {
		if(view.getId() == id_btn_settag){
			setTags();
		}
		else if(view.getId() == id_btn_deltag){
			delTags();
		}
	 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");
        updateDisplay();
    }
    
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Utils.setLogText(getApplicationContext(), Utils.logStringCache);
        super.onDestroy();
    }
    
	/*
	 * set notification styles
	 */
	public void SetCloudPushNotificationStyle(){
		CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(getApplicationContext(),
				resource.getIdentifier("notification_custom_builder", "layout", packageName),
                resource.getIdentifier("notification_icon", "id", packageName),
                resource.getIdentifier("notification_title", "id", packageName),
                resource.getIdentifier("notification_text", "id", packageName));
		
		cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE);
		cBuilder.setStatusbarIcon(android.R.drawable.stat_sys_download);
		cBuilder.setLayoutDrawable(resource.getIdentifier(
                "simple_notification_icon", "drawable", packageName));
		PushManager.setNotificationBuilder(getApplicationContext(), 1, cBuilder);
		
	}
	/*
	 * check the setting for message push
	 
	public boolean checkCloudPushState(){
		SharedPreferences settings = getSharedPreferences(cloudPushState, 0);
		int count = settings.getInt("count", 0);
		if(count == 0) return false;
		return true;
	}
	
	
	 * set the setting for message push
	 * @param count: set the push state
	 
	public void setCloudPushState(int count){
		SharedPreferences settings = getSharedPreferences(cloudPushState, 0);
		Editor  editor = settings.edit();
		editor.putInt("count", 1);
		editor.commit();
		finish();
	}
	
	
	 * start cloud push message method
	 * must be construct a class extends FrontiaPushMessageReceiver or BraodcastReceiver
	 * @use the member variable 
	 
	public void startCloudPush(){
		
	}*/
	

	private void setTags() {
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText textviewGid = new EditText(MainActivity.this);
        textviewGid.setHint("Add Tags At Here");
        layout.addView(textviewGid);

        AlertDialog.Builder builder = new AlertDialog.Builder(
        		MainActivity.this);
        builder.setView(layout);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Push: 璁剧疆tag璋冪敤鏂瑰紡
                        List<String> tags = Utils.getTagsList(textviewGid
                                .getText().toString());
                        PushManager.setTags(getApplicationContext(), tags);
                    }

                });
        builder.show();
    }
	
	private void delTags() {
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText textviewGid = new EditText(MainActivity.this);
        textviewGid.setHint("Del Tags At Here");
        layout.addView(textviewGid);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setView(layout);
        builder.setPositiveButton("OK DEL",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        List<String> tags = Utils.getTagsList(textviewGid
                                .getText().toString());
                        PushManager.delTags(getApplicationContext(), tags);
                    }
                });
        builder.show();
    }
	
	
	private void updateDisplay() {
        Log.d(TAG, "updateDisplay, logText:" + infoshow + " cache: "
                + Utils.logStringCache);
        if (infoshow != null) {
        	infoshow.setText(Utils.logStringCache);
        }
        if (scrollView != null) {
        	scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

}
