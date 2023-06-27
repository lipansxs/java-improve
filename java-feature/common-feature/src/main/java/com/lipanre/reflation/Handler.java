package com.lipanre.reflation;

/**
 * 接口处理器
 *
 * @author LiPan
 */
public interface Handler <I, O>{

    /**
     * 处理输入的数据
     *
     * @param i
     * @return
     */
    O handle(I input);

}
