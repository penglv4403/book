package com.orange.book.bookArticle.spider;

import com.orange.book.bookAccessHistory.serviceImpl.BookAccessHistoryServiceImpl;
import com.orange.book.bookArticle.serviceImpl.BookArticleServiceImpl;
import com.orange.book.bookArticle.util.BookArticleUtil;
import com.orange.book.httpClient.HttpClientUtils;
import com.orange.book.httpClient.Page;
import com.orange.book.httpClient.PageParserTool;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class BookArticleSpider {
    private static final Logger log = LoggerFactory.getLogger(BookArticleSpider.class);

    @Autowired
    private BookAccessHistoryServiceImpl bookAccessHistoryService;
    @Autowired
    private BookArticleUtil bookArticleUtil;

    /**
     * 爬虫列表服务
     */
    public void getArticle(String url) {


        try {
            log.info("待访问地址=====》" + url);

            Page page = HttpClientUtils.httpGet(url);

            Elements em = PageParserTool.select(page, "div[class=novellist]");
            if (em == null || em.size() == 0) {
                log.debug("待访问地址为空" + url);
            }
            Iterator<Element> iterator = em.iterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();

                Elements a = element.select("a");
                Iterator<Element> aList = a.iterator();
                while (aList.hasNext()) {
                    Element next = aList.next();
                    if (next.hasAttr("href")) {
                        bookArticleUtil.getArticle(next.attr("href"));
                    }
                }
            }

        } catch (Exception e) {
            // log.debug(spiderBookUrl + "===>访问失败");
            e.printStackTrace();
        }
    }

    public static void main(String[] a) {
        BookArticleSpider bookArticleSpider = new BookArticleSpider();
        bookArticleSpider.getArticle("http://www.xbiquge.la/xiaoshuodaquan/");
    }

}
