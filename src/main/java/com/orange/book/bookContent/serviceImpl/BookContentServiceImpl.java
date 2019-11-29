package com.orange.book.bookContent.serviceImpl;




import com.orange.book.bookContent.bean.BookContentBean;
import com.orange.book.bookContent.dao.BookContentMapper;
import com.orange.book.bookContent.service.BookContentService;
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
	public BookContentBean addBook(BookContentBean book) {
		bookContentMapper.insert(book);
		bookContentMapper.insertContent(book);
		return book;
	}

	@Override
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
