package com.kreggysoft.footao.activities;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.kreggysoft.footao.Dialogs.GameDialogFragment;
import com.kreggysoft.footao.asynctasks.FollowTask;
import com.kreggysoft.footao.fragments.FollowedGamesListFragment;
import com.kreggysoft.footao.fragments.TrendingGamesFragment;
import com.kreggysoft.footao.interfaces.GameDialogListener;
import com.kreggysoft.footao.utils.Game;
import com.kreggysoft.footao.utils.GamesDB;
import com.kreggysoft.footao.R;

public class FollowActivity extends AnalyticsActivity implements
		ActionBar.TabListener, GameDialogListener {

	public static final int NUMBER_OF_TABS = 2;
	private SectionsPagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;
	public ArrayList<Game> gameList;
	private GamesDB gamesDB;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_follow);
		openDB();
		gameList = new ArrayList<Game>();
		sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		viewPager = (ViewPager) findViewById(R.id.follow_pager);
		viewPager.setAdapter(sectionsPagerAdapter);
		viewPager.setOffscreenPageLimit(0);
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(sectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

	}


	

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}


	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private Fragment[] fragmentArray;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragmentArray = new Fragment[] {
					new FollowedGamesListFragment(gamesDB),
					new TrendingGamesFragment(gamesDB) };
		}

		@Override
		public Fragment getItem(int i) {
			return fragmentArray[i];
		}

		@Override
		public int getCount() {
			return NUMBER_OF_TABS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.followed).toUpperCase(Locale.FRANCE);
			case 1:
				return getString(R.string.trending).toUpperCase(Locale.FRANCE);

			}
			return null;
		}
	}

	@Override
	public void onNegativeButton(GameDialogFragment dialog) {
	}

	@Override
	public void onPositiveButton(DialogInterface dialog, Game g) {
		FollowTask followTask = new FollowTask(this);
		followTask.execute(new Object[] { (long) g.getGameID(), getDB() });

	}

}
