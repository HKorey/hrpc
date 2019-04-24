package com.hquery.hrpc.controller;

import com.hquery.hrpc.export.DemoDTO;
import com.hquery.hrpc.export.DemoInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hquery.huang
 * 2018/5/3 16:55
 */
@Slf4j
@RestController
@RequestMapping("/hrpc")
public class HomeController {

    @Autowired
    private DemoInterface demoInterface;

    @GetMapping("/test")
    public Map<String, Object> home() {
        Map<String, Object> result = new HashMap<>();
        DemoDTO demoDTO = new DemoDTO();
        demoDTO.setName("124214");
        result.put("demoInterface", demoInterface.hello(demoDTO).getName());
        return result;
    }


}
