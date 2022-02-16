package com.jiahe.iot.common.bean;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceMessage {

    /**
     * 消息类型
     * 设备上下线：deviceOnlineType
     * 设备属性变更：devicePropertyType
     * 设备属性变更：deviceEventType
     * 设备属性变更：deviceSeviceType
     */
    private DeviceMessageType messageType;
    private String traceId;
    private String deviceName;
    private String productKey;
    private String requestId;
    private String identifier;
    private boolean success;
    private Map<String, Object> value = new HashMap<>();


    public DeviceMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(DeviceMessageType messageType) {
        this.messageType = messageType;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }
}
