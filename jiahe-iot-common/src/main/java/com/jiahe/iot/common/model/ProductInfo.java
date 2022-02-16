package com.jiahe.iot.common.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

    private String productId;

    private String productCode;

    private String pid;

    private String productKey;

    private String productName;
    /**
     * 设备类型Id
     */
    private String categoryId;
    /**
     * 设备通讯类型: ble，wifi/eth
     */
    private String networking;
    /**
     * 物理模型
     */
    private List<ThingMode> thingModel;

    private String createTime;

    private String resetIndex;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getNetworking() {
        return networking;
    }

    public void setNetworking(String networking) {
        this.networking = networking;
    }

    public List<ThingMode> getThingModel() {
        return thingModel;
    }

    public void setThingModel(List<ThingMode> thingModel) {
        this.thingModel = thingModel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getResetIndex() {
        return resetIndex;
    }

    public void setResetIndex(String resetIndex) {
        this.resetIndex = resetIndex;
    }

    @Data
    public static class ThingMode {
        public String dpid;
        public String name;
        public String identifier;
        public String speechCode;
        public String modleType;
        public List<Map<String, Object>> upData;
        public List<Map<String, Object>> downData;

        public ThingMode() {
        }

        public String getDpid() {
            return dpid;
        }

        public void setDpid(String dpid) {
            this.dpid = dpid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getSpeechCode() {
            return speechCode;
        }

        public void setSpeechCode(String speechCode) {
            this.speechCode = speechCode;
        }

        public String getModleType() {
            return modleType;
        }

        public void setModleType(String modleType) {
            this.modleType = modleType;
        }

        public List<Map<String, Object>> getUpData() {
            return upData;
        }

        public void setUpData(List<Map<String, Object>> upData) {
            this.upData = upData;
        }

        public List<Map<String, Object>> getDownData() {
            return downData;
        }

        public void setDownData(List<Map<String, Object>> downData) {
            this.downData = downData;
        }
    }
}
