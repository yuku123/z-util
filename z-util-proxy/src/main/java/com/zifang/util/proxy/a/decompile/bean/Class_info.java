package com.zifang.util.proxy.a.decompile.bean;

import com.zifang.util.proxy.a.decompile.bean.attribute.Attribute_info;
import com.zifang.util.proxy.a.decompile.bean.constant.Constant_X_info;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class文件信息模型<br>
 * 表示解析后的Java Class文件的完整结构
 */
public class Class_info {
    private String magic;
    private String minor_version;
    private String major_version;
    private int cp_count;
    private Map<Integer, Constant_X_info> constant_pool_Map;
    private String access_flag;
    private int this_class_index;
    private int super_class_index;
    private int interfaces_count;
    private List<Integer> interfacesList;
    private int fields_count;
    private List<Fields_info> fields_info_List;
    private int Methods_count;
    private List<Methods_info> methods_info_List;
    private int attributes_count;
    private List<Attribute_info> attributes;

    /**
     * 获取魔数
     *
     * @return 魔数值
     */
    public String getMagic() {
        return magic;
    }

    /**
     * 设置魔数
     *
     * @param magic 魔数值
     */
    public void setMagic(String magic) {
        this.magic = magic;
    }

    /**
     * 获取次版本号
     *
     * @return 次版本号字符串
     */
    public String getMinor_version() {
        return minor_version;
    }

    /**
     * 设置次版本号
     *
     * @param minor_version 次版本号
     */
    public void setMinor_version(String minor_version) {
        this.minor_version = minor_version;
    }

    /**
     * 获取主版本号
     *
     * @return 主版本号字符串
     */
    public String getMajor_version() {
        return major_version;
    }

    /**
     * 设置主版本号
     *
     * @param major_version 主版本号
     */
    public void setMajor_version(String major_version) {
        this.major_version = major_version;
    }

    /**
     * 获取常量池数量
     *
     * @return 常量池数量
     */
    public int getCp_count() {
        return cp_count;
    }

    /**
     * 设置常量池数量
     *
     * @param cp_count 常量池数量
     */
    public void setCp_count(int cp_count) {
        this.cp_count = cp_count;
    }

    /**
     * 获取常量池Map
     *
     * @return 常量池Map
     */
    public Map<Integer, Constant_X_info> getConstant_pool_Map() {
        return constant_pool_Map;
    }

    /**
     * 设置常量池Map
     *
     * @param constant_pool_Map 常量池Map
     */
    public void setConstant_pool_Map(Map<Integer, Constant_X_info> constant_pool_Map) {
        this.constant_pool_Map = constant_pool_Map;
    }

    /**
     * 获取访问标志
     *
     * @return 访问标志字符串
     */
    public String getAccess_flag() {
        return access_flag;
    }

    /**
     * 设置访问标志
     *
     * @param access_flag 访问标志
     */
    public void setAccess_flag(String access_flag) {
        this.access_flag = access_flag;
    }

    /**
     * 获取thisClass索引
     *
     * @return thisClass索引
     */
    public int getThis_class_index() {
        return this_class_index;
    }

    /**
     * 设置thisClass索引
     *
     * @param this_class_index thisClass索引
     */
    public void setThis_class_index(int this_class_index) {
        this.this_class_index = this_class_index;
    }

    /**
     * 获取superClass索引
     *
     * @return superClass索引
     */
    public int getSuper_class_index() {
        return super_class_index;
    }

    /**
     * 设置superClass索引
     *
     * @param super_class_index superClass索引
     */
    public void setSuper_class_index(int super_class_index) {
        this.super_class_index = super_class_index;
    }

    /**
     * 获取接口数量
     *
     * @return 接口数量
     */
    public int getInterfaces_count() {
        return interfaces_count;
    }

    /**
     * 设置接口数量
     *
     * @param interfaces_count 接口数量
     */
    public void setInterfaces_count(int interfaces_count) {
        this.interfaces_count = interfaces_count;
    }

    /**
     * 获取接口列表
     *
     * @return 接口索引列表
     */
    public List<Integer> getInterfacesList() {
        return interfacesList;
    }

    /**
     * 设置接口列表
     *
     * @param interfacesList 接口索引列表
     */
    public void setInterfacesList(List<Integer> interfacesList) {
        this.interfacesList = interfacesList;
    }

    /**
     * 获取字段数量
     *
     * @return 字段数量
     */
    public int getFields_count() {
        return fields_count;
    }

    /**
     * 设置字段数量
     *
     * @param fields_count 字段数量
     */
    public void setFields_count(int fields_count) {
        this.fields_count = fields_count;
    }

    /**
     * 获取字段信息列表
     *
     * @return 字段信息列表
     */
    public List<Fields_info> getFields_info_List() {
        return fields_info_List;
    }

    /**
     * 设置字段信息列表
     *
     * @param fields_info_List 字段信息列表
     */
    public void setFields_info_List(List<Fields_info> fields_info_List) {
        this.fields_info_List = fields_info_List;
    }

    /**
     * 获取方法数量
     *
     * @return 方法数量
     */
    public int getMethods_count() {
        return Methods_count;
    }

    /**
     * 设置方法数量
     *
     * @param methods_count 方法数量
     */
    public void setMethods_count(int methods_count) {
        this.Methods_count = methods_count;
    }

    /**
     * 获取方法信息列表
     *
     * @return 方法信息列表
     */
    public List<Methods_info> getMethods_info_List() {
        return methods_info_List;
    }

    /**
     * 设置方法信息列表
     *
     * @param methods_info_List 方法信息列表
     */
    public void setMethods_info_List(List<Methods_info> methods_info_List) {
        this.methods_info_List = methods_info_List;
    }

    /**
     * 获取属性数量
     *
     * @return 属性数量
     */
    public int getAttributes_count() {
        return attributes_count;
    }

    /**
     * 设置属性数量
     *
     * @param attributes_count 属性数量
     */
    public void setAttributes_count(int attributes_count) {
        this.attributes_count = attributes_count;
    }

    /**
     * 获取属性列表
     *
     * @return 属性列表
     */
    public List<Attribute_info> getAttributes() {
        return attributes;
    }

    /**
     * 设置属性列表
     *
     * @param attributes 属性列表
     */
    public void setAttributes(List<Attribute_info> attributes) {
        this.attributes = attributes;
    }
}
