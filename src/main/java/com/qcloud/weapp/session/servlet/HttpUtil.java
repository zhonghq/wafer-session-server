package com.qcloud.weapp.session.servlet;

import com.qcloud.weapp.session.utils.MyX509TrustManager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * https请求工具类
 *
 * @author zhong
 * @date 2018/4/28
 */
public class HttpUtil {

    /**
     * 处理网络请求
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式 值为"GET"或"POST"
     * @param outputStr
     * @return
     */
    public static String request(String requestUrl,String requestMethod,String outputStr){
        try {
            URL url = new URL(requestUrl);
            if ("HTTPS".equals(url.getProtocol().toUpperCase())){
                return httpsRequest(requestUrl,requestMethod,outputStr);
            }else {
                return httpRequest(requestUrl,requestMethod,outputStr);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("请求地址错误:"+requestUrl);
        }
    }

    /**
     * 处理http请求
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式 值为"GET"或"POST"
     * @param outputStr
     * @return
     */
    public static String httpRequest(String requestUrl,String requestMethod,String outputStr){
        StringBuffer buffer=null;
        try{
            URL url=new URL(requestUrl);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(requestMethod);
            conn.connect();
            //往服务器端写内容 也就是发起http请求需要带的参数
            if(null!=outputStr){
                OutputStream os=conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            //读取服务器端返回的内容
            InputStream is=conn.getInputStream();
            InputStreamReader isr=new InputStreamReader(is,"utf-8");
            BufferedReader br=new BufferedReader(isr);
            buffer=new StringBuffer();
            String line=null;
            while((line=br.readLine())!=null){
                buffer.append(line);
            }
            if (conn.getResponseCode() != 200){
                throw new RuntimeException("Http请求失败");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 处理https
     * @param requestUrl 请求地址
     * @param requestMethod GET/POST请求
     * @param outputStr
     * @return
     */
    public static String httpsRequest(String requestUrl,String requestMethod,String outputStr){
        StringBuffer buffer=null;
        try{
            //创建SSLContext
            SSLContext sslContext=SSLContext.getInstance("SSL");
            TrustManager[] tm={new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());;
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            URL url=new URL(requestUrl);
            HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            //设置当前实例使用的SSLSoctetFactory
            conn.setSSLSocketFactory(ssf);
            conn.connect();
            //往服务器端写内容
            if(null!=outputStr){
                OutputStream os=conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            //读取服务器端返回的内容
            InputStream is=conn.getInputStream();
            InputStreamReader isr=new InputStreamReader(is,"utf-8");
            BufferedReader br=new BufferedReader(isr);
            buffer=new StringBuffer();
            String line=null;
            while((line=br.readLine())!=null){
                buffer.append(line);
            }
            if (conn.getResponseCode() != 200){
                throw new RuntimeException("Http请求失败");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }

}
