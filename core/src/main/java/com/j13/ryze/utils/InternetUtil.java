package com.j13.ryze.utils;

import com.google.common.collect.Lists;
import com.j13.ryze.core.ErrorCode;
import com.j13.poppy.RequestParams;
import com.j13.poppy.exceptions.CommonException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InternetUtil {
    private static Logger LOG = LoggerFactory.getLogger(InternetUtil.class);

    public static String get(String url) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        try {
            HttpPost httpGet = new HttpPost(url);
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
                    "Accept-Encoding: gzip, deflate, br");
            httpGet.setHeader("Cookie", "_qqq_uuid_=\"2|1:0|10:1558581654|10:_qqq_uuid_|56:Y2I5ZTQ0NDMwMmRjZDg1Yzc3MzBmM2IzYjlhNzA5OTZiZGNhZWE0Zg==|f8d1d14f8b70e5d63d1645e07a86bd38865aa4a2c625b52df69804f0d8ac94a7\"; _ga=GA1.2.1642137361.1558581655; __cur_art_index=3100; Hm_lvt_18a964a3eb14176db6e70f1dd0a3e557=1558598327; _xsrf=2|eadcd414|757fe4c54ae33b07786b9c7fbeb3a8a7|1558931221; Hm_lvt_2670efbdd59c7e3ed3749b458cafaa37=1558581655,1558595632,1558931224; Hm_lpvt_2670efbdd59c7e3ed3749b458cafaa37=1558940281");
            httpGet.setHeader("If-None-Match:", "da9de8206a798ade7c1979cec436380c26e210d4");
            httpGet.setHeader("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 301) {
                return null;
            }
            String rawResponse = EntityUtils.toString(entity);
            return rawResponse;
        } catch (IOException e) {
            LOG.error("url={}", url, e);
            throw new CommonException(ErrorCode.System.SYSTEM_ERROR);
        } finally {
        }
    }



    public static String post(String url, Map<String, Object> params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = Lists.newLinkedList();

            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                if (params.get(key) != null)
                    nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
            }


            HttpEntity httpEntity = new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8"));

            httpPost.setEntity(httpEntity);

            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String rawResponse = EntityUtils.toString(entity);
            return rawResponse;
        } catch (IOException e) {
            LOG.error("url={}", url, e);
            throw new CommonException(ErrorCode.System.SYSTEM_ERROR);
        } finally {

            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("url={}", url, e);
                    throw new CommonException(ErrorCode.System.SYSTEM_ERROR);
                }

            }
            try {
                httpclient.close();
            } catch (IOException e) {
                LOG.error("url={}", url, e);
                throw new CommonException(ErrorCode.System.SYSTEM_ERROR);
            }
        }
    }


    public static String post(String url, String data) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity content = new StringEntity(data, Charset.forName("utf-8"));
            httpPost.setEntity(content);
            content.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
            httpPost.setHeader("Cookie", "PHPSESSID=hsjm32j9hk574k9tsdk7brqco2; webcms_session=a%3A6%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%222c681358e4f090fc6c2b5ff5488957e4%22%3Bs%3A10%3A%22ip_address%22%3Bs%3A13%3A%22123.57.117.69%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A120%3A%22Mozilla%2F5.0+%28Macintosh%3B+Intel+Mac+OS+X+10_12_4%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F59.0.3071.115+Safari%2F537.3%22%3Bs%3A13%3A%22last_activity%22%3Bi%3A1500340255%3Bs%3A9%3A%22user_data%22%3Bs%3A0%3A%22%22%3Bs%3A14%3A%22participant620%22%3Bi%3A1%3B%7D44d39a8647ebfb0240f20d255ab5f338; UM_distinctid=15d533f8938528-073654ff8a5ef2-30677808-fa000-15d533f8939acf; CNZZDATA1262242692=1152692324-1500336817-%7C1500336817");
            content.setContentEncoding("utf-8");
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String rawResponse = EntityUtils.toString(entity);
            return rawResponse;
        } catch (IOException e) {
            LOG.error("url={}", url, e);
            throw new CommonException(ErrorCode.System.SYSTEM_ERROR);
        } finally {

            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("url={}", url, e);
                    throw new CommonException(ErrorCode.System.SYSTEM_ERROR);
                }

            }
            try {
                httpclient.close();
            } catch (IOException e) {
                LOG.error("url={}", url, e);
                throw new CommonException(ErrorCode.System.SYSTEM_ERROR);
            }
        }
    }

    public static String post(String url, RequestParams p) {
        return InternetUtil.post(url, p.end());
    }


    public static void main(String[] args) {
        String s = "token=9f2cb8f141b2";
        String data = InternetUtil.post("http://www.luckybabywudao.cn/index.php/welcome/vote/vXAKaPVngEzl", s);
        System.out.println(data);

    }
}
