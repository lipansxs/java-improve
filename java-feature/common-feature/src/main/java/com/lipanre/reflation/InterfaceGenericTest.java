package com.lipanre.reflation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * 接口范型测试类
 *
 * @author LiPan
 */
public class InterfaceGenericTest {

    public static void main(String[] args) {

        Handler<?, ?> handler = new StringHandler();
        Class<? extends Handler> handlerClass = handler.getClass();
        Type[] genericInterfaces = handlerClass.getGenericInterfaces();
        ParameterizedType genericInterface = (ParameterizedType) genericInterfaces[0];
        Type[] actualTypeArguments = genericInterface.getActualTypeArguments();

        // 可以吧Type数组元素类型转为Class
        System.out.println(((Class) actualTypeArguments[0]));
        System.out.println(Arrays.toString(actualTypeArguments));
    }

}
