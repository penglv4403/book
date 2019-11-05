package com.orange.book.bookContent.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 章节内容
 */
public class BookContentBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String contentId;

    private String articleId;

    private String chapterId;

    private String indexPage;

    private String nextPage;

    private String title;

    private String contentUrl;

    private Date createDate;

    private Date updateDate;

    private String bak1;

    private String bak2;

    private String bak3;

    private String content;
    private String chapterNum;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId == null ? null : contentId.trim();
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId == null ? null : chapterId.trim();
    }

    public String getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(String indexPage) {
        this.indexPage = indexPage == null ? null : indexPage.trim();
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage == null ? null : nextPage.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl == null ? null : contentUrl.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate == null ? null : createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate == null ? null : updateDate;
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1 == null ? null : bak1.trim();
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2 == null ? null : bak2.trim();
    }

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3 == null ? null : bak3.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(String chapterNum) {
        this.chapterNum = chapterNum;
    }

    @Override
    public String toString() {
        return "BookContentBean{" +
                "contentId='" + contentId + '\'' +
                ", articleId='" + articleId + '\'' +
                ", chapterId='" + chapterId + '\'' +
                ", indexPage='" + indexPage + '\'' +
                ", nextPage='" + nextPage + '\'' +
                ", title='" + title + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", bak1='" + bak1 + '\'' +
                ", bak2='" + bak2 + '\'' +
                ", bak3='" + bak3 + '\'' +
                ", content='" + content + '\'' +
                ", chapterNum='" + chapterNum + '\'' +
                '}';
    }
}