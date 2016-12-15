package com.deerlight.androiddaily;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;


/**
 * Created by taaze on 2016/12/9.
 */
public class WebViewDialog  {

	Context context;
	String title;
	String link;
	WebView myWebView;
	PopupWindow popupWindow;
	private static final String TAG = "WebViewDialog";

	public WebViewDialog(Context context) {
		this.context = context;
	}

	public void popUpWebView(String title,String link) {
		this.title = title;
		this.link = link;
		myWebView = new WebView(context);
		myWebView.loadUrl(link);
		myWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(final WebView view, String url, Bitmap favicon) {

				if(popupWindow == null){
					LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View progress = inflater.inflate(R.layout.loading_progress, null);
					popupWindow = new PopupWindow(progress, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
					popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
				}
				Log.v(TAG,"onPageStarted");
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {

				if(popupWindow != null){
					popupWindow.dismiss();
					popupWindow = null;
				}
				Log.v(TAG, "onPageFinished");
				super.onPageFinished(view, url);
			}
		});

		new AlertDialog.Builder(context)
				.setView(myWebView)
				.setTitle(title)
				.setPositiveButton("Exit", null)
				.setOnKeyListener(keylistener)
				.show();
	}

	DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0
					&& myWebView.canGoBack() && event.getAction() == KeyEvent.ACTION_UP) {

				myWebView.goBack();
				return true;
			}
			else {
				return false;
			}
		}
	} ;

}
