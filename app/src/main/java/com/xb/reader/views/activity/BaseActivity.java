package com.xb.reader.views.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.xb.reader.R;
import com.xb.reader.util.StatusBarCompat;

import butterknife.ButterKnife;

/**
 * Created by asus on 2017/8/15.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setStatusBar();
    }


    protected void setStatusBar() {
        StatusBarCompat.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }


}
