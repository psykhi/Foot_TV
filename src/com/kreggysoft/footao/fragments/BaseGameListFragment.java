package com.kreggysoft.footao.fragments;

import com.kreggysoft.footao.Dialogs.GameDialogFragment;
import com.kreggysoft.footao.activities.AnalyticsActivity;
import com.kreggysoft.footao.adapters.OneDayListAdapter;
import com.kreggysoft.footao.asynctasks.FollowTask;
import com.kreggysoft.footao.utils.Game;
import com.kreggysoft.footao.utils.GameOperations;
import com.kreggysoft.footao.utils.GamesDB;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public abstract class BaseGameListFragment extends BaseFragment {
	
	protected GamesDB DB;
	protected ListView gameListView;
	protected OneDayListAdapter oneDayListAdapter;
	protected Cursor cursor;

	public BaseGameListFragment(GamesDB DB) {
		this.DB = DB;
	}

	public BaseGameListFragment(){
		super();
		DB=((AnalyticsActivity)getActivity()).getDB();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		prepareCursor();
		super.onCreate(savedInstanceState);
	}

	protected abstract Cursor prepareCursor();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return initStickyListView(prepareCursor());
	}

	

	private View initStickyListView(Cursor prepareCursor) {
		gameListView = new ListView(getActivity());

		oneDayListAdapter = new OneDayListAdapter(getActivity(), DB,
				prepareCursor);
		gameListView.setAdapter(oneDayListAdapter);
		gameListView.setOnItemClickListener(new OnItemClickListener() {
			
			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Game g;
				Cursor gameCursor=DB.getID((int) id);
				if(gameCursor.moveToFirst())
					g=GameOperations.cursorGame(gameCursor);
					else return ;
				GameDialogFragment dialog= new GameDialogFragment(g);
				dialog.show(getFragmentManager(), "gameDialog");
				
			}
		});
		gameListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				FollowTask followTask = new FollowTask(getActivity());
				followTask.execute(new Object[]{id,DB});
				
				
				return true;
				

			}
		});

		return gameListView;
	}
}
