package com.orange.book.bookContent.thread;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.books.crawler.bookContent.util.BookContentUtil;


public class BoolContentThread implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(BoolContentThread.class);

    private BookContentUtil bookContentUtil = null;
    private String url="";
    private String mailFlag="";
    public BoolContentThread(BookContentUtil bookContentUtil, String url, String mailFlag){
        this.bookContentUtil = bookContentUtil;
        this.url = url;
        this.mailFlag=mailFlag;
    }


    @Override
    public void run() {
    	bookContentUtil.getContent(url, mailFlag);
    }
}
