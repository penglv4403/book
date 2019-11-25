package com.orange.book.bookAccessHistory.dao;

import com.orange.book.bookAccessHistory.bean.BookAccessHistoryBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BookAccessHistoryMapper {
    int insert(BookAccessHistoryBean record);

    int insertSelective(BookAccessHistoryBean record);

    BookAccessHistoryBean getBeanById(String book_id);
    
    List<BookAccessHistoryBean> getBeanList();

	BookAccessHistoryBean getBeanByUrl(String url);
}