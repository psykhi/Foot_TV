package com.kreggysoft.footao.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

import com.kreggysoft.footao.R;

public class Game {

	private int gameID;
	private String time;
	private String date;
	private String channel;
	private String homeTeam;
	private String awayTeam;
	private int followers;
	private String competition;
	private int like;
	private int dislike;
	private int follow;
	private int channelRessourceID;
	private long millis;
	private String channelKey;
	private int broadcastType = AppManager.GAME_UNKNOWN;
	
	
	private SimpleDateFormat formatter = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm");


	public Game() {
		
	}
	
	public Game(int gameID, String date, String time, String channel,
			String homeTeam, String awayTeam, int followers,
			String competition, int like, int dislike, int broadcastType) {
		this.time = time;
		this.date = date;
		this.channel = channel;
		this.competition = competition;
		this.dislike = dislike;
		this.like = like;
		this.channelKey = ChannelManager.getChannelKey(channel);
		this.followers = followers;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.gameID = gameID;
		this.broadcastType = broadcastType;
		follow = 0;
		try {
			Date matchDate = formatter.parse(date + " " + time);
			this.millis = matchDate.getTime();
		} catch (ParseException e) {
			Log.e("Read match time", "Problem parsing date");
			e.printStackTrace();
		}
		channelRessourceID = R.drawable.ic_launcher;
	}

	public Game(int gameID, String date, String time, String channel,
			String homeTeam, String awayTeam, int followers,
			String competition, int like, int dislike, int follow,
			int broadcastType) {
		this.time = time;
		this.date = date;
		this.channel = channel;
		this.channelKey = ChannelManager.getChannelKey(channel);
		this.competition = competition;
		this.dislike = dislike;
		this.like = like;
		this.followers = followers;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.gameID = gameID;
		this.follow = follow;
		this.broadcastType = broadcastType;
		channelRessourceID = R.drawable.ic_launcher;
	
		try {
			Date matchDate = formatter.parse(date + " " + time);
			this.millis = matchDate.getTime();
		} catch (ParseException e) {
			Log.e("Read match time", "Problem parsing date");
			e.printStackTrace();
		}
	}

	public Game(int gameID, String date, String time, String channel,
			String homeTeam, String awayTeam, int followers,
			String competition, int like, int dislike, int follow,
			int ressourceID, int broadcastType) {
		this.broadcastType = broadcastType;
		this.channelKey = ChannelManager.getChannelKey(channel);
		this.time = time;
		this.date = date;
		this.channel = channel;
		this.competition = competition;
		this.dislike = dislike;
		this.like = like;
		this.followers = followers;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.gameID = gameID;
		this.follow = follow;
		this.channelRessourceID = ressourceID;
	
		try {
			Date matchDate = formatter.parse(date + " " + time);
			this.millis = matchDate.getTime();
		} catch (ParseException e) {
			Log.e("Read match time", "Problem parsing date");
			e.printStackTrace();
		}
	}

	public long getMillis() {
		return millis;
	}

	public int getChannelRessourceID() {
		return channelRessourceID;
	}

	public void setChannelRessourceID(int channelRessourceID) {
		this.channelRessourceID = channelRessourceID;
	}

	public int getBroadcastType() {
		return this.broadcastType;
	}

	public int getFollow() {
		return follow;
	}

	public void setFollow(int follow) {
		this.follow = follow;
	}

	public int getGameID() {
		return gameID;
	}

	public String getTime() {
		return time;
	}

	public String getChannel() {
		return channel;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public int getFollowers() {
		return followers;
	}

	public String getCompetition() {
		return competition;
	}

	public int getLike() {
		return like;
	}

	public int getDislike() {
		return dislike;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void log() {
		Log.i("game log", "/---------GAME--------/");
		Log.i("game log", "The " + date + " at " + time);
		Log.i("game log", "between " + homeTeam + " and " + awayTeam);
		Log.i("game log", "for " + competition + " on " + channel);
		Log.i("game log", "follows " + follow);
		Log.d("game log", "/---------GAME--------/");

	}

	public boolean isVisible(SharedPreferences prefs) {
		return prefs.getBoolean(channelKey, true);

	}

	public String getMatchText() {
		if (getAwayTeam().equals("special"))
			return getHomeTeam();
		else
			return getHomeTeam() + " - " + getAwayTeam();
	}

	public int getBackgroundColorForGame() {
		switch (broadcastType) {
		case 0:
			return R.drawable.game_selector_live;
			// return Color.parseColor("#FFFFFF");
		case 1:
			return R.drawable.game_selector_delayed;
			// return Color.parseColor("#FFA88C");
		case 2:
			return R.drawable.game_selector_rebroadcast;
			// return Color.parseColor("#C1CBFF");
		case 3:
			return R.drawable.game_selector_live;
			// return Color.parseColor("#FFFFFF");
		default:
			return R.drawable.game_selector_live;
			// return Color.parseColor("#FFFFFF");

		}

	}
	
	public String broadcastToText() {
		switch (broadcastType) {
		case 0:
			return "En direct";
		case 1:
			return "En différé";
			// return Color.parseColor("#FFA88C");
		case 2:
			return "Rediffusion";
			// return Color.parseColor("#C1CBFF");
		case 3:
			return "En direct";
			// return Color.parseColor("#FFFFFF");
		default:
			return "En direct";
			// return Color.parseColor("#FFFFFF");

		}

	}

	public int getTimeColorForGame() {
		switch (broadcastType) {
		case 0:
			return Color.parseColor("darkgray");
			// return Color.parseColor("#FFFFFF");

		default:
			return Color.parseColor("white");
			// return Color.parseColor("#FFFFFF");

		}

	}

	public String getDateText() {
		Date gameDate = new Date();
		GregorianCalendar gregCal = new GregorianCalendar();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			gameDate = formatter.parse(this.getDate());
			gregCal.setTime(gameDate);

			String fullDate = gregCal.getDisplayName(
					GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.LONG,
					Locale.FRANCE);
			fullDate += " " + this.getDate().substring(8, 10);
			fullDate += " "
					+ gregCal.getDisplayName(GregorianCalendar.MONTH,
							GregorianCalendar.LONG, Locale.FRANCE);
			return fullDate;
		} catch (ParseException e) {
			return this.getDate();

		}
	}
}
