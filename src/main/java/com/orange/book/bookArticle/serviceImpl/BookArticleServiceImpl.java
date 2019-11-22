package com.orange.book.bookArticle.serviceImpl;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import javax.annotation.Resource;


import com.orange.book.bookArticle.bean.BookArticleBean;
import com.orange.book.bookArticle.dao.BookArticleMapper;
import com.orange.book.bookArticle.service.BookArticleService;
import com.orange.book.bookArticle.util.BookArticleUtil;
import com.orange.book.httpClient.HttpClientUtils;
import com.orange.book.httpClient.Page;
import com.orange.book.httpClient.PageParserTool;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class BookArticleServiceImpl implements BookArticleService {
	private static final Logger log = LoggerFactory.getLogger(BookArticleServiceImpl.class);
	@Value("${spiderBookUrl}")
	private String spiderBookUrl;
	@Autowired
	private BookArticleUtil bookArticleUtil;


	@Resource
	private BookArticleMapper bookArticleMapper;

	public BookArticleServiceImpl() {
	}

	@Override
	public BookArticleBean addArticle(BookArticleBean bookArticleBean) {
		bookArticleMapper.insert(bookArticleBean);
		return bookArticleBean;
	}

	@Override
	public List<BookArticleBean> getBeanList() {
		List<BookArticleBean> bookArticleBeanList = null;
		bookArticleBeanList = bookArticleMapper.getBeanList();
		return bookArticleBeanList;
	}
	@Override
	public BookArticleBean getBeanByUrl(String url) {
		BookArticleBean bookArticleBeanList = null;
		bookArticleBeanList = bookArticleMapper.getBeanByUrl(url);
		return bookArticleBeanList;
	}
	
	@Override
	public BookArticleBean getBeanById(String id) {
		BookArticleBean bookArticleBean = null;
		bookArticleBean = bookArticleMapper.getBeanById(id);
		return bookArticleBean;
	}

	List<String> queryList = new ArrayList<String>();
	/**
	 * 爬虫列表服务
	 */
	public void getArticle(String url){
		//spiderBookUrl =  "";

		try {
			log.info("带访问地址=====》" + url);

			Page page = HttpClientUtils.httpGet(url);
			Elements em = PageParserTool.select(page, "a");
			if (em == null || em.size() == 0) {
				log.debug("待访问地址为空" + url);
			}




				Iterator<Element> iterator = em.iterator();
				while (iterator.hasNext()) {
					Element element = iterator.next();
					String href = "";
					if (element.hasAttr("href")) {
						href = element.attr("href"); // 取href值
						if (!href.startsWith("/") || href.equals("/")) {
							continue;
						}
						if (queryList.contains(spiderBookUrl + href)) {
							continue;
						} else {
							boolean article = getArticles(spiderBookUrl + href);
							if (article) {
								queryList.add(spiderBookUrl + href);
							} else {
								queryList.add(spiderBookUrl + href);
								getArticle(spiderBookUrl + href);
							}
						}

					}
				}


		} catch (Exception e) {
			log.debug(spiderBookUrl + "===>访问失败");
			e.printStackTrace();

		}
	}
	public boolean getArticles(String url) {
		Page page1 = HttpClientUtils.httpGet(url);
		Elements list = PageParserTool.select(page1, "a");

		if (list != null && list.size() != 0) {

				return bookArticleUtil.getArticle(url);
		}
		return false;
	}
}
