package com.qcloud.weapp.session.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * session 工具类
 *
 * @author zhonghongqiang
 * @date 2017-05-26
 */
public class RequestUtil {

    /**
     * 获取输入流的字符信息
     * @param request
     * @return
     * @throws IOException
     */
    public static String getInputStreamStr(HttpServletRequest request) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
        StringBuilder sb = new StringBuilder("");
        String temp;
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        br.close();
        return sb.toString();
    }

}
