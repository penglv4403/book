package com.orange.book.bookaccesshistory.dao;

import com.orange.book.bookaccesshistory.bean.BookAccessHistoryBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BookAccessHistoryMapper {
    int insert(BookAccessHistoryBean record);

    int insertSelective(BookAccessHistoryBean record);

    BookAccessHistoryBean getBeanById(String bookId);
    
    List<BookAccessHistoryBean> getBeanList();

	BookAccessHistoryBean getBeanByUrl(String url);
}