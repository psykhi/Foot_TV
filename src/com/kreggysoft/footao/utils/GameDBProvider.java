package com.kreggysoft.footao.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class GameDBProvider extends ContentProvider {

	private GamesDB gamesDB;

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		
		return null;
	}

	@Override
	public boolean onCreate() {
		gamesDB = new GamesDB(getContext());
		gamesDB.open();

		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		String query = uri.getLastPathSegment();
		Log.d("search", query);
		return gamesDB.searchInDB(query);
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		
		return 0;
	}

}
