package com.moberella.app;

import com.moberella.app.R;
import com.moberella.util.Constants;
import com.moberella.util.FileUtil;
import com.moberella.util.LangUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 4000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LangUtil.setLocale(this);
        
        setContentView(R.layout.activity_splashscreen);
 
        new Thread(new Runnable() {			
			@Override
			public void run() {
				createApplicationFolderAndCopyDataset();
			}
		}).start();
                
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    
	private void createApplicationFolderAndCopyDataset() {			
		FileUtil.createDirectoryIfNotExists(Constants.DATASET_FOLDER);		
		FileUtil.copyFileIfNotExists(this, Constants.DATASET_DESTINATION_FILE, Constants.DATASET_SOURCE_FILE);
	}
}
