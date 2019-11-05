package com.orange.book.bookContent.util;

import com.books.crawler.bookContent.bean.BookContentBean;
import com.books.crawler.bookContent.service.BookContentService;
import com.books.crawler.httpClient.HttpClientUtils;
import com.books.crawler.httpClient.Page;
import com.books.crawler.httpClient.PageParserTool;
import com.books.crawler.mail.bean.MailBean;
import com.books.crawler.mail.service.SendMailService;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BookContentUtil {
	private static final Logger log = LoggerFactory.getLogger(BookContentUtil.class);

	@Autowired
	private BookContentService bookContentService;
	@Autowired
	private SendMailService sendMailService;

	/**
	 * 根据小说章节地址获取小说信息
	 */

	public void getContent(String url, String mailFlag) {
		try {
			// TODO Auto-generated method stub
			BookContentBean content = new BookContentBean();
			Page pageCenter = HttpClientUtils.httpGet(url);
			Elements em = PageParserTool.select(pageCenter, "div[id=content]");
			if (em == null || em.size() == 0) {
				log.error(url + "访问URL获取文章内容失败");
				return;
			}
			Elements title = PageParserTool.select(pageCenter, "h1");
			Pattern p1 = Pattern.compile("(?<=booktitle = \").*?(?=\\\")");
			Pattern p2 = Pattern.compile("(?<=chapter_id = \").*?(?=\\\")");
			Pattern p3 = Pattern.compile("(?<=article_id = \").*?(?=\\\")");
			Pattern p4 = Pattern.compile("(?<=index_page = \").*?(?=\\\")");
			Pattern p5 = Pattern.compile("(?<=next_page = \").*?(?=\\\")");
			Matcher matcher1 = p1.matcher(pageCenter.getHtml());
			Matcher matcher2 = p2.matcher(pageCenter.getHtml());
			Matcher matcher3 = p3.matcher(pageCenter.getHtml());
			Matcher matcher4 = p4.matcher(pageCenter.getHtml());
			Matcher matcher5 = p5.matcher(pageCenter.getHtml());
			String booktitle = "";
			while (matcher1.find()) {
				booktitle = matcher1.group(0);
			}
			String chapter_id = "";
			while (matcher2.find()) {
				chapter_id = matcher2.group(0);
			}
			String article_id = "";
			while (matcher3.find()) {
				article_id = matcher3.group(0);
			}
			String index_page = "";
			while (matcher4.find()) {
				index_page = matcher4.group(0);
			}
			String next_page = "";
			while (matcher5.find()) {
				next_page = matcher5.group(0);
			}
			String con = Base64.getEncoder().encodeToString(em.html().getBytes());
			content.setContentUrl(url); // 文章url
			content.setContent(con);// 文章内容
			content.setArticleId(article_id);// 书id
			content.setChapterId(chapter_id);// 章节id
			content.setTitle(title.text());// 标题
			content.setIndexPage(index_page);
			content.setNextPage(next_page);// 下一章
			content.setContentId(article_id + chapter_id);
			String titleVal = title.text();
			String num = "";
			if (titleVal.contains("第") && titleVal.contains("章")) {
				num = String.valueOf(
						NumberToCN.zn2num(titleVal.substring(titleVal.indexOf("第") + 1, titleVal.indexOf("章"))));
			} else if (titleVal.contains("第") && titleVal.contains(" ")) {
				num = String.valueOf(
						NumberToCN.zn2num(titleVal.substring(titleVal.indexOf("第") + 1, titleVal.indexOf(" "))));

			}
			content.setChapterNum(num);
			BookContentBean beanById = bookContentService.getBeanById(article_id + chapter_id);
			if (beanById == null) {
				log.info(url + "获取到新章节===>" + content.toString());
				bookContentService.addBook(content);
				if (mailFlag.equals("0")) { // 是否发送邮件标志（1：发送，0：不发送）
					MailBean mail = new MailBean();
					mail.setContent(em.html());
					mail.setSubject(content.getTitle());
					sendMailService.sendHtmlMail(mail);
				}

			}
		} catch (Exception e) {
			log.error("请求错误" + url, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 根据小说章节地址获取小说信息
	 */

	public void getContent(String url) {
		// TODO Auto-generated method stub
		try {
			BookContentBean content = new BookContentBean();
			Page pageCenter = HttpClientUtils.httpGet(url);
			Elements em = PageParserTool.select(pageCenter, "div[id=content]");
			if (em == null || em.size() == 0) {
				log.error(url + "访问URL获取文章内容失败");
				return;
			}
			Elements title = PageParserTool.select(pageCenter, "h1");
			Pattern p1 = Pattern.compile("(?<=booktitle = \").*?(?=\\\")");
			Pattern p2 = Pattern.compile("(?<=chapter_id = \").*?(?=\\\")");
			Pattern p2_1 = Pattern.compile("(?<=readid = \").*?(?=\\\")");
			Pattern p3 = Pattern.compile("(?<=article_id = \").*?(?=\\\")");
			Pattern p3_1 = Pattern.compile("(?<=bookid = \").*?(?=\\\")");
			Pattern p4 = Pattern.compile("(?<=index_page = \").*?(?=\\\")");
			Pattern p5 = Pattern.compile("(?<=next_page = \").*?(?=\\\")");
			Matcher matcher1 = p1.matcher(pageCenter.getHtml());
			Matcher matcher2 = p2.matcher(pageCenter.getHtml());
			Matcher matcher2_1 = p2_1.matcher(pageCenter.getHtml());
			Matcher matcher3 = p3.matcher(pageCenter.getHtml());
			Matcher matcher3_1 = p3_1.matcher(pageCenter.getHtml());

			Matcher matcher4 = p4.matcher(pageCenter.getHtml());
			Matcher matcher5 = p5.matcher(pageCenter.getHtml());
			String booktitle = "";
			while (matcher1.find()) {
				booktitle = matcher1.group(0);
			}
			String chapter_id = "";
			if (matcher2.find()) {
				chapter_id = matcher2.group(0);
			} else if (matcher2_1.find()) {
				chapter_id = !chapter_id.equals("") ? chapter_id : matcher2_1.group(0);
			}
			String article_id = "";
			if (matcher3.find()) {
				article_id = matcher3.group(0);
			} else if (matcher3_1.find()) {
				article_id = !article_id.equals("") ? article_id : matcher3_1.group(0);
			}
			String index_page = "";
			while (matcher4.find()) {
				index_page = matcher4.group(0);
			}
			String next_page = "";
			while (matcher5.find()) {
				next_page = matcher5.group(0);
			}
			String con = Base64.getEncoder().encodeToString(em.html().getBytes());
			// byte[] byteArr=Base64.getDecoder().decode(msg);
			// String msg2=new String(byteArr);
			// System.out.println("解密:"+msg2);

			content.setContentUrl(url); // 文章url
			content.setContent(con);// 文章内容
			content.setArticleId(article_id);// 书id
			content.setChapterId(chapter_id);// 章节id
			content.setTitle(title.text());// 标题
			content.setIndexPage(index_page);
			content.setNextPage(next_page);// 下一章
			content.setContentId(article_id + chapter_id);
			content.setCreateDate(new Date());
			String titleVal = title.text();
			String num = "";
			if (titleVal.contains("第") && titleVal.contains("章")) {
				num = String.valueOf(
						NumberToCN.zn2num(titleVal.substring(titleVal.indexOf("第") + 1, titleVal.indexOf("章"))));
			} else if (titleVal.contains("第") && titleVal.contains(" ")) {
				num = String.valueOf(
						NumberToCN.zn2num(titleVal.substring(titleVal.indexOf("第") + 1, titleVal.indexOf(" "))));

			}
			content.setChapterNum(num);
			BookContentBean beanById = bookContentService.getBeanById(article_id + chapter_id);
			if (beanById == null) {
				log.info(url + "获取到新章节===>" + content.toString());
				bookContentService.addBook(content);
				MailBean mail = new MailBean();
				mail.setContent(em.html());
				mail.setSubject(content.getTitle());
				sendMailService.sendHtmlMail(mail);
			}
		} catch (Exception e) {
			log.error("请求错误" + url, e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String content = "Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5aW95Ye254yb55qE5LiA6ZSk44CC4oCd56em5LqR44CB5Y+l6IqS5pyJ5Lqb5oOK6K625Y206YO95Lid5q+r5LiN5oWM77yM5LuW5Lus5L+p5LiA5Liq5YmR6Zi154us5q2l5LiJ55WM77yM5Y+m5LiA5Liq5Lmf5piv5re35rKM5pyo56We77yM6YO96IO95bqU5a+56L+Z5LiA6ZSk44CCCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+iTkOaUtuWNtOaYr+ecvOS4reazm+i1t+aImOaEj++8jOS7luWPs+aJi+S4gOS8uO+8jOaJi+S4remHkeWFieWMluS9nOS6huS4gOafhOmHkeiJsuWkp+aWp++8jOi/juedgOmCo+eguOS4i+eahOWmguWxseWkp+mUpOS+v+WKiOS6hui/h+WOu+OAguS4pOi+ueS9k+Wei+ebuOW3ruWkquWkp++8jOmHkeeUsuiTkOaUtuS5n+S7heS7heavlOenpuS6kemtgeaip+mbhOWjruS6m+e9ouS6hu+8jOaMpeWKqOeahOmHkeiJsuWkp+aWp+S5n+S7heS7heS4iOiuuOWkp++8jOWSjOmCo+Wkp+mUpOavlOi1t+adpe+8jOeugOebtOaYr+Wwj+S4jeeCueOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvlmK3vvIEKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75Y+M5pa556Kw5pKe55qE5aOw6Z+z77yM5Y205piv5Lqn55Sf6IKJ55y85Y+v6KeB55qE5rOi57q577yM5rOi57q55qiq5omr5Zub5pa544CCCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+iTkOaUtuiiq+mch+W+l+W+gOWQjuWAkumjnuS6huS4gOaIqu+8jOmCo+W3jeWzqOW3qOS6uuS5n+iiq+mch+W+l+i4iei3hOS6huS4i++8jOS7lueequWkp+ecvOedm+eci+edgOmHkeeUsuiTkOaUtu+8muKAnOWlveWkp+eahOWKm+awlO+8geKAnQo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvigJznoaznorDnoazvvIzmiJHov5jku47mnaXmsqHmgJXov4fosIHjgILigJ3ph5HnlLLok5DmlLbmiqzlpLTnnIvnnYDvvIzlhrfnrJHpgZPvvIzigJzooKLotKfvvIzlj5jov5nkuYjlpKfvvIzlj6rkvJrlvLHngrnmm7TmmI7mmL7vvIHigJ0KPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75ZK744CCCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+iTkOaUtuWMluS9nOS4gOmBk+mHkeiJsuaui+W9se+8jOW/q+eahOaDiuS6uu+8jOWGsuWIsOmCo+W3qOS6uueahOWktOmiheWkhO+8jOS7luaJi+S4reaPoeedgOS4gOafhOmHkeiJsuW8r+WIgOebtOaOpeaoquedgOWIh+WJsui/h+WOu+OAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvpgqPlt43ls6jlt6jkurrli4nlvLrkuIDovazlpLTvvIzpgb/lvIDkuobov5nliIflibLjgIIKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75ZK744CCCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+iTkOaUtuWPiOaYr+S4gOmXqu+8jOS4gOafhOWJkeS7jumCo+W3qOS6uuWktOmhtuS4iuaWueassuimgeWIuuS4i++8jOW3qOS6uui/nuW/meS4gOaJi+aOjOaMoeS9j++8jOWZl+eahOS4gOWjsO+8jOmCo+aJi+aOjOWLieW8uuiiq+WIuuWFpeS6humDqOWIhu+8jOS4jei/h+aJi+aOjOWPmOWkp+S6huWQjuKApuKApuWNtOS5n+WPmOW+l+abtOWOmu+8jOmYsuW+oeS5n+W8uuWkmuS6huOAguiTkOaUtuaJi+S4reeahOmHkeiJsumVv+WJkeS7heS7heWIuuWFpeS4iOiuuO+8jOWLieW8uuWIuuegtOi/meW3qOWkp+aJi+aOjOeahOearuiCpOWxgu+8jOinpuWPiuWIsOeti+iCieiAjOW3suOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvlkrvvvIHlkrvvvIHlkrvvvIEKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76JOQ5pS26YCf5bqm6LaF57ud77yM6L+R6Lqr5pCP5p2A5omL5q615Y+Y5YyW6I6r5rWL77yM5omL5Lit5YW15Zmo5Lmf5LiN5pat5Y+Y5YyW44CCCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+aIluaYr+Wkp+aWp+OAgeWkp+mUpOetiemHjeWFteWZqO+8jAo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvmiJbmmK/plb/mnqrjgIHmo43mo5LvvIwKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75oiW5piv5pqX5Zmo4oCm4oCmCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+S9nOS4uuWSjOelneiejeOAgeWFseW3peOAgeWQjuWcn+WomOWomOOAgeWPpeiKkum9kOWQjeeahOmHkeelnuKAmOiTkOaUtuKAme+8jOaJp+aOjOWFiOWkqeiHs+WuneKAmOmHkeiZjueIquKAme+8jOWPguaCn+eahOaYr+WFiOWkqemHkeihjOWkp+mBk++8jOS7lueahOaJi+auteaYr+WHuuS6huWQjeeahOiHs+mYs+iHs+WImuOAgemUi+WIqeaXoOWMue+8geWFiOWkqeiHs+WuneKAmOmHkeiZjueIquKAmeS5n+aYr+iDveWNg+WPmOS4h+WMlu+8jOWPmOaIkOS7u+S9leS4gOenjeWFteWZqOOAguiTkOaUtuS5n+iDveWwhuS7u+S9leS4gOWFteWZqOe7meWujOe+juWPkeaMpeWHuuadpe+8jOWNs+S+v+aYr+W8k+eureS5i+acr++8jOiTkOaUtuS5n+iDveS7peWFiOWkqemHkeihjOWkp+mBk+aWveWxleW8k+eureS5i+acr++8jOWogeWKm+WQjOagt+aDiuS6uuOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvigJzovbDjgILigJ0KPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76YeR55Sy6JOQ5pS256uZ5Zyo5Y2K56m677yM5omL5oyB6YeR6Imy5byT566t77yM5LiA566t5bCE5Ye677yM566t5aSq5b+r77yM6YKj5beo5Lq65p2l5LiN5Y+K5oyl5Yqo5YW15Zmo6Zi75oyh77yM5Y+q6IO95YuJ5by65LiA5L6n6Lqr77yM5LuW55qE6IKp5aS06KGA6IKJ54K46KOC77yM5bCE5Ye65LqG5LiI6K645aSn55qE6KGA56qf56q/44CC5Y+v5a+55pW05Liq5beo5Lq65bqe5aSn6Lqr5L2T6ICM6KiA77yM5L6d5pen5LiN6LW355y855qE5b6I44CCCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+KAnOWPmOOAguKAnemHkeeUsuiTkOaUtuecvOedm+S4gOS6ru+8jOmCo+eureefoueri+WNs+WMluS9nOS6huS4gOafhOafhOWwj+WJke+8jOWwj+WJkeayv+edgOS8pOWPo+acneW3qOS6uuS9k+WGhemSu+OAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvlj6/lt6jkurrnmoTogqnlpLTkvKTlj6PlnKjov4XpgJ/mhIjlkIjvvIzkuI3mlq3mirXmipfnnYDkuIDmn4Tmn4TlsI/liZHnmoTmuJfpgI/jgIIKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5LuW55qE6IKJ6Lqr5Lmf5aSq5by65LqG77yM5q+U56em5YmR5LuZ5L2g5p2A55qE6JqK6YGT5Lq66IKJ6Lqr6L+Y6KaB5pu05by644CC4oCd6YeR55Sy6JOQ5pS25Lyg6Z+z6YGT77yM4oCc5pW05Liq5LiJ55WM77yM5Lmf5bCx6b6Z5peP6YKj6ICB5a625LyZ6IO95Y6L5LuW5LiA5aS044CC4oCdCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+KAnOW+iOWlve+8jOS9oOeahOWFteWZqOW+iOWOieWus++8jOiDveWNg+WPmOS4h+WMlu+8jOS9oOeahOWKm+mHj+S5n+WPquaYr+avlOaIkeeVpeWwj+S6m+OAguKAneW3qOS6uuWNtOaYr+WcqOWkp+eske+8jOS8vOS5juW+iOa7oeaEj+mHkeeUsuiTkOaUtueahOWunuWKm+OAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvigJzlj6XoipLvvIzopoHkvaDluK7lv5nkuobjgILigJ3ph5HnlLLok5DmlLbkvKDpn7PpgZPjgIIKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5aW944CC4oCdCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+acqOelnuWPpeiKkueCueWktOOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvku5bnq5nlnKjljp/lnLDvvIzlj6/ku5bouqvkuIrljbTmnInkuIDmnaHmnaHol6TolJPplb/lh7rvvIzmlbDljYHmnaHol6TolJPnnqzpl7TlsLHlj5jlvpfml6Dmr5TnmoTplb/vvIzkuIDmnaHmnaHnlq/ni4LnvKDnu5XlkJHpgqPlt43ls6jlt6jkurrvvIzlt43ls6jlt6jkurrohLjoibLkuIDlj5jov57mjKXoiJ7lpKfplKTnoLjjgIHliojjgIHmiavigKbigKbnp43np43miYvmrrXmlr3lsZXvvIzmiavov4flm5vpnaLlhavmlrnvvIzlj6/mmK/ov5nkupvol6TolJPoh7PpmLToh7Pmn5TvvIzku7vlh63lpKfplKTmgJLnoLjvvIzov5nkupvol6TolJPkvp3ml6flvK/mm7LnnYDnvKDnu5XkuobkuIrljrvjgIIKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A757yg57uV5Zyo5LuW55qE6IW/5LiK44CB5omL6IeC5LiK44CB6IWw5LiK44CB5ZaJ5ZKZ5LiK4oCm4oCmCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+S4gOWkhOWkhOe8oOe7leOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvlt6jkurrnvKnlsI/ouqvkvZPvvIzol6TolJPot5/nnYDnvKnlsI/jgIIKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75beo5Lq66Zeq6Lqy77yM5Y+v6Jek6JST56y8572p5aSE5aSE44CCCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+W3qOS6uuaKk+S9j+S4gOadoeiXpOiUk+eMm+eEtueUqOWKm+aJr+aWre+8jOS9huWFtuS7luiXpOiUk+S+neaXp+e8oOe7lei/h+adpe+8jOaWreijgueahOiXpOiUk+WPiOi/hemAn+i/nuaOpeOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvmgLvkuYvlho3lpoLkvZXmjKPmiY7vvIzkvp3ml6fooqvph43ph43mnZ/nvJrnnYDjgIIKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5LiN77yM5LiN4oCU4oCU4oCd5beN5bOo5beo5Lq66KKr6YeN6YeN5p2f57ya552A5oyj5omO5LiN5byA77yM57uI5LqO5LiN55Sx6Ieq5Li75YCS5Zyw77yM6L2w55qE56C45Zyo5aSn5Zyw5LiK77yM6YO95Y6L5Z2P5LqG5LiN5bCR5bu6562R44CC5LuW55qE5aSn6ZSk5Lmf6LeM6JC95Zyo5LiA5peB5Zyw6Z2i5LiK77yM5LuW56ut5Yqb5oyj5omO552A77yM5LuW5Yqb5aSn5peg56m35Luk6Jek6JST6YO95Ye6546w6KOC55eV77yM5L2G5piv6Jek6JST6JW05ZCr5peg5bC955Sf5py677yM6KOC55eV556s6Ze05Y+I5oSI5ZCI44CCCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+KAnOS7lueahOWKm+mHj+avlOaIkeW8uu+8jOiCiei6q+S5n+W8uuaoquOAguWPr+aDnOS4gOeJqemZjeS4gOeJqe+8jOmBh+WIsOWPpeiKkuWFhO+8jOS7luWGjeaMo+aJjumDveaYr+aXoOeUqOOAguKAneiTkOaUtueskeedgOOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvlj6XoipLkuZ/lvq7nrJHnnYDvvJrigJzov5jlnKjmjKPmiY7vvIzov5jlvpflho3liqDkuIrkupvlipvmsJTmiY3og73lm7Dmrbvku5bjgILigJ3ku5bouqvkuIrlj4jmnInkuInmnaHol6TolJPplb/lh7rvvIznvKDnu5XlnKjlt43ls6jlt6jkurrouqvkuIrvvIzlt43ls6jlt6jkurrpob/ml7blho3kuZ/lj43mipfkuI3lvpfvvIzkuIDlhbHotrPotrPlm5vljYHkuInmoLnol6TolJPph43ph43mnZ/nvJrjgILlt43ls6jlt6jkurrlho3mgJLlkLzlmo7lj6vvvIzlho3mjKPmiY7pg73mmK/ml6DnlKjjgIIKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc55yL5p2l5rKh5oiR5LuA5LmI5LqL5LqG44CC4oCd56em5LqR56yR55yL552A6L+Z5bmV77yM4oCc5Y+l6IqS5YWE6L276L275p2+5p2+5bCx5pOS5ou/5LiL5LuW44CC4oCdCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+KAnOaIkeS5n+WPquaYr+WImuWlveWFi+WItuS7luOAguKAneWPpeiKkuivtOmBk++8jOKAnOS4jei/h+S7lueahOWPjeaKl+S5n+W+iOW8uu+8jOiTkOaUtu+8jOaIkeW3sue7j+WbsOatu+S7lu+8jOS7luaXoOazleWPjeaKl++8jOS9oOmAn+mAn+adgOS6huS7luOAguaIkeS7rOWPr+aYr+i/mOimgei/lOWbnueahO+8jOaAu+S4jeiDveaIkeWbnuWOu+eahOWFreWNgeWFq+W5tO+8jOaIkeS5n+i/meS5iOS4gOebtOWbsOedgOS7luWQp++8jOi/meWPr+aYr+W+iOe0r+eahOOAguKAnQo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvkuIDnm7Tnu7TmjIHnnYDvvIznmoTnoa7mtojogJfkuI3lsI/jgIIKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5aW944CC4oCdCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+iTkOaUtueCueWktO+8jOS4gOS8uOaJi++8jOaJi+aOjOS4iuaWueWHuueOsOS6huaXoOaVsOeahOmHkeiJsuaymeeyku+8jOi/meaXoOaVsOmHkeiJsuaymeeykuacnemCo+W3jeWzqOW3qOS6uumjnuWOu+OAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvkuIDml4Hlj6XoipLljbTmmK/lkoznp6bkupHku4vnu43pgZPvvJrigJzok5DmlLbov5nlhYjlpKnoh7Plrp3ph5HomY7niKrlj6/ljYPlj5jkuIfljJbvvIzogIzljJbkvZzov5nph5HoibLmspnnspLvvIzljbTmmK/mnIDmk4Xplb/mtojno6jvvIzpgqPkupvogonouqvlvLrnmoTkuZ/kvJrooqvmtojno6jmiJDnoo7nsonjgILigJ0KPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5Zek5Zek5Zek44CC4oCdCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+aXoOaVsOmHkeaymeWFiOaYr+ayv+edgOW3jeWzqOW3qOS6uueahOm8u+WtlOW+gOmHjOmSu++8jOS9huaYr+W+iOW/q+iiq+mYu+aMoeOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvigJzku5bog73mjqfliLbogonouqvlhoXpg6jlj5jljJbvvIzlrozlhajpmLvloZ7pvLvlrZTpgJrpgZPjgILigJ3ok5DmlLbnrJHnnYDvvIzigJzkvYbmmK/msqHnlKjnmoTvvIzmu7TmsLTnqb/nn7PvvIzph5Hmspnmtojno6jvvIzlrprog73ov5vlhaXku5bkvZPlhoXvvIzlsIblhbbnga3mnYDjgILigJ0KPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76YeR5rKZ5rK/552A6IO45Y+j5b6A6YeM6ZK744CCCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+W+iOW/q+mSu+WHuuS4queqn+eqv++8jOWPquaYr+S7luW3jeWzqOW3qOS6uui6q+S9k+WPmOWkp+WQju+8jOiDuOWPo+eti+iCieS5n+i2s+acieeZvuS4iOWOmu+8jOmHkeaymeS4jeaWremSu+edgO+8jOWPr+eti+iCieS5n+WcqOS4jeaWreaBouWkje+8jOaMpOWOi+edgOi/meS6m+mHkeaymeOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvigJzov5nvvIzov5nigKbigKbigJ3ok5DmlLbpnLLlh7rmg4rmhJXkuYvoibLvvIzigJzku5booqvmjYbnnYDml6Dms5Xlj43mipfvvIzmiJHnq5/nhLbml6Dms5XpkrvpgI/ku5bnmoTouqvkvZPvvJ/igJ0KPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5ZOI5ZOI5ZOI77yM6L+Z5piv5oiR55qE5pys5ZG956ys5LiA5aSp6LWL44CC4oCd5beN5bOo5beo5Lq66KKr5o2G552A5L6d5pen5aSn56yR77yM4oCc5beo5Z6L5YyW5ZCO77yM5oiR55qE6IKJ6Lqr6Ziy5b6h5Y205piv5q+U5bmz5bi45by65LiK5Y2B5YCN6YO95LiN5q2i77yM5L2g5Lus6IO95rS75o2J5oiR77yM5Y205LyR5oOz5p2A5q275oiR44CC6L+Y5pyJ5L2g6L+Z5Liq5pOF6ZW/5p2f57ya55qE77yM5L2g6ZyA6KaB5LiA55u05raI6ICX5Yqb6YeP6ZWH5Y6L5oiR77yM5b2T5L2g55qE5Yqb6YeP5raI6ICX5q6G5bC95pe277yM5oiR5bCx6IO96ISx5Zuw5LqG44CC4oCdCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+KAnOaAjuS5iOWKnu+8n+KAneiTkOaUtueci+WQkeWPpeiKkuOAgeenpuS6keOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvigJzkvaDpg73mnYDkuI3kuobku5bvvIzmiJHkuZ/kuI3ooYzvvIzmiJHmnYDomorpgZPkurrpg73lvpfogJfotLnmlbDmnIjlip/lpKvvvIzku5blj6/mr5Tku5bomorpgZPkurrogonouqvopoHlvLrlvpflpJrjgILigJ3np6bkupHmkYflpLTjgIIKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75Y+l6IqS5Lmf5peg5aWI6YGT77ya4oCc5pei54S25p2A5LiN5LqG77yM5bCx5Y+q6IO95LiA6Lev5p2f57ya552A5bim6LWw5LqG77yB5YWt5Y2B5YWr5bm077yM5omA6ZyA5raI6ICX55qE5rOV5Yqb77yM5oiR6L+Y5piv5om/5Y+X5b6X6LW355qE44CC4oCdCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+S9nOS4uuacqOelnu+8jOazleWKm+WPr+aYr+mbhOa1keeahOW+iOOAggo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDvigJzlj6rog73ovpvoi6blj6XoipLlhYTkuobjgILigJ3ok5DmlLbor7TpgZPjgIIKPGJyPgo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5bCx6L+Z5LmI57uT5p2f5LqG77yf4oCd56em5LqR5Y+I5oOK6K625Y+I5oSf5Yiw5LiA5Lid6L275p2+77yM6L+Z5qyh55qE56Gu6L275p2+77yM5LuF5LuF5pa95bGV5LqG54Of6Zuo6aKG5Z+f5biu5b+Z5oq15oyh6buR5rC077yM5Y+l6IqS5bCx5rS75o2J5LqG5pWM5Lq644CCCjxicj4KPGJyPiZuYnNwOyZuYnNwOyZuYnNwOyZuYnNwO+iAjOiiq+Wbmuemgeadn+e8mueahOW3jeWzqOW3qOS6uuihqOmdouS4iui/mOWcqOaAkuWQvO+8jOi/mOWcqOS4gOasoeasoeaMo+aJjuedgO+8jOWPr+WunumZheS4iuS7luS4gOebtOinguWvn+edgOenpuS6keS7luS7rOS4ieS4qu+8muKAnOi/meS4ieS4queMjueJqe+8jOWSjOaIkei/kei6q+aQj+adgOeahO+8jOiCiei6q+W+iOW8uu+8jOWPquavlOaIkeeVpemAiuS4gOS4neOAguiAjOi/meS4quWbsOS9j+aIkeadn+e8muaIkeeahO+8jOacrOS9k+W6lOivpeaYr+S4gOagquakjeeJqe+8jOeUn+WRveWKm+S4jeS6muS6juaIke+8jOacgOaYr+mavue8oOOAguWAkuaYr+mCo+S4quaThemVv+mihuWfn+eahO+8jOS8vOS5juaYr+elnumtguS5i+S9k++8jOWAkuaYr+S4ieS4quS4rei6q+S9k+acgOiEhuW8seeahOS4gOS4qu+8jOi/measoeaImOaWl+S7luS5n+aYr+i6suWcqOacgOi/nOWkhO+8jOW6lOivpeaYr+acgOWuueaYk+WHu+adgOeahOS4gOS4quOAguKAnQo8YnI+Cjxicj4mbmJzcDsmbmJzcDsmbmJzcDsmbmJzcDs=";
		byte[] byteArr = Base64.getDecoder().decode(content.getBytes());
		String msg2 = new String(byteArr);
		System.out.println("解密:" + msg2);
	}
}
