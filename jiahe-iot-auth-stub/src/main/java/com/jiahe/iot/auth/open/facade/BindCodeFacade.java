package com.jiahe.iot.auth.open.facade;

import com.jiahe.iot.common.bean.BindCodeBean;
import com.jiahe.iot.common.bean.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;


@Component
@FeignClient("jiahe-auth")
@RequestMapping(value = "/jiahe-auth/bindCode/facade/BindCodeFacade")
public interface BindCodeFacade {
    @RequestMapping(value = "/bindCode/get")
    public Response getBindCode( BindCodeBean bindCodeBean) throws Exception;

}
