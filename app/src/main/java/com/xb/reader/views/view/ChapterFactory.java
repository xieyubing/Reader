package com.xb.reader.views.view;

import com.xb.reader.bean.Book;
import com.xb.reader.bean.Chapter;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;

/**
 * Created by asus on 2017/8/9.
 */

public class ChapterFactory {

    public static final String KEYWORD_ZHANG = "章";
    public static final String KEYWORD_JIE = "节";
    public static final String KEYWORD_HUI = "回";

    private MappedByteBuffer mappedByteBuffer;
    private int mappedFileLength;

    private Book book;

    private ArrayList<Chapter> chapters = new ArrayList<>();
    private final ArrayList<Integer> positions = new ArrayList<>();

    public ChapterFactory(){
        book = PageFactory.getInstance().getBook();
        mappedByteBuffer = PageFactory.getInstance().getMappedFile();
        mappedFileLength = PageFactory.getInstance().getFileLength();
    }

}
