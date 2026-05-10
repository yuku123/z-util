package com.zifang.util.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

public class LogTest {
    private static final Logger log = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void aaa() {
        log.info("INFOXXXXX");
        log.debug("debug XXXXX");

    }
}
