package com.jiahe.iot.common.bean;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrData {
    String err;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
