package in.hocg.boot.cps.autoconfiguration.impl.dataoke.lib;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @projectName:openapi
 * @author:
 * @createTime: 2019/04/24 14:55
 * @description:
 */
@Deprecated
public class HttpUtils {

    public static String doGet(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String sendGet(String getUrl, Map<String, String> paraMap) throws RuntimeException {
        if (paraMap == null) {
            paraMap = new HashMap<>();
        }
        paraMap = new TreeMap<>(paraMap);
        StringBuilder sb = new StringBuilder();
        paraMap.forEach((key, value) -> {
            sb.append(key);
            sb.append("=");
            sb.append(HttpUtil.encodeStr(value));
            sb.append("&");
        });
        getUrl = getUrl.contains("?") ? getUrl : getUrl + "?";
        return doGet(getUrl + sb.toString());
    }
}
