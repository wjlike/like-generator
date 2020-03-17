package com.like.generate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import net.sf.json.util.JSONUtils;


/**
 * Created by heyangyang on 2016/5/17.
 */
public class HttpClientUtil {

    private static Log logger = LogFactory.getLog(HttpClientUtil.class);


    private static Map<String, String> headers = new HashMap<String, String>();

    // 连接超时时间
    private static final int httpConnectTimeOut = 5000;
    // 读取超时时间
    private static final int httpReadtimeOut = 10000;

    static {
        headers.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)");
        headers.put("Accept-Language", "zh-cn,zh;q=0.5");
        headers.put("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        headers.put("Accept", " image/gif, image/x-xbitmap, image/jpeg, "
                + "image/pjpeg, application/x-silverlight, application/vnd.ms-excel, "
                + "application/vnd.ms-powerpoint, application/msword, application/x-shockwave-flash, */*");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept-Encoding", "gzip, deflate");
    }

    public static String httpPost(String url, Map<String, Object> params) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String body = null;
        HttpPost post = postForm(url, params);
        body = invoke(httpclient, post);
        httpclient.getConnectionManager().shutdown();
        return body;
    }


    private static String invoke(DefaultHttpClient httpclient,
                                 HttpUriRequest httpost) {
        HttpResponse response = sendRequest(httpclient, httpost);
        String body = paseResponse(response);
        return body;
    }

    private static String paseResponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        String charset = EntityUtils.getContentCharSet(entity);
        String body = null;
        try {
            body = EntityUtils.toString(entity);
            // log.info(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    private static HttpResponse sendRequest(DefaultHttpClient httpclient,
                                            HttpUriRequest httpost) {
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static HttpPost postForm(String url, Map<String, Object> params) {
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key) == null ? "" : params.get(key).toString()));
        }
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpost;
    }

    /**
     * 异常或者没拿到返回结果的情况下,result为""
     *
     * @param url
     * @param param
     * @return
     */
    public static String httpPostJson(String url, Map<String, Object> param) {
        logger.info("httpPost URL [" + url + "] start ");
        HttpClient httpclient = null;
        HttpPost httpPost = null;
        HttpResponse response = null;
        HttpEntity entity = null;
        String result = "";
        String postData = JSONUtils.valueToString(param);
        StringBuffer suf = new StringBuffer();
        try {
            httpclient = new DefaultHttpClient();
            httpPost = new HttpPost(url);

            headers.put("Content-Type", "application/json;charset=UTF-8");
            headers.put("Accept", "application/json");

            // 设置各种头信息
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            httpPost.setEntity(new StringEntity(postData, "utf-8"));

            // 设置连接超时时间
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), httpConnectTimeOut);
            // 设置读数据超时时间
            HttpConnectionParams.setSoTimeout(httpPost.getParams(), httpReadtimeOut);

            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("HttpStatus ERROR" + "Method failed: " + response.getStatusLine());
                return "";
            } else {
                entity = response.getEntity();
                if (null != entity) {
                    byte[] bytes = EntityUtils.toByteArray(entity);
                    result = new String(bytes, "UTF-8");
                } else {
                    logger.error("httpPost URL [" + url + "],httpEntity is null.");
                }
                return result;
            }
        } catch (Exception e) {
            logger.error("httpPost URL [" + url + "] error, ", e);
            return "";
        } finally {
            if (null != httpclient) {
                httpclient.getConnectionManager().shutdown();
            }
            logger.info("RESULT:  [" + result + "]");
            logger.info("httpPost URL [" + url + "] end ");
        }
    }


}
