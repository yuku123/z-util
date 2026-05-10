package com.zifang.util.zex.bust.chapter6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {
    private static final Logger log = LoggerFactory.getLogger(LogTest.class);
    public static void main(String[] args) {
        log.info("打印info日志");
        log.debug("打印debug日志");
        log.warn("打印warn日志");
        log.error("打印error日志");
    }
}
