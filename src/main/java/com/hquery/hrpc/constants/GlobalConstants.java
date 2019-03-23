package com.hquery.hrpc.constants;

import com.hquery.hrpc.core.codec.Hessian2Serializer;
import com.hquery.hrpc.core.codec.Serializer;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

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

    public static String DEFAULT_VERSION = "1.0";

    public static int RPC_TIMEOUT = 30000;

    public static int NODE_ID = 1;

    /**
     * 获取可用CPU个数
     */
    public static int EXECUTOR_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;

    //	public static Serializer SERIALIZER = new JdkSerializer() ;

//	public static Serializer SERIALIZER = KryoSerializer.getInstance() ;

    public static Serializer SERIALIZER = new Hessian2Serializer();

//	public static Serializer SERIALIZER = new ProtostuffSerializer() ;

    public static int ZK_SESSION_TIMEOUT = 5000;

    public static String ZK_ADDRESS = "192.168.102.101:2181";

    public static String ZK_REGISTRY_PATH = "/rpc/data";

    public static String ZK_SERVER_PATH = "/providers";

    public static String ZK_CLIENT_PATH = "/consumers";

}
