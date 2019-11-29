package com.orange.book.bookArticle.bean;

import java.io.Serializable;

/**
 * 书籍
 */
public class BookArticleBean implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private int seqNo;
	private String bookId;

    private String bookType;

    private String bookUrl;

    private String bookName;

    private String bookAuthor;

    private String articleIntroduction;
    
    private String mailFlag;
    private String spiderFlag;

    private String createDate;
    private String updateDate;
    private String updateFlag;
    private String bak;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getBak() {
        return bak;
    }

    public void setBak(String bak) {
        this.bak = bak;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId == null ? null : bookId.trim();
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType == null ? null : bookType.trim();
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl == null ? null : bookUrl.trim();
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor == null ? null : bookAuthor.trim();
    }

    public String getArticleIntroduction() {
        return articleIntroduction;
    }

    public void setArticleIntroduction(String articleIntroduction) {
        this.articleIntroduction = articleIntroduction == null ? null : articleIntroduction.trim();
    }

	public String getMailFlag() {
		return mailFlag;
	}

	public void setMailFlag(String mailFlag) {
		this.mailFlag = mailFlag;
	}

	public String getSpiderFlag() {
		return spiderFlag;
	}

	public void setSpiderFlag(String spiderFlag) {
		this.spiderFlag = spiderFlag;
	}

	@Override
	public String toString() {
		return "BookArticleBean [bookId=" + bookId + ", bookType=" + bookType + ", bookUrl=" + bookUrl + ", bookName="
				+ bookName + ", bookAuthor=" + bookAuthor + ", articleIntroduction=" + articleIntroduction
				+ ", mailFlag=" + mailFlag + ", spiderFlag=" + spiderFlag + "]";
	}
    
    
}