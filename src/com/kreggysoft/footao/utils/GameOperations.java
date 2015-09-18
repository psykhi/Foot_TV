package com.kreggysoft.footao.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.CalendarContract.Events;
import android.util.Log;

import com.kreggysoft.footao.R;

public final class GameOperations {
	
	
	private static int id;
	private static String date;
	private static String time;
	private static String channel;
	private static String homeTeam;
	private static String awayTeam;
	private static int followers;
	private static String competition;
	private static int like;
	private static int dislike;
	private static int follow;
	private static int millis;
	private static int broadcastType;
	private static Integer resID;

	public static Game cursorGame(Cursor gamesOfTheDay) {
		 id = gamesOfTheDay.getInt(0);
		 date = gamesOfTheDay.getString(1);
		 time = gamesOfTheDay.getString(2);
		 channel = gamesOfTheDay.getString(3);
		 homeTeam = gamesOfTheDay.getString(4);
		 awayTeam = gamesOfTheDay.getString(5);
		 competition = gamesOfTheDay.getString(6);
		 followers = gamesOfTheDay.getInt(7);
		 like = gamesOfTheDay.getInt(8);
		 dislike = gamesOfTheDay.getInt(9);
		 follow = gamesOfTheDay.getInt(10);
		 millis = gamesOfTheDay.getInt(11);
		Log.d("millis",millis+ " date "+date);
		 broadcastType=gamesOfTheDay.getInt(12);
		
		 resID=ChannelManager.getChannelMapInstance().get(channel);
		

		return new Game(id, date, time, channel, homeTeam, awayTeam, followers,
				competition, like, dislike, follow,resID,broadcastType);
	}
	
	public  static int getColorForGame(String broadcastType) {
		switch (Integer.parseInt(broadcastType)){
		case 0:
			return Color.parseColor("#FFFFFF");
		case 1:
			return Color.parseColor("#FFA88C");
		case 2:
			return Color.parseColor("#C1CBFF");
		case 3:
			return Color.parseColor("#FFFFFF");
			default:
				return Color.parseColor("#FFFFFF");
			
		}
		
	}
	
	public  static int getColorForGame(int broadcastType) {
		switch (broadcastType){
		case 0:
			return R.drawable.game_selector_live;
			//return Color.parseColor("#FFFFFF");
		case 1:
			return R.drawable.game_selector_delayed;
		//	return Color.parseColor("#FFA88C");
		case 2:
			return R.drawable.game_selector_rebroadcast;
			//return Color.parseColor("#C1CBFF");
		case 3:
			return R.drawable.game_selector_live;
			//return Color.parseColor("#FFFFFF");
			default:
				return R.drawable.game_selector_live;
			//	return Color.parseColor("#FFFFFF");
			
		}
		
	}
	
	public static void addToCalendar(Game g,Context context){
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			date = formatter.parse(g.getDate()+" "+g.getTime());
			System.out.println(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra("beginTime", cal.getTimeInMillis());
			intent.putExtra("allDay", false);
			intent.putExtra(Events.EVENT_LOCATION, g.getChannel());
			// intent.putExtra("rrule", "FREQ=DAILY");
			intent.putExtra("endTime", cal.getTimeInMillis() + 120 * 60 * 1000);// 2
																				// hours
			intent.putExtra("title", g.getMatchText());
			context.startActivity(intent);

		} catch (java.text.ParseException e) {
		
			e.printStackTrace();
		}
	}

	public static String getDayText(String date) {
		Date gameDate = new Date();
		GregorianCalendar gregCal = new GregorianCalendar();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			gameDate = formatter.parse(date);
			gregCal.setTime(gameDate);

			String fullDate = gregCal.getDisplayName(
					GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.LONG,
					Locale.FRANCE);
			if (fullDate.length()==1)
				return fullDate.toUpperCase(Locale.FRANCE);
			fullDate=fullDate.substring(0, 1).toUpperCase(Locale.FRANCE)+fullDate.substring(1);
			
			return fullDate;
		} catch (ParseException e) {
			return date;

		}
	}
}
