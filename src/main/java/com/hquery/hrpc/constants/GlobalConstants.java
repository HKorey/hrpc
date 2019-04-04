package com.hquery.hrpc.constants;

import com.hquery.hrpc.core.codec.Hessian2Serializer;
import com.hquery.hrpc.core.codec.Serializer;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author hquery.huang
 * 2018/5/3 16:48
 */
@Slf4j
public class GlobalConstants {

    private static Unsafe UNSAFE = null;

    public static final String THE_UNSAFE = "theUnsafe";

    static {
        Field f = null;
        try {
            f = Unsafe.class.getDeclaredField(THE_UNSAFE);
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    public static final String DEFAULT_VERSION = "1.0";

    public static final int RPC_TIMEOUT = 30000;

    public static final int DATA_CENTER_ID = 1;

    public static final int WORKER_ID = 1;

    /**
     * 获取可用CPU个数
     */
    public static final int EXECUTOR_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;

    //	public static Serializer SERIALIZER = new JdkSerializer() ;

//	public static Serializer SERIALIZER = KryoSerializer.getInstance() ;

    public static final Serializer SERIALIZER = new Hessian2Serializer();

//	public static Serializer SERIALIZER = new ProtostuffSerializer() ;

    public static final String DEFAULT_LOCAL_HOST = getHostAddress();

    public static final int DEFAULT_HRPC_PORT = 52710;

    public static final String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取本地host出现异常", e);
        }
        return "127.0.0.1";
    }

    public static final String REGISTRY_PROTOCOL_SUFFIX = "Registry";
}
