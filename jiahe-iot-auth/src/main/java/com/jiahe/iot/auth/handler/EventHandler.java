package com.jiahe.iot.auth.handler;


import com.alibaba.fastjson.JSON;
import com.jiahe.iot.common.bean.Request;
import com.jiahe.iot.common.bean.Response;
import com.jiahe.iot.common.constant.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@TopicHandler
@RestController
public class EventHandler {

    @RequestMapping("/sys/{productKey}/{deviceName}/thing/event/{identifier}/post")
    public Response<?> eventPost(
            @PathVariable("productKey") String productKey,
            @PathVariable("deviceName") String deviceName,
            @PathVariable("identifier") String identifier,
            @RequestBody EventRequest request) {
        log.info("EventHandler request:" + JSON.toJSONString(request));
        if (identifier.equals("online") || identifier.equals("offline")) {
            return null;
        }

        return Response.builder().code(ResultCode.suc).id(request.getId()).build();
    }

    static class EventRequest extends Request<Map<String, Object>> {

    }
}
