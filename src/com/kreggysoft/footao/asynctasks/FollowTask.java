package com.kreggysoft.footao.asynctasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.kreggysoft.footao.utils.AppManager;
import com.kreggysoft.footao.utils.Game;
import com.kreggysoft.footao.utils.GameOperations;
import com.kreggysoft.footao.utils.GamesDB;

public class FollowTask extends AsyncTask<Object, Object, Integer> {

	private int ID;
	private GamesDB DB;
	
	private int type = -1;
	private Game g;
	private BasicResponseHandler responseHandler;
	private String response;
	private int result=-1;
	private Context context;
	

	public FollowTask(Context applicationContext) {
		context=applicationContext;
	}

	@Override
	protected Integer doInBackground(Object... params) {
		ID = (int) (long) (Long) params[0];
		DB = (GamesDB) params[1];
		Thread.currentThread().setName("Asynctask follow "+ID);
		type = DB.follow(ID);
		Cursor games=DB.getID(ID);
		if(games.moveToFirst())
		g=GameOperations.cursorGame(games);
		else return -1;
		
		String id = String.valueOf(ID);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost postFollow = new HttpPost(AppManager.SITE_URL + "/"
				+ AppManager.FOOT_APPLI + "/" + AppManager.FOLLOW_SCRIPT);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("follow", String.valueOf(type)));
		try {
			postFollow.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			responseHandler = new BasicResponseHandler();
			// Execute HTTP Post Request
			response = httpclient.execute(postFollow, responseHandler);
			// //Log.d("follow","game"+followedGame.getHomeTeam()+" - "+followedGame.getAwayTeam()+" followed");
			Log.d("follow", response);
			return type;

		} catch (Exception e) {
			Log.e("follow", "Failed to send post");
			Log.d("follow", response);
			e.printStackTrace();
			return type;
		}
	}

	@Override
	protected void onPreExecute() {
		Toast.makeText(context, "Traitement...", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	protected void onPostExecute(Integer ok) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean addToCalendar = sharedPref.getBoolean("addToCalendar", true);
		if (type == 1) {
			if (addToCalendar)
				GameOperations.addToCalendar(g,context);
		}
		if (ok==1)
		Toast.makeText(context, "Match suivi", Toast.LENGTH_SHORT).show();
		
		super.onPostExecute(result);
	}
	

}
