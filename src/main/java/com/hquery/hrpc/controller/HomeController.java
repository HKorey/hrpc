package com.hquery.hrpc.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hquery.huang
 * 2018/5/3 16:55
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("/test")
    public Map<String, String> home(@RequestBody Map<String, Object> body) {
        Map<String, String> result = new HashMap<>();
//        try {
//            Field declaredField = ((Class) body.get("Class")).getDeclaredField("globalMap");
//            System.out.println(declaredField);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        String url = "http://172.16.1.33:8080/home/test/";
//        RestClient.post(url, body, HttpContentTypeConstant.JSON.getName());
        return result;
    }


}
