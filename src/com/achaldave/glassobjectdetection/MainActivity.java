package com.achaldave.glassobjectdetection;

import zxing.library.DecodeCallback;
import zxing.library.ZXingFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.Result;

public class MainActivity extends FragmentActivity {

	static String TAG = MainActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		final ZXingFragment xf = (ZXingFragment) getSupportFragmentManager()
				.findFragmentById(R.id.scanner);

		xf.setDecodeCallback(new DecodeCallback() {

			@Override
			public void handleBarcode(Result result, Bitmap arg1, float arg2) {
				Log.d(TAG, "Received qr code data:" + result.getText());
				final Toast toast = Toast.makeText(MainActivity.this,
						result.getText(), Toast.LENGTH_SHORT);
				toast.show();
				xf.restartScanningIn(200);
			}

		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}