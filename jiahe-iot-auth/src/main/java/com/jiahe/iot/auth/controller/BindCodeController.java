package com.jiahe.iot.auth.controller;


import com.alibaba.fastjson.JSON;
import com.jiahe.iot.auth.component.RedisCacheManager;
import com.jiahe.iot.auth.open.facade.BindCodeFacade;
import com.jiahe.iot.common.bean.BindCodeBean;
import com.jiahe.iot.common.bean.Response;
import com.jiahe.iot.common.constant.CacheKeys;
import com.jiahe.iot.common.constant.ResultCode;
import com.jiahe.iot.common.util.JwtUtils;
import com.jiahe.iot.common.util.UniqueIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class BindCodeController implements BindCodeFacade {

    @Autowired
    RedisCacheManager redisCacheManager;

    @Override
    public Response getBindCode( BindCodeBean bindCodeBean) throws Exception {
        log.info("getBindCode:" + JSON.toJSONString(bindCodeBean));
        if (bindCodeBean == null || bindCodeBean.getType() == null || bindCodeBean.getType().equals("")) {
            return Response.builder().code(ResultCode.fail).message("参数缺失").build();
        }
        if (bindCodeBean.getType().equals("phone")) {
            String token = bindCodeBean.getToken();
            if (token == null || bindCodeBean.getToken().equals("")) {
                return Response.builder().code(ResultCode.fail).message("token参数缺失").build();
            }
            /**
             * jwt 校验，表示合法的token
             */
            String object = JwtUtils.verifyToken(token);
            if (object != null) {
                String s = UniqueIdUtil.newRamdomString(6);
                Map<String, String> data = new HashMap<>();
                data.put("bindcode", s);
                redisCacheManager.set(CacheKeys.getBindeCodeKey(s), token, 5);
                return Response.builder().code(ResultCode.suc).data(data).build();
            }
        }
        return Response.builder().code(ResultCode.fail).message("token参数缺失").build();
    }
}
