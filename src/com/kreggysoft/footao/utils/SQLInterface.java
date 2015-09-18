package com.kreggysoft.footao.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLInterface extends SQLiteOpenHelper {

	public SQLInterface(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}

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

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + GAME_TABLE + " (" + ID + " integer, " + DATE
				+ " text not null, " + TIME + " text not null, " + CHANNEL + " text not null, "
				+ HOMETEAM + " text not null, " + AWAYTEAM + " text not null, "
				+ COMPETITION + " text not null, " + FOLLOWERS + " integer, "
				+ LIKE + " integer, " + DISLIKE + " integer, "+FOLLOW+" integer default 0, "+MILLIS+ " long," +BROADCAST_TYPE+" integer default 4);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

		db.execSQL("DROP TABLE IF EXISTS " + GAME_TABLE);
		onCreate(db);

	}



}
