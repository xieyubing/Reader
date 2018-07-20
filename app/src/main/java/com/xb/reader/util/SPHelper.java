package com.xb.reader.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.xb.reader.bean.Book;
import com.xb.reader.views.activity.MyApplication;

/**
 * Created by asus on 2017/8/8.
 */

public class SPHelper {
    private SharedPreferences config = MyApplication.getGlobalContext().getSharedPreferences("config", Context.MODE_PRIVATE);
    private SharedPreferences.Editor configEditor = config.edit();
    private SharedPreferences bookmark = MyApplication.getGlobalContext().getSharedPreferences("bookmark",Context.MODE_PRIVATE);
    private SharedPreferences.Editor bookmarkEditor = bookmark.edit();
    private static SPHelper instance;

    private SPHelper(){

    }

    public static SPHelper getInstance(){
        if(instance == null){
            synchronized(SPHelper.class){
                if(instance == null){
                    instance = new SPHelper();
                }
            }
        }
        return instance;
    }

    public int getFontSize(){
        return config.getInt("font_size",45);
    }
    public void setFontSize(int size){
        configEditor.putInt("font_size",size).apply();
    }
    public void setNightMode(boolean which){
        configEditor.putBoolean("night_mode",which).apply();
    }
    public boolean isNightMode(){
        Log.i("真",config.getBoolean("night_mode",false)+"");
        return config.getBoolean("night_mode",false);
    }


    public void setBookmarkStart(String bookName,int position){
        bookmarkEditor.putInt(bookName+"start",position).apply();
    }
    public int getBookmarkStart(String bookName){
        return bookmark.getInt(bookName+"start",0);
    }
    public void setBookmarkEnd(String bookName,int position){
        bookmarkEditor.putInt(bookName+"end",position).apply();
    }
    public int getBookmarkEnd(String bookName){
        return bookmark.getInt(bookName+"end",0);
    }

    public void clearAllBookMarkData(){
        bookmarkEditor.clear().apply();
    }

    public String getBookEncoding(Book book){
        return config.getString(book.getUrl(),"");
    }
    public void setBookEncoding(Book book,String encoding){
        configEditor.putString(book.getUrl(),encoding).apply();
    }
    public void deleteBookMark(String bookName){
        bookmarkEditor.remove(bookName).apply();
    }

}
