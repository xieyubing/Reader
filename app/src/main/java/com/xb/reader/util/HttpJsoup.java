package com.xb.reader.util;

import com.xb.reader.bean.Book;
import com.xb.reader.bean.Chapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/9.
 */

public class HttpJsoup {

    public static String Url = "http://";

    /**
     * 设置URl
     * @param url
     */
    public static void setUrl(String url){
        Url = url;
    }

    /**
     * 搜索解析
     * @param str
     * @return
     */
    public static List<Book> Records(String str){
        List<Book> list = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(Url+"/s_"+str).timeout(30 * 1000).get();
            Elements elements = doc.select("ul.list_content");
            for (Element e:elements){
                Book book = new Book();
                book.setBookname(e.select("li.cc2").select("a").text());
                book.setUrl(Url+e.select("li.cc2").select("a").attr("href"));
                book.setUpchapter(e.select("li.cc3").select("a").text());
                book.setBokauthor(e.select("li.cc4").select("a").text());
                book.setBookupdate(ConvertDemo.StrToDate(e.select("li.cc5").text()));
                book.setState(2);
                list.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 章节目录
     * @return
     */
    public static List<Chapter> ChapterCatalog(String str){
        List<Chapter> chapters = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(str).timeout(30 * 1000).get();
            Elements elements = doc.select("div.chapters").select("div:has(a)");
            for (Element e:elements){
                Chapter chapter = new Chapter();
                chapter.setChapterName(e.select("div.chapter").select("a").attr("title"));
                chapter.setChapterUrl(Url+e.select("div.chapter").select("a").attr("href"));
                chapters.add(chapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chapters;
    }


    public static void main(String[] args) {
       /* List<Chapter> chapters = ChapterCatalog("http://qxs.la/21532/");
        for (Chapter chapter : chapters){
            System.out.println(chapter.toString());
        }*/
        String a  = Chapter("http://qxs.la/157739/12220126/");
        System.out.println(a);
    }

    /**
     * 章节内容
     */
    public static String Chapter(String str){
        String content = null;
        Document doc = null;
        try {
            doc = Jsoup.connect(str).timeout(30 * 1000).get();
            String a  = doc.select("div#content").text();
            String  b  = a.replaceAll("\\s+", "\r\n\u3000\u3000");
            content = b.replaceAll("[\\s\\S]*\\）", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


}
