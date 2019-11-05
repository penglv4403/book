package com.orange.book.bookArticle.serviceImpl;


import java.util.List;


import javax.annotation.Resource;


import com.orange.book.bookArticle.bean.BookArticleBean;
import com.orange.book.bookArticle.dao.BookArticleMapper;
import com.orange.book.bookArticle.service.BookArticleService;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;



@Service
public class BookArticleServiceImpl implements BookArticleService {
	private static final Logger log = LoggerFactory.getLogger(BookArticleServiceImpl.class);

	@Resource
	private BookArticleMapper bookArticleMapper;

	@Override
	@CachePut(value="bookArticleCatch",key="#bookArticleBean.getBookId()")
	public BookArticleBean addArticle(BookArticleBean bookArticleBean) {
		bookArticleMapper.insert(bookArticleBean);
		return bookArticleBean;
	}

	@Override
	public List<BookArticleBean> getBeanList() {
		List<BookArticleBean> bookArticleBeanList = null;
		bookArticleBeanList = bookArticleMapper.getBeanList();
		return bookArticleBeanList;
	}
	@Override
	public BookArticleBean getBeanByUrl(String url) {
		BookArticleBean bookArticleBeanList = null;
		bookArticleBeanList = bookArticleMapper.getBeanByUrl(url);
		return bookArticleBeanList;
	}
	
	@Override
	@Cacheable(value="bookArticleCatch",key="#id",unless="#result == null")
	public BookArticleBean getBeanById(String id) {
		BookArticleBean bookArticleBean = null;
		bookArticleBean = bookArticleMapper.getBeanById(id);
		return bookArticleBean;
	}


}
