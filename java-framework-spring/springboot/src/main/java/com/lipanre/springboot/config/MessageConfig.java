package com.lipanre.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

/**
 * message 配置类
 *
 * @author LiPan
 */
@Configuration
public class MessageConfig {

    @Bean("channel1")
    public MessageChannel channel1() {
        return new PublishSubscribeChannel();
    }

    @Bean("channel2")
    public MessageChannel channel2() {
        return new PublishSubscribeChannel();
    }

    @ServiceActivator(inputChannel = "channel1")
    public void handle(Message<String> message, @Header(name = "foo") String foo) {
        System.out.println("handle 方法被调用" + message + "Header: foo = " + foo);
    }
}
