package com.kreggysoft.footao.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.analytics.tracking.android.EasyTracker;
import com.kreggysoft.footao.R;
import com.kreggysoft.footao.Dialogs.FirstStartDialog;
import com.kreggysoft.footao.Dialogs.GameDialogFragment;
import com.kreggysoft.footao.Dialogs.PushDialogFragment;
import com.kreggysoft.footao.asynctasks.FollowTask;
import com.kreggysoft.footao.asynctasks.PushDialogTask;
import com.kreggysoft.footao.asynctasks.RefreshProgramTask;
import com.kreggysoft.footao.fragments.BaseFragment;
import com.kreggysoft.footao.fragments.NextGamesFragment;
import com.kreggysoft.footao.fragments.OneDayFragment;
import com.kreggysoft.footao.interfaces.GameDialogListener;
import com.kreggysoft.footao.utils.ChannelManager;
import com.kreggysoft.footao.utils.Game;

public class MainActivity extends AnalyticsActivity implements
		ActionBar.TabListener, GameDialogListener {

	public static final int NUMBER_OF_TABS = 4;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	public ArrayList<Game> gameList;
	private int readyLists = 0;
	public ArrayList<BaseFragment> fragmentList;
	private SharedPreferences sharedPref;

	@Override
	protected void onResumeFragments() {

		super.onResumeFragments();
		readyLists = 0;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		ChannelManager.initChannelMaps();
		displayPushDialog();
		
		// Start the tracker in manual dispatch mode...
		// tracker.startNewSession("UA-37042145-2",20, this);
		openDB();
		gamesDB.refresh();
		gameList = new ArrayList<Game>();
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		if (lastUpdateWasMoreThan2HoursAgo())
			new RefreshProgramTask(this)
					.execute(new Object[] { gamesDB, this });

	}

	public void expandableListReady() {
		readyLists++;
		Log.d("exp list", readyLists + " lists ready");
		if (readyLists == 3) {
			if (lastUpdateWasMoreThan2HoursAgo())
				new RefreshProgramTask(this).execute(new Object[] { gamesDB,
						this });
		}
	}

	private void displayPushDialog() {

		if (sharedPref.getBoolean("firstStart", true)) {
			FirstStartDialog firstStartDialog = new FirstStartDialog();
			firstStartDialog.show(getFragmentManager(), "firstStart");
			Editor edit=sharedPref.edit();
			edit.putBoolean("firstStart", false);
			edit.commit();
		}
		else{

		PushDialogTask pushDialogTask = new PushDialogTask(this);
		pushDialogTask.execute(sharedPref.getInt("lastPushID", 0));
		}

	}

	private boolean lastUpdateWasMoreThan2HoursAgo() {
		long lastUpdateMillis = sharedPref.getLong("lastUpdateMillis", 0);
		Calendar cal = Calendar.getInstance();
		Long now = cal.getTimeInMillis();
		Long elpasedTimeSinceLastUpdate = now - lastUpdateMillis;
		if (elpasedTimeSinceLastUpdate > 1000 * 60 * 60 * 2)
			return true;
		else
			return false;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;

		case R.id.menu_refresh:

			new RefreshProgramTask(this)
					.execute(new Object[] { gamesDB, this });
			EasyTracker.getTracker().trackEvent("ui_button", "refresh",
					getClass().getSimpleName(), (long) 0);

			return true;

		case R.id.menu_trending:
			Intent intentTrending = new Intent(this, FollowActivity.class);
			EasyTracker.getTracker().trackEvent("ui_button",
					"toFollowActivity", getClass().getSimpleName(), (long) 0);
			startActivity(intentTrending);
			return true;
		case R.id.menu_settings:
			Intent intentSettings = new Intent(this, SettingsActivity.class);
			EasyTracker.getTracker().trackEvent("ui_button", "toSettings",
					getClass().getSimpleName(), (long) 0);
			startActivity(intentSettings);
			return true;
		case R.id.tuto:
			Intent intentTuto = new Intent(this, TutorialActivity.class);
			EasyTracker.getTracker().trackEvent("ui_button", "toTutorial",
					getClass().getSimpleName(), (long) 0);
			startActivity(intentTuto);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public ArrayList<BaseFragment> getFragmentList() {
		return fragmentList;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			OneDayFragment dayFragment;
			NextGamesFragment nextGamesFragment;
			nextGamesFragment = new NextGamesFragment(gamesDB);
			fragmentList = new ArrayList<BaseFragment>();
			for (int i = 0; i < getCount() - 1; i++) {
				dayFragment = new OneDayFragment(gamesDB);
				Bundle indexargs = new Bundle();
				indexargs.putInt("index", i);
				dayFragment.setArguments(indexargs);
				fragmentList.add(dayFragment);
			}
			Bundle indexargs = new Bundle();
			indexargs.putInt("index", NUMBER_OF_TABS - 1);
			nextGamesFragment.setArguments(indexargs);
			fragmentList.add(nextGamesFragment);

		}

		@Override
		public Fragment getItem(int i) {

			return fragmentList.get(i);
		}

		@Override
		public int getCount() {
			return NUMBER_OF_TABS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(Locale.FRANCE);
			case 1:
				return getString(R.string.title_section2).toUpperCase(Locale.FRANCE);
			case 2:
				return getString(R.string.title_section3).toUpperCase(Locale.FRANCE);
			case 3:
				return getString(R.string.title_section4).toUpperCase(Locale.FRANCE);
			}
			return null;
		}


	}

	@Override
	public void onNegativeButton(GameDialogFragment dialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPositiveButton(DialogInterface dialog, Game g) {
		FollowTask followTask = new FollowTask(this);
		followTask.execute(new Object[] { (long) g.getGameID(), getDB() });

	}
}
