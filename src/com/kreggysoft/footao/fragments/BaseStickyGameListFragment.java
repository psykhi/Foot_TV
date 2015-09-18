package com.kreggysoft.footao.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.kreggysoft.footao.R;
import com.kreggysoft.footao.Dialogs.GameDialogFragment;
import com.kreggysoft.footao.activities.AnalyticsActivity;
import com.kreggysoft.footao.adapters.GameDatesListAdapter;
import com.kreggysoft.footao.asynctasks.FollowTask;
import com.kreggysoft.footao.utils.Game;
import com.kreggysoft.footao.utils.GameOperations;
import com.kreggysoft.footao.utils.GamesDB;

public abstract class BaseStickyGameListFragment extends BaseFragment {

	protected GamesDB DB;
	protected StickyListHeadersListView stickyListView;
	protected GameDatesListAdapter gameDatesListAdapter;
	private LayoutInflater inflater;
	protected View noGamesTextView;

	public BaseStickyGameListFragment(GamesDB DB) {
		this.DB = DB;
	}

	public BaseStickyGameListFragment() {
		super();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (DB == null)
			DB = ((AnalyticsActivity) getActivity()).getDB();
		prepareCursor();
		super.onCreate(savedInstanceState);
	}

	protected abstract Cursor prepareCursor();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		noGamesTextView= this.inflater.inflate(R.layout.empty_list_layout, null);
		if (noGameText!=null)
		((TextView) noGamesTextView.findViewById(R.id.noGameText)).setText(noGameText);
		return initStickyListView(prepareCursor());
	}

	protected View initStickyListView(Cursor cursor) {
		gameDatesListAdapter = new GameDatesListAdapter(getActivity(), DB,
				cursor);

		stickyListView = new StickyListHeadersListView(getActivity());
		stickyListView.setDividerHeight(1);
		stickyListView.setId(42);
		stickyListView.setAdapter(gameDatesListAdapter);
		stickyListView.setHeaderDividersEnabled(true);
		stickyListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						BaseAdapter adapter = (BaseAdapter) parent.getAdapter();

						GamesDB DB = BaseStickyGameListFragment.this.DB;
						FollowTask followTask = new FollowTask(getActivity());
						followTask.execute(new Object[] { id, DB, adapter });

						return true;

					}
				});
		stickyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Game g;
				Cursor gameCursor = DB.getID((int) id);
				if (gameCursor.moveToFirst())
					g = GameOperations.cursorGame(gameCursor);
				else
					return;
				GameDialogFragment dialog = new GameDialogFragment();
				dialog.setGame(g);
				dialog.show(getFragmentManager(), "gameDialog");

			}
		});

		if (gameDatesListAdapter.getGamesCount() > 0)
			return stickyListView;
		else
			return noGamesTextView;

	}
	
	public void setEmptyListText(String text){
		noGameText=text;
		if (noGamesTextView!=null)
			((TextView) noGamesTextView.findViewById(R.id.noGameText)).setText(text);
		
	}
}
