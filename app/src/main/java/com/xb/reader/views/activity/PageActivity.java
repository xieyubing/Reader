package com.xb.reader.views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.xb.reader.R;
import com.xb.reader.bean.Book;
import com.xb.reader.bean.Chapter;
import com.xb.reader.util.HttpJsoup;
import com.xb.reader.util.ThreadPoolManager;
import com.xb.reader.views.view.PageFactory;
import com.xb.reader.views.view.PageView;

import org.litepal.crud.DataSupport;

import java.nio.ByteBuffer;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus on 2017/8/7.
 */

public class PageActivity extends AppCompatActivity {

    @BindView(R.id.reading_view)
    PageView readingView;
    @BindView(R.id.reading_activity_status_bar)
    View readingActivityStatusBar;
    @BindView(R.id.reading_activity_toolbar)
    Toolbar readingActivityToolbar;
    @BindView(R.id.reading_activity_action_bar)
    LinearLayout readingActivityActionBar;
    @BindView(R.id.reading_activity_root)
    CoordinatorLayout readingActivityRoot;
    private Book book;
    private PageFactory pageFactory;
    private boolean isActionBarHidden = true;
    private ProgressDialog progressDialog;
    private List<Chapter> chapters;
    private String booklr;
    private ByteBuffer buffer = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                progressDialog.dismiss();
                booklr = (String) msg.obj;
                buffer = ByteBuffer.wrap(booklr.getBytes());
                pageFactory.setBook(buffer, booklr.length());
                pageFactory.nextPage();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        ButterKnife.bind(this);
        readingView.setSystemUiVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        long id = intent.getLongExtra("Id", 0);
        final String url = intent.getStringExtra("URl");
        Log.i("Id", id + "");
        if (url == null || url.length() < 1) {
            book = DataSupport.find(Book.class, id);
        } else {
            progressDialog = ProgressDialog.show(PageActivity.this, "请稍等...", "获取数据中...", true);
            book = new Book();
            book.setBookname("小可爱");
            book.setEncoding("utf-8"); //辰东
            book.setState(2);
            ThreadPoolManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    chapters = HttpJsoup.ChapterCatalog(url);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String a = HttpJsoup.Chapter(chapters.get(2).getChapterUrl());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = a;
                    handler.sendMessage(message);
                }
            });
        }
        pageFactory = PageFactory.getInstance(readingView, book);
        pageFactory.nextPage();
        readingView.setOnClickCallback(new PageView.OnClickCallback() {
            @Override
            public void onLeftClick() {
                pageFactory.prePage();
            }

            @Override
            public void onMiddleClick() {
                changeActionState();
            }

            @Override
            public void onRightClick() {
                pageFactory.nextPage();
            }
        });
        readingView.setOnScrollListener(new PageView.OnScrollListener() {
            @Override
            public void onLeftScroll() {
                pageFactory.nextPage();
            }

            @Override
            public void onRightScroll() {
                pageFactory.prePage();
            }
        });
    }

    private void changeActionState() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        pageFactory.saveBookmark();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PageFactory.close();
            finish();
        }
        return false;
    }
}
