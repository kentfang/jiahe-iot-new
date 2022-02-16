package com.jiahe.iot.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AreaInfo {

    private String areaId;
    private String areaName;
    private String userId;
    private String createTime;
    /**
     * 区域类型
     * 0：代表默认区域
     */
    private int areaType;

}
