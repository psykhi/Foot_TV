package com.kreggysoft.footao.fragments;

import com.kreggysoft.footao.utils.GamesDB;
import android.database.Cursor;

public class TrendingGamesFragment extends BaseStickyGameListFragment {

	public TrendingGamesFragment(){
		
	}
	public TrendingGamesFragment(GamesDB DB) {
		super(DB);
		
	}

	@Override
	protected Cursor prepareCursor() {
		return DB.getTrendingGames();
	}

	@Override
	public void refreshDataset() {
		// TODO Auto-generated method stub
		
	}
}

