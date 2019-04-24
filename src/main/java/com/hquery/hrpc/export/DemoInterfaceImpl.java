package com.hquery.hrpc.export;

import com.hquery.hrpc.annotation.RegisterRpcServer;
import org.springframework.stereotype.Service;

/**
 * @author hquery.huang
 * 2019/3/30 17:41
 */
@Service
@RegisterRpcServer(exportInterface = DemoInterface.class)
public class DemoInterfaceImpl implements DemoInterface {


    @Override
    public DemoDTO hello(DemoDTO demo) {
        DemoDTO demoDTO = new DemoDTO();
        demoDTO.setName("Hello :" + demo.getName());
        return demoDTO;
    }
}
