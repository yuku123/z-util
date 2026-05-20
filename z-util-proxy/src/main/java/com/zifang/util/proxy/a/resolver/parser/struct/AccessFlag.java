package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 访问标志常量定义<br>
 * 定义了Java Class文件中用于描述类、接口、字段、方法访问权限的标志位
 */
public interface AccessFlag {

    /**
     * public访问标志
     */
    int PUBLIC = 0x0001;

    /**
     * final访问标志
     */
    int FINAL = 0x0010;

    /**
     * super访问标志
     */
    int SUPER = 0x0020;

    /**
     * interface访问标志
     */
    int INTERFACE = 0x0200;

    /**
     * abstract访问标志
     */
    int ABSTRACT = 0x0400;

    /**
     * synthetic访问标志，表示该类或成员由编译器生成
     */
    int SYNTHETIC = 0x1000;

    /**
     * annotation访问标志，表示该类型是注解类型
     */
    int ANNOTATION = 0x2000;

    /**
     * enum访问标志，表示该类型是枚举类型
     */
    int ENUM = 0x4000;
}
