package com.kreggysoft.footao.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.google.analytics.tracking.android.EasyTracker;
import com.kreggysoft.Billing.util.IabHelper;
import com.kreggysoft.Billing.util.IabResult;
import com.kreggysoft.Billing.util.Inventory;
import com.kreggysoft.Billing.util.KeyManager;
import com.kreggysoft.Billing.util.Purchase;
import com.kreggysoft.footao.fragments.SettingsFragment;
import com.kreggysoft.footao.utils.AppManager;

public class SettingsActivity extends Activity {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		billingHelper.handleActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	private IabHelper billingHelper;
	private SettingsFragment settingsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		billingHelper = new IabHelper(this, KeyManager.getKEY());
		settingsFragment = new SettingsFragment(billingHelper);
		
		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, settingsFragment).commit();
		billingHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					//Log.d("Billing", "Problem setting up In-app Billing: "
					//		+ result);

				} else
					//Log.d("Billing", "OK " + result);
				settingsFragment.onBillingHelperReady();
				billingHelper.queryInventoryAsync(mGotInventoryListener);

			}
		});
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();

		EasyTracker.getInstance().activityStop(this); // Add this method.
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
			//finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		if (billingHelper != null)
			billingHelper.dispose();
		billingHelper = null;
		super.onDestroy();
	}

	// Listener that's called when we finish querying the items we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		private Purchase old;

		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			//Log.d("inapp", "Query inventory finished.");
			if (result.isFailure()) {
				//Log.d("lol", "Failed to query inventory: " + result);
				return;
			}

			old = inventory.getPurchase(AppManager.getNO_ADS());
			if (old != null) {
				SharedPreferences sharedPref = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());

				SharedPreferences.Editor editor = sharedPref
						.edit();
				editor.putBoolean("adsFree", true);
				editor.commit();
//				billingHelper.consumeAsync(old,
//						new OnConsumeFinishedListener() {
//
//							@Override
//							public void onConsumeFinished(Purchase purchase,
//									IabResult result) {
//								//Log.d("consume", "done");
//								SharedPreferences sharedPref = PreferenceManager
//										.getDefaultSharedPreferences(getApplicationContext());
//
//								SharedPreferences.Editor editor = sharedPref
//										.edit();
//								editor.putBoolean("adsFree", false);
//								editor.commit();
//
//							}
//						});
			}
			//Log.d("inventory", "Query inventory was successful.");

			//Log.d("Inventory",
		//			"Initial inventory query finished; enabling main UI.");
		}
	};

}
