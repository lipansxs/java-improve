package com.lipanre.queue;

import lombok.Data;

import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时消息
 *
 * @author LiPan
 */
@Data
public class DelayMessage implements Delayed {

    private String payload;

    private Long ttl;

    public static DelayMessage create(String payload) {
        return new DelayMessage(payload, 0L);
    }

    public DelayMessage(String payload, Long ttl) {
        this.payload = payload;
        this.ttl = System.currentTimeMillis() + ttl;
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(this.ttl - System.currentTimeMillis(), TimeUnit.MICROSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.ttl - o.getDelay(TimeUnit.MICROSECONDS));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DelayMessage that = (DelayMessage) o;
        return Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload);
    }
}
