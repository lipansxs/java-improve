package com.lipanre.springbootdisruptor.event;

import lombok.Data;

import java.io.Serializable;

/**
 * 数字事件
 *
 * @author LiPan
 */
@Data
public class NumberEvent implements Serializable {

    private Integer data;

}
