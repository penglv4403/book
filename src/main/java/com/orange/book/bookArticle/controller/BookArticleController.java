package com.orange.book.bookArticle.controller;

import com.orange.book.bookArticle.spider.BookArticleSpider;
import com.orange.book.bookArticle.util.BookArticleUtil;
import com.orange.book.schedule.BookSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

@Controller
public class BookArticleController {
    @Autowired
    private BookArticleSpider bookArticleSpider;
    @Autowired
    private BookArticleUtil bookArticleUtil;

    @RequestMapping("/startArticle")
    @ResponseBody
    public String startArticle(){
        StartActicle startActicle = new StartActicle();
        Thread th = new Thread(startActicle);
        th.start();
        return "1";
    }

    public class StartActicle implements Runnable{
        @Override
        public void run() {
            bookArticleSpider.getArticle("http://www.xbiquge.la/xiaoshuodaquan/");
        }
    }
    @RequestMapping("/updateArticle")
    @ResponseBody
    public String updateArticle(){
        UpdateArticle updateArticle = new UpdateArticle();
        Thread th = new Thread(updateArticle);
        th.start();
        return "2";
    }


    public class UpdateArticle implements Runnable{
        @Override
        public void run() {
            try {
                bookArticleUtil.updateArticle();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
