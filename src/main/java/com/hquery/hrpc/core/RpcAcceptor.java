package com.hquery.hrpc.core;

import java.io.IOException;

/**
 * @author hquery.huang
 * 2019/3/23 15:50:09
 */
public interface RpcAcceptor {

    void setHost(String host);

    void setPort(int port);

    void start() throws IOException;

    void stop() throws IOException;

    void setProcessor(RpcProcessor processor);
}
