package com.kreggysoft.footao.activities;

import android.support.v4.app.FragmentActivity;

import com.google.analytics.tracking.android.EasyTracker;
import com.kreggysoft.footao.utils.GamesDB;

public class AnalyticsActivity extends FragmentActivity {

	
	protected GamesDB gamesDB;
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
	
	public void openDB(){
		gamesDB = new GamesDB(this);
		gamesDB.open();
	}
	
	public GamesDB getDB(){
		if(gamesDB==null)
			openDB();
		return gamesDB;
	}



	@Override
	protected void onDestroy() {
		gamesDB.close();
		super.onDestroy();
	}
	

	
}
