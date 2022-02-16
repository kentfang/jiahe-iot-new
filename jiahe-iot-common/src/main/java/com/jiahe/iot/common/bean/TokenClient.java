package com.jiahe.iot.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenClient {

    /**
     * 1 代表 设备
     * 2 代表 user
     */
    private int type;

    private String object;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
