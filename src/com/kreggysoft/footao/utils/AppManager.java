package com.kreggysoft.footao.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppManager {
	protected static int adsSpace=9;
	private static String NO_ADS = "no_ad";
	public static final String SITE_URL = "http://theboredengineers.com";
	public static final String FOOT_APPLI = "appliFoot2";
	public static final String FOLLOW_SCRIPT = "follow.php";
	public static final String PROGRAM_SCRIPT = "programRequest.php";
	public static final String PUSH_SCRIPT = "push.php";
	public static final String TRENDING_SCRIPT = "getTrendingGames.php";
	public static final String TEAM_SCRIPT = "getTeamLogo.php";
	public static final String TUTORIAL = "tutorial.php";
	public static String TIME = "TIME";
	public static String ID = "ID";
	public static String MATCH = "MATCH";
	public static String DATE = "DATE";
	public static final String FOLLOWERS = "FOLLOWERS";
	public static final String CHANNEL = "CHANNEL";
	public static final String COMPETITION = "COMPETITION";
	public static final String FOLLOW = "FOLLOW";
	public static final String BROADCAST_TYPE = "BROADCAST_TYPE";
	
	
	public static int GAME_LIVE=0;
	public static int GAME_DELAYED=1;
	public static int GAME_REBROADCAST=2;
	public static int GAME_UNKNOWN=3;

	public static String getNO_ADS() {
		return NO_ADS;
	}

	public static int getAdsSpace() {
		return adsSpace;
	}
	
	public static String getDate(int index) {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, index);
		Date tomorrow = cal.getTime();
		String date = (new SimpleDateFormat("yyyy-MM-dd")).format(tomorrow);
		return date;
	}

}
