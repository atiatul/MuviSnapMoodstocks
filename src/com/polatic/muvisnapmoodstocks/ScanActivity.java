package com.polatic.muvisnapmoodstocks;

import com.moodstocks.android.AutoScannerSession.Listener;
import com.moodstocks.android.AutoScannerSession;
import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Result;
import com.moodstocks.android.Scanner;
import com.polatic.muvisnapmoodstocks.utils.LogManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.SurfaceView;

public class ScanActivity extends Activity implements Listener {

	private AutoScannerSession session = null;
	private static final int TYPES = Result.Type.IMAGE | Result.Type.QRCODE
			| Result.Type.EAN13;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		SurfaceView preview = (SurfaceView) findViewById(R.id.preview);

		try {
			session = new AutoScannerSession(this, Scanner.get(), this, preview);
			session.setResultTypes(TYPES);
		} catch (MoodstocksError e) {
			LogManager.error("catch initial session");
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		session.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		session.stop();
	}

	@Override
	public void onCameraOpenFailed(Exception e) {
		LogManager.debug("onCameraOpenFailed ScanActivity : "+e.getMessage());
	}

	@Override
	public void onResult(Result result) {
		LogManager.debug("onResult ScanActivity");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setNeutralButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				session.resume();
			}
		});
		builder.setTitle(result.getType() == Result.Type.IMAGE ? "Image:" : "Barcode:");
		builder.setMessage(result.getValue());
		builder.show();
	}

	@Override
	public void onWarning(String arg0) {
		LogManager.debug("onWarning ScanActivity : "+arg0);
	}
}
