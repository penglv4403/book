package com.orange.book.bookAccessHistory.serviceImpl;


import com.orange.book.bookAccessHistory.bean.BookAccessHistoryBean;
import com.orange.book.bookAccessHistory.service.BookAccessHistoryService;
import com.orange.book.bookAccessHistory.dao.BookAccessHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;


@Service
public class BookAccessHistoryServiceImpl implements BookAccessHistoryService {
	private static final Logger log = LoggerFactory.getLogger(BookAccessHistoryServiceImpl.class);

	@Resource
	private BookAccessHistoryMapper BookAccessHistoryMapper;


	@Override
	public BookAccessHistoryBean addBean(BookAccessHistoryBean BookAccessHistoryBean) {
		BookAccessHistoryMapper.insert(BookAccessHistoryBean);
		return BookAccessHistoryBean;
	}

	@Override
	public List<BookAccessHistoryBean> getBeanList() {
		List<BookAccessHistoryBean> BookAccessHistoryBeanList = null;
		BookAccessHistoryBeanList = BookAccessHistoryMapper.getBeanList();
		return BookAccessHistoryBeanList;
	}

	
	@Override
	public BookAccessHistoryBean getBeanById(String id) {
		BookAccessHistoryBean BookAccessHistoryBean = null;
		BookAccessHistoryBean = BookAccessHistoryMapper.getBeanById(id);
		return BookAccessHistoryBean;
	}

}
