package com.orange.book.schedule;

import com.orange.book.bookArticle.controller.BookArticleController;
import com.orange.book.bookArticle.spider.BookArticleSpider;
import com.orange.book.bookArticle.util.BookArticleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@Configuration
@EnableScheduling
public class BookSchedule {

    private static final Logger log = LoggerFactory.getLogger(BookArticleUtil.class);


    @Autowired
    private BookArticleUtil bookArticleUtil;
    @Autowired
    private BookArticleSpider bookArticleSpider;

    @Scheduled(cron="0 0 0/12 * * ?")
    public void startGetArticle(){
        log.info("定时任务：更新文章线程启动");
        bookArticleSpider.getArticle("http://www.xbiquge.la/xiaoshuodaquan/");
    }

    @Scheduled(cron="0 0 0/3 * * ?")
    public void updateArticle(){
        log.info("定时任务：更新章节线程启动");
        bookArticleUtil.updateArticle("");
    }

}
