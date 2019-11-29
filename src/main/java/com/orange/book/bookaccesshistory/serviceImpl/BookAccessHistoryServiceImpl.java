package com.orange.book.bookaccesshistory.serviceImpl;


import com.orange.book.bookaccesshistory.bean.BookAccessHistoryBean;
import com.orange.book.bookaccesshistory.service.BookAccessHistoryService;
import com.orange.book.bookaccesshistory.dao.BookAccessHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;


@Service
public class BookAccessHistoryServiceImpl implements BookAccessHistoryService {
	private static final Logger log = LoggerFactory.getLogger(BookAccessHistoryServiceImpl.class);

	@Resource
	private BookAccessHistoryMapper bookAccessHistoryMapper;


	@Override
	public BookAccessHistoryBean addBean(BookAccessHistoryBean BookAccessHistoryBean) {
		bookAccessHistoryMapper.insert(BookAccessHistoryBean);
		return BookAccessHistoryBean;
	}

	@Override
	public List<BookAccessHistoryBean> getBeanList() {
		List<BookAccessHistoryBean> BookAccessHistoryBeanList = null;
		BookAccessHistoryBeanList = bookAccessHistoryMapper.getBeanList();
		return BookAccessHistoryBeanList;
	}

	
	@Override
	public BookAccessHistoryBean getBeanById(String id) {
		BookAccessHistoryBean BookAccessHistoryBean = null;
		BookAccessHistoryBean = bookAccessHistoryMapper.getBeanById(id);
		return BookAccessHistoryBean;
	}

}
