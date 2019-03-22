package com.hquery.hrpc.utils;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author hquery.huang
 * 2019/3/21 16:46:58
 */
public class RemotingUtil {

    public static String parseRemoteAddress(Channel channel) {
        if (null == channel) {
            return "";
        } else {
            SocketAddress remote = channel.remoteAddress();
            return doParse(remote != null ? remote.toString().trim() : "");
        }
    }

    public static String parseLocalAddress(Channel channel) {
        if (null == channel) {
            return "";
        } else {
            SocketAddress local = channel.localAddress();
            return doParse(local != null ? local.toString().trim() : "");
        }
    }

    public static String parseRemoteIP(Channel channel) {
        if (null == channel) {
            return "";
        } else {
            InetSocketAddress remote = (InetSocketAddress) channel.remoteAddress();
            return remote != null ? remote.getAddress().getHostAddress() : "";
        }
    }

    public static String parseRemoteHostName(Channel channel) {
        if (null == channel) {
            return "";
        } else {
            InetSocketAddress remote = (InetSocketAddress) channel.remoteAddress();
            return remote != null ? remote.getAddress().getHostName() : "";
        }
    }

    public static String parseLocalIP(Channel channel) {
        if (null == channel) {
            return "";
        } else {
            InetSocketAddress local = (InetSocketAddress) channel.localAddress();
            return local != null ? local.getAddress().getHostAddress() : "";
        }
    }

    public static int parseRemotePort(Channel channel) {
        if (null == channel) {
            return -1;
        } else {
            InetSocketAddress remote = (InetSocketAddress) channel.remoteAddress();
            return remote != null ? remote.getPort() : -1;
        }
    }

    public static int parseLocalPort(Channel channel) {
        if (null == channel) {
            return -1;
        } else {
            InetSocketAddress local = (InetSocketAddress) channel.localAddress();
            return local != null ? local.getPort() : -1;
        }
    }

    public static String parseSocketAddressToString(SocketAddress socketAddress) {
        return socketAddress != null ? doParse(socketAddress.toString().trim()) : "";
    }

    public static String parseSocketAddressToHostIp(SocketAddress socketAddress) {
        InetSocketAddress addrs = (InetSocketAddress) socketAddress;
        if (addrs != null) {
            InetAddress addr = addrs.getAddress();
            if (null != addr) {
                return addr.getHostAddress();
            }
        }

        return "";
    }

    private static String doParse(String addr) {
        if (StringUtils.isBlank(addr)) {
            return "";
        } else if (addr.charAt(0) == '/') {
            return addr.substring(1);
        } else {
            int len = addr.length();

            for (int i = 1; i < len; ++i) {
                if (addr.charAt(i) == '/') {
                    return addr.substring(i + 1);
                }
            }
            return addr;
        }
    }
}
