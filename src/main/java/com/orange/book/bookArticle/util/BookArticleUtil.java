package com.orange.book.bookArticle.util;



import com.orange.book.bookArticle.bean.BookArticleBean;
import com.orange.book.bookArticle.bean.BookArticleChapterBean;
import com.orange.book.bookArticle.service.BookArticleService;
import com.orange.book.httpClient.HttpClientUtils;
import com.orange.book.httpClient.Page;
import com.orange.book.httpClient.PageParserTool;
import com.orange.book.utils.DateTimeUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.SQLSyntaxErrorException;
import java.text.ParseException;
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
        log.info("获取地址" + url);
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
        Elements list = PageParserTool.select(bookArticle, "div[id=list]");
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
        Elements bookName = PageParserTool.select(bookArticle, "meta[property=og:novel:book_name]");
        Elements description = PageParserTool.select(bookArticle, "meta[property=og:description]");
        Elements category = PageParserTool.select(bookArticle, "meta[property=og:novel:category]");
        Pattern p1 = Pattern.compile("(?<=bookid = \").*?(?=\\\")");
        Matcher matcher1 = p1.matcher(bookArticle.getHtml());
        String bookId = "";
        while (matcher1.find()) {
            bookId = matcher1.group(0);
        }
        bookArticleBean.setBookName(bookName.attr("content"));
        bookArticleBean.setArticleIntroduction(description.attr("content"));
        bookArticleBean.setBookAuthor(author.attr("content"));
        bookArticleBean.setBookId(bookId);
        bookArticleBean.setBookType(category.attr("content"));
        bookArticleBean.setBookUrl(url);
        bookArticleBean.setCreateDate(DateTimeUtil.getDateFormat("yyyyMMdd"));
        bookArticleBean.setUpdateDate(DateTimeUtil.getDateFormat("yyyyMMdd"));
        bookArticleBean.setUpdateFlag("01");
        if (bookArticleService.getBeanById(bookId) != null) {
            log.info("数据库已存储该路径" + url);
        } else {
            log.info("获取到新小说" + url+"\n"+bookArticleBean.toString());
            bookArticleService.addArticle(bookArticleBean);
            getChapter(bookArticle,bookArticleBean);
            return true;
        }

        return false;
    }
    public void getChapter(Page bookArticle,BookArticleBean bookArticleBean) {
        Elements chapterlist = PageParserTool.select(bookArticle, "div[id=list]");
        Elements a = chapterlist.select("a");
        Iterator<Element> iterator = a.iterator();
        List<BookArticleChapterBean> bookArticleChapterBeans = new ArrayList<BookArticleChapterBean>();
        int i = 0;
        BookArticleBean beanById = bookArticleService.getBeanById(bookArticleBean.getBookId());
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String href = "";
            BookArticleChapterBean bookArticleChapterBean = new BookArticleChapterBean();
            if (element.hasAttr("href") && element.attr("href").contains(".html")
                    && !element.attr("href").contains("http")) {
                href = element.attr("href"); // 取href值
                if(!href.startsWith("http")){
                    String bookUrl = bookArticleBean.getBookUrl();
                    if(bookUrl.contains("/")){
                        String http = bookUrl.substring(0,bookUrl.indexOf("/")-1); //http
                        bookUrl = bookUrl.replace(http+"://","");
                        if(bookUrl.contains("/")){
                            String newUrl = bookUrl.substring(0, bookUrl.indexOf("/") - 1);
                            href = http + "://" + newUrl + href;
                        }
                    }

                }
                bookArticleChapterBean.setBookId(bookArticleBean.getBookId());
                bookArticleChapterBean.setArticleSeqNo(String.valueOf(beanById.getSeqNo()));
                bookArticleChapterBean.setBookContentUrl(href);
                bookArticleChapterBean.setChapter(element.text());
                // bookArticleService.insertChapter(bookArticleChapterBean);

                i = i + 1;
                bookArticleChapterBeans.add(bookArticleChapterBean);

                if (i == 100) {
                    bookArticleService.insertChapterList(bookArticleChapterBeans);
                    bookArticleChapterBeans.clear();
                    i = 0;
                }
            }
        }
        try{
            bookArticleService.insertChapterList(bookArticleChapterBeans);
        }catch (Exception e) {
            if(e instanceof SQLSyntaxErrorException){
                for (BookArticleChapterBean bean:bookArticleChapterBeans) {
                    try {
                        bookArticleService.insertChapter(bean);
                    } catch (Exception e1) {
                        if (e1 instanceof SQLSyntaxErrorException) {
                            log.error(bean.toString());
                        }
                    }
                }
            }
            e.printStackTrace();
        }

    }

    public void updateArticleChapter(String url){
        Page bookArticle = HttpClientUtils.httpGet(url);
        Elements chapterlist = PageParserTool.select(bookArticle, "div[id=list]");
        Elements a = chapterlist.select("a");
        Iterator<Element> iterator = a.iterator();
        List<BookArticleChapterBean> bookArticleChapterBeans = new ArrayList<BookArticleChapterBean>();
        int i = 0;
        String bookId = "";
        Pattern p1 = Pattern.compile("(?<=bookid = \").*?(?=\\\")");
        Matcher matcher1 = p1.matcher(bookArticle.getHtml());
        while (matcher1.find()) {
            bookId = matcher1.group(0);
        }
        BookArticleBean beanById = bookArticleService.getBeanById(bookId);
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String href = "";
            BookArticleChapterBean bookArticleChapterBean = new BookArticleChapterBean();

            if (element.hasAttr("href") && element.attr("href").contains(".html")
                    && !element.attr("href").contains("http")) {
                href = element.attr("href"); // 取href值
                if(!href.startsWith("http")){
                    String bookUrl = beanById.getBookUrl();
                    if(bookUrl.contains("/")){
                        String http = bookUrl.substring(0,bookUrl.indexOf("/")-1); //http
                        bookUrl = bookUrl.replace(http+"://","");
                        if(bookUrl.contains("/")){
                            String newUrl = bookUrl.substring(0, bookUrl.indexOf("/"));
                            href = http + "://" + newUrl + href;
                        }
                    }

                }
                bookArticleChapterBean.setBookId(bookId);
                bookArticleChapterBean.setArticleSeqNo(String.valueOf(beanById.getSeqNo()));
                bookArticleChapterBean.setBookContentUrl(href);
                bookArticleChapterBean.setChapter(element.text());
                // bookArticleService.insertChapter(bookArticleChapterBean);
                bookArticleChapterBeans.add(bookArticleChapterBean);
            }
        }
        BookArticleChapterBean bookArticleChapterBean = new BookArticleChapterBean();
        bookArticleChapterBean.setBookId(bookId);
        bookArticleChapterBean.setArticleSeqNo(String.valueOf(beanById.getSeqNo()));
        List<BookArticleChapterBean> articleList = bookArticleService.getArticleList(bookArticleChapterBean);
        log.info("本地存储总数:"+articleList.size() +"<===>网络数据总数:"+bookArticleChapterBeans.size());
        if (articleList!= null && bookArticleChapterBeans !=null && articleList.size() != bookArticleChapterBeans.size()){
            articleList.clear();
            BookArticleBean bookArticleBean = new BookArticleBean();
            bookArticleBean.setSeqNo(beanById.getSeqNo());
            bookArticleBean.setUpdateFlag("02");//更新中
            bookArticleBean.setUpdateDate(DateTimeUtil.getDateFormat("yyyyMMdd"));
            bookArticleService.update(bookArticleBean);
            BookArticleChapterBean bookArticleChapter = new BookArticleChapterBean();
            for (BookArticleChapterBean book:bookArticleChapterBeans) {
                bookArticleChapter.setBookId(book.getBookId());
                bookArticleChapter.setArticleSeqNo(book.getArticleSeqNo());
                bookArticleChapter.setBookContentUrl(book.getBookContentUrl());
                bookArticleChapter.setChapter(book.getChapter());
                List<BookArticleChapterBean> articleList1 = bookArticleService.getArticleList(bookArticleChapter);
                if (articleList1.isEmpty()){
                    bookArticleService.insertChapter(book);
                }
            }
        }
    }

    public static void main(String[] args) {
        String bookUrl = "http://www.xbiquge.la/14/14634/10645858.html";
        String href = bookUrl.substring(0,bookUrl.indexOf("/")-1);
        System.out.println(href);
    }
    /**
     * 超过10期限断更状态 超过一个月已完结状态
     */
    @Async("asyncPromiseExecutor")
    public void updateArticle() {
        List<BookArticleBean> beanList = bookArticleService.getBeanList(new BookArticleBean());
        for (BookArticleBean book:beanList) {
            //判断更新状态01：初始化，02:更新中 ，03：断更，04：已完结
            String updateFlag = book.getUpdateFlag();
            String newDate = DateTimeUtil.getDateFormat("yyyyMMdd");
            String updateDate = book.getUpdateDate();
            int diffDate = 0;
            try {
                diffDate = DateTimeUtil.diffDate(newDate, updateDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            BookArticleBean bookArticleBean = new BookArticleBean();
            bookArticleBean.setSeqNo(book.getSeqNo());

            if("01".equals(updateFlag) || "02".equals(updateFlag)){
                if(diffDate > 10 &&  diffDate < 31){
                    updateFlag = "03";
                    bookArticleBean.setUpdateFlag("03");//更新中
                    bookArticleService.update(bookArticleBean);
                }else if(diffDate >30){
                    updateFlag = "04";
                    bookArticleBean.setUpdateFlag("04");//更新中
                    bookArticleService.update(bookArticleBean);
                }else{
                    updateArticleChapter(book.getBookUrl());
                    continue;
                }
            }
            if("03".equals(updateFlag)){
                log.info("id:"+book.getBookId()+"名字："+book.getBookName()+"断更");
                if(diffDate == 10 || diffDate == 20 || diffDate==30){
                    updateArticleChapter(book.getBookUrl());
                    continue;
                }
            }

            if("04".equals(updateFlag)){
                log.info("id:"+book.getBookId()+"名字："+book.getBookName()+"已完结");
                continue;
            }
        }
    }
}
