package com.jiahe.iot.common.constant;

public final class CacheKeys {

    public static String getTokenKey(String token) {
        return "str:iot:token:" + token;
    }

    public static String getCodeKey(String code) {
        return "str:iot:code:" + code;
    }

    public static String getBindeCodeKey(String code) {
        return "str:iot:bindcode:" + code;
    }

    public static String getProductThings(String productKey) {
        return "str:iot:product:things:" + productKey;
    }

    public static String getCategoryThings(String categoryId) {
        return "str:iot:category:things:" + categoryId;
    }


}
