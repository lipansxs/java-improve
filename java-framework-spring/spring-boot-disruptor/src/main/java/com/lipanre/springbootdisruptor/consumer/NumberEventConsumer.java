package com.lipanre.springbootdisruptor.consumer;

import com.lipanre.springbootdisruptor.event.NumberEvent;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 数字消费者
 *
 * @author LiPan
 */
@Slf4j
@Component
public class NumberEventConsumer implements EventHandler<NumberEvent> {

    @Override
    public void onEvent(NumberEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info("消费到了数据: {}", event);
    }
}
