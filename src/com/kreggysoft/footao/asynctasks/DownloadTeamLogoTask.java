package com.kreggysoft.footao.asynctasks;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.kreggysoft.footao.utils.AppManager;
import com.kreggysoft.footao.utils.ImageDownloader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadTeamLogoTask extends AsyncTask<Object, Object, Bitmap> {

	private ImageView teamLogoView;
	private String teamName;
	
	private BasicResponseHandler responseHandler;
	private String response;
	
	
	@Override
	protected void onPostExecute(Bitmap teamLogo) {
		if (teamLogo!=null)
			teamLogoView.setImageBitmap(teamLogo);
		super.onPostExecute(teamLogo);
	}

	@Override
	protected Bitmap doInBackground(Object... params) {
		teamName=(String) params[0];
		teamLogoView=(ImageView) params[1];
		Thread.currentThread().setName("Asynctask:logo "+teamName);
		try {
			Bitmap logo= ImageDownloader.downloadImage(getTeamLogoURL(teamName));
			return logo;
		} catch (MalformedURLException e) {
			Log.e("Team logo URL", "Fail to get logo url for team "+ teamName);
			e.printStackTrace();
		}
		return null;
	}
	
	private String getTeamLogoURL(String name){
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost postTeamLogo = new HttpPost(AppManager.SITE_URL + "/"
				+ AppManager.FOOT_APPLI + "/" + AppManager.TEAM_SCRIPT);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("teamName", name));
		
		try {
			postTeamLogo.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			responseHandler = new BasicResponseHandler();
			// Execute HTTP Post Request
			response = httpclient.execute(postTeamLogo, responseHandler);
			// //Log.d("follow","game"+followedGame.getHomeTeam()+" - "+followedGame.getAwayTeam()+" followed");
			Log.d("Team logo URL", response);
			return response;

		} catch (Exception e) {
			Log.e("Team logo URL", "Fail to get logo url for team "+ name);
			
			e.printStackTrace();
			return "FAIL";
		}
		
	}

}
