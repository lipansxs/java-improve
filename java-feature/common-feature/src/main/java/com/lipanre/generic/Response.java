package com.lipanre.generic;

import lombok.Data;

/**
 * 响应
 *
 * @author LiPan
 */

@Data
public class Response<T>{

    private Integer code;

    private T data;

}
