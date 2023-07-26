package com.lipanre.springbootdisruptor.event;

import lombok.Data;

/**
 * 基础事件
 *
 * @author LiPan
 */
@Data
public final class BaseEvent<T> {
    
    private T data;
    
    public static BaseEvent getInstance() {
        return new BaseEvent();
    }
    
}
