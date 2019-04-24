package com.hquery.hrpc.controller;

import com.hquery.hrpc.export.DemoInterface;
import com.trustlife.tis.mutual.claim.facade.FgwFileUploadFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/hrpc")
public class HomeController {

    @GetMapping("/test")
    public Map<String, Object> home() {
        Map<String, Object> result = new HashMap<>();
        return result;
    }


}
