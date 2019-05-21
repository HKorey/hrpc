package com.hquery.hrpc.core.server;

import com.hquery.hrpc.core.route.AbstractRpcRoute;
import com.hquery.hrpc.core.route.RouteClient;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hquery.huang
 * 2019/5/21 14:14:19
 */
@Data
@Accessors(chain = true)
public class RemoteServerWrapper {

    private Class<?> serviceInterface;

    private List<RouteClient> routeClients;

    private String routeType;

    public RemoteServerWrapper(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
        this.routeClients = new ArrayList<>();
        this.routeType = AbstractRpcRoute.ROUTE_TYPE_RANDOM;
    }
}
