package com.qcloud.weapp.session.utils;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author zhong
 * @date 2018/4/29
 */
public class UUIDUtils {
    /**
     * 获取唯一编码
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
