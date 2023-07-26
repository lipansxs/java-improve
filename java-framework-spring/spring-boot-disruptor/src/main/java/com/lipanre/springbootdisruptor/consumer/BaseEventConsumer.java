package com.lipanre.springbootdisruptor.consumer;

import com.lipanre.springbootdisruptor.event.BaseEvent;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试泛型事件
 *
 * @author LiPan
 */
@Slf4j
@Component
public class BaseEventConsumer implements EventHandler<BaseEvent<String>> {
    @Override
    public void onEvent(BaseEvent<String> event, long sequence, boolean endOfBatch) throws Exception {
        log.info("测试泛型事件消费者：{}", event);
    }
}
