package com.kreggysoft.footao.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.google.ads.AdView;
import com.kreggysoft.footao.R;
import com.kreggysoft.footao.utils.AppManager;
import com.kreggysoft.footao.utils.ChannelManager;
import com.kreggysoft.footao.utils.Game;
import com.kreggysoft.footao.utils.GameOperations;
import com.kreggysoft.footao.utils.GamesDB;

public class GameDatesListAdapter extends BaseAdapter implements
		StickyListHeadersAdapter {

	private LayoutInflater inflater;
	private SparseArray<Game> games;
	private SparseIntArray positionIDMap;
	private GamesDB DB;
	private Context context;
	private final int AD = -42;
	private Cursor cursor;
	private Game tempGame;
	private GregorianCalendar gregCal;
	private Typeface roboto;

	public GameDatesListAdapter(Context context, GamesDB DB,
			Cursor allGamesCursor) {
		gregCal = new GregorianCalendar();
		roboto = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Thin.ttf");
		this.DB = DB;
		this.context = context;
		this.cursor = allGamesCursor;
		inflater = LayoutInflater.from(context);
		if (positionIDMap != null)
			positionIDMap.clear();
		else
			positionIDMap = new SparseIntArray();
		populatePositionMap(cursor);

	}
	public int getGamesCount(){
		return games.size();
	}

	private void populatePositionMap(Cursor cursor) {

		games = new SparseArray<Game>();
		int row = 0; // current position in the map
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean adsFree = sharedPref.getBoolean("adsFree", false);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			if (row % AppManager.getAdsSpace() == 0) {
				if (!adsFree) {
					positionIDMap.put(row, AD);
					row++;
				}
			}
			tempGame = GameOperations.cursorGame(cursor);
			if (tempGame.isVisible(sharedPref)){
			games.put(tempGame.getGameID(), tempGame);
			positionIDMap.put(row, tempGame.getGameID());
			row++;
			}
			cursor.moveToNext();

		}
		cursor.close();
	}

	@Override
	public int getCount() {

		return positionIDMap.size();
	}

	@Override
	public Object getItem(int position) {
		return games.get(positionIDMap.get(position));

	}

	@Override
	public long getItemId(int position) {
		return (long) positionIDMap.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (getItemId(position) == AD) {
			if (!(convertView instanceof FrameLayout)) {

				convertView = inflater.inflate(R.layout.admoblayout, null);
				AdView v = (AdView) convertView.findViewById(R.id.adView);

			}
			return convertView;
		} else

		{
			Game game = (Game) getItem(position);
			holder = new ViewHolder();
			if (convertView == null || convertView instanceof FrameLayout) {

				convertView = inflater.inflate(
						com.kreggysoft.footao.R.layout.gamelistparentview,
						parent, false);
			}

			holder.gameText = (TextView) convertView.findViewById(R.id.Game);
			holder.timeText = (TextView) convertView.findViewById(R.id.time);
			holder.channelImage = (ImageView) convertView
					.findViewById(R.id.channelImage);

			holder.gameText.setText(game.getMatchText());
			holder.timeText.setTextColor(game.getTimeColorForGame());
			holder.timeText.setText(game.getTime());
			holder.channelImage.setImageResource(ChannelManager
					.getChannelMapInstance().get(game.getChannel()));

			convertView.setBackgroundResource(game.getBackgroundColorForGame());

			return convertView;
		}
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		Game game;
		if (getItemId(position) != AD)
			game = (Game) getItem(position);

		else {
			if (position != 0)

				game = (Game) getItem(position - 1);
			else
				game = (Game) getItem(position + 1);
		}
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = inflater.inflate(
					com.kreggysoft.footao.R.layout.game_date_list_header,
					parent, false);
			holder.dateText = (TextView) convertView
					.findViewById(R.id.headerDate);

			convertView.setTag(holder);

		} else
			holder = (HeaderViewHolder) convertView.getTag();
		Date gameDate = new Date();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			gameDate = formatter.parse(game.getDate());
			gregCal.setTime(gameDate);

			String fullDate = gregCal.getDisplayName(
					GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.LONG,
					Locale.FRANCE);
			fullDate += " " + game.getDate().substring(8, 10);
			fullDate += " "
					+ gregCal.getDisplayName(GregorianCalendar.MONTH,
							GregorianCalendar.LONG, Locale.FRANCE);
			holder.dateText.setTypeface(roboto);
			holder.dateText.setText(fullDate.toUpperCase(Locale.FRANCE));

		} catch (ParseException e) {
			holder.dateText.setText(game.getDate());
			e.printStackTrace();
		}

		return convertView;
	}

	@Override
	public long getHeaderId(int position) {

		Game game;
		if (getItemId(position) != AD)
			game = (Game) getItem(position);

		else {
			if (position != 0)

				game = (Game) getItem(position - 1);
			else
				game = (Game) getItem(position + 1);
		}
		String date = game.getDate().substring(8, 10);
		return Integer.parseInt(date);

	}

	class HeaderViewHolder {
		TextView dateText;
	}

	class ViewHolder {
		TextView timeText;
		TextView gameText;
		ImageView channelImage;
	}

	public void update(Cursor nextGames) {
		

	}

	public void setCursor(Cursor prepareCursor) {
		if (games != null)
			games.clear();
		else
			games = new SparseArray<Game>();
		if (positionIDMap != null)
			positionIDMap.clear();
		else
			positionIDMap = new SparseIntArray();
		populatePositionMap(prepareCursor);
		
	}

}
