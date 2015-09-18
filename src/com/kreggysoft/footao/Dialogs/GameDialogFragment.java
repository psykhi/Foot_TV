package com.kreggysoft.footao.Dialogs;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kreggysoft.footao.R;
import com.kreggysoft.footao.asynctasks.DownloadTeamLogoTask;
import com.kreggysoft.footao.interfaces.GameDialogListener;
import com.kreggysoft.footao.utils.ChannelManager;
import com.kreggysoft.footao.utils.Game;

public class GameDialogFragment extends DialogFragment {
	
	Game g;
	TextView gameDateText;
	TextView competitionText;
	TextView followersText;
	TextView homeTeamText;
	TextView awayTeamText;
	TextView broadcastTypeText;
	ImageView homeTeamLogo;
	ImageView awayTeamLogo;
	ImageView channelImage;
	private GameDialogListener dialogListener;
	
	public GameDialogFragment(){
		
	}
	public GameDialogFragment(Game g){
		this.g=g;
	}

	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		try {
            dialogListener = (GameDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement GameDialogListener");
        }
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
		
	    View v;
	    if(g.getAwayTeam().equals("special")){
	     v=inflater.inflate(R.layout.simple_game_dialog_layout, null);
	    }
	    else
	    {
	     v=inflater.inflate(R.layout.game_dialog_layout, null);
	    awayTeamText =(TextView) v.findViewById(R.id.gameDialogAwayTeam);
	    awayTeamText.setText(g.getAwayTeam());
	    awayTeamLogo =(ImageView) v.findViewById(R.id.gameDialogAwayTeamImage);
	    new DownloadTeamLogoTask().execute(new Object[]{g.getAwayTeam(),awayTeamLogo});
	    }
	    builder.setView(v);
		
		
	    gameDateText=(TextView) v.findViewById(R.id.gameDialogDate);
	    competitionText= (TextView) v.findViewById(R.id.gameDialogCompetition);
	    followersText = (TextView) v.findViewById(R.id.gameDialogFollowText);
	    homeTeamText =(TextView) v.findViewById(R.id.gameDialogHomeTeam);
	    homeTeamLogo =(ImageView) v.findViewById(R.id.gameDialogHomeTeamImage);
	    channelImage=(ImageView) v.findViewById(R.id.gameDialogChannelView);
	    broadcastTypeText=(TextView) v.findViewById(R.id.gameDialogBroadcastText);
	    
	    ;
	    
	    new DownloadTeamLogoTask().execute(new Object[]{g.getHomeTeam(),homeTeamLogo});
	    
	    gameDateText.setText((g.getDateText()+ " à "+g.getTime()).toUpperCase(Locale.FRANCE));
	    competitionText.setText(g.getCompetition());
	    followersText.setText(g.getFollowers()+" ");
	    homeTeamText.setText(g.getHomeTeam());
	    
	    broadcastTypeText.setText(g.broadcastToText()+" sur");
	    channelImage.setImageResource(ChannelManager.getChannelMapInstance().get(g.getChannel()));
	    builder.setTitle(g.getMatchText());
	    
	    builder.setNegativeButton("Annuler", null);
	    String positiveButtonText="Ne plus suivre";
	    if(g.getFollow()==0)
	    	positiveButtonText="Suivre";
	    
	    
	    builder.setPositiveButton(positiveButtonText, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialogListener.onPositiveButton(dialog,GameDialogFragment.this.g);
				
			}
		});
	    
	    
		return builder.create();	
	}
	public void setGame(Game g) {
		this.g=g;
		
	}
	

}
