package com.orange.book.bookArticle.service;

import java.util.List;

import com.orange.book.bookArticle.bean.BookArticleBean;
import com.orange.book.bookArticle.bean.BookArticleChapterBean;


public  interface BookArticleService {

    BookArticleBean addArticle(BookArticleBean book);
    List<BookArticleBean> getBeanList();
    BookArticleBean getBeanById(String bookId);
	BookArticleBean getBeanByUrl(String url);
    int insertChapterList(List<BookArticleChapterBean> list);
    int insertChapter(BookArticleChapterBean bookArticleChapterBean);
    List<BookArticleChapterBean> getArticleList(BookArticleChapterBean bookArticleChapterBean);
    int update(BookArticleBean record);

}
