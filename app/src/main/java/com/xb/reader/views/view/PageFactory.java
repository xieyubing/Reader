package com.xb.reader.views.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.xb.reader.R;
import com.xb.reader.bean.Book;
import com.xb.reader.util.SPHelper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/8.
 */

public class PageFactory {
    private int screenHeight, screenWidth;
    private int pageHeight,pageWidth;
    private PageView mView;
    private RandomAccessFile randomFile;
    private MappedByteBuffer mappedFile;
    private ByteBuffer buffer;
    private Paint mPaint;
    private Context mContext;
    private int begin;
    private int end;
    private int fontSize ;
    private Canvas mCanvas;
    private int fileLength;
    private static PageFactory instance;
    private Book book;
    private String encoding;
    private int lineNumber;
    private static final int margin = 16;
    private int lineSpace = 16;
    private ArrayList<String> content = new ArrayList<>();

    private boolean isNightMode = SPHelper.getInstance().isNightMode();

    public static PageFactory getInstance(PageView view,Book book){
        if(instance == null){
            synchronized (PageFactory.class){
                if(instance == null){
                    instance = new PageFactory(view);
                    if (book.getState() == 1){
                        instance.openBook(book);
                    }else {
                        instance.openBook1(book);
                    }

                }
            }
        }
        return instance;
    }
    public static PageFactory getInstance(){
        return instance;
    }
    private PageFactory(PageView view){
        DisplayMetrics metrics = new DisplayMetrics();
        mContext = view.getContext();
        mView = view;
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        fontSize = SPHelper.getInstance().getFontSize();
        pageHeight = screenHeight - margin*2 - fontSize;
        pageWidth = screenWidth -margin*2;
        lineNumber = pageHeight/(fontSize+lineSpace);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(fontSize);
        mPaint.setColor(isNightMode ? ContextCompat.getColor(mContext,R.color.nightModeTextColor) :
                ContextCompat.getColor(mContext,R.color.dayModeTextColor));
        Bitmap bitmap = Bitmap.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_8888);
        mView.setBitmap(bitmap);
        mCanvas = new Canvas(bitmap);
    }

    private void openBook(Book book){
        this.book = book;
        encoding = book.getEncoding();
        File file = new File(book.getUrl());
        begin = SPHelper.getInstance().getBookmarkStart(book.getBookname());
        end = SPHelper.getInstance().getBookmarkEnd(book.getBookname());
        fileLength = (int) file.length();
        try {
            randomFile = new RandomAccessFile(file, "r");
            mappedFile = randomFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, (long) fileLength);
            buffer = mappedFile;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("PageFactory","电子书打开失败");
        }
        Log.e(book.getBookname(),encoding);
    }

    private void openBook1(Book book){
        this.book = book;
        encoding = book.getEncoding();
    }

    public void setBook(ByteBuffer buffer,int len){
        if (buffer != null){
            this.buffer = buffer;
            fileLength = len;
        }
    }


    private byte[] readParagraphForward(int end){
        byte b0;
        int before = 0;
        int i = end;
        while(i < fileLength){
            b0 = buffer.get(i);
            if(encoding.equals("UTF-16LE")) {
                if (b0 == 0 && before == 10) {
                    break;
                }
            }else{
                if(b0 == 10){
                    break;
                }
            }
            before = b0;
            i++;
        }

        i = Math.min(fileLength-1,i);
        int nParaSize = i - end + 1 ;

        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] =  buffer.get(end + i);
        }
        return buf;
    }

    private byte[] readParagraphBack(int begin){
        byte b0 ;
        byte before = 1;
        int i = begin -1 ;
        while(i > 0){
            b0 = buffer.get(i);
            if(encoding.equals("UTF-16LE")){
                if(b0 == 10 && before==0 && i != begin-2){
                    i+=2;
                    break;
                }
            }
            else{
                if(b0 == 0x0a && i != begin -1 ){
                    i++;
                    break;
                }
            }
            i--;
            before = b0;
        }
        int nParaSize = begin -i ;
        byte[] buf = new byte[nParaSize];
        for (int j = 0; j < nParaSize; j++) {
            buf[j] = buffer.get(i + j);
        }
        return buf;

    }

    private void pageDown(){
        String strParagraph = "";
        while((content.size()<lineNumber) && (end< fileLength)){
            byte[] byteTemp = readParagraphForward(end);
            end += byteTemp.length;
            try{
                strParagraph = new String(byteTemp, encoding);
            }catch(Exception e){
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n","  ");
            strParagraph = strParagraph.replaceAll("\n", " ");
            while(strParagraph.length() >  0){
                int size = mPaint.breakText(strParagraph,true,pageWidth,null);
                content.add(strParagraph.substring(0,size));
                strParagraph = strParagraph.substring(size);
                if(content.size() >= lineNumber){
                    break;
                }
            }
            if(strParagraph.length()>0){
                try{
                    end -= (strParagraph).getBytes(encoding).length;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    private  void pageUp(){
        String strParagraph = "";
        List<String> tempList = new ArrayList<>();
        while(tempList.size()<lineNumber && begin>0){
            byte[] byteTemp = readParagraphBack(begin);
            begin -= byteTemp.length;
            try{
                strParagraph = new String(byteTemp, encoding);
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n","  ");
            strParagraph = strParagraph.replaceAll("\n","  ");
            while(strParagraph.length() > 0){
                int size = mPaint.breakText(strParagraph,true,pageWidth,null);
                tempList.add(strParagraph.substring(0, size));
                strParagraph = strParagraph.substring(size);
                if(tempList.size() >= lineNumber){
                    break;
                }
            }
            if(strParagraph.length() > 0){
                try{
                    begin+= strParagraph.getBytes(encoding).length;
                }catch (UnsupportedEncodingException u){
                    u.printStackTrace();
                }
            }
        }
    }

    public void nextPage(){
        if(end >= fileLength){
            return;
        }else{
            content.clear();
            begin = end;
            pageDown();
        }
        printPage();
    }
    public void prePage(){
        if(begin <= 0){
            return;
        }else{
            content.clear();
            pageUp();
            end = begin;
            pageDown();
        }
        printPage();
    }

    public void printPage(){
        if(content.size()>0){
            int y = margin;
            if(isNightMode){
                mCanvas.drawColor(ContextCompat.getColor(mContext,R.color.nightModeBackgroundColor));
            }else{
                mCanvas.drawColor(ContextCompat.getColor(mContext,R.color.dayModeBackgroundColor));
            }
            for(String line : content){
                y += fontSize+lineSpace;
                mCanvas.drawText(line,margin,y, mPaint);
            }
            float percent = (float) begin / fileLength *100;
            DecimalFormat format = new DecimalFormat("#0.00");
            String readingProgress = format.format(percent)+"%";
            int length = (int ) mPaint.measureText(readingProgress);
            mCanvas.drawText(readingProgress, (screenWidth - length) / 2, screenHeight - margin, mPaint);
            mView.invalidate();
        }
    }

    public void saveBookmark(){
        if (book.getState() == 1){
            SPHelper.getInstance().setBookmarkEnd(book.getBookname(),end);
            SPHelper.getInstance().setBookmarkStart(book.getBookname(),begin);
        }

    }
    public void setFontSize(int size){
        if(size < 15){
            return;
        }
        fontSize = size;
        mPaint.setTextSize(fontSize);
        pageHeight =  screenHeight - margin*2 - fontSize;
        lineNumber = pageHeight/(fontSize+lineSpace);
        end = begin;
        nextPage();
        SPHelper.getInstance().setFontSize(size);
    }

    public void setNightMode(boolean which){
        isNightMode = which;
        mPaint.setColor(which ? ContextCompat.getColor(mContext,R.color.nightModeTextColor) :
                ContextCompat.getColor(mContext,R.color.dayModeTextColor));
        printPage( );
    }

    public Book getBook(){
        return book;
    }
    public void setEncoding(String encoding){
        this.encoding = encoding;
    }
    public int getFileLength(){
        return fileLength;
    }
    public MappedByteBuffer getMappedFile(){
        return mappedFile;
    }

    public static void close(){
        if(instance != null && instance.book.getState() == 1){
            try{
                instance.randomFile.close();
            }catch (IOException i){
                i.printStackTrace();
            }
            instance = null;
        }else {
            instance = null;
        }
    }
}
