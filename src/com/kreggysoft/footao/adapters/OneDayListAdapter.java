package com.kreggysoft.footao.adapters;

import java.util.GregorianCalendar;
import java.util.HashMap;

import com.google.ads.AdView;
import com.kreggysoft.footao.R;
import com.kreggysoft.footao.adapters.GameDatesListAdapter.ViewHolder;
import com.kreggysoft.footao.utils.AppManager;
import com.kreggysoft.footao.utils.ChannelManager;
import com.kreggysoft.footao.utils.Game;
import com.kreggysoft.footao.utils.GameOperations;
import com.kreggysoft.footao.utils.GamesDB;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class OneDayListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private SparseArray<Game> games;
	private SparseIntArray positionIDMap;
	private GamesDB DB;
	private Context context;
	private final int AD = -42;
	private Cursor cursor;
	private Game tempGame;

	public OneDayListAdapter(Context context, GamesDB DB, Cursor allGamesCursor) {

		this.DB = DB;
		this.context = context;
		this.cursor = allGamesCursor;
		inflater = LayoutInflater.from(context);
		setCursor(this.cursor);

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
			if (!game.getAwayTeam().equals("special"))
				holder.gameText.setText(game.getHomeTeam() + " - "
						+ game.getAwayTeam());
			else
				holder.gameText.setText(game.getHomeTeam());
			holder.timeText.setTextColor(game.getTimeColorForGame());
			holder.timeText.setText(game.getTime());
			holder.channelImage.setImageResource(ChannelManager
					.getChannelMapInstance().get(game.getChannel()));

			convertView.setBackgroundResource(game.getBackgroundColorForGame());

			return convertView;
		}

	}

	class ViewHolder {
		TextView timeText;
		TextView gameText;
		ImageView channelImage;
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
