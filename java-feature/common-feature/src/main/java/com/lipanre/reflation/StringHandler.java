package com.lipanre.reflation;

/**
 * 字符串处理器
 *
 * @author LiPan
 */


public class StringHandler implements Handler<String, Void>{
    @Override
    public Void handle(String input) {
        System.out.println("处理字符串");
        return null;
    }
}
