package com.kreggysoft.footao.asynctasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kreggysoft.footao.Dialogs.RefreshProgressDialog;
import com.kreggysoft.footao.activities.MainActivity;
import com.kreggysoft.footao.fragments.BaseFragment;
import com.kreggysoft.footao.utils.AppManager;
import com.kreggysoft.footao.utils.Game;
import com.kreggysoft.footao.utils.GamesDB;

public class RefreshProgramTask extends AsyncTask<Object, Object, Boolean> {
	JSONArray programJSON;

	private GamesDB DB;
	private MainActivity activity;
	private ArrayList<BaseFragment> fragmentList;
	private ProgressBar progressBar;
	private RefreshProgressDialog progressDialog;
	private Game lastGame;
	private int numberOfGames;
	private int progress;
	private TextView gameLogText;

	public RefreshProgramTask(MainActivity activity) {
		super();
		this.activity = activity;
	}

	@Override
	protected Boolean doInBackground(Object... params) {
		Thread.currentThread().setName("AsyncTask refresh program");
		DB = (GamesDB) params[0];
		activity = (MainActivity) params[1];
		fragmentList = activity.getFragmentList();
		programJSON = getProgramJSON();
		if (programJSON == null)
			return false;
		try {
			JSONArray JSONGames = (JSONArray) programJSON;
			numberOfGames = JSONGames.length();

			for (int i = 0; i < numberOfGames; i++) {
				JSONObject gameJSON = JSONGames.getJSONObject(i);
				lastGame = writeInDB(gameJSON);
				publishProgress(i, lastGame);

			}

			Calendar cal = Calendar.getInstance();
			Long now = cal.getTimeInMillis();
			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(activity);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putLong("lastUpdateMillis", now);
			editor.commit();
			// Log.d("commit update time", "Status " + commitStatus);
			return true;

		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}

	}

	@Override
	protected void onProgressUpdate(Object... progressBundle) {

		Integer currentGame = (Integer) progressBundle[0];
		Game g = (Game) progressBundle[1];
		progress = (int) (((double) currentGame / (double) numberOfGames) * 100);
		Log.d("progress", progress + "%");
		if (progressBar == null)
			progressBar = progressDialog.getProgressBar();
		if (gameLogText == null)
			gameLogText = progressDialog.getGameLogText();
		gameLogText.setText(g.getMatchText() + "  " + currentGame + "/"
				+ numberOfGames);
		progressBar.setProgress(progress);
		super.onProgressUpdate(progressBundle);
	}

	private Game writeInDB(JSONObject gameJSON) {

		try {

			String broadcastType = gameJSON.getString("broadcastType");
			int type = AppManager.GAME_UNKNOWN;
			if (broadcastType.equals("live"))
				type = AppManager.GAME_LIVE;
			else if (broadcastType.equals("rebroadcast"))
				type = AppManager.GAME_REBROADCAST;
			else if (broadcastType.equals("delayed"))
				type = AppManager.GAME_DELAYED;
			Game g = new Game(Integer.parseInt(gameJSON.getString("id")),
					gameJSON.getString("date"), gameJSON.getString("time"),
					gameJSON.getString("channel"),
					gameJSON.getString("homeTeam"),
					gameJSON.getString("awayTeam"), Integer.parseInt(gameJSON
							.getString("followers")),
					gameJSON.getString("competition"),
					Integer.parseInt(gameJSON.getString("like")),
					Integer.parseInt(gameJSON.getString("dislike")), type);
			DB.insertGame(g);
			return g;
		}

		// mSectionsPagerAdapter.updateFragments();;
		catch (JSONException e) {
			e.printStackTrace();
			return null;

		}

	}

	@Override
	protected void onPostExecute(Boolean success) {
		progressDialog.dismiss();
		if (success) {
			Toast.makeText(activity, "Programme actualisé", Toast.LENGTH_SHORT)
					.show();

			try {
				for (int i = 0; i < fragmentList.size(); i++)
					fragmentList.get(i).refreshDataset();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			Toast.makeText(activity, "Poblème de connexion", Toast.LENGTH_LONG)
					.show();
		super.onPostExecute(success);
	}

	@Override
	protected void onPreExecute() {
		numberOfGames = 0;
		Log.d("program refresh", "start");
		progressDialog = new RefreshProgressDialog();
		progressBar = progressDialog.getProgressBar();
		gameLogText = progressDialog.getGameLogText();
		if (progressDialog != null)
			progressDialog.show(activity.getFragmentManager(), "refresh");

	}

	private JSONArray getProgramJSON() {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet getProgram = new HttpGet(AppManager.SITE_URL + "/"
				+ AppManager.FOOT_APPLI + "/" + AppManager.PROGRAM_SCRIPT);
		HttpResponse response;
		try {
			response = httpclient.execute(getProgram);
			// //Log.d("json",EntityUtils.toString(response.getEntity()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), HTTP.UTF_8));
			StringBuilder builder = new StringBuilder();

			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
			String res = builder.toString();
			// //Log.d("Program received ", res);

			// JSONTokener tokener = new JSONTokener(builder.toString());
			JSONArray finalResult = new JSONArray(res);
			// //Log.d("PRogram", finalResult.toString());

			return finalResult;
		} catch (UnknownHostException e) {

			return null;
		} catch (Exception e) {
			Log.e(" Get Program", "Failed to parse JSON");
			e.printStackTrace();
			return null;
		}
	}
}
