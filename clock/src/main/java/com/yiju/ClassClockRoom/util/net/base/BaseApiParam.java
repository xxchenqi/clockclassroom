package com.yiju.ClassClockRoom.util.net.base;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by geliping on 2016/8/11.
 */
public abstract class BaseApiParam {

    /**
     * 创建请求参数（String）
     */
    private static RequestBody createRequestBody(String param) {
        return RequestBody.create(MediaType.parse("text/plain"), param);
    }

    /**
     * 创建请求参数（图片）
     */
    private static RequestBody createRequestBody(File param) {
        return RequestBody.create(MediaType.parse("image/*"), param);
    }

    /**
     * 创建请求参数（文件）
     */
    private static RequestBody createRequestBody(String type, File param) {
        return RequestBody.create(MediaType.parse(type), param);
    }

    /**
     * 获得文件上传的key
     */
    private static String getUpLoadFileKey(String key) {
        return key + "\"; filename=\"" + key + ".jpg" + "";
    }

    protected static class PostParams {
        Map<String, RequestBody> map;

        public PostParams() {
            map = new HashMap<>();
        }

        public Map<String, RequestBody> getMap() {
            return map;
        }

        public void put(String key, String value) {
            map.put(key, createRequestBody(value));
        }

        public void put(String key, File value) {
            map.put(getUpLoadFileKey(key), createRequestBody(value));
        }

        public void put(String key, String type, File value) {
            map.put(getUpLoadFileKey(key), createRequestBody(type, value));
        }
    }
}
