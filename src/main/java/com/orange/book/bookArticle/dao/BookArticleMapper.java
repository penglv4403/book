package com.orange.book.bookArticle.dao;

import java.util.List;

import com.orange.book.bookArticle.bean.BookArticleBean;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface BookArticleMapper {
    int insert(BookArticleBean record);

    int insertSelective(BookArticleBean record);

    BookArticleBean getBeanById(String book_id);
    
    List<BookArticleBean> getBeanList();

	BookArticleBean getBeanByUrl(String url);
}