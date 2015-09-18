package com.kreggysoft.footao.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class PushDialogFragment extends DialogFragment {
	private String text;
	private String title;
	public PushDialogFragment(){
		super();
	}

	public PushDialogFragment(String text, String title) {
		this.text = text;
		this.title = title;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(text);
		builder.setTitle(title);
		builder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();

			}
		});

		return builder.create();
	}
}
