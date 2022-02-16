package com.jiahe.iot.auth.component;

import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;

public class MyMqttPahoMessageDrivenChannelAdapter extends MqttPahoMessageDrivenChannelAdapter {
    public MyMqttPahoMessageDrivenChannelAdapter(String url, String clientId, MqttPahoClientFactory clientFactory, String... topic) {
        super(url, clientId, clientFactory, topic);
    }

    public MyMqttPahoMessageDrivenChannelAdapter(String clientId, MqttPahoClientFactory clientFactory, String... topic) {
        super(clientId, clientFactory, topic);
    }

    public MyMqttPahoMessageDrivenChannelAdapter(String url, String clientId, String... topic) {
        super(url, clientId, topic);
    }


    @Override
    protected void doStart() {

    }


    public void myDoStart() {
        super.doStart();
    }
}
