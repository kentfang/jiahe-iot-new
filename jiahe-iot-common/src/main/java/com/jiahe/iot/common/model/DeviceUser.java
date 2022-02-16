package com.jiahe.iot.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUser {

    private String deviceName;
    private String userId;
    private String nickName;
    private String areaId;
}
