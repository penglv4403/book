package com.orange.book.bookArticle.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.orange.book.bookArticle.spider.BookArticleSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleConfig {
    @Autowired
    private BookArticleSpider bookArticleSpider;

    @ApolloConfigChangeListener
    public void articleChange(ConfigChangeEvent configEvent) {
        if (configEvent.isChanged("articleChange")) {
            bookArticleSpider.getArticle("http://www.xbiquge.la/xiaoshuodaquan/");
        }
    }
}
