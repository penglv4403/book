package com.orange.book.bookArticle.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.stereotype.Component;

@Component
public class ArticleConfig {
    @ApolloConfigChangeListener
    public  void articleChange(ConfigChangeEvent configEvent){
        if(configEvent.isChanged("articleChange")){

        }
    }
}
