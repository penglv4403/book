package com.orange.book.bookAccessHistory.bean;

import java.io.Serializable;

/**
 * 书籍
 */
public class BookAccessHistoryBean implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    private String browseUrl;

    private String browseDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrowseUrl() {
        return browseUrl;
    }

    public void setBrowseUrl(String browseUrl) {
        this.browseUrl = browseUrl;
    }

    public String getBrowseDate() {
        return browseDate;
    }

    public void setBrowseDate(String browseDate) {
        this.browseDate = browseDate;
    }

    @Override
    public String toString() {
        return "BookAccessHistoryBean{" +
                "id='" + id + '\'' +
                ", browseUrl='" + browseUrl + '\'' +
                ", browseDate='" + browseDate + '\'' +
                '}';
    }
}