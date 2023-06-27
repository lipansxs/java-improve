package com.lipanre.generic;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

/**
 * 测试获取泛型
 *
 * @author LiPan
 */
public class GenericTest {

    public static <T> Response<T> getResponse(String json, TypeReference<Response<T>> typeReference) {
        return JSONObject.parseObject(json, typeReference);
    }



    public static void main(String[] args) {

        Person person = new Person();
        person.setName("testName");
        person.setAge(18);

        Response<Person> response = new Response<>();
        response.setCode(200);
        response.setData(person);

        // {"code":200,"data":{"age":18,"name":"testName"}}
        // new 一个类，后面跟上{}代表创建一个指定诶的匿名子类对象
        Response<Person> responsePerson = getResponse(JSONObject.toJSONString(response), new TypeReference<Response<Person>>() {});
        System.out.println(responsePerson.getData().getClass());

    }

}
