package com.jiahe.iot.auth.handler;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface TopicHandler {
    String value() default "";
}