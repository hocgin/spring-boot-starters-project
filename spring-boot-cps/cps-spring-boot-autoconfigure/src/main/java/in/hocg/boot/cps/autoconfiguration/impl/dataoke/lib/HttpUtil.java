package in.hocg.boot.cps.autoconfiguration.impl.dataoke.lib;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class HttpUtil {
    private static PoolingHttpClientConnectionManager cm;
    private static String EMPTY_STR = "";
    private static String UTF_8 = "UTF-8";
    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            // 整个连接池最大连接数
            cm.setMaxTotal(1000);
            // 每路由最大连接数，默认值是2
            cm.setDefaultMaxPerRoute(cm.getMaxTotal());
        }
    }

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom()
                .setRetryHandler(new HttpRequestRetryHandler(){
                    @Override
                    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                        if (executionCount >= 1) {
                            // Do not retry if over max retry count
                            return false;
                        }
                        return true;
                    }
                })
                .setConnectionManager(cm).setConnectionManagerShared(true).build();
    }

    /**
     * @param url
     * @return
     */
    public static String httpGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000)   //设置连接超时时间
                .setConnectionRequestTimeout(5000) // 设置请求超时时间
                .setSocketTimeout(5000)
                .setRedirectsEnabled(false)//默认允许自动重定向
                .build();
        httpGet.setConfig(config);
        httpGet.setHeader("Accept", "application/json");
        httpGet.addHeader("Accept-Encoding" ,"gzip"); //请求使用数据压缩
        return getResult(httpGet);
    }

    public static String httpGetRequest(String url, Map<String, String> params) throws URISyntaxException {
        String paramsStr = covertParamsForStr(params);
        url = url.contains("?") ? url : url + "?";
        HttpGet httpGet = new HttpGet(url + paramsStr);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000)   //设置连接超时时间
                .setConnectionRequestTimeout(5000) // 设置请求超时时间
                .setSocketTimeout(5000)
                .setRedirectsEnabled(false)//默认允许自动重定向
                .build();
        httpGet.setConfig(config);
        httpGet.setHeader("Accept", "application/json");
        httpGet.addHeader("Accept-Encoding" ,"gzip"); //请求使用数据压缩
        return getResult(httpGet);
    }

    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);
        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet);
    }

    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url,String json) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json,"utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

        return getResult(httpPost);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return pairs;
    }

    private static ArrayList<NameValuePair> covertParams2NVPSForStr(Map<String, String> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            try {
                pairs.add(new BasicNameValuePair(param.getKey(), URLEncoder.encode(param.getValue(), "utf-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return pairs;
    }

    private static String covertParamsForStr(Map<String, String> paraMap) {
        if(paraMap == null){
            paraMap = new HashMap<>();
        }
        paraMap= new TreeMap<>(paraMap);
        StringBuilder sb = new StringBuilder();
        paraMap.entrySet().stream().forEach(entry ->{
            sb.append(entry.getKey());
            sb.append("=");
            try {
                sb.append(URLEncoder.encode(entry.getValue(),"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append("&");
        });
        return sb.toString();
    }

    /**
     * 处理Http请求
     */
    private static String getResult(HttpRequestBase request) {
        CloseableHttpClient httpClient = getHttpClient();
        String result = "";
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
            response.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }finally {
            try{
                if(httpClient != null){
                    httpClient.close(); //释放资源
                }
            }catch (Exception e){

            }
        }
        return result;
    }
}
