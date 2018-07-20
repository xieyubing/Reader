package com.xb.reader.bean;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by asus on 2017/8/3.
 */

public class Book extends DataSupport{
    private long id;
    private String bookname;  //书名
    private String bokauthor;  //作者
    private String imgurl;  //封面
    private Date bookupdate;  //更新时间
    private String upchapter;  //更新章节名
    private String url;  //路径
    private String encoding;  //编码
    private long accessTime = 0;  //最后打开时间
    private int state =1;//状态 1 本地  2在线

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBokauthor() {
        return bokauthor;
    }

    public void setBokauthor(String bokauthor) {
        this.bokauthor = bokauthor;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Date getBookupdate() {
        return bookupdate;
    }

    public void setBookupdate(Date bookupdate) {
        this.bookupdate = bookupdate;
    }

    public String getUpchapter() {
        return upchapter;
    }

    public void setUpchapter(String upchapter) {
        this.upchapter = upchapter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookname='" + bookname + '\'' +
                ", bokauthor='" + bokauthor + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", bookupdate=" + bookupdate +
                ", upchapter='" + upchapter + '\'' +
                ", url='" + url + '\'' +
                ", encoding='" + encoding + '\'' +
                ", accessTime=" + accessTime +
                '}';
    }
}
