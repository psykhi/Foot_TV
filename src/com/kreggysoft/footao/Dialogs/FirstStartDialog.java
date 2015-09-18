package com.kreggysoft.footao.Dialogs;

import com.google.analytics.tracking.android.EasyTracker;
import com.kreggysoft.footao.R;
import com.kreggysoft.footao.activities.TutorialActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class FirstStartDialog extends DialogFragment {


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(getString(R.string.firstStartText));
		builder.setTitle(getString(R.string.firstStartTitle));
		builder.setPositiveButton("Tutorial", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
	
				Intent intentTuto = new Intent(getActivity(),TutorialActivity.class);
				EasyTracker.getTracker().trackEvent("firstStartDialog", "toTutorial",
						getClass().getSimpleName(), (long) 0);
				startActivity(intentTuto);

			}
		});
		builder.setNegativeButton("Plus tard!", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
				
			}
		});

		return builder.create();
	}

}
