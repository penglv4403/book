package com.orange.book.bookContent.serviceImpl;


import com.books.crawler.bookContent.bean.BookContentBean;
import com.books.crawler.bookContent.dao.BookContentMapper;
import com.books.crawler.bookContent.service.BookContentService;

import com.books.crawler.mail.service.SendMailService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class BookContentServiceImpl implements BookContentService {

	@Resource
	private BookContentMapper bookContentMapper;

	@Override
	@CachePut(value = "bookContentCatch",key = "#book.getContentId()")
	public BookContentBean addBook(BookContentBean book) {
		bookContentMapper.insert(book);
		bookContentMapper.insertContent(book);
		return book;
	}

	@Override
	@Cacheable(value = "bookContentCatch",key = "#contentId", unless = "#result == null")
	public BookContentBean getBeanById(String contentId) {
		BookContentBean bookContentBean=bookContentMapper.selectById(contentId);
		return bookContentBean;
	}

	@Override
	public BookContentBean getBeanByURL(String url) {
		BookContentBean bookContentBean = null;
		bookContentBean = bookContentMapper.getBeanByURL(url);
		return bookContentBean;
	}

	@Override
	public List<BookContentBean> getBeanList() {
		List<BookContentBean> bookContentBeans = null;
		bookContentBeans = bookContentMapper.getBeanList();
		return bookContentBeans;
	}

	@Override
	public int insertContent(BookContentBean record){
		
		return bookContentMapper.insertContent(record);
	}
	
	@Override
	public void insertArticleContent(BookContentBean book) {
		bookContentMapper.insertArticleContent(book);
	}
	@Override
	public void updateChines2Num(BookContentBean book){
		bookContentMapper.updateChines2Num(book);
	}
}
