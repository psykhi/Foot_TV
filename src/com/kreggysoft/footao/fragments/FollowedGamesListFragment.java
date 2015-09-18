package com.kreggysoft.footao.fragments;

import com.kreggysoft.footao.utils.GamesDB;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FollowedGamesListFragment extends BaseStickyGameListFragment {

	public FollowedGamesListFragment() {
		super();
		noGameText = "Vous ne suivez aucun match.";
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser)
			try{
			refreshDataset();
			}
		catch (NullPointerException e){
			
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	public FollowedGamesListFragment(GamesDB DB) {
		
		super(DB);
		noGameText = "Vous ne suivez aucun match.";

	}

	@Override
	protected Cursor prepareCursor() {

		return DB.getFollowedGames();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void refreshDataset() {
		ViewGroup v = (ViewGroup) this.getView();
		gameDatesListAdapter.setCursor(prepareCursor());
		gameDatesListAdapter.notifyDataSetInvalidated();
		if (gameDatesListAdapter.getGamesCount() > 0) {
			if (v.getChildAt(0).getId() == com.kreggysoft.footao.R.id.noGameLayout) {
				v.removeAllViews();
				v.addView(stickyListView);
				return;
			}
		} else {
			if (!(v.getChildAt(0).getId() == com.kreggysoft.footao.R.id.noGameLayout)) {
				v.removeAllViews();
				v.addView(noGamesTextView);
				return;
			}
		}

	}

}