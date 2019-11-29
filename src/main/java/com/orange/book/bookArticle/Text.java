package com.orange.book.bookArticle;


import com.orange.book.BookApplication;
import com.orange.book.bookArticle.dao.BookArticleMapper;
import com.orange.book.bookArticle.serviceImpl.BookArticleServiceImpl;
import com.orange.book.bookArticle.spider.BookArticleSpider;
import com.orange.book.bookArticle.util.BookArticleUtil;
import com.orange.book.bookContent.util.BookContentUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;


@SpringBootTest(classes = BookApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class Text {
    private static final Logger log = LoggerFactory.getLogger(Text.class);

    @Autowired
    private BookContentUtil bookContentUtil;
    @Autowired
    private BookArticleUtil bookArticleUtil;
    @Value("${spiderBookUrl}")
    private String spiderBookUrl;
    @Autowired
    private BookArticleServiceImpl bookArticleService;
    @Autowired
    private BookArticleSpider bookArticleSpider;
    @Resource
    private BookArticleMapper bookArticleMapper;

    @Test
    public void text() throws Exception {
        log.info("123");
		/*Map<String, Object> select = bookArticleMapper.select("2398");
		Set<Map.Entry<String, Object>> entries = select.entrySet();
		for (Map.Entry<String, Object> en: entries ) {
			System.out.println(en.getKey() +en.getValue());
		}*/
        bookContentUtil.getContent("http://www.xbiquge.la/15/15409/8570780.html");
        //bookArticleUtil.updateArticle();
        //bookArticleSpider.getArticle("http://www.xbiquge.la/xiaoshuodaquan/");
        //bookContentUtil.getContent("https://www.xbikuge.com/203/203231/59270484.html");
        //	bookArticleUtil.getArticle("https://www.xbikuge.com/0/10/");
		/* BookContentBean beanById = bookContentService.getBeanById("588078511821");
		 MailBean mail = new MailBean();
         mail.setContent(beanById.getContent());
         mail.setSubject(beanById.getTitle());
         sendMailService.sendHtmlMail(mail);*/
        //bookArticleService.getArticle(spiderBookUrl);
    }


}