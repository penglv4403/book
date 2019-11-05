package com.orange.book.bookContent.dao;


import com.orange.book.bookContent.bean.BookContentBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookContentMapper {
    int insert(BookContentBean record);

    int insertSelective(BookContentBean record);

    BookContentBean selectById(String contentId);

    List<BookContentBean> getBeanList();

    int insertArticleContent(BookContentBean bookContentBean);
    
    BookContentBean getBeanByURL(String url);
    void updateChines2Num(BookContentBean bookContentBean);
    
    int insertContent(BookContentBean record);
}