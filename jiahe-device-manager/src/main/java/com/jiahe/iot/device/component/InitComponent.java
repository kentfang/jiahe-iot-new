package com.jiahe.iot.device.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitComponent implements ApplicationRunner, ApplicationListener<ContextRefreshedEvent> {
    private ApplicationContext context;


    @Override
    public void run(ApplicationArguments args) throws Exception {
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.context = contextRefreshedEvent.getApplicationContext();
    }
}
