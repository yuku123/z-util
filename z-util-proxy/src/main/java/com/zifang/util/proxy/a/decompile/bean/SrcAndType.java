package com.zifang.util.proxy.a.decompile.bean;

import com.zifang.util.proxy.a.decompile.bean.attribute.Attribute_info;

/**
 * 源码与类型组合类
 * <p>
 * 用于存储源码片段及其对应的类型信息。
 */
public class SrcAndType {

    private String src;  // 名字
    private String type; // 类型
    private boolean flag;
    private String time;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
