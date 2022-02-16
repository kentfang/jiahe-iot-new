package com.jiahe.iot.device.open.facade;


import com.jiahe.iot.common.model.DsInfo;
import com.jiahe.iot.device.dao.DsInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class DsInfoFacadeImpl implements DsFacade {

    @Autowired
    DsInfoDao dsInfoDao;

    @Override
    public DsInfo getDsInfo(@RequestBody Map<String, String> getdsinfo) {
        if (getdsinfo != null && !getdsinfo.isEmpty()) {
            DsInfo one = dsInfoDao.find(getdsinfo);
            return one;
        }
        return null;
    }

}
