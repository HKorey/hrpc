package com.hquery.hrpc.core;

import com.hquery.hrpc.constants.GlobalConstants;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hquery.huang
 * 2019/3/23 15:50:34
 */
public class Exporter {

    protected ConcurrentHashMap<String, Object> serviceEngine = new ConcurrentHashMap<>();

    private Exporter() {
    }

    public void export(Class<?> clazz, Object obj, String version) {
        try {
            obj.getClass().asSubclass(clazz);
        } catch (ClassCastException e) {
            throw new RpcException(obj.getClass().getName() + " can't cast " + clazz.getName());
        }
        if (version == null) {
            version = GlobalConstants.DEFAULT_VERSION;
        }
        String exeKey = this.genExeKey(clazz.getName(), version);
        Object service = serviceEngine.get(exeKey);
        if (service != null && service != obj) {
            throw new RpcException("can't register service " + clazz.getName() + " again");
        }
        if (obj == service) {
            return;
        }
        serviceEngine.put(exeKey, obj);
    }

    public Object findService(String clazzName, String version) {
        if (version == null) {
            version = GlobalConstants.DEFAULT_VERSION;
        }
        String exeKey = this.genExeKey(clazzName, version);
        Object service = serviceEngine.get(exeKey);
        return service;

    }

    private String genExeKey(String serviceName, String version) {
        return version == null ? serviceName : (serviceName + "_" + version);
    }

    private static class Holder {
        public static final Exporter INSTANCE = new Exporter();
    }

    public static Exporter getInstance() {
        return Holder.INSTANCE;
    }
}
