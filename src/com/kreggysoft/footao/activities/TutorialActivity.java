package com.kreggysoft.footao.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.analytics.tracking.android.EasyTracker;
import com.kreggysoft.footao.R;
import com.kreggysoft.footao.utils.AppManager;

public class TutorialActivity extends Activity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.tutorial_layout);
		webView=(WebView) findViewById(R.id.tutorialWebView);
		webView.loadUrl(AppManager.SITE_URL + "/"
				+ AppManager.FOOT_APPLI + "/" + AppManager.TUTORIAL);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStart() {
		EasyTracker.getInstance().activityStart(this);
		super.onStart();
	}



	@Override
	protected void onStop() {
		EasyTracker.getInstance().activityStop(this);
		super.onStop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
