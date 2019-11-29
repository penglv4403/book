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
        bookArticleSpider.getArticle("http://www.xbiquge.la/xiaoshuodaquan/");
        return "1";
    }



    @RequestMapping("/updateArticle")
    @ResponseBody
    public String updateArticle() throws ParseException {
        bookArticleUtil.updateArticle();
        return "2";
    }

}
