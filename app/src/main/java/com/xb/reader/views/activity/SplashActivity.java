package com.xb.reader.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.xb.reader.MainActivity;
import com.xb.reader.R;

import org.litepal.tablemanager.Connector;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by asus on 2017/8/4.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Connector.getDatabase();
        File sdcardDir = Environment.getExternalStorageDirectory();
        String path=sdcardDir.getPath()+"/PleaseSeeBook";
        File path1 = new File(path);
        if (!path1.exists()) {
            path1.mkdirs();
        }
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(task,3000);
    }
}
