package com.polatic.muvisnapmoodstocks;

import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Scanner;
import com.moodstocks.android.Scanner.SyncListener;
import com.polatic.muvisnapmoodstocks.utils.LogManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements SyncListener{

	private static final String API_KEY = "vxtdvwsgcsbjawgsscvn";
	private static final String API_SECRET = "xe1Di9MqGEpWDyT3";
	
	private boolean compatible = false;
	private Scanner scanner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		compatible = Scanner.isCompatible();
		if (compatible) {
			try {
				scanner = Scanner.get();
				String path = Scanner.pathFromFilesDir(this, "scanner.db");
				scanner.open(path, API_KEY, API_SECRET);
				scanner.setSyncListener(this);
				scanner.sync();
				LogManager.debug("compatible true open scanner");
			} catch (MoodstocksError e) {
				LogManager.error("catch open scanner");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (compatible) {
			try {
				scanner.close();
				scanner.destroy();
				scanner = null;
				LogManager.debug("compatible true open scanner");
			} catch (MoodstocksError e) {
				LogManager.error("catch close scanner");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onSyncStart() {
		LogManager.debug("Sync will start.");
	}

	@Override
	public void onSyncComplete() {
		try {
			LogManager.debug("Sync succeeded ("+scanner.count()+" images)");
		} catch (MoodstocksError e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSyncFailed(MoodstocksError e) {
		LogManager.error("Sync error #"+e.getErrorCode()+": "+e.getMessage());
	}

	@Override
	public void onSyncProgress(int total, int current) {
		int percent = (int)((float)current/(float)total*100);
		LogManager.debug("Sync progressing: "+percent+"%");
	}

	public void onScanButtonClicked(View view){
		if (compatible) {
			startActivity(new Intent(this, ScanActivity.class));
		}
	}
}
