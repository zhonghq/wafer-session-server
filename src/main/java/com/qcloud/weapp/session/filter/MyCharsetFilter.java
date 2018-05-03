package com.qcloud.weapp.session.filter;


import javax.servlet.*;
import java.io.IOException;


/**
 * @author zhonghongqiang
 *         Created on 2018-05-02.
 */
public class MyCharsetFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("MyCharSetFilter初始化");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json; charset=UTF-8");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    public void destroy() {

    }
}
