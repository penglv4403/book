package com.orange.book.httpClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.List;


import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HttpClientUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtils.class);

    public synchronized static Page sendRequstAndGetResponse(String url) {
        HttpGet httpGet = new HttpGet(url);
        //HttpHost proxy = new HttpHost("116.209.55.39", 9999, "http");//.setProxy(proxy).
        httpGet.setConfig(RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build());
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseStr = "";
        try {
            httpClient = HttpClientBuilder.create().build();
            HttpClientContext context = HttpClientContext.create();
            response = httpClient.execute(httpGet, context);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.error("Method failed: " + response.getStatusLine());
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseStr = EntityUtils.toString(entity, "utf-8");
            }
            // 4.处理 HTTP 响应内容
            System.out.println(responseStr);
        } catch (Exception e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } finally {
            // 释放连接
            //getMethod.releaseConnection();
        }
        return null;
    }

    public synchronized static Page httpGet(String url) {
        Page page = null;
        // 通过httpClient获取网页响应,将返回的响应解析为纯文本119.102.132.134	9999
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build());

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseStr = "";
        try {
            httpClient = new SSLClient();
            HttpClientContext context = HttpClientContext.create();
            response = httpClient.execute(httpGet, context);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.error("Method failed: " + response.getStatusLine());
            }
            HttpEntity entity = response.getEntity();
            boolean isGzip = false;
            String contentType = "";
            for (Header header : response.getAllHeaders()) {
                if ("Content-Type".equals(header.getName())) {
                    contentType = header.getValue();
                }
                if("gzip".equals(header.getValue())){
                    isGzip = true;
                }
            }

            if (entity != null){
                if(isGzip){
                    //需要进行gzip解压处理
                    responseStr = EntityUtils.toString(new GzipDecompressingEntity(entity),"UTF-8");
                }else{
                    responseStr = EntityUtils.toString(entity,"UTF-8");
                }

            }
            page = new Page(responseStr.getBytes("UTF-8"), url, contentType); // 封装成为页面

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return page;
    }

}
