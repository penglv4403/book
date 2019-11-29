package com.orange.book.bookaccesshistory.service;

import com.orange.book.bookaccesshistory.bean.BookAccessHistoryBean;

import java.util.List;


public  interface BookAccessHistoryService {

    BookAccessHistoryBean addBean(BookAccessHistoryBean book);
    List<BookAccessHistoryBean> getBeanList();
    BookAccessHistoryBean getBeanById(String id);


}
