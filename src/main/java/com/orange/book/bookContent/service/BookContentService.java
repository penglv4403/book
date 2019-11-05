package com.orange.book.bookContent.service;

import com.books.crawler.bookContent.bean.BookContentBean;

import java.util.List;

public  interface BookContentService {

    BookContentBean addBook(BookContentBean book);

    BookContentBean getBeanById(String contentId);

    List<BookContentBean> getBeanList();

    void insertArticleContent(BookContentBean book);
    void updateChines2Num(BookContentBean bookContentBean);

	BookContentBean getBeanByURL(String url);

	int insertContent(BookContentBean record);
}
