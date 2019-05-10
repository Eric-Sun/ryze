package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.j13.poppy.config.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class IAcsClientService {
    private static Logger LOG = LoggerFactory.getLogger(IAcsClientService.class);
    private IAcsClient client = null;
    private String accessKeyId = null;
    private String accessKeySecret = null;

    private static long EXPIRE_TIME = 3600l * 1000;

    @Autowired
    PropertiesConfiguration propertiesConfiguration;

    @PostConstruct
    public void init() {
        accessKeyId = propertiesConfiguration.getStringValue("ossclient.accessKeyId");
        accessKeySecret = propertiesConfiguration.getStringValue("ossclient.accessKeySecret");
        IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret);
        client = new DefaultAcsClient(profile);
        LOG.info("IAcsClientService init successfully.");
    }


    /**
     * 检测文本
     *
     * @param content
     * @return
     */
    public boolean scan(String content) {

        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId("cn-shanghai");
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
        /**
         * 待检测的文本，长度不超过10000个字符
         */
        task1.put("content", content);
        tasks.add(task1);
        JSONObject data = new JSONObject();

        /**
         * 检测场景，文本垃圾检测传递：antispam
         **/
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        LOG.debug(JSON.toJSONString(data, true));
        try {
            textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
            // 请务必设置超时时间
            textScanRequest.setConnectTimeout(3000);
            textScanRequest.setReadTimeout(6000);
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
                LOG.debug(JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if (200 == ((JSONObject) taskResult).getInteger("code")) {
                            JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject) sceneResult).getString("scene");
                                String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                                //根据scene和suggetion做相关处理
                                //suggestion == pass 未命中垃圾  suggestion == block 命中了垃圾，可以通过label字段查看命中的垃圾分类
                                LOG.debug("args = [" + scene + "]");
                                LOG.debug("args = [" + suggestion + "]");
                                if (suggestion.equals("block")) {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        } else {
                            LOG.debug("task process fail:" + ((JSONObject) taskResult).getInteger("code"));
                        }
                    }
                } else {
                    LOG.debug("detect not success. code:" + scrResponse.getInteger("code"));
                }
            } else {
                LOG.debug("response not success. status:" + httpResponse.getStatus());
            }
        } catch (ServerException e) {
            LOG.error(e.getMessage());
        } catch (ClientException e) {
            LOG.error(e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return true;
    }
}
