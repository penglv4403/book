package com.orange.book.bookArticle.controller;

import com.orange.book.bookArticle.spider.BookArticleSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BookArticleController {
    @Autowired
    private BookArticleSpider bookArticleSpider;
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
}
