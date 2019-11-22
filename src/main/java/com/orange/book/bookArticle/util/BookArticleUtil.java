package com.orange.book.bookArticle.util;



import com.orange.book.bookArticle.bean.BookArticleBean;
import com.orange.book.bookArticle.service.BookArticleService;
import com.orange.book.httpClient.HttpClientUtils;
import com.orange.book.httpClient.Page;
import com.orange.book.httpClient.PageParserTool;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BookArticleUtil {
    private static final Logger log = LoggerFactory.getLogger(BookArticleUtil.class);

    @Autowired
    private BookArticleService bookArticleService;

    /*
     * 根据URL获取目录
     */
    public boolean getArticle(String url) {
        BookArticleBean bookArticleBean = new BookArticleBean();
        log.info("获取文章" + url);
        Page bookArticle = HttpClientUtils.httpGet(url);
        if (bookArticle == null) {
            log.error("获取小说失败" + url);
            return false;
        }
        Elements error = PageParserTool.select(bookArticle, "div[class=mod-404]");
        if (error != null && error.size() != 0) {
            log.error("访问页面404" + url);
            return false;
        }
        Elements list = PageParserTool.select(bookArticle, "ul[id=chapterlist]");
        if (list == null || list.size() == 0) {
            log.error("本地址非章节目录地址：" + url);
            return false;
        }
        Elements em = PageParserTool.select(bookArticle, "a");
        if (em == null || em.size() == 0) {
            log.error("待访问地址为空" + url);
            return false;
        }

        Elements author = PageParserTool.select(bookArticle, "meta[property=og:novel:author]");
        Elements book_name = PageParserTool.select(bookArticle, "meta[property=og:novel:book_name]");
        Elements description = PageParserTool.select(bookArticle, "meta[property=og:description]");
        Elements category = PageParserTool.select(bookArticle, "meta[property=og:novel:category]");
        Pattern p1 = Pattern.compile("(?<=bookid = \").*?(?=\\\")");
        Matcher matcher1 = p1.matcher(bookArticle.getHtml());
        String bookId = "";
        while (matcher1.find()) {
            bookId = matcher1.group(0);
        }
        bookArticleBean.setBookName(book_name.attr("content"));
        bookArticleBean.setArticleIntroduction(description.attr("content"));
        bookArticleBean.setBookAuthor(author.attr("content"));
        bookArticleBean.setBookId(bookId);
        bookArticleBean.setBookType(category.attr("content"));
        bookArticleBean.setBookUrl(url);

        if (bookArticleService.getBeanById(bookId) != null) {
            log.info("数据库已存储该路径" + url);
        } else {
            log.info("获取到新小说" + url+"/n"+bookArticleBean.toString());
            bookArticleService.addArticle(bookArticleBean);
            return true;
        }

        return false;
    }
    public List<String> getChapter(String url){
        Page bookArticle = HttpClientUtils.httpGet(url);
        List<String> contentList = new ArrayList<String>();
        Elements em = PageParserTool.select(bookArticle, "a");
        Elements chapterlist = PageParserTool.select(bookArticle, "div[class=article_texttitleb]");
        Elements a = chapterlist.select("a");
        Iterator<Element> iterator = em.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String href = "";
            if (element.hasAttr("href") && element.attr("href").contains(".html")
                    && !element.attr("href").contains("http")) {
                href = element.attr("href"); // 取href值
                contentList.add(href);
            }
        }
        return contentList;
    }
}
