package com.orange.book.bookAccessHistory.service;

import com.orange.book.bookAccessHistory.bean.BookAccessHistoryBean;

import java.util.List;


public  interface BookAccessHistoryService {

    BookAccessHistoryBean addBean(BookAccessHistoryBean book);
    List<BookAccessHistoryBean> getBeanList();
    BookAccessHistoryBean getBeanById(String id);


}
