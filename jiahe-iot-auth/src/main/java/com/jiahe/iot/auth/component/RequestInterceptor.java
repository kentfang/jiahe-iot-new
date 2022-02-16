package com.jiahe.iot.auth.component;


import com.alibaba.fastjson.JSONObject;
import com.jiahe.iot.auth.Utils;
import com.jiahe.iot.common.bean.Response;
import com.jiahe.iot.common.constant.ResultCode;
import com.jiahe.iot.common.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired
    RedisCacheManager redisCacheManager;


    private int maxCount = 1;
    private int seconds = 2;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = Utils.getIpAddr(request);
        String local = IpUtil.getIpAddress();
//        log.info("local:"+local);
        if (ip.equals("127.0.0.1") || ip.equals("47.119.195.237") || IpUtil.getIpAddress().equals(ip)) {
            return true;
        }
        String key = request.getServletPath() + ":" + ip;
        String s = redisCacheManager.get(key);
        Integer count = 0;
        if (s != null) {
            count = Integer.valueOf(s);
        }

        if (count < maxCount) {
            count = count + 1;
            redisCacheManager.set(key, count + "", seconds);
            return true;
        }
        if (count >= maxCount) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            Response result = Response.builder().code(ResultCode.request_too_more).message("请求太频繁").build();
            Object obj = JSONObject.toJSON(result);
            response.getWriter().write(JSONObject.toJSONString(obj));
            return false;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
