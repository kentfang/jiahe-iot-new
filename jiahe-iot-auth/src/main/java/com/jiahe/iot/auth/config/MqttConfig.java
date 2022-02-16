package com.jiahe.iot.auth.config;


import com.jiahe.iot.auth.component.MyMqttPahoMessageDrivenChannelAdapter;
import com.jiahe.iot.auth.handler.MqttMessageHandler;
import com.jiahe.iot.common.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
public class MqttConfig {
    /**
     * 订阅的bean名称
     */
    public static final String CHANNEL_NAME_IN = "mqttInboundChannel";
    /**
     * 发布的bean名称
     */
    public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.host}")
    private String url;

    @Value("${mqtt.productId}")
    private String productId;

    @Value("${mqtt.consumeId}")
    private String consumeId;

    private String topics[] = {
            //事件上报
            "$queue//sys/+/+/thing/event/+/post",
            //属性上报
            "$queue//sys/+/+/thing/property/up_raw"
    };

    /**
     * MQTT连接器选项
     */
    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        log.info("mqtt.host   " + url);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setServerURIs(new String[]{url});
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(180);
        return options;
    }


    /**
     * MQTT客户端
     *
     * @return {@link MqttPahoClientFactory}
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    /**
     * MQTT信息通道（生产者）
     *
     * @return {@link MessageChannel}
     */
    @Bean(name = CHANNEL_NAME_OUT)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（生产者）
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                productId + StringUtils.leftPad(IpUtil.getIpAddress().split("\\.")[3], 4, '0'),
                mqttClientFactory());
        messageHandler.setAsync(true);
//        messageHandler.setDefaultTopic(topics);
        return messageHandler;
    }

    @Bean("mqttConsumeBean")
    public MyMqttPahoMessageDrivenChannelAdapter inbound() {
        MyMqttPahoMessageDrivenChannelAdapter adapter =
                new MyMqttPahoMessageDrivenChannelAdapter(
                        consumeId + StringUtils.leftPad(IpUtil.getIpAddress().split("\\.")[3], 4, '0'),
                        mqttClientFactory(),
                        topics);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }

    /**
     * MQTT信息通道（消费者）
     *
     * @return {@link MessageChannel}
     */
    @Bean(name = CHANNEL_NAME_IN)
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（消费者）
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler handler(MqttChannel mqttChannel) {
        return new MqttMessageHandler(mqttChannel);
    }

}