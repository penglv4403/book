package com.orange.book.bookContent.util;


import com.orange.book.bookContent.bean.BookContentBean;
import com.orange.book.bookContent.service.BookContentService;
import com.orange.book.httpClient.HttpClientUtils;
import com.orange.book.httpClient.Page;
import com.orange.book.httpClient.PageParserTool;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BookContentUtil {
    private static final Logger log = LoggerFactory.getLogger(BookContentUtil.class);

    @Autowired
    private BookContentService bookContentService;


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
                if ("0".equals(mailFlag)) { // 是否发送邮件标志（1：发送，0：不发送）
					/*MailBean mail = new MailBean();
					mail.setContent(em.html());
					mail.setSubject(content.getTitle());
					sendMailService.sendHtmlMail(mail);*/
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

    public BookContentBean getContent(String url) {
        // TODO Auto-generated method stub
        String con = "";
        try {
            BookContentBean content = new BookContentBean();
            Page pageCenter = HttpClientUtils.httpGet(url);
            Elements em = PageParserTool.select(pageCenter, "div[id=content]");
            if (em == null || em.size() == 0) {
                log.error(url + "访问URL获取文章内容失败");
                return null;
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
                chapter_id = !"".equals(chapter_id) ? chapter_id : matcher2_1.group(0);
            }
            String article_id = "";
            if (matcher3.find()) {
                article_id = matcher3.group(0);
            } else if (matcher3_1.find()) {
                article_id = !"".equals(article_id) ? article_id : matcher3_1.group(0);
            }
            String index_page = "";
            while (matcher4.find()) {
                index_page = matcher4.group(0);
            }
            String next_page = "";
            while (matcher5.find()) {
                next_page = matcher5.group(0);
            }

            con = Base64.getEncoder().encodeToString(em.html().getBytes());
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
				/*MailBean mail = new MailBean();
				mail.setContent(em.html());
				mail.setSubject(content.getTitle());
				sendMailService.sendHtmlMail(mail);*/
            }
            con = toPlainText(em.html());
            content.setContent(con);
            return content;
        } catch (Exception e) {
            log.error("请求错误" + url, e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static String toPlainText(final String html) {
        if (StringUtils.isEmpty(html)) {
            return "";
        }

        final Document document = Jsoup.parse(html);
        final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        document.outputSettings(outputSettings);
       // document.select("br").append("\\n");
        //document.select("p").prepend("\\n");
       // document.select("p").append("\\n");
        final String newHtml = document.html().replaceAll("\n \n", "\n");
        final String plainText = Jsoup.clean(newHtml, "", Whitelist.none(), outputSettings);
        final String result = StringEscapeUtils.unescapeHtml3(plainText.trim()).replace("\n \n","\n");
        return result;
    }

    public static void main(String[] args) {
        String content = "Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A755Sf5q275Yaz5oiY77yM5b2x5ZON5Yiw5oiY5paX5Yqb55qE5LiN5LuF5LuF5piv5L+u5Li677yM56We6YCa77yM5Yqf5rOV77yM5ZCM5qC36L+Y5pyJ5b+D55CG77yM6L+Y5pyJ5pm65oWn44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75a+56L+Z5LiA54K577yM56em54mn5rex5pyJ5L2T5Lya44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76YGT6Zeo5Lit5pyJ6K645aSa6auY5omL5Zyo6Z2i5a+555Sf5q275Yaz5oiY5pe277yM5b6A5b6A5Lya5Zyo5oiY5YmN6Z2Z5b+D77yM54Sa6aaZ5rKQ5rW077yM5pyJ55qE55Sa6Iez5Lya5YWl5a6a5LiJ5pel77yM5o6S6Zmk6Ieq5bex6ISR5rW35Lit55qE5ZCE56eN5p2C5b+177yM5LiN6K6p5Lu75L2V5oCd57uq5bmy5omw5Yiw6Ieq5bex5oiY5paX55qE5pm65oWn77yM6K6p6Ieq5bex55qE5pm65oWn5ZyG6YCa44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Jma55Sf6Iqx5bC9566h6Ieq6KiA5omT6YGN5LiK6IuN77yM5ZCM5aKD55WM5peg5a+55omL77yM5L2G5piv5LiK6IuN5Lit55qE5a+55Yaz5b6A5b6A5LiN5piv55Sf5q275a+55Yaz44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW5Zyo6L+Z5pa56Z2i55qE57uP6aqM5aSq5bCR77yM55Sa6Iez5a6M5YWo5Y+v5Lul6K+05piv5LiA5byg55m957q444CC6ICM5LiL55WM5bC9566h5a+55LuW5p2l6K+05piv4oCc5rGh5rWK4oCd55qE77yM54S26ICM5ZCE56eN55Sf5q275pCP5p2A5bGC5Ye65LiN56m377yM5peg6K665p6X6L2p6YGT5Li744CB54+t5YWs5o6q6L+Z5qC36auY6auY5Zyo5LiK55qE5Lq654mp77yM5oqR5oiW5piv54G15q+T56eA44CB5Y+46Iq46aaZ44CB5rKI5LiH5LqR6L+Z562J5bm06L276auY5omL77yM5Y+I5oiW5piv5bu25bq35Zu95biI44CB5bu25Liw5bid6L+Z5qC35LmF6LSf55ub5ZCN55qE5a2Y5Zyo77yM6YO95piv5Zyo55Sf5q2756Oo56C65Lit6ISx6aKW6ICM5Ye644CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75oiY5paX5Lit55qE5pm65oWn77yM5LuW5Lus6LaF6L+H5LqG6Jma55Sf6Iqx5aSq5aSa44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75YCY6Iul5piv56em54mn77yM5L6/5LiN5Lya5Zyo55Sf5q275Yaz5oiY5LmL5YmN5Y675p+l55yL5bCE5pel56We54Ku55qE6Zu25Lu25ZKM6YOo5Lu255qE5p6E6YCg44CB6Zi15Zu+57q555CG77yM5Zug5Li66YKj5aSq5raI6ICX6Ieq5bex55qE6ISR5Yqb44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756em54mn6K6+6K6h6ZS76YCg5bCE5pel56We54Ku77yM6K+35p2l55qE5pyv5pWw6auY5omL5pWw5Lul55m+6K6h77yM6L+Y5pyJ5ZCE56eN54K85a6d55qE5aSn5a6244CB5Lq65omN77yM5Yeg5LmO5piv6YeN5paw6K6+6K6h5LqG552j6YCg5Y6C77yM5LuW6ZuG5ZCI6L+Z5Lqb5Lq655qE5pm65oWn5Yi26YCg5bCE5pel56We54Ku77yM6Jma55Sf6Iqx5oOz6KaB5o6i5a+75bCE5pel56We54Ku55qE5aWl56eY77yM5L6/5piv6ZyA6KaB5Zyo55+t5pe26Ze05YaF6K6w5b+G5LiL5LuW5Lus6L+Z5Lqb5Lq655qE5pm65oWn77yM5a+56ISR5Yqb55qE5o2f6ICX5LmL5aSn77yM5Y+v5oOz6ICM55+l44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76ISR5Yqb55qE5o2f6ICX77yM5b+F54S25Lya5bim5p2l5a6e5Yqb55qE5o2f6ICX77yM5Zyo5oiY5paX5Lit5bqU5Y+Y6YCf5bqm5YeP5oWi44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75a+55LqO56em54mn6L+Z5qC355qE6auY5omL5p2l6K+077yM5ZOq5oCV6Jma55Sf6Iqx55qE5bqU5Y+Y6YCf5bqm5oWi5LqG5LiA5bCY5LiA5Z+D5LiA5ri677yM6YO96Laz5Lul5Yaz5a6a6IOc6LSf77yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Jma55Sf6Iqx5Zyo552j6YCg5Y6C5Lit5LiA5LiA5p+l55yL6L+Z5Lqb6Zu25Lu26YOo5Lu25LiK55qE57q555CG5p6E6YCg77yM5o6o566X5bCE5pel56We54Ku55qE5b2i5oCB5ZKM5aiB6IO977yM6L+Z5Lqb6Zu25Lu26YOo5Lu25pWw5Lul5LiH6K6h77yM5q+P5LiA5Liq6Zu25Lu26YOo5Lu25LiK55qE6Zi157q556ym5paH5Y+I5ZCE5LiN55u45ZCM77yM5omA55So5Yiw55qE6Zi15rOV57q555CG5LiN5ZCM77yM5rKh5pyJ6aG65bqP55qE6K+d5L6/6ZyA6KaB5LuW6Ieq5bex5YCf5Yqp5by65aSn55qE6ISR5Yqb5Y675o6o5ryU57uE5ZCI77yM5pu05Yqg5o2f6ICX6ISR5Yqb44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756qB54S277yM5LuW5pyJ5Lqb5aS05pmV77yM54yb5Zyw6YaS5oKf6L+H5p2l77yM5oCl5b+Z6Zet5LiK55y8552b77yM6L+H5LqG54mH5Yi777yM6L+Z5omN5byg5byA55y8552b5ZCR56em54mn55yL5p2l44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756em54mn6Zyy5Ye656yR5a6577yM6aKU6aaW56S65oSP44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756em54mn56yR5b6X5b6I5piv6Ziz5YWJ77yM5YOP5piv5Liq5q+r5peg5b+D5py655qE5aSn55S35a2p77yM5L2G5piv6JC95Zyo5LuW55qE55y85Lit77yM6L+Z5Liq54G/54OC6Ziz5YWJ55qE56yR5a655L6/5pi+5b6X5peg5q+U6YKq5oG25LqG44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5aSa6LCi56em5YWE77yM5oiR5a2m5Yiw5LqG5LiA5oub44CC4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Jma55Sf6Iqx5a6a5LqG5a6a56We77yM6LWw5LqG6L+H5p2l77yM5LiA6L656LWw5p2l77yM5LiA6L6556ut5Yqb5Y675b+Y6K6w6Ieq5bex5Yia5omN6K6w5LiL55qE6YKj5Lqb6Zi157q556ym5paH77yM6Jm954S26L+Z5Y+q5piv5Lqh576K6KGl54mi77yM5L2G6L+Y5piv6KaB5q+U5LuA5LmI6YO95LiN5YGa6KaB5aW944CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756em54mn5b6u5b6u5LiA56yR77yM5ZCR552j6YCg5Y6C5aSW6LWw5Y6777yM5oKg54S26YGT77ya4oCc6Jma5YWE6L+Y5Lul5Li65ZGG5Zyo5LiK6IuN5Lit5b6I5aW95ZCX77yf4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Jma55Sf6Iqx5pGH5aS077ya4oCc5ZGG5Zyo5LiK6IuN5Lit77yM5rKh5pyJ57uP5Y6G6L+H55yf5q2j55qE5oiY5paX77yM5oiR55qE56Gu6Zq+5Lul5oiQ6ZW/44CC5rWK5LiW5omN5piv6K6p5Lq65oiQ6ZW/55qE5Zyw5pa577yM56em5YWE5Zyo6L+Z5Liq5rWK5LiW5Lit6ZW/5aSn77yM55yf5piv5oiR6YGH5Yiw55qE5pyA5by65pyA5Y+v5oCV55qE5a+55omL44CC4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75Lik5Lq65bm26IKp6ICM6KGM77yM6LWw5LiK5raC5rGf44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW5Lus6Lqr5ZCO77yM54uQ54G15YS/44CB54G15q+T56eA44CB546J5p+z5ZKM5Lqs54eV5Zub5Liq5aWz5a2p6Lef552A77yM6LiP5LiK5raC5rGf55qE5rGf6Z2i44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76ICM5Zyo5q2k5pe277yM6K646K645aSa5aSa6YGT5aOr6LWw5Ye65LqG552j6YCg5Y6C77yM5Li66aaW55qE5L6/5piv5p6X6L2p6YGT5Li777yM5Lqm5q2l5Lqm6LaL55qE6Lef552A5LuW5Lus77yM5p2l5Yiw5rGf6Z2i5LiK44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75p6X6L2p6YGT5Li755uu5YWJ5aWH5byC77yM5peg6KeG5Zub5Liq5aWz5a2p77yM55uu5YWJ6JC95Zyo5q2j5Zyo5rGf6Z2i5LiK6KGM6LWw55qE56em54mn5ZKM6Jma55Sf6Iqx6Lqr5LiK44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc6YGT5Li777yM6L+Z5Lik5Liq5bCR5bm06YO95b6I5by677yB4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LiA5L2N6ICB6YGT5Lq65L2O5aOw6YGT77ya4oCc56em5pWZ5Li75YCS6L+Y572i5LqG77yM5LuW55qE5omL5q616auY5rex77yM5Y+m5LiA5Liq5bCR5bm05piv5LuA5LmI5p2l5aS077yf4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5LiK6IuN6Jma55Sf6Iqx77yM5oiR6ZqP552A5biI5bCK5Zyo5bCP546J5Lqs5YGa5a6i5pe26YGH5Yiw5LqG5LuW44CC4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75p6X6L2p6YGT5Li755uu5YWJ6Zeq5Yqo77ya4oCc5LuW55So5LqU5oub6LSl5oiR44CC4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76K+45aSa6YGT6Zeo55qE6YGT5aOr5b+D5aS05aSn6ZyH44CC5Y2z5L6/5piv56em54mn77yM5Lmf5LiN5pWi6K+06IO95aSf5LqU5oub5L6/5Ye76LSl5p6X6L2p6YGT5Li744CC6L+Z5Lqb5bm05p2l77yM5p6X6L2p6YGT5Li76Lef6ZqP6ICB6YGT5Li75L+u6KGM77yM5Zyo5pyv5pWw5LiK55qE6YCg6K+j5oSI5Y+R5rex5Y6a77yM5a+56YGT5YmR55qE6aKG5oKf6LaK5p2l6LaK6auY5rex44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW55qE5a6e5Yqb77yM57ud5LiN6YCK5LqO6ICB6YGT5Li75b2T5bm077yM55Sa6Iez6L+Y5pyJ5omA6LaF6LaK77yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75p6X6L2p6YGT5Li76ISa5q2l5LiN5YGc77yM5bim552A5LyX6YGT5aOr5ZCR5YmN6LWw5Y6777yM6L275aOw6YGT77ya4oCc5LuW55qE5Yqf5rOV56We6YCa6YGH5by65YiZ5by677yM5pyA5piv5Y+v5oCV77yM6LaK5by655qE5a+55omL77yM6LaK6IO95r+A5Y+R5LuW56We6YCa5ZKM5Yqf5rOV55qE5aiB5Yqb44CC5bCP546J5Lqs5Lit77yM5LuW5Ye76LSl5LqG5b6I5aSa5Lq644CC5oiR5b6I5oOz55+l6YGT77yM5piv5ZCm5pyJ5Lq66IO95aSf5o6l5LiL5LuW55qE56We6YCa77yM5piv5ZCm5pyJ5Lq66IO95aSf6YC85b6X5LuW56qB56C05LuW5Yqf5rOV56We6YCa55qE5p6B6ZmQ77yM6K6p5LuW56C05peg5Y+v56C077yB4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5Lq655qH77yM5oiW6K645Lya5piv6L+Z5qC355qE5Lq644CC4oCd5LuW5oiY5oSP6Zmh54S25pe655ub6LW35p2l77yM5Y205o6n5Yi25L2P6Ieq5bex55qE5oiY5oSP77yM5rKh5pyJ5bmy5omw5Yiw5Lik5Lq644CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75rGf5rC05ruU5ruU77yM6YCd6ICF5aaC5pav44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756em54mn5ZKM6Jma55Sf6Iqx5rKh5pyJ5YGc5LiL6ISa5q2l77yM57un57ut5ZCR5YmN6LWw5Y6777yM5b6I5b+r6LWw5Yiw5rGf5a+55bK477yM6ISa5q2l5L6d5pen5LiN5YGc44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5LuW5Zyo57uZ5oiR5LyR5oGv55qE5py65Lya44CC4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Jma55Sf6Iqx5Y+I5a2m5Yiw5LqG5LiA5oub77yM5b+D5aS05b6u6ZyH77ya4oCc5LuW5bim5oiR5p2l552j6YCg5Y6C77yM6K6p5oiR5o2f6ICX6ISR5Yqb77yM6K6p6Ieq5bex5Y2g5o2u5LqG5LiK6aOO44CC6ICM546w5Zyo57uZ5oiR5LyR5oGv55qE5py65Lya77yM5piv6KGo5piO5LuW5LiN5Zyo5LmO5oiR55qE6ISR5Yqb5o2f5LiN5o2f6ICX77yM5Zug5Li65LuW5aeL57uI5Y2g5o2u5LiK6aOO77yB5Yia5omN5piv5o2f6ICX5oiR55qE6ISR5Yqb77yM6ICM546w5Zyo5YiZ5piv5Zyo57K+56We5bGC6Z2i5LiK5pa95Y6L77yM6K6p5oiR5YaF5b+D5Lit6Ieq6K6k5byx5LuW5LiA562577yB4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW6Jm954S25piO55m956em54mn55qE5oOz5rOV77yM5L2G5piv546w5Zyo5LuW5bey57uP6Zm35YWl56em54mn55qE5bGA5Lit77yM5oyj5omO5LiN6ISx44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Ieq5LuO56em54mn5LiO5LuW5YaN5bqm5Lya6Z2i77yM5Zyo6Iqx5be35ZCs6Zuo6ZiB5Lit5Zad6YWS55qE6YKj5LiA5Yi777yM5oiY5paX5L6/5bey57uP5byA5aeL77yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuO6YKj5pe26LW377yM5LuW5L6/6L+b5YWl5LqG56em54mn55qE6IqC5aWP5LmL5Lit77yM5Yiw5LqG546w5Zyo77yM56em54mn55qE5aSn5Yq/5bey5oiQ77yM5YCY6Iul5Lik5Lq65Yqo5omL77yM5Y+v5oOz6ICM55+l56em54mn55qE5pS75Ye75piv5L2V562J55qE6Zy46YGT5L2V562J55qE55WF5b+r5reL5ryT77yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc6Zy45L2T55qE56Gu5LiN5Yeh44CC4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Jma55Sf6Iqx56iz5L2P5b+D56We77yM55uu5YWJ6Zeq5Yqo77ya4oCc5LiN6L+H5oiR5Lmf5piv6Zy45L2T77yB5ZCM5Li66Zy45L2T77yM5oiR5LiN5Lya5byx5LqO5LuW77yB4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75Lik5Lq655qE6ISa5q2l5riQ5riQ5pyJ5LqG5YWI5ZCO77yM56em54mn6L+I5Ye65LiA5q2l77yM6Jma55Sf6Iqx6Lef5LiK5LiA5q2l77yM6Jma55Sf6Iqx5YOP5piv5LuW55qE5b2x5a2Q77yM57uZ5Lq65LiA56eN5Y+k5oCq55qE5oSf6KeJ44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Lef5Zyo5LuW5Lus6Lqr5ZCO55qE5Zub5Liq5aWz5a2p55yL5Yiw6L+Z5LiA5bmV77yM6L+Z5Lik5Liq5aSn55S35a2p5LiA5Liq5Zyo5YmN6Z2i6LWw77yM5bCG5ZCO5b+D6Zyy5Ye657uZ6Lqr5ZCO55qE5Lq677yM6ICM6Lqr5ZCO55qE5bCR5bm05Y205Lqm5q2l5Lqm6LaL77yM5rKh5pyJ6LaB5py655eb5LiL5p2A5omL77yM5Y+N6ICM5YOP5piv6KKr5LiA5p2h57uz57Si5ou05L2P77yM5LiN55Sx6Ieq5Li755qE6Lef552A5YmN6Z2i55qE5Lq65YmN6KGM44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5YWs5a2Q6JC95Zyo5LiL6aOO5LqG77yB4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7546J5p+z5ZKM5Lqs54eV5b+D5aS05b6u6ZyH77yM5aW55Lus6L+Y5piv5aS05LiA5qyh6KeB5Yiw5bCa5pyq5Yqo5omL6Jma55Sf6Iqx5L6/6JC95YWl5LiL6aOO55qE5oOF5b2i44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LiK6IuN5Lit77yM6Jma55Sf6Iqx5piv5L2V562J55qE5oOK6YeH57ud6Imz77yM6ZyH5oOK5LqG5LiK6IuN5Lit55qE5LiA5bCK5bCK56We56WH77yM6KKr6K645Li65LqU55m+5bm05p2l6LWE6LSo5pyA6auY55qE5Lq644CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76ICM6Jma55Sf6Iqx5LiL55WM77yM55qE56Gu5Lmf5LiN6LSf55ub5ZCN44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76buE6YeR5a6r55qE54+t5YWs5o6q6YG/5oiY77yM5aSn6Zu36Z+z5a+65LiN5oiY77yM5bCP546J5Lqs5Lit5oyR5oiY5p6X6L2p6YGT5a2Q77yM5oiY6IOc5bCP546J5Lqs55qE6auY5omL77yM5oyR5oiY5LiJ5YWD5q6/5LqU5rCU5q6/77yM6aG65Yip6YCa5YWz44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7546w5Zyo77yM6Jma55Sf6Iqx57uI5LqO6YGH5Yiw5LqG5pyA5Li65Y+v5oCV55qE5a+55omL77yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756em54mn5bim552A6Jma55Sf6Iqx6LWw5Ye655m+5Y2B6YeM5Zyw77yM5b+D5Lit5Lmf5pyJ5Lqb6ZyH5oOK77yM55u05Yiw546w5Zyo77yM6Jma55Sf6Iqx55qE6ISa5q2l5L6d5pen5Lid5q+r5LiN5Lmx77yM6Lqr5LiK6L+Y5piv5rKh5pyJ6Zyy5Ye65Y2K54K556C057u977yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76L+Z5Yeg5LmO5piv5LiN5Y+v6IO95Yqe5Yiw55qE5LqL5oOF77yM5Y2z5L6/5piv54+t5YWs5o6q6L+Z5Liq5rS75LqG5LiH5bm055qE6ICB5aaW5oCq77yM5Lmf5LiN5Y+v6IO95L6d5pen6LWw5b6X5bmz56iz77yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A754+t5YWs5o6q57K+6YCa5aSn6Zu36Z+z5a+644CB6YGT6Zeo44CB5bCP546J5Lqs5ZKM5aSp6a2U5pWZ5Yqf5rOV56We6YCa77yM5YCY6Iul6KKr56em54mn5Y6L6L+H5LiA5aS077yM6KKr56em54mn54m1552A6LWw77yM6YKj5LmI5LuW6LWw5Yiw5Y2B6YeM5Zyw5pe277yM5L6/5Lya5Lic5YCS6KW/5q2q77yM56ut5Yqb5Y+Y5YyW6Lqr5b2i5omN6IO95L+d6K+B5LiN6Zyy5Ye656C057u944CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76ICM5Yiw5LqM5Y2B6YeM5Zyw77yM54+t5YWs5o6q5L6/5Lya6Zyy5Ye656C057u977yM5YaN6Zq+5pyJ5Lqb5Y+Y5YyW77yM5LqM5Y2B5LiA6YeM5pe277yM54+t5YWs5o6q5L6/5b+F6aG7546H5YWI5ZCR56em54mn5Ye65omL77yM5Yqb5Zu+5oqi5aS65YWI5py677yM5ZCm5YiZ5b+F5q275peg55aR77yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75YCY6Iul54+t5YWs5o6q57un57ut5Z2a5oyB6Lef552A56em54mn55qE6ISa5q2l6ICM5LiN5Ye65omL55qE6K+d77yM5LqM5Y2B5Zub6YeM77yM5L6/5piv5LuW55qE5q275pyf77yM57ud5a+55Lya6KKr56em54mn5LiA5Ye75q+Z5ZG977yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A754S26ICM6Jma55Sf6Iqx5Y205aeL57uI6Lef5LiK56em54mn55qE6ISa5q2l77yM5bC9566h6KKr56em54mn54m1552A6LWw77yM5L2G5piv6ISa5q2l6Lqr5rOV57q55Lid5LiN5Lmx77yM5peg5oeI5Y+v5Ye777yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuO5raC5rGf5Yiw6L+Z6YeM77yM5LiA55m+5L2Z6YeM5Zyw77yM5LuW5rKh5pyJ6Zyy5Ye65Lu75L2V56C057u977yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc546L5rKQ54S26K+077yM5LuW55qE5Yqf5rOV56We6YCa5b6I5piv5Y+k5oCq77yM6YGH5by65YiZ5by677yM6LaK5by655qE5a+55omL77yM5Zyo5LuW5omL5Lit6LSl5b6X6LaK5b+r77yB5LuW55qE56We6YCa6YO95piv5Li05Zy65bqU5Y+Y77yM5a+55oiY5LmL5Lit5Yib6YCg6ICM5Ye644CC6L+Z56eN5Yqf5rOV5b+F54S25p6B5Li66auY562J77yM5piv5LiA5LiK5omL5L6/6LaF6LaK5LqG5pyv77yM6L+b5YWl5rOV55qE5bGC5qyh77yB4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756em54mn6LaK5p2l6LaK5YW05aWL77yM5rKh5pyJ5Zue5aS057un57ut5ZCR5YmN6LWw5Y6777yM6YCf5bqm6LaK5p2l6LaK5b+r77yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Jma55Sf6Iqx55qE5Yqf5rOV5b6I5piv56We5aWH77yM6L+Z56eN56We5aWH55qE5Yqf5rOV6IO95aSf6K6p5LuW55u05o6l6LaK6L+H5pyv77yM6L+b5YWl5Yib6YCg56We6YCa55qE5rOV55qE5bGC5qyh44CC6L+Z56eN56We5aWH55qE5Yqf5rOV77yM5LuW6L+Y5piv5aS05LiA5qyh6YGH5Yiw44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW5oOz6K+V5o6i5Ye66L+Z56eN5Yqf5rOV55qE5p6B6ZmQ77yM5q+U6Zy45L2T5LiJ5Li55Yqf5aaC5L2V77yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW55qE6ISa5q2l6LaK5p2l6LaK5b+r77yM5rCU5Yq/6LaK5p2l6LaK5by677yM5Yeg5LmO5piv6aOO6amw55S15o6j5LiA6Iis5ZCR5YmN6aOe5Y6777yM5L2G5piv5LuW6L+I5Ye655qE6ISa5q2l5Y206L+Y5piv5pi+5b6X5LuO5a6577yM5rKh5pyJ5Y2K54K555qE5LuT5L+D5LmL5oSf44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Jma55Sf6Iqx6KKr5LuW5bim552A6Lef5LiK5LuW55qE6ISa5q2l77yM5q2l5rOV5L6d5pen56iz5YGl77yM5Lid5q+r5LiN5Lmx44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75Lik5Lq655qE6YCf5bqm6LaK5p2l6LaK5b+r77yM56qB54S277yM6Jma55Sf6Iqx6Z2i6Imy6IuN55m977yM5o6l552A6IS46Imy6LaK5p2l6LaK6IuN55m977yM5LuW6KKr56em54mn5bim552A5aWU6LeR5LqG55m+5L2Z6YeM77yM5YaN5Lmf5b+N5LiN5L2P77yM5ZOH55qE5LiA5Y+j6bKc6KGA5Za35Ye644CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756em54mn6ISa5q2l5LiN5YGc77yM6Jma55Sf6Iqx6Lef552A5LuW5ZCR5YmN6LeR5Y6777yM5Y+I5piv5ZOH55qE5ZCQ5LqG5Y+j6bKc6KGA44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW5LiA6L656LeR77yM5LiA6L655ZCQ6KGA77yM5L2G6L+Y5piv5rKh5pyJ6Zyy5Ye65Lu75L2V56C057u977yM5L2G5piv6L+Z5qC35LiN5pat5ZCQ6KGA77yM6L+f5pep5Lya5bCG5L2T5YaF55qE6KGA5ZCQ5a6M77yM5LiA5ZG95ZGc5ZG877yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Ieq5aeL6Iez57uI77yM56em54mn5pyq5pu+5ZCR5LuW5Ye66L+H5omL77yM6K6p5LuW5Y+X5Lyk55qE5piv5LuW6Ieq5bex44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756em54mn5ZCR5YmN6LWw77yM5YW25a6e5piv5Lik5Lq65LmL6Ze055qE5rCU5Yq/6Lqr5rOV5ZKM56We6YCa5a+55Yaz77yM6Jm954S255yL5Ly85rKh5pyJ5Lu75L2V5Ye26Zmp77yM5L2G5Yaz5oiY5bey57uP5bGV5byA77yM5a655LiN5b6X5Y2K5YiG5beu6ZSZ44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW55qE5Yqf5rOV56We6YCa6Z2g55qE5piv5Li05Zy65bqU5Y+Y77yM6Z2g55qE5piv5Yib6YCg77yM5a+56ISR5Yqb55qE6KaB5rGC5p6B6auY44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW5Zyo552j6YCg5Y6C5Lit5o2f6ICX5LqG5aSn6YeP6ISR5Yqb77yM56em54mn5bC9566h57uZ5LqG5LuW5LyR5oGv5bmz5aSN55qE5py65Lya77yM5L2G6ISR5Yqb5o2f6ICX5LiN5piv6YKj5LmI5a655piT5L6/6IO96KGl5YWF5Zue5p2l44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW546w5Zyo5bey57uP57Sv5b6X5ZCQ6KGA77yM57un57ut5LiL5Y6755qE6K+d77yM6ISR5Yqb5o2f6ICX5aSq5aSn77yM56em54mn5qC55pys5peg6ZyA5Yqo5omL77yM5L6/5Lya5bCG5LuW55Sf55Sf5ouW5q2777yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A757un57ut6LWw77yM5LuW55qE6ISR5Yqb5b+F54S25p6v56ut44CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Jma55Sf6Iqx55qE5aS05Y+R5byA5aeL5Y+Y5b6X54Gw55m977yM6buR5Y+R5Y+Y5oiQ5LiA57yV57yV55m95Y+R77yM5LiA6L655ZKz6KGA5LiA6L656Lef5LiK56em54mn55qE6ISa5q2l44CC5LuW55qE6ISR5Yqb5o2f6ICX5bey57uP57Sv5Y+K6IKJ6Lqr77yM6KaB5LiN5LqG5aSa5LmF77yM5LuW5L6/5Lya6Ieq5bex57Sv5q276Ieq5bex77yBIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A756qB54S277yM56em54mn54yb5Zyw5YGc5LiL6ISa5q2l77yM56yR6YGT77ya4oCc6L+Z5qC35p2A5L2g77yM5L2g5LiA5a6a5LiN5pyN77yM6ICM5oiR5Lmf5LiN5Lya5aSq54i944CC6Jma5YWE77yM5L2g5aW95aW95YW75Lyk77yM6KGl5LiA6KGl5YWD5rCU77yM5LiL5qyh5YaN5LiA5Yaz55Sf5q2744CC4oCdIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A76Jma55Sf6Iqx5YGc5q2l77yM5byg5LqG5byg5Zi077yM5oOz6KaB6K+06K+d77yM56qB54S25ZmX6YCa5LiA5aOw5YCS5Zyw77yM5piP5q276L+H5Y6744CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A74oCc5piv5Liq5Y+v5pWs5Y+v5oCV55qE5a+55omL44CC4oCd56em54mn6LWe5LqG5LiA5aOw77yM5bCG5piP5YCS55qE6Jma55Sf6Iqx5oqb5Zyo6YKj6YeM77yM6L2s6Lqr56a75Y6744CCIAo8YnI+IAo8YnI+Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A75LuW55qE5Zi06KeS5rqi6KGA77yM6L+e5b+Z5bCG5raM5LiK5ZaJ5aS055qE6KGA5ZK95LqG5Zue5Y6777yM5LuW5beu54K557Sv5q275LqG6Jma55Sf6Iqx77yM5L2G5piv6Ieq5bex5Lmf5beu54K55pSv5pKR5LiN5L2P44CC5LiN6L+H5p2R6YeM5Lq66K+06L+H77yM6L6T5Lq65LiN6L6T6Zi177yM5LiH5LiH5LiN6IO96Zyy5oCv44CCIAo8YnI+IAo8YnI+Cjxicj4KPGJyPgo8cD48YSBocmVmPSJodHRwOi8va291YmVpLmJhaWR1LmNvbS9zL3hiaXF1Z2UubGEiIHRhcmdldD0iX2JsYW5rIj7kurIs54K55Ye76L+b5Y67LOe7meS4quWlveivhOWRlyzliIbmlbDotorpq5jmm7TmlrDotorlv6ss5o2u6K+057uZ5paw56yU6Laj6ZiB5omT5ruh5YiG55qE5pyA5ZCO6YO95om+5Yiw5LqG5ryC5Lqu55qE6ICB5amG5ZOmITwvYT48YnI+5omL5py656uZ5YWo5paw5pS554mI5Y2H57qn5Zyw5Z2A77yaaHR0cDovL20ueGJpcXVnZS5sYe+8jOaVsOaNruWSjOS5puetvuS4jueUteiEkeermeWQjOatpe+8jOaXoOW5v+WRiua4heaWsOmYheivu++8gTwvcD4=";
        byte[] byteArr = Base64.getDecoder().decode(content.getBytes());
        String msg2 = new String(byteArr);
        System.out.println("解密:" + msg2);
        String str = "    叮铃铃！！ \n" +
                " \n" +
                "    床头，闹钟在拼命发出刺耳的嘶吼，一副不达到目的誓不罢休的姿态。 \n" +
                " \n" +
                "    “闹钟，到点了吗？” \n" +
                " \n" +
                "    并不奢华，只能算是普通的席梦思上，一名显得有些瘦弱的青年猛地自床上坐了起来，脸上突然流露出惶恐诧异，不可思议，茫然之色。 \n" +
                " \n" +
                "    极为的复杂。 \n" +
                " \n" +
                "    脸色变幻间，比起川剧变脸都要来的神奇。 \n" +
                " \n" +
                "    “不对，我怎么会在这里，难道之前是在做梦，都是假的。日期，对，日期。” \n" +
                " \n" +
                "    青年脸上流露出震惊之色，随即就慌乱的看向闹钟上现世出的日历。 \n" +
                " \n" +
                "    “2019年9月20日。” \n" +
                " \n" +
                "    “怎么会是20号，难道我回到了三天前，还是我之前根本就是在做梦。” \n" +
                " \n" +
                "    青年脸上一阵青一阵白，口中喃喃自语。 \n" +
                " \n" +
                "    青年名叫易天行。 \n" +
                " \n" +
                "    原先是一名孤儿，从小就被人抛弃，被天福孤儿院收养，被抛弃时，是在五岁的时候，其原因有些诡异，是因为精神不正常才被抛弃，因为他在刚开始记事时，就开始向父母说起他能看到鬼，看到常人所看不到的鬼魂。小孩子，口无遮掩，言语中又是荒诞无稽，而且是不是说自己身边有鬼，走在路上，对着空气说有满头是鲜血的大叔在吓自己。这不管怎么看，都不想是正常孩子。 \n" +
                " \n" +
                "    在精神病医院诊断下，他也就直接被判定精神有问题。连亲生父母都无法忍受他这种不时的神神叨叨的话语，最终将其抛弃，送进孤儿院中，成为一名孤儿。 \n" +
                " \n" +
                "    前两年亲生父母还会不时的私下过来看望一两次，但在数年后，有了新的孩子，而且还是个男孩，之后就再也没有来探望过。 \n" +
                " \n" +
                "    易天行在六七岁后，就已经开始懂事，看到别人对于自己如同看到鬼一样的恐惧厌恶眼神，也就越来越沉默，变得孤僻寡言，在人前，再也不开口说自己能看到那些鬼怪的事情。表现的跟一个正常孩子没什么区别。 \n" +
                " \n" +
                "    八岁时，得一对好心夫妇看中，收养在身下，成为养父母，养父母分别叫赵海峰，黄玉。膝下只有一女，七岁，名为赵紫嫣。因为特殊原因，他们在生下赵紫嫣后，始终无法再怀上，所以，就收养了易天行。 \n" +
                " \n" +
                "    在收养后，对易天行也是极好，如同亲生一样，有什么好吃的，好玩的，都是一视同仁的给予他们。没有任何有色眼镜什么的。 \n" +
                " \n" +
                "    在养父母的呵护下，易天行原先冰冷的心也不由的温暖起来，与赵紫嫣也是情同兄妹，爱护有加，感情极深。真的将赵海峰，黄玉看成是自己的亲生父母。 \n" +
                " \n" +
                "    对于原先的亲生父母，早已经埋藏在心底，不去触动。 \n" +
                " \n" +
                "    如今易天行已经二十三岁，已经从大学中毕业，参加工作，不过，表面是正常工作，实则暗中却是做的捉鬼驱邪的生意。 \n" +
                " \n" +
                "    他的一双眼睛天生不凡，能看阴阳，是一双先天的阴阳眼。 \n" +
                " \n" +
                "    虽然自孤儿院出来后，就从来没有对家人乃至是周围的朋友说起过任何关于自身眼睛的事情。但随着年岁的增长，这一双阴阳眼亦变得更加神异，不仅仅是能窥见阴阳，看见妖魔鬼怪，而且，具有玄妙的瞳术神通，名为阴阳锁。 \n" +
                " \n" +
                "    只是现在只能施展出两条阴阳锁。易天行试过，这阴阳锁十分厉害，对于厉鬼更加厉害，只要被阴阳锁给锁住，立即就会束手就擒，反抗不得。这些年，凭借这本事，易天行还被人称之为大师。对于钱财，可是半点都不缺。 \n" +
                " \n" +
                "    要说是就这么下去，生活同样有滋有味，过的极为精彩。 \n" +
                " \n" +
                "    不过，之前所经历的事情却让他心中生出无尽的恐惧。 \n" +
                " \n" +
                "    “确实是三天前。” \n" +
                " \n" +
                "    易天行打开电视，上面赫然在播放着三天前自己看过的一则新闻。家中的各种摆设也是跟记忆中三天前时一模一样，半点差异都没有。 \n" +
                " \n" +
                "    哗啦！！ \n" +
                " \n" +
                "    拉开窗帘，在窗外繁华的街道上，消防车的警笛声在不断震荡耳膜，一间店铺中正燃起熊熊大火。 \n" +
                " \n" +
                "    这些都是易天行在记忆中已经经历过的。 \n" +
                " \n" +
                "    所有的一切，都印证着，这确实是三天前，自己莫名其妙的返回到三天前。 \n" +
                " \n" +
                "    而三天前与三天后，简直就是天堂与地狱般的分割线。 \n" +
                " \n" +
                "    “那些到底是什么东西，真的是末日降临么。怪物，骷髅，僵尸，不死亡灵，虫子，怪兽。”易天行看着窗外的行人，眼中的视线却仿佛直接贯穿时间长河，看到了未来。 \n" +
                " \n" +
                "    脑海中浮现出三天后所经历的场景。 \n" +
                " \n" +
                "    就在2019年9月23日清晨，易天行站立在大厦天台观看旭日东升景色的时候，突然间，整个太阳仿佛在刚刚钻出来的一刹那，直接被一片诡异的黑暗阴影所吞噬掉，天地间一片漆黑，紧跟着，虚空中电闪雷鸣，一道道裂缝几乎凭空出现。 \n" +
                " \n" +
                "    密集的分布在整个地球上。 \n" +
                " \n" +
                "    那些裂缝撕裂的景象，如同末日降临，紧跟着，自裂缝中，就冲出无数怪物。 \n" +
                " \n" +
                "    那些诡异的魔兽，怪兽，虫子，亡灵，骷髅等等，如雨点般直接从裂缝中喷涌而出。见人就杀，在城市中疯狂破坏，几乎眨眼间，就有海量的人员伤亡，甚至被撕成碎片。到处一片混乱。那场景，十分恐怖。 \n" +
                " \n" +
                "    比末日还要来的更加可怕。 \n" +
                " \n" +
                "    而且，自那裂缝中，还喷吐出各种神光，向世界各地散播出去。 \n" +
                " \n" +
                "    那些神光仿佛流星一样，不知道包裹着什么东西。 \n" +
                " \n" +
                "    而且，易天行更是亲眼看到，有一道七彩斑斓的流星直接朝着自己砸落过来，这一砸，哪怕是易天行本身身手就极为不错，甚至练过武，学过太极拳，阴阳眼眼力极强，也根本没办法在这种无比唐突的情况下做出躲闪。 \n" +
                " \n" +
                "    只来得及做出一次避闪的动作，就被那道流星砸在头上。最失去意识前，方才看清楚，那砸落下来的，是一枚七彩斑斓的珠子，一看就不是凡物。但易天行只感觉到一阵剧痛，整个意识就彻底消散，再次醒来时，就已经回到三天前。 \n" +
                " \n" +
                "    也就是现在。 \n" +
                " \n" +
                "    那一幕幕的画面，极为清楚的烙印在脑海中。 \n" +
                " \n" +
                "    “那些都是真的，三天后，就是末日降临之时，天降无数怪物，空间崩裂，日月无光。人类文明在短短时间内就会被摧毁，无数生命在末日中陨落。这不是人为，而是天灾，只怕根本躲都躲不开，这是一次末日大劫，谁都逃不过。” \n" +
                " \n" +
                "    易天行心中暗自苦笑。 \n" +
                " \n" +
                "    那震撼的一幕，不断的在脑海中回荡，也知道，这几乎是不可避免的，这不是人为所致，也不是局部性的灾难，而是真正覆盖全球，覆盖整个地球的末日天灾，一旦爆发，没有人能够避免，那铺天盖地的怪物，几乎能摧毁一切。 \n" +
                " \n" +
                "    在那样的天灾下，整个人类文明都会遭受到重创。 \n" +
                " \n" +
                "    只怕无数人类，能千人中存活一人，都是一件值得幸运的事情。 \n" +
                " \n" +
                "    那样的怪物，普通人面对，存活的几率极为微小。 \n" +
                " \n" +
                "    在这样的情况下，只怕军队也要抵挡不住。 \n" +
                " \n" +
                "    易天行深吸一口气，没有迟疑，快速拿出手机，拨打出一个最为熟悉的号码。 \n" +
                " \n" +
                "    “怎么大风越狠，我心越荡” \n" +
                " \n" +
                "    一首熟悉的铃声在耳边响起。 \n" +
                " \n" +
                "    “哥，你怎么突然给我打电话，是不是想我了。”不多时，手机已经接通，在里面传来一阵清脆悦耳的笑声，听的出，对面的主人显得很高兴。 \n" +
                " \n" +
                "    “紫嫣。” \n" +
                " \n" +
                "    易天行听到手机中传来的话音，脸上露出一抹发自内心的温柔，不过，随即就脸色一正，道：“现在我说的话，你给我好好记住，并且立即去办。” \n" +
                " \n" +
                "    “什么事啊，哥！！” \n" +
                " \n" +
                "    赵紫嫣诧异的询问道。 \n" +
                " \n" +
                "    “不要问太多，你立即联系爸妈，然后以最快的速度赶到sh我在家中等你们过来。沿途中不要有任何耽搁。有什么事，等到见面后再说。”易天行快速的开口说道。 \n" +
                " \n" +
                "    赵紫嫣听到，明显感觉到有些不对，似乎感觉有什么大事要发生一样，出于信任，并没有多问，只是说道：“哥，爸妈在研究所中，之前通过电话，正在进行一项重要研究，已经进行到关键阶段，一旦开始，和外界的联系都会中断，这次研究的时间，只怕要两三个月。现在根本联系不到他们。研究所的位置也是军事机密，我也不知道在哪里。” \n" +
                " \n" +
                "    “在研究所？” \n" +
                " \n" +
                "    易天行听到，也是无奈，父母所在的研究所是军事机密，而且，研究所的位置，根本就不是在地面，而是建立在底下，里面的安保，极为严密，哪怕是顶尖的特工都未必能轻易的进入其中，在研究所中，所有通讯全部都是屏蔽的。不到特定时候，外界是无法进行联系。 \n" +
                " \n" +
                "    而且，进入研究所后，呆的的时间长短，都是无法确定的，但现在，这短短一两天时间内，肯定没有办法联系上。好在，在研究所，也极为安全，完全可以等到以后前去寻找营救。 \n" +
                " \n" +
                "    “爸妈的事情你暂时不用管，以后再说，你立即以最快的速度赶回来，不能超过两天。必须回来。”易天行果决的说到。 \n" +
                " \n" +
                "    赵紫嫣听到后，虽然不解，但也答应尽快赶回来。\n\n" +
                "\n" +
                "亲,点击进去,给个好评呗,分数越高更新越快,据说给新笔趣阁打满分的最后都找到了漂亮的老婆哦!手机站全新改版升级地址：http://m.xbiquge.la，数据和书签与电脑站同步，无广告清新阅读！";
        System.out.println(str.replace("\n \n","$%").replace("$%","\n"));
    }
}
