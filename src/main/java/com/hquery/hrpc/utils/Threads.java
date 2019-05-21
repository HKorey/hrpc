package com.hquery.hrpc.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hquery.huang
 * 2019/5/21 18:39:08
 */
@Slf4j
public class Threads {

    /**
     * sleep
     *
     * @param millis
     * @return void
     * @author hquery
     * 2019/5/21 18:41:39
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("error", e);
        }
    }

}
