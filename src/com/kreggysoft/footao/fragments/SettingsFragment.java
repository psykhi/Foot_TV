package com.kreggysoft.footao.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.kreggysoft.Billing.util.IabHelper;
import com.kreggysoft.Billing.util.IabHelper.OnIabPurchaseFinishedListener;
import com.kreggysoft.Billing.util.IabResult;
import com.kreggysoft.Billing.util.Purchase;
import com.kreggysoft.footao.utils.AppManager;
import com.kreggysoft.footao.R;

public class SettingsFragment extends PreferenceFragment {
	private IabHelper helper;
	private Activity activity;
	private OnIabPurchaseFinishedListener noAdsPurchased;
	private boolean billingHelperReady = false;
	

	public SettingsFragment(IabHelper billingHelper) {

		helper = billingHelper;
		//helper.enableDebugLogging(true);
		noAdsPurchased = new OnIabPurchaseFinishedListener() {

			@Override
			public void onIabPurchaseFinished(IabResult result,
					Purchase purchase) {
				if (result.getResponse() == 7) {
					Log.e("NO ADS purchase", "Item already owned " + result);
					Toast.makeText(getActivity(), " Achat déjà effectué",
							Toast.LENGTH_LONG).show();
					EasyTracker.getTracker().trackEvent("purchase", "no_ads", "already owned", (long) result.getResponse());

					return;
				}
				if (result.isFailure()) {
					Log.e("NO ADS purchase", "Error purchasing: " + result);
					Toast.makeText(getActivity(), " Problème durant l'achat",
							Toast.LENGTH_LONG).show();
					EasyTracker.getTracker().trackEvent("purchase", "no_ads", "failure", (long) result.getResponse());

					return;
				} else if (purchase.getSku().equals(AppManager.getNO_ADS())) {
					Log.e("NO ADS purchase", "Done " + result);
					Toast.makeText(
							getActivity(),
							"Achat validé, les pubs disparaîtront au prochain rafraîchissement des matchs",
							Toast.LENGTH_LONG).show();
					EasyTracker.getTracker().trackEvent("purchase", "no_ads", "done", (long) result.getResponse());
					refreshPreferences();
					
					
				}

			}
		};

	}

	protected void refreshPreferences() {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putBoolean("adsFree", true);
					editor.commit();
		
	}

	public SettingsFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		activity = getActivity();
		// Load the preferences from an XML resource
		
		Preference noAdsPurchasePreference = (Preference) findPreference("removeAds");
		noAdsPurchasePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				if (billingHelperReady&&!helper.isAsyncInProgress()) {
					
					helper.launchPurchaseFlow(getActivity(), AppManager.getNO_ADS(), 3,
							noAdsPurchased, "test");
				}
				else Toast.makeText(getActivity(), "Veuillez essayer à nouveau dans quelques secondes", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}

	public void onBillingHelperReady() {
		billingHelperReady = true;

	}
}
