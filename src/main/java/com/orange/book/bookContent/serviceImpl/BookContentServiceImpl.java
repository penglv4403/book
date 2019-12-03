package com.orange.book.bookContent.serviceImpl;


import com.orange.book.bookArticle.bean.BookArticleBean;
import com.orange.book.bookArticle.bean.BookArticleChapterBean;
import com.orange.book.bookArticle.service.BookArticleService;
import com.orange.book.bookArticle.util.BookArticleUtil;
import com.orange.book.bookContent.bean.BookContentBean;
import com.orange.book.bookContent.dao.BookContentMapper;
import com.orange.book.bookContent.service.BookContentService;
import com.orange.book.bookContent.util.BookContentUtil;
import com.orange.book.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Base64;
import java.util.List;


@Service
public class BookContentServiceImpl<main, mai> implements BookContentService {

    @Resource
    private BookContentMapper bookContentMapper;
    @Autowired
    private BookContentUtil bookContentUtil;
    @Autowired
    private BookArticleService bookArticleService;
    @Autowired
    private BookArticleUtil bookArticleUtil;

    @Override
    public BookContentBean addBook(BookContentBean book) {
        bookContentMapper.insert(book);
        bookContentMapper.insertContent(book);
        return book;
    }

    @Override
    public BookContentBean getBeanById(String contentId) {
        BookContentBean bookContentBean = bookContentMapper.selectById(contentId);
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
    public int insertContent(BookContentBean record) {

        return bookContentMapper.insertContent(record);
    }

    @Override
    public void insertArticleContent(BookContentBean book) {
        bookContentMapper.insertArticleContent(book);
    }

    @Override
    public void updateChines2Num(BookContentBean book) {
        bookContentMapper.updateChines2Num(book);
    }

    @Override
    public void getBook(String bookName, String filePath) {

        BookArticleBean bookArticleBean = new BookArticleBean();
        bookArticleBean.setBookName(bookName);
        List<BookArticleBean> beanList = bookArticleService.getBeanList(bookArticleBean);

        if (beanList != null && beanList.size() > 0) {
            BookArticleBean bookArticleBean1 = beanList.get(0);
            bookArticleUtil.updateArticle(bookArticleBean1.getBookName());
            BookArticleChapterBean bookArticleChapterBean = new BookArticleChapterBean();
            bookArticleChapterBean.setArticleSeqNo(String.valueOf(bookArticleBean1.getSeqNo()));
            List<BookArticleChapterBean> articleList = bookArticleService.getArticleList(bookArticleChapterBean);
            for (BookArticleChapterBean chapter : articleList) {
                BookContentBean con = bookContentUtil.getContent(chapter.getBookContentUrl());
                /*byte[] byteArr = Base64.getDecoder().decode(con.getBytes());
                String msg2 = new String(byteArr);*/
                FileUtil.appendStringToFile(filePath, con.getTitle());
                FileUtil.appendStringToFile(filePath, con.getContent());
            }
        }


    }

    public static void main(String[] args) {
        FileUtil.appendStringToFile("C:\\\\Users\\\\ME\\\\Desktop\\a.txt", "123");
    }

}
