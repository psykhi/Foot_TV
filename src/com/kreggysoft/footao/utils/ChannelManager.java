package com.kreggysoft.footao.utils;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.kreggysoft.footao.R;

public class ChannelManager {

	protected static Map<String, Integer> channelMap = new HashMap<String, Integer>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Integer get(Object key) {
			Integer mapGet = super.get(key);
			if( mapGet==null)
				return R.drawable.ic_launcher;
			else return mapGet;
		}
		
	};
	protected static Map<String, String> channelKeyMap = new HashMap<String, String>();
	protected static boolean isOpen = false;

	public static Map<String, Integer> getChannelMapInstance() {
		if (channelMap!=null)
			return channelMap;
		else {
			
			return initChannelMaps();
		}
	}

	public static Map<String, Integer> initChannelMaps() {
		channelMap.put("Grand Lille TV", R.drawable.grandlilletv);
		channelMap.put("beIN Sport 1", R.drawable.beinsport1);
		channelMap.put("beIN Sport 2", R.drawable.beinsport2);
		channelMap.put("Sport +", R.drawable.sportplus);
		channelMap.put("Canal+", R.drawable.canal);
		channelMap.put("Eurosport", R.drawable.eurosport);
		channelMap.put("Eurosport 2", R.drawable.eurosport2);
		channelMap.put("Canal+ Sport", R.drawable.canalsport);
		channelMap.put("beIN MAX 1", R.drawable.beinsportmax);
		channelMap.put("beIN MAX 2", R.drawable.beinsportmax);
		channelMap.put("beIN MAX 3", R.drawable.beinsportmax);
		channelMap.put("beIN MAX 4", R.drawable.beinsportmax);
		channelMap.put("beIN MAX 5", R.drawable.beinsportmax);
		channelMap.put("beIN MAX 6", R.drawable.beinsportmax);
		channelMap.put("beIN MAX 7", R.drawable.beinsportmax);
		channelMap.put("beIN MAX 8", R.drawable.beinsportmax);
		channelMap.put("beIN MAX 9", R.drawable.beinsportmax);
		channelMap.put("beIN MAX 10", R.drawable.beinsportmax);
		channelMap.put("OM TV", R.drawable.omtv);
		channelMap.put("OL TV", R.drawable.oltv);
		channelMap.put("Girondins TV", R.drawable.girondinstv);
		channelMap.put("France 2", R.drawable.france2);
		channelMap.put("France 3", R.drawable.france3);
		channelMap.put("France 4", R.drawable.france4);
		channelMap.put("France Ô", R.drawable.franceo);
		channelMap.put("France O", R.drawable.franceo);
		channelMap.put("FOOT+ 1", R.drawable.footplus);
		channelMap.put("FOOT+ 2", R.drawable.footplus);
		channelMap.put("FOOT+ 3", R.drawable.footplus);
		channelMap.put("FOOT+ 4", R.drawable.footplus);
		channelMap.put("FOOT+ 5", R.drawable.footplus);
		channelMap.put("FOOT+ 6", R.drawable.footplus);
		channelMap.put("FOOT+ 7", R.drawable.footplus);
		channelMap.put("FOOT+ 8", R.drawable.footplus);
		channelMap.put("MCS", R.drawable.machainesport);
		channelMap.put("Mont-Blanc TV", R.drawable.tv8montblanc);
		channelMap.put("Onzéo", R.drawable.onzeo);
		channelMap.put("W9", R.drawable.w9);
		channelMap.put("TF1", R.drawable.tf1);
		channelMap.put("D8", R.drawable.d8);
		channelMap.put("D17", R.drawable.d17);
		channelMap.put("NT1", R.drawable.nt1);
		channelMap.put("TV Toulouse", R.drawable.tlt);
		channelMap.put("FFF.fr", R.drawable.fff);
		initChannelKeys();
		// Log.d("hashmap", "image" + channelMap.get("beIN Sport 1"));
		Log.d("channels", "loaded");
		
		return channelMap;
	}
	
	public static Map<String, String> initChannelKeys() {
		channelKeyMap.put("Grand Lille TV","grandlilletv");
		channelKeyMap.put("beIN Sport 1","beinsport");
		channelKeyMap.put("beIN Sport 2", "beinsport");
		channelKeyMap.put("Sport +", "sportplus");
		channelKeyMap.put("Canal+", "canalplus");
		channelKeyMap.put("Eurosport", "eurosport");
		channelKeyMap.put("Eurosport 2", "eurosport2");
		channelKeyMap.put("Canal+ Sport", "canalsport");
		channelKeyMap.put("beIN MAX 1", "beinsport");
		channelKeyMap.put("beIN MAX 2", "beinsport");
		channelKeyMap.put("beIN MAX 3", "beinsport");
		channelKeyMap.put("beIN MAX 4", "beinsport");
		channelKeyMap.put("beIN MAX 5", "beinsport");
		channelKeyMap.put("beIN MAX 6", "beinsport");
		channelKeyMap.put("beIN MAX 7", "beinsport");
		channelKeyMap.put("beIN MAX 8", "beinsport");
		channelKeyMap.put("beIN MAX 9", "beinsport");
		channelKeyMap.put("beIN MAX 10", "beinsport");
		channelKeyMap.put("OM TV", "omtv");
		channelKeyMap.put("OL TV", "oltv");
		channelKeyMap.put("Girondins TV", "girondinstv");
		channelKeyMap.put("France 2", "francetv");
		channelKeyMap.put("France 3","francetv");
		channelKeyMap.put("France 4", "francetv");
		channelKeyMap.put("France Ô", "francetv");
		channelKeyMap.put("France O", "francetv");
		channelKeyMap.put("FOOT+ 1", "footplus");
		channelKeyMap.put("FOOT+ 2", "footplus");
		channelKeyMap.put("FOOT+ 3", "footplus");
		channelKeyMap.put("FOOT+ 4", "footplus");
		channelKeyMap.put("FOOT+ 5", "footplus");
		channelKeyMap.put("FOOT+ 6", "footplus");
		channelKeyMap.put("FOOT+ 7", "footplus");
		channelKeyMap.put("FOOT+ 8", "footplus");
		channelKeyMap.put("MCS", "machainesport");
		channelKeyMap.put("Mont-Blanc TV", "tv8montblanc");
		channelKeyMap.put("Onzéo", "onzeo");
		channelKeyMap.put("W9", "w9");
		channelKeyMap.put("TF1", "tf1");
		channelKeyMap.put("D8", "d8");
		channelKeyMap.put("D17", "d17");
		channelKeyMap.put("NT1", "nt1");
		channelKeyMap.put("TV Toulouse", "tlt");
		channelKeyMap.put("FFF.fr", "fff");

		// Log.d("hashmap", "image" + channelKeyMap.get("beIN Sport 1"));
		Log.d("channels", "loaded");
		
		return channelKeyMap;
	}

	public static String getChannelKey(String channel) {
		return channelKeyMap.get(channel);
		
	}
}
