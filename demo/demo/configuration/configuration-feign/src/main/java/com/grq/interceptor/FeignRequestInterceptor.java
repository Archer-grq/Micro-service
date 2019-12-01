package com.grq.interceptor;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Enumeration;

/**
 * Feign拦截器
 *
 * 用于设置请求头，传递 Token
 * 并将其他信息原样封装
 *
 * 在使用 Feign 请求资源服务器时需要携带 token 信息
 * 如果没有 token 将没有权限访问 资源服务器
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //获取request对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        //设置请求头
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                requestTemplate.header(name,value);
            }
        }

        //设置请求体，这里主要为了传递 token
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuilder body =new StringBuilder();
        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                String value = request.getParameter(name);

                //将 token 加入请求头
                if("access_token".equals(name)){
                    requestTemplate.header("authorization", "Bearer " + value);

                }

                //其他加入请求体
                else {
                    body.append(name).append('=').append(value).append('&');
                }
            }
        }

        //设置请求体
        if(body.length()>0){
            //去掉最后一位 &
            body.deleteCharAt(body.length()-1);
            requestTemplate.body(Request.Body.bodyTemplate(body.toString(), Charset.defaultCharset()));
        }
    }
}
