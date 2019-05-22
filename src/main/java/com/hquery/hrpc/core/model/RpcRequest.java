package com.hquery.hrpc.core.model;


import java.util.Arrays;

/**
 * Created by HQuery on 2018/12/1.
 */
public class RpcRequest extends RpcCommand {

    private static final long serialVersionUID = 1316676253005873673L;

    private long requestId;

    private String className;

    private String methodName;

    private String version;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RpcRequest{");
        sb.append("requestId=").append(requestId);
        sb.append(", className='").append(className).append('\'');
        sb.append(", methodName='").append(methodName).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", parameterTypes=").append(Arrays.toString(parameterTypes));
        sb.append(", parameters=").append(Arrays.toString(parameters));
        sb.append('}');
        return sb.toString();
    }
}
