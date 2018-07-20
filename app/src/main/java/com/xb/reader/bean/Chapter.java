package com.xb.reader.bean;

/**
 * Created by asus on 2017/8/9.
 */

public class Chapter {

    private String chapterName;  //章节名称
    private int chapterBytePosition, chapterParagraphPosition;  //章节 字节 段落位置
    private String chapterUrl;

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getChapterBytePosition() {
        return chapterBytePosition;
    }

    public void setChapterBytePosition(int chapterBytePosition) {
        this.chapterBytePosition = chapterBytePosition;
    }

    public int getChapterParagraphPosition() {
        return chapterParagraphPosition;
    }

    public void setChapterParagraphPosition(int chapterParagraphPosition) {
        this.chapterParagraphPosition = chapterParagraphPosition;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "chapterName='" + chapterName + '\'' +
                ", chapterBytePosition=" + chapterBytePosition +
                ", chapterParagraphPosition=" + chapterParagraphPosition +
                ", chapterUrl='" + chapterUrl + '\'' +
                '}';
    }
}
