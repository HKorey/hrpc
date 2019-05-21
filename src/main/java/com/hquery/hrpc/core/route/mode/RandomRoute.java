package com.hquery.hrpc.core.route.mode;

import com.hquery.hrpc.core.exception.RpcException;
import com.hquery.hrpc.core.route.AbstractRpcRoute;
import com.hquery.hrpc.core.route.RouteClient;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 随机
 *
 * @author hquery.huang
 * 2019/4/4 14:00:02
 */
@NoArgsConstructor
public class RandomRoute extends AbstractRpcRoute {

    @Override
    public RouteClient doRoute(List<RouteClient> clients) {
        if (CollectionUtils.isEmpty(clients)) {
            throw new RpcException("无可用服务");
        }
        int k = RandomUtils.nextInt(0, clients.size());
        return clients.get(k);
    }

}
