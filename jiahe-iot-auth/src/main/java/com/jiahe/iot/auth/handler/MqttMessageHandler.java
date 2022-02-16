package com.jiahe.iot.auth.handler;


import com.jiahe.iot.auth.config.MqttChannel;
import com.jiahe.iot.common.bean.Response;
import com.jiahe.iot.common.constant.ResultCode;
import com.jiahe.iot.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Slf4j
@RestController
public class MqttMessageHandler implements MessageHandler, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final List<HandlerMethodInfo> topicHandlerMethods = new ArrayList<>();

    private final MqttChannel mqttChannel;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        try {
            String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
            if (topic == null) {
                log.warn("topic is null");
                return;
            }
            Object payload = message.getPayload();
            if (!(payload instanceof String)) {
                payload = JsonUtil.toJsonString(payload);
            }

            log.info("receive mqtt msg,topic:{},payload:{}", topic, payload);

            HandlerMethodInfo methodInfo = findMethod(topic);
            if (methodInfo == null) {
                return;
            }

            Object[] params = getMethodParams(methodInfo, payload.toString());
            //调用处理方法进行消息处理
            Object result;
            try {
                result = methodInfo.invoke(params);
            } catch (Throwable e) {
                log.error("handler process error", e);
                result = Response.builder().code(ResultCode.fail).build();
            }

            //如果处理返回的结果是响应消息，则将结果进行回复
            String resultJson = result == null ? "" : JsonUtil.toJsonString(result);
            log.info("handler process result:{}", resultJson);
            if (result instanceof Response) {
                mqttChannel.send(topic + "_reply", 1, resultJson);
            }

        } catch (Throwable e) {
            log.error("handleMessage error", e);
        }
    }

    @Autowired
    public MqttMessageHandler(MqttChannel mqttChannel) {
        this.mqttChannel = mqttChannel;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void initHandler() {
        Map<String, Object> handlers = applicationContext.getBeansWithAnnotation(TopicHandler.class);
        handlers.forEach((name, obj) -> {
            Method[] methods = obj.getClass().getDeclaredMethods();
            for (Method topicHandler : methods) {
                Annotation[] annotations = topicHandler.getDeclaredAnnotations();
                //找到有RequestMapping注解的方法
                Optional<Annotation> optAnn = Arrays.stream(annotations)
                        .filter((ann) -> ann instanceof RequestMapping).findAny();
                if (!optAnn.isPresent()) {
                    continue;
                }
                RequestMapping requestMapping = (RequestMapping) optAnn.get();
                String[] paths = requestMapping.value();
                if (paths.length < 1) {
                    continue;
                }
                String path = paths[0];

                HandlerMethodInfo methodInfo = new HandlerMethodInfo();
                topicHandlerMethods.add(methodInfo);
                methodInfo.setOwner(obj);
                methodInfo.setMethod(topicHandler);
                methodInfo.setPath(path);
                List<HandlerMethodInfo.HandlerParam> params = new ArrayList<>();
                methodInfo.setParams(params);

                //取方法参数定义
                Parameter[] parameters = topicHandler.getParameters();
                for (Parameter param : parameters) {
                    PathVariable pathVariable = param.getAnnotation(PathVariable.class);
                    if (pathVariable != null) {
                        methodInfo.setWithPathVariable(true);
                        params.add(new HandlerMethodInfo.HandlerParam(true, false,
                                pathVariable.value(), param.getType(), null));
                        continue;
                    }
                    RequestBody requestBody = param.getAnnotation(RequestBody.class);
                    if (requestBody != null) {
                        params.add(new HandlerMethodInfo.HandlerParam(false, true,
                                param.getName(), param.getType(), null));
                    }
                }
            }
        });
    }

    private HandlerMethodInfo findMethod(String topic) {
        for (HandlerMethodInfo methodInfo : topicHandlerMethods) {
            String path = methodInfo.getPath();
            //将路径和topic用/拆分，逐个按位置比对，只要出现一个不匹配则整体不匹配
            String[] pathParts = path.split("/");
            String[] topicParts = topic.split("/");
            if (pathParts.length != topicParts.length) {
                continue;
            }

            int matchCount = 0;
            for (int i = 0; i < pathParts.length; i++) {
                String pathPart = pathParts[i];
                //PathVariable跳过
                if (pathPart.startsWith("{")) {
                    String pathVar = pathPart.replace("{", "")
                            .replace("}", "");
                    //找到topic中对应值更新到参数值中
                    for (HandlerMethodInfo.HandlerParam param : methodInfo.getParams()) {
                        if (param.getName().equals(pathVar)) {
                            param.setValue(topicParts[i]);
                        }
                    }

                    matchCount++;
                    continue;
                }
                //不匹配
                if (!pathPart.equals(topicParts[i])) {
                    continue;
                }
                matchCount++;
            }

            //全部匹配
            if (matchCount == pathParts.length) {
                return methodInfo;
            }
        }

        return null;
    }

    /**
     * 通过topic和payload找到方法调用需要传递的参数
     */
    private Object[] getMethodParams(HandlerMethodInfo methodInfo, String payload) {
        List<HandlerMethodInfo.HandlerParam> handlerParams = methodInfo.getParams();
        List<Object> params = new ArrayList<>();
        for (HandlerMethodInfo.HandlerParam param : handlerParams) {
            //只会有一个RequestBody参数
            if (param.isRequestBody() && param.getType() instanceof Class) {
                param.setValue(JsonUtil.parse(payload, (Class<?>) param.getType()));
            }
            params.add(param.getValue());
        }
        return params.toArray();
    }
}
