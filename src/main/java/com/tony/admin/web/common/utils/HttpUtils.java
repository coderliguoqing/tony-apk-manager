package com.tony.admin.web.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 处理http请求
 *
 * @author: LiHongxing
 * @date: Create in 2018-01-29 9:42
 * @modefied:
 */
public class HttpUtils {

    private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();

    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0";

    private HttpUtils() {}

    private static HttpEntity makeMultipartEntity(Map<String, String> params, final Map<String, File> files) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); //如果有SocketTimeoutException等情况，可修改这个枚举

        //builder.setCharset(Charset.forName("UTF-8"));
        //不要用这个，会导致服务端接收不到参数

        if (!ObjectUtils.isEmpty(params) && params.size() > 0) {
            for (Entry<String, String> p : params.entrySet()) {
                builder.addTextBody(p.getKey(), p.getValue(), ContentType.TEXT_PLAIN.withCharset("UTF-8"));
            }
        }

        if (files != null && files.size() > 0) {
            Set<Entry<String, File>> entries = files.entrySet();
            for (Entry<String, File> entry : entries) {
                builder.addPart(entry.getKey(), new FileBody(entry.getValue()));
            }
        }
        return builder.build();
    }

    /**
     * 发送post请求
     *
     * @param url 请求路径
     * @param params 请求参数string -> string
     * @param files 上传文件集合
     * @param headers 头设置
     * @throws IOException
     */
    public static HttpResponse sendPost(String url, Map<String, String> params, final Map<String, File> files, Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        /* set header */
        if (!ObjectUtils.isEmpty(headers)) {
            for (Entry<String, String> header : headers.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
            }
        }
        HttpEntity entity = makeMultipartEntity(params, files);
        httpPost.setEntity(entity);

        HttpResponse httpResponse = HTTP_CLIENT.execute(httpPost);
        return httpResponse;
    }

    public static String getResponseStr(HttpResponse httpResponse, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), charset));
        String inputLine;
        StringBuffer sb = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            sb.append(inputLine);
        }
        reader.close();
        String responseStr = sb.toString();
        return responseStr;
    }

    public static HttpResponse sendGET(String url, Map<String, String> headers) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        /* set header */
        if (!ObjectUtils.isEmpty(headers)) {
            for (Entry<String, String> header : headers.entrySet()) {
                httpGet.setHeader(header.getKey(), header.getValue());
            }
        }
        HttpResponse httpResponse = HTTP_CLIENT.execute(httpGet);
        return httpResponse;
    }

    public static String defaultUserAgent() {
        return DEFAULT_USER_AGENT;
    }
}
