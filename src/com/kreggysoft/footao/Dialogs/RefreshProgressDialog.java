package com.kreggysoft.footao.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kreggysoft.footao.R;

public class RefreshProgressDialog extends DialogFragment {

	ProgressBar progressBar;
	TextView gameLogText;

	public TextView getGameLogText() {
		return gameLogText;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v=inflater.inflate(R.layout.refresh_dialog, null);
	    progressBar =(ProgressBar) v.findViewById(R.id.refreshProgressBar);
	    progressBar.setProgress(0);
	    gameLogText =(TextView) v.findViewById(R.id.refreshGameLogText);
	    builder.setTitle("Chargement des matchs...");
	    builder.setView(v);
		return builder.create();
	}
	
	
}
