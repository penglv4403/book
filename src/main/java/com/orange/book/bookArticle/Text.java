package com.orange.book.bookArticle;

import java.util.*;


import com.orange.book.BookApplication;
import com.orange.book.bookArticle.dao.BookArticleMapper;
import com.orange.book.bookArticle.serviceImpl.BookArticleServiceImpl;
import com.orange.book.bookArticle.spider.BookArticleSpider;
import com.orange.book.bookArticle.util.BookArticleUtil;
import com.orange.book.bookContent.util.BookContentUtil;
import com.orange.book.document.ReadExcel;
import com.orange.book.httpClient.HttpClientUtils;
import com.orange.book.httpClient.Page;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


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
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertApi(){
        String insertSql = "insert into apilist (api_id,api_name,api_version,api_des) values ('1','2','3','4')";
        jdbcTemplate.execute(insertSql);

    }
    @Autowired
    private ReadExcel readExcel;
    @Test
    public void text() throws Exception {
        log.info("123");
        readExcel.readExcel("D:\\360MoveData\\Users\\hundsun\\Desktop\\03.xlsx");
		/*Map<String, Object> select = bookArticleMapper.select("2398");
		Set<Map.Entry<String, Object>> entries = select.entrySet();
		for (Map.Entry<String, Object> en: entries ) {
			System.out.println(en.getKey() +en.getValue());
		}*/
        //bookContentUtil.getContent("http://www.xbiquge.la/15/15409/8570780.html");
        // bookArticleUtil.updateArticle("");
        //bookArticleSpider.getArticle("http://www.xbiquge.la/xiaoshuodaquan/");
      //  bookContentUtil.getContent("http://www.xbiquge.la/14/14930/6753054.html");
        //	bookArticleUtil.getArticle("https://www.xbikuge.com/0/10/");
		/* BookContentBean beanById = bookContentService.getBeanById("588078511821");
		 MailBean mail = new MailBean();
         mail.setContent(beanById.getContent());
         mail.setSubject(beanById.getTitle());
         sendMailService.sendHtmlMail(mail);*/
        //bookArticleService.getArticle(spiderBookUrl);
    }

    public static void toDo(String str) {
        System.out.println(str);
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
     
        //map.forEach();
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("456");
        list.forEach(Text::toDo);
        // Page page = HttpClientUtils.httpGet("https://www.xiashu.cc/251944/");
        //System.out.println(page.getHtml());
    }

}