package com.zifang.util.proxy.a.resolver;

/**
 * 常量池项类型常量定义<br>
 * 定义了Java Class文件中常量池可能包含的各种常量类型标识
 */
public interface ConstantPoolItemType {

    /**
     * UTF-8编码的字符串常量
     */
    int UTF8 = 1;

    /**
     * 整数常量
     */
    int INTEGER = 3;

    /**
     * 浮点数常量
     */
    int FLOAT = 4;

    /**
     * 长整数常量
     */
    int LONG = 5;

    /**
     * 双精度浮点数常量
     */
    int DOUBLE = 6;

    /**
     * 类或接口类型的符号引用
     */
    int CLASS = 7;

    /**
     * 字符串常量
     */
    int STRING = 8;

    /**
     * 字段的符号引用
     */
    int FIELD_REF = 9;

    /**
     * 类方法的符号引用
     */
    int METHOD_REF = 10;

    /**
     * 接口方法的符号引用
     */
    int INTERFACE_METHOD_REF = 11;

    /**
     * 名称和类型的符号引用
     */
    int NAME_ANT_TYPE = 12;

    /**
     * 方法句柄
     */
    int METHOD_HANDLE = 15;

    /**
     * 方法类型
     */
    int METHOD_TYPE = 16;

    /**
     * 动态计算调用点
     */
    int INVOKE_DYNAMIC = 18;
}
