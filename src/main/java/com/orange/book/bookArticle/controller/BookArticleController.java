package com.orange.book.bookArticle.controller;

import com.orange.book.bookArticle.spider.BookArticleSpider;
import com.orange.book.bookArticle.util.BookArticleUtil;
import com.orange.book.bookContent.service.BookContentService;
import com.orange.book.schedule.BookSchedule;
import com.orange.book.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;

@Controller
public class BookArticleController {
    @Autowired
    private BookArticleSpider bookArticleSpider;
    @Autowired
    private BookArticleUtil bookArticleUtil;

    @Autowired
    private BookContentService bookContentService;
    @Value("bookPath")
    private String bookPath;

    @RequestMapping("/startArticle")
    @ResponseBody
    public String startArticle() {
        bookArticleSpider.getArticle("http://www.xbiquge.la/xiaoshuodaquan/");
        return "1";
    }


    @RequestMapping("/updateArticle")
    @ResponseBody
    public String updateArticle() throws ParseException {
        bookArticleUtil.updateArticle();
        return "2";
    }

    @RequestMapping("/downloadFile")
    @ResponseBody
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
       // String downloadFilePath = "/home/app/file/";//被下载的文件在服务器中的路径,
        String bookName = request.getParameter("bookName");
        String fileName = bookName + ".txt";//被下载文件的名称
        bookPath="C:\\\\Users\\\\ME\\\\Desktop\\";
        File file = new File(bookPath + fileName);
        bookContentService.getBook(bookName,bookPath + fileName);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }

                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "下载失败";
    }


}
