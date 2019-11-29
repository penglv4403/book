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
        StartActicle startActicle = new StartActicle();
        Thread th = new Thread(startActicle);
        th.start();
    }

    @Scheduled(cron="0 0 0/6 * * ?")
    public void updateArticle(){
        log.info("定时任务：更新章节线程启动");
        UpdateArticle updateArticle = new UpdateArticle();
        Thread th = new Thread(updateArticle);
        th.start();
    }
    public class StartActicle implements Runnable{

        @Override
        public void run() {
            bookArticleSpider.getArticle("http://www.xbiquge.la/xiaoshuodaquan/");
        }
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
