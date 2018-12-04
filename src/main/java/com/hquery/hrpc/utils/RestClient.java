package com.hquery.hrpc.utils;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author hquery.huang
 * 2018/5/3 17:04
 */
public class RestClient {

    private static RestTemplate restTemplate;

    static {
        RestTemplateBuilder builder = SpringContextUtil.getBean(RestTemplateBuilder.class);
        builder.setConnectTimeout(30000);
        builder.setReadTimeout(30000);
        restTemplate = builder.build();
    }

    public static ResponseEntity<String> post(String uri, String body, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(contentType);
        headers.setContentType(type);
        return postContent(uri, body, headers);
    }

    public static ResponseEntity<String> postContent(String uri, String body, MultiValueMap<String, String> map) {
        HttpEntity<Object> request = new HttpEntity<>(body, map);
        ResponseEntity<String> entity = restTemplate.postForEntity(uri, request, String.class);
        return entity;
    }

}
