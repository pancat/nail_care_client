package com.pancat.fanrong.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.pancat.fanrong.R;

public class AdvertiseActivity extends Activity{

	WebView wv;
	public TextView tvTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advertise);
		tvTitle = (TextView)findViewById(R.id.tv_title);
		wv = (WebView) findViewById(R.id.webView_main);
		WebSettings webSettings = wv.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//插件
		webSettings.setPluginState(PluginState.ON_DEMAND);
		//密码
		webSettings.setSavePassword(true);
		//cache模式
		webSettings.setCacheMode(WebSettings.LOAD_NORMAL);
		webSettings.setBuiltInZoomControls(true);//缩放
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);//缩放密度
		webSettings.setLightTouchEnabled(true);//触控事件
		
		wv.setWebChromeClient(new ChromeClient());
		wv.setWebViewClient(new WebViewClient(){
			public <Bitmap> void onPageStarted(WebView view, String url, Bitmap favicon){// 页面开始加载时进行自定义处理
				if(url.equals("")){
					//ShowDialog(DIALOG_WAIT);
					setTitle(view.getTitle());
				}
				setTitle(view.getTitle());
			}
			public void onPageFinished(WebView view, String url){// 页面加载结束时进行自定义处理
				if(url.equals("")){
					//dismissDiaLog(DIALOG_WAIT);
				}
			}
			public boolean shouldOverrideUrlLoading(WebView view, String url){// 覆盖默认的按键处理过程
				view.loadUrl(url);
				return true;
			}
		});
		wv.loadUrl("http://fangmingdesign.net/m/index.php");
	}
	
	class ChromeClient extends WebChromeClient{ 

        @Override 
        public void onProgressChanged(WebView view, int newProgress) { 
            //动态在标题栏显示进度条 
            super.onProgressChanged(view, newProgress); 
        } 

        @Override 
        public void onReceivedTitle(WebView view, String title) { 
            //设置当前activity的标题栏 
        	tvTitle.setText(title);
            super.onReceivedTitle(view, title);
        } 
    }
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}
	}
}
