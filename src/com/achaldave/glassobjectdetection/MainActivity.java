package com.achaldave.glassobjectdetection;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

	static String TAG = MainActivity.class.getName();

	private Camera mCamera;
	private Preview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mCamera = getCameraInstance();
        mPreview = new Preview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.preview);
        preview.addView(mPreview);
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
	}

	@Override
	protected void onPause() {
		if (mCamera != null) mCamera.release();
		super.onPause();
	}

	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
			Log.e(TAG, "Couldn't get camera!");
		}
		return c;
	}

	public class Preview extends SurfaceView implements SurfaceHolder.Callback {
		private SurfaceHolder mHolder;
		private Camera mCamera;

		@SuppressWarnings("deprecation")
		public Preview(Context context, Camera camera) {
			super(context);
			mCamera = camera;

			mHolder = getHolder();
			mHolder.addCallback(this);

			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

	    public void surfaceCreated(SurfaceHolder holder) {
	        try {
	        	updateParams();
	            mCamera.setPreviewDisplay(holder);
	            mCamera.startPreview();
	        } catch (IOException e) {
	            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
	        }
	    }

	    public void surfaceDestroyed(SurfaceHolder holder) {
	        // empty. Take care of releasing the Camera preview in your activity.
	    }

	    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	        if (mHolder.getSurface() == null){
	          return;
	        }

	        // stop preview before making changes
	        try {
	            mCamera.stopPreview();
	        } catch (Exception e){
	          // ignore: tried to stop a non-existent preview
	        }

	        try {
	        	updateParams();
	            mCamera.setPreviewDisplay(mHolder);
	            mCamera.startPreview();

	        } catch (Exception e){
	            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
	        }
	    }
	    
	    private void updateParams() {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(1920,1080);
	        parameters.setPreviewFpsRange(30000, 30000);
            mCamera.setParameters(parameters);
	    }
	}
}