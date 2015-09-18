package com.kreggysoft.footao.asynctasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.kreggysoft.footao.Dialogs.PushDialogFragment;
import com.kreggysoft.footao.utils.AppManager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

public class PushDialogTask extends AsyncTask<Integer, Object, String[]> {

	private Integer lastID;
	FragmentActivity activity;
	public PushDialogTask(FragmentActivity activity){
		this.activity=activity;
	}
	@Override
	protected void onPostExecute(String[] args) {
		if (args != null) {
			if (args[0] != null) {
				PushDialogFragment pushFragment = new PushDialogFragment(
						args[0], args[1]);
				pushFragment.show(activity.getSupportFragmentManager(), "push");
				SharedPreferences sharedPrefs = PreferenceManager
						.getDefaultSharedPreferences(activity);
				Editor edit = sharedPrefs.edit();
				edit.putInt("lastPushID", Integer.parseInt(args[2]));
				edit.commit();
			}
		}

		super.onPostExecute(args);
	}

	
	@Override
	protected String[] doInBackground(Integer... params) {
		lastID = params[0];
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet getPushMessage = new HttpGet(AppManager.SITE_URL + "/"
				+ AppManager.FOOT_APPLI + "/" + AppManager.PUSH_SCRIPT+"?version=1");
		HttpResponse response;
		try {
			response = httpclient.execute(getPushMessage);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							response.getEntity().getContent(), HTTP.UTF_8));
			StringBuilder builder = new StringBuilder();

			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
			String res = builder.toString();
			// JSONTokener tokener = new JSONTokener(builder.toString());
			JSONObject finalResult = new JSONObject(res);
			// //Log.d("PRogram", finalResult.toString());
			int ID = finalResult.getInt("ID");
			String text = finalResult.getString("text");
			String title = finalResult.getString("title");
			if (ID > lastID)
				return new String[] { text, title, String.valueOf(ID) };
			else
				return null;
		} catch (UnknownHostException e) {

			return null;
		} catch (Exception e) {
			// Log.e(" Get Program", "Failed to parse JSON");
			e.printStackTrace();
			return null;
		}
	}

}
