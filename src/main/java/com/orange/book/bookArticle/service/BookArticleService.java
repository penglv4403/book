package com.orange.book.bookArticle.service;

import java.util.List;

import com.orange.book.bookArticle.bean.BookArticleBean;


public  interface BookArticleService {

    BookArticleBean addArticle(BookArticleBean book);
    List<BookArticleBean> getBeanList();
    BookArticleBean getBeanById(String id);
	BookArticleBean getBeanByUrl(String url);

}
