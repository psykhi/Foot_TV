package com.kreggysoft.footao.activities;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import com.kreggysoft.footao.R;
import com.kreggysoft.footao.Dialogs.GameDialogFragment;
import com.kreggysoft.footao.asynctasks.FollowTask;
import com.kreggysoft.footao.interfaces.GameDialogListener;
import com.kreggysoft.footao.utils.Game;
import com.kreggysoft.footao.utils.GamesDB;

public class SearchActivity extends AnalyticsActivity implements GameDialogListener{

	private GamesDB gamesDB;

	public GamesDB getGamesDB() {
		return gamesDB;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		openDB();
		setContentView(R.layout.search_layout);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
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

	@Override
	public void onNegativeButton(GameDialogFragment dialog) {
		// Nothing to do
		
	}

	@Override
	public void onPositiveButton(DialogInterface dialog, Game g) {
		FollowTask followTask = new FollowTask(this);
		followTask.execute(new Object[]{(long)g.getGameID(),gamesDB});
		
	}

}
