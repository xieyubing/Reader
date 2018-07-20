package com.xb.reader.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.xb.reader.bean.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 2017/8/13.
 */

public class QueryFiles {

    public static  List<Book> queryFiles(Context context) {
        List<Book> books = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE
        };
        String[] columns = new String[] {MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.DATA };
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/file"),
                columns,
                MediaStore.Files.FileColumns.DATA + " like ?",
                new String[]{"%.txt"},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idindex = cursor
                        .getColumnIndex(MediaStore.Files.FileColumns._ID);
                int dataindex = cursor
                        .getColumnIndex(MediaStore.Files.FileColumns.DATA);
                int sizeindex = cursor
                        .getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                int date_modified = cursor.
                        getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED);
                do {
                    Book book = new Book();
                    String id = cursor.getString(idindex);
                    Log.e("test",id);
                    String path = cursor.getString(dataindex);
                    book.setUrl(path);
                    book.setEncoding(UrlEncoder.getEncoding(path));
                    Log.e("test",path);
                    String size = cursor.getString(sizeindex);
                    Log.e("test",size);
                    String a=path.substring(path.lastIndexOf("/")+1);
                    String name = a.substring(0,a.lastIndexOf("."));
                    book.setBookname(name);
                    Log.e("test",name);
                    int i = Integer.parseInt(cursor.getString(date_modified));
                    book.setBookupdate(new Date(i * 1000L));
                    book.setState(1);
                    book.save();
                    books.add(book);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return books;
    }


}
