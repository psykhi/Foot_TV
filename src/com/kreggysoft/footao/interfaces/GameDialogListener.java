package com.kreggysoft.footao.interfaces;

import android.content.DialogInterface;

import com.kreggysoft.footao.Dialogs.GameDialogFragment;
import com.kreggysoft.footao.utils.Game;

public interface GameDialogListener {
	
	public abstract void onNegativeButton(GameDialogFragment dialog);
	
	

	public abstract void onPositiveButton(DialogInterface dialog, Game g);

}
