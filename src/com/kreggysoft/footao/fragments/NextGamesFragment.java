package com.kreggysoft.footao.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemLongClickListener;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.kreggysoft.footao.activities.MainActivity;
import com.kreggysoft.footao.adapters.GameDatesListAdapter;
import com.kreggysoft.footao.asynctasks.FollowTask;
import com.kreggysoft.footao.utils.AppManager;
import com.kreggysoft.footao.utils.GamesDB;

public class NextGamesFragment extends BaseStickyGameListFragment {

	
	public NextGamesFragment(){
		
		super();
	}
	public NextGamesFragment(GamesDB DB) {
		super(DB);
	}

	private String date;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
		int index = args.getInt("index");
		 date = AppManager.getDate(index);
		super.onCreate(savedInstanceState);
	}


	@Override
	protected Cursor prepareCursor() {
		 
		return DB.getNextGames(date);
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
