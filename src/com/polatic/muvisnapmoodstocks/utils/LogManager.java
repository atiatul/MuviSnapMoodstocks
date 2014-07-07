package com.polatic.muvisnapmoodstocks.utils;

import android.util.Log;

public class LogManager {

	public static void debug(String message){
		Log.d("Moodstock", message);
	}
	
	public static void error(String message){
		Log.e("Moodstock", message);
	}
}
