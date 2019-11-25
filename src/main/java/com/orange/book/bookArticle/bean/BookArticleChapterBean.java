package com.orange.book.bookArticle.bean;

import java.io.Serializable;

public class BookArticleChapterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String articleSeqNo;
    private String bookId;
    private String chapter;
    private String bookContentUrl;

    public String getArticleSeqNo() {
        return articleSeqNo;
    }

    public void setArticleSeqNo(String articleSeqNo) {
        this.articleSeqNo = articleSeqNo;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getBookContentUrl() {
        return bookContentUrl;
    }

    public void setBookContentUrl(String bookContentUrl) {
        this.bookContentUrl = bookContentUrl;
    }
}
