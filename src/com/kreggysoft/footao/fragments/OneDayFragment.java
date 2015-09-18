package com.kreggysoft.footao.fragments;

import android.R;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.google.ads.v;
import com.kreggysoft.footao.utils.AppManager;
import com.kreggysoft.footao.utils.GamesDB;

public class OneDayFragment extends BaseStickyGameListFragment {

	public OneDayFragment() {
		super();
	}

	public OneDayFragment(GamesDB DB) {
		super(DB);

	}

	private int index;

	private String date;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
		index = args.getInt("index");
		date = AppManager.getDate(index);
		super.onCreate(savedInstanceState);
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

	@Override
	protected Cursor prepareCursor() {
		return DB.getGamesForDate(date);
	}

}
