package com.jiahe.iot.auth.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

@Data
public class HandlerMethodInfo {

    private Object owner;

    private String path;

    private Method method;

    private List<HandlerParam> params;

    private boolean withPathVariable;

    public Object invoke(Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (owner == null) {
            return null;
        }
        return method.invoke(owner, args);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HandlerParam {
        private boolean isPathVariable;
        private boolean isRequestBody;
        private String name;
        private Type type;
        private Object value;
    }
}
