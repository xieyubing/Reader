package com.xb.reader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xb.reader.bean.Book;
import com.xb.reader.util.QueryFiles;
import com.xb.reader.util.SPHelper;
import com.xb.reader.util.StatusBarCompat;
import com.xb.reader.util.ThreadPoolManager;
import com.xb.reader.views.activity.BaseActivity;
import com.xb.reader.views.activity.RecordsActivity;
import com.xb.reader.views.adapter.ViewPagerAdapter;
import com.xb.reader.views.fragment.Fragment_Find;
import com.xb.reader.views.fragment.Fragment_List;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.img_search)
    ImageView imgSearch;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private int width = 0;
    private LinearLayout.LayoutParams lp;
    @BindView(R.id.cursor)
    ImageView cursor;
    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.tl_custom)
    Toolbar tlCustom;
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    ProgressDialog progressDialog;
    Fragment_List list;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                progressDialog.dismiss();
                list.upBook((List<Book>) msg.obj);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setLocalNightMode(SPHelper.getInstance().isNightMode() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawerLayout.setFitsSystemWindows(true);
            drawerLayout.setClipToPadding(false);
        }
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, tlCustom, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        ArrayList<Fragment> fragments = new ArrayList<>();
        list = new Fragment_List();
        fragments.add(list);
        fragments.add(new Fragment_Find());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        container.setAdapter(adapter);
        container.setCurrentItem(0);
        lp = (LinearLayout.LayoutParams) cursor.getLayoutParams();
        lp.width = getScreenWidth() / 2;
        cursor.setLayoutParams(lp);
        container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                lp.leftMargin = (int) ((positionOffset + position) * getScreenWidth() / 2);
                cursor.setLayoutParams(lp);
            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setNavigationViewListener();
    }

    @Override
    protected void setStatusBar() {
        StatusBarCompat.setColorForDrawerLayout(this, drawerLayout, ContextCompat.getColor(this, R.color.colorPrimary));
    }
    private void setNavigationViewListener() {
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_1:
                        break;
                    case R.id.item_2:
                        setDayNight();
                        break;
                    case R.id.item_3:
                        progressDialog = ProgressDialog.show(MainActivity.this, "请稍等...", "扫描数据中...", true);
                        ThreadPoolManager.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                //作[\u4e00-\u9fa5].*\n 获取作者
                                List<Book> books = QueryFiles.queryFiles(MainActivity.this);
                                Message message = new Message();
                                message.what = 1;
                                message.obj = books;
                                handler.sendMessage(message);
                            }
                        });
                        break;
                    case R.id.item_4:
                        break;
                    case R.id.item_5:
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });


    }

    @OnClick({R.id.img_1, R.id.img_2, R.id.img_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_1:
                if (container.getCurrentItem() != 0) {
                    container.setCurrentItem(0);
                }
                break;
            case R.id.img_2:
                if (container.getCurrentItem() != 1) {
                    container.setCurrentItem(1);
                }
                break;
            case R.id.img_search:
                Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void setDayNight() {
        if (SPHelper.getInstance().isNightMode()) {
            SPHelper.getInstance().setNightMode(false);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();

        } else {
            SPHelper.getInstance().setNightMode(true);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            recreate();
        }

    }
    //获取屏幕宽度
    public int getScreenWidth() {
        if (width == 0) {
            DisplayMetrics out = new DisplayMetrics();
            getWindow().getWindowManager().getDefaultDisplay().getMetrics(out);
            width = out.widthPixels;
        }
        return width;
    }

}
