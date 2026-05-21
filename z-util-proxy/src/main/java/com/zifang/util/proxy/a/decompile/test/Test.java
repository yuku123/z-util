package com.zifang.util.proxy.a.decompile.test;

import com.zifang.util.proxy.a.decompile.app.App;

/**
 * 测试类
 * <p>
 * 用于测试类文件解析功能。
 */
public class Test {

    public static void main(String[] args) throws Exception {
        App app = new App("/Users/malcolmfeng/Documents/code/mine/Decomplier-Maven/src/main/java/org/example/test/", "FileClass");
        app.doDecomplie();
    }
}
