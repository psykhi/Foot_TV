package com.kreggysoft.footao.fragments;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.analytics.tracking.android.EasyTracker;
import com.kreggysoft.footao.activities.SearchActivity;
import com.kreggysoft.footao.utils.GamesDB;

public class SearchFragment extends BaseStickyGameListFragment {

	private Intent intent;

	public SearchFragment(GamesDB DB, Intent intent) {
		this(DB);
		this.intent = intent;
		noGameText = "La recherche n'a retourné aucun résultat";
	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
	}

	public SearchFragment(GamesDB DB) {
		super(DB);
		noGameText = "La recherche n'a retourné aucun résultat";
	}

	public SearchFragment() {
		super();
		noGameText = "La recherche n'a retourné aucun résultat";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		SearchActivity activity = (SearchActivity) getActivity();
		DB = activity.getGamesDB();
		intent = activity.getIntent();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected Cursor prepareCursor() {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			EasyTracker.getTracker().trackEvent("search", "query", query,
					(long) 0);
			return DB.cursorRawSearchInDB(query);
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// Handle a suggestions click (because the suggestions all use
			// ACTION_VIEW)
			Uri data = intent.getData();
			int gameID = Integer.parseInt(data.toString());
			EasyTracker.getTracker().trackEvent("search", "query",
					"suggestion", (long) gameID);

			// Log.d("suggestion search", gameID + " ");
			return DB.getID(gameID);
		} else
			return null;
	}

	@Override
	public void refreshDataset() {
		// TODO Auto-generated method stub

	}

}
