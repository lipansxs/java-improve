package com.lipanre.springbootdisruptor.publish;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 事件发布器
 *
 * @author LiPan
 */
@Slf4j

@Component
public class EventPublisher {

    @Resource
    private List<EventHandler> eventHandlers;

    private static final Map<Class<?>, RingBuffer<?>> ringBufferMap = new HashMap<>();

    public static <T> void publish(T event) {
        RingBuffer ringBuffer = ringBufferMap.get(event.getClass());
        if (Objects.isNull(ringBuffer)) {
            log.warn("找不到事件{}的消费者，请检查是否创建该事件消费者", event.getClass());
            return;
        }
        ringBuffer.publishEvent(((event1, sequence, arg0) -> BeanUtils.copyProperties(arg0, event1)), event);
    }

    @PostConstruct
    public void init() {
        int ringBufferSize = 16;

        for (EventHandler eventHandler : eventHandlers) {
            Class eventClass = getComsuerEvent(eventHandler);
            Disruptor disruptor = new Disruptor<>(() -> getEventFactory(eventClass), ringBufferSize, DaemonThreadFactory.INSTANCE);
            disruptor.handleEventsWith(eventHandler);
            disruptor.start();
            ringBufferMap.put(eventClass, disruptor.getRingBuffer());
        }
    }

    private Object getEventFactory(Class eventClass) {
        return ReflectUtils.newInstance(eventClass);
    }

    private Class getComsuerEvent(EventHandler eventHandler) {
        ResolvableType resolvableType = ResolvableType.forClass(eventHandler.getClass());
        ResolvableType generic = resolvableType.getInterfaces()[0].getGeneric(0);
        return generic.getRawClass();
    }

}
