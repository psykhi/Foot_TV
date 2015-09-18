package com.kreggysoft.footao.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class GamesDB {
	private static final String DB_NAME = "games.db";
	private static final int DB_VERSION = 2;
	// FIELDS
	public static final String GAME_TABLE = "games";
	public static final String ID = "id";
	public static final String DATE = "date";
	public static final String TIME = "time";
	public static final String CHANNEL = "channel";
	public static final String HOMETEAM = "homeTeam";
	public static final String AWAYTEAM = "awayTeam";
	public static final String COMPETITION = "competition";
	public static final String FOLLOWERS = "followers";
	public static final String LIKE = "like";
	public static final String DISLIKE = "dislike";
	public static final String FOLLOW = "follow";
	public static final String MILLIS = "millis";
	public static final String BROADCAST_TYPE = "broadcastType";
	// --------
	private SQLInterface gamesAccess;
	private SQLiteDatabase gamesDB;
	private String date;
	private int id;
	private String channel;
	private String time;
	private String homeTeam;
	private String awayTeam;
	private String competition;
	private int followers;
	private int like;
	private int dislike;
	private int follow;
	private String matchText;
	private long millis;
	private int broadcastType;

	public GamesDB(Context context) {
		gamesAccess = new SQLInterface(context, DB_NAME, null, DB_VERSION);
	}

	public void open() {
		gamesDB = gamesAccess.getWritableDatabase();
	}

	public void reset() {
		gamesDB.execSQL("delete from " + GAME_TABLE);
		// gamesDB.execSQL("delete from sqlite_sequence where name ="+GAME_TABLE);
		// gamesAccess.onCreate(gamesDB);

	}

	public void close() {
		gamesDB.close();
	}

	public SQLiteDatabase getDB() {
		return gamesDB;
	}

	public Cursor searchInDB(String query) {

		Cursor rawQueryCursor = gamesDB.rawQuery("select * from " + GAME_TABLE
				+ " where " + HOMETEAM + " LIKE" + " ? OR " + AWAYTEAM
				+ " LIKE" + " ? COLLATE NOCASE", new String[] {
				"%" + query + "%", "%" + query + "%" });
		// Cursor rawQueryCursor=gamesDB.rawQuery("select * from " + GAME_TABLE
		// ,
		// null);
		String[] columns = { BaseColumns._ID,
				SearchManager.SUGGEST_COLUMN_TEXT_1,
				SearchManager.SUGGEST_COLUMN_TEXT_2,
				SearchManager.SUGGEST_COLUMN_INTENT_DATA };
		MatrixCursor result = new MatrixCursor(columns);
		rawQueryCursor.moveToFirst();
		while (!rawQueryCursor.isAfterLast()) {
			id = rawQueryCursor.getInt(0);
			date = rawQueryCursor.getString(1);
			time = rawQueryCursor.getString(2);
			channel = rawQueryCursor.getString(3);
			homeTeam = rawQueryCursor.getString(4);
			awayTeam = rawQueryCursor.getString(5);
			competition = rawQueryCursor.getString(6);
			followers = rawQueryCursor.getInt(7);
			like = rawQueryCursor.getInt(8);
			dislike = rawQueryCursor.getInt(9);
			follow = rawQueryCursor.getInt(10);
			millis = rawQueryCursor.getLong(11);
			broadcastType=rawQueryCursor.getInt(12);
			if (awayTeam.equals("special"))
				matchText = homeTeam;
			else
				matchText = homeTeam + " - " + awayTeam;
			result.addRow(new String[] { Integer.toString(id), matchText,
					GameOperations.getDayText(date) + " à " + time + " sur " + channel,
					Integer.toString(id) });
			rawQueryCursor.moveToNext();
		}
		rawQueryCursor.close();
		return result;
	}

	public Cursor cursorRawSearchInDB(String query) {
		return gamesDB.rawQuery("select * from " + GAME_TABLE + " where "
				+ HOMETEAM + " LIKE" + " ? OR " + AWAYTEAM + " LIKE"
				+ " ? COLLATE NOCASE", new String[] { "%" + query + "%",
				"%" + query + "%" });
	}

	public long insertGame(Game g) {
		Cursor testCursor = gamesDB.rawQuery("select * from " + GAME_TABLE
				+ " where " + ID + "=?",
				new String[] { String.valueOf(g.getGameID()) });
		if (testCursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put(ID, g.getGameID());
			values.put(DATE, g.getDate());
			values.put(TIME, g.getTime());
			values.put(CHANNEL, g.getChannel());
			values.put(HOMETEAM, g.getHomeTeam());
			values.put(AWAYTEAM, g.getAwayTeam());
			values.put(COMPETITION, g.getCompetition());
			values.put(FOLLOWERS, g.getFollowers());
			values.put(LIKE, g.getLike());
			values.put(DISLIKE, g.getDislike());
			values.put(MILLIS, g.getMillis());
			values.put(BROADCAST_TYPE, g.getBroadcastType());
		
			return gamesDB.insert(GAME_TABLE, null, values);
		} else {
			String where = "id=?";
			String[] whereArgs = new String[] { String.valueOf(g.getGameID()) };
			ContentValues values = new ContentValues();
			values.put(ID, g.getGameID());
			values.put(DATE, g.getDate());
			values.put(TIME, g.getTime());
			values.put(CHANNEL, g.getChannel());
			values.put(HOMETEAM, g.getHomeTeam());
			values.put(AWAYTEAM, g.getAwayTeam());
			values.put(COMPETITION, g.getCompetition());
			values.put(FOLLOWERS, g.getFollowers());
			values.put(LIKE, g.getLike());
			values.put(DISLIKE, g.getDislike());
			values.put(MILLIS, g.getMillis());
			values.put(BROADCAST_TYPE, g.getBroadcastType());
			return gamesDB.update(GAME_TABLE, values, where, whereArgs);

		}

	}

	public int follow(int GameID) {
		int ret = 0;
		Cursor testCursor = gamesDB.rawQuery("select * from " + GAME_TABLE
				+ " where " + ID + "=?",
				new String[] { String.valueOf(GameID) });
		if (testCursor.getCount() == 1) {
			testCursor.moveToFirst();

			int exisingFollowers = testCursor.getInt(7);
			int follow = testCursor.getInt(10);
			String where = "id=?";
			String[] whereArgs = new String[] { String.valueOf(GameID) };
			ContentValues values = new ContentValues();

			if (follow == 1) {
				values.put(FOLLOW, "0");
				Log.d("unfollow in DB", "updating DB");
				values.put(FOLLOWERS,
						String.valueOf(Math.max(exisingFollowers - 1, 0)));

			} else {
				values.put(FOLLOWERS, String.valueOf(exisingFollowers + 1));
				values.put(FOLLOW, "1");
				Log.d("follow in DB", "updating DB");
				ret = 1;
			}
			gamesDB.update(GAME_TABLE, values, where, whereArgs);
			return ret;
		} else {
			Log.e("follow in DB", "couldnt update DB");
			return -1;
		}
	}

	public Cursor getGamesForDate(String date) {

		
		Cursor gamesCursor = gamesDB.rawQuery("select * from " + GAME_TABLE
				+ " where " + DATE + "=? ORDER BY millis ",
				new String[] { date });

		return gamesCursor;

	}

	public Cursor getID(int gameID) {

		return gamesDB.rawQuery("select * from " + GAME_TABLE + " where " + ID
				+ "=?", new String[] { String.valueOf(gameID) });
	}

	public Cursor getFollowedGames() {
		return gamesDB.rawQuery("select * from " + GAME_TABLE + " where "
				+ FOLLOW + "=1", null);
		// return gamesDB.rawQuery("select * from " + GAME_TABLE , null);

	}

	public Cursor getTrendingGames() {
		return gamesDB.rawQuery("select * from " + GAME_TABLE + " where "
				+ FOLLOWERS + ">=1 ORDER BY followers DESC LIMIT 10", null);

	}

	public void refresh() {
		Date gameDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Cursor allGamesCursor = gamesDB.rawQuery("select * from " + GAME_TABLE,
				null);
		allGamesCursor.moveToFirst();

		while (!allGamesCursor.isAfterLast()) {
			String gameDateString = allGamesCursor.getString(1);
			int id = allGamesCursor.getInt(0);
			try {
				gameDate = formatter.parse(gameDateString);
				Calendar calNow = Calendar.getInstance();
				Calendar calGame = Calendar.getInstance();
				calGame.setTime(gameDate);

				calNow.add(Calendar.DAY_OF_YEAR, -1);
				Date now = calNow.getTime();
				if (gameDate.compareTo(now) < 0) {
					int removed = removeFromDB(id);
					Log.d("refresh table", removed + " game removed");
				}
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			allGamesCursor.moveToNext();
		}
		allGamesCursor.close();
	}

	private int removeFromDB(int id) {
		return gamesDB.delete(GAME_TABLE, ID + "=?",
				new String[] { String.valueOf(id) });

	}

	public Cursor getNextGames(String startingDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long startMillis=0;
		try {
			Date matchDate = formatter.parse(startingDate+" 00:00");
			 startMillis = matchDate.getTime();
			 Cursor gamesCursor = gamesDB.rawQuery("select * from " + GAME_TABLE
						+ " where " + MILLIS + ">=? ORDER BY millis",
						new String[] {String.valueOf( startMillis) });
			 return gamesCursor;
		} catch (ParseException e) {
			Log.e("Get Next Games","Problem parsing date");
			e.printStackTrace();
			return null;
		}
		
		
		
	}
}
