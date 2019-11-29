package com.orange.book.bookArticle.dao;

import java.util.List;
import java.util.Map;

import com.orange.book.bookArticle.bean.BookArticleBean;
import com.orange.book.bookArticle.bean.BookArticleChapterBean;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface BookArticleMapper {
    int insert(BookArticleBean record);

    int update(BookArticleBean record);

    int insertSelective(BookArticleBean record);

    BookArticleBean getBeanById(String book_id);

    List<BookArticleBean> getBeanList();

    BookArticleBean getBeanByUrl(String url);

    int insertChapterList(List<BookArticleChapterBean> list);

    int insertChapter(BookArticleChapterBean bookArticleChapterBean);

    List<BookArticleChapterBean> getArticleList(BookArticleChapterBean bookArticleChapterBean);
}