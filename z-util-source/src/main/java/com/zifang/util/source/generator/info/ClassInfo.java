package com.zifang.util.source.generator.info;

import java.util.ArrayList;
import java.util.List;

/**
 * 类信息标准封装类
 * <p>
 * 用于封装Java类的完整元数据信息，包括：
 * <ul>
 *   <li>类名、包名、修饰符</li>
 *   <li>父类信息</li>
 *   <li>实现的接口列表</li>
 *   <li>字段列表</li>
 *   <li>方法列表</li>
 *   <li>导入语句和类注释</li>
 * </ul>
 * 支持通过静态方法从Class对象构建实例，是代码生成和分析的核心数据载体。
 *
 * @author zifang
 * @version 1.0.0
 */
public class ClassInfo {

    /**
     * 标记当前classInfo是否为接口
     * <p>
     * true : 是接口
     * false : 不是接口
     */
    private Boolean interfaceType;

    /**
     * 当前类简单名称（不含包名）
     */
    private String simpleClassName;

    /**
     * 当前包名
     */
    private String packageName;

    /**
     * 父类信息
     */
    private ClassInfo superClass;

    /**
     * 导入语句列表
     */
    private List<String> imports = new ArrayList<>();

    /**
     * 类注释列表
     */
    private List<String> comments = new ArrayList<>();

    /**
     * 接口列表
     */
    private List<ClassInfo> interfaces = new ArrayList<>();

    /**
     * 字段列表
     */
    private List<FieldInfo> fields = new ArrayList<>();

    /**
     * 方法列表
     */
    private List<MethodInfo> methods = new ArrayList<>();

    /**
     * 类的修饰符，使用 java.lang.reflect.Modifier 的常量值
     */
    private int modifiers;

    /**
     * 内部类列表
     */
    private List<ClassInfo> innerClasses = new ArrayList<>();

    /**
     * 注解列表
     */
    private List<AnnotationInfo> annotations = new ArrayList<>();

    /**
     * 获取全类路径名称
     *
     * @return 包名.类名的格式
     */
    public String getName() {
        return packageName + "." + simpleClassName;
    }

    /**
     * 批量添加字段
     *
     * @param fieldInfos 字段信息列表
     */
    public void appendFields(List<FieldInfo> fieldInfos) {
        checkField();
        fields.addAll(fieldInfos);
    }

    /**
     * 添加单个字段
     *
     * @param fieldInfo 字段信息
     */
    public void appendField(FieldInfo fieldInfo) {
        checkField();
        fields.add(fieldInfo);
    }

    /**
     * 批量添加方法
     *
     * @param methodInfos 方法信息列表
     */
    public void appendMethods(List<MethodInfo> methodInfos) {
        checkMethod();
        methods.addAll(methodInfos);
    }

    /**
     * 添加单个方法
     *
     * @param methodInfo 方法信息
     */
    public void appendMethod(MethodInfo methodInfo) {
        checkMethod();
        methods.add(methodInfo);
    }

    /**
     * 批量添加接口
     *
     * @param interfaceClassInfos 接口信息列表
     */
    public void appendInterfaces(List<ClassInfo> interfaceClassInfos) {
        checkInterface();
        interfaces.addAll(interfaceClassInfos);
    }

    /**
     * 添加单个接口
     *
     * @param interfaceClassInfo 接口信息
     */
    public void appendInterfaces(ClassInfo interfaceClassInfo) {
        checkInterface();
        interfaces.add(interfaceClassInfo);
    }

    /**
     * 将一个运行态class直接解析转化为ClassInfo
     *
     * @param clazz 要解析的Class对象
     * @return 解析后的ClassInfo对象
     */
    public static ClassInfo parser(Class clazz) {
        return null; // @todo
    }

    /**
     * 最小闭环构造器
     * <p>
     * 使用提供的所有参数创建一个完整的ClassInfo对象
     *
     * @param interfaceType   是否为接口
     * @param modifiers      类修饰符
     * @param packageName     包名
     * @param comments       类注释列表
     * @param simpleClassName 简单类名
     * @param superClass     父类信息
     * @param interfaces     接口列表
     * @param fieldInfos     字段列表
     * @param methodInfos    方法列表
     * @return 构建完成的ClassInfo对象
     */
    public static ClassInfo build(
            Boolean interfaceType,
            Integer modifiers,
            String packageName,
            List<String> comments,
            String simpleClassName,
            ClassInfo superClass,
            List<ClassInfo> interfaces,
            List<FieldInfo> fieldInfos,
            List<MethodInfo> methodInfos) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setInterfaceType(interfaceType);
        classInfo.setModifiers(modifiers);
        classInfo.setPackageName(packageName);
        classInfo.setComments(comments);
        classInfo.setSimpleClassName(simpleClassName);
        classInfo.setSuperClass(superClass);
        classInfo.setInterfaces(interfaces);
        classInfo.setFields(fieldInfos);
        classInfo.setMethods(methodInfos);
        return classInfo;
    }


    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ClassInfo getSuperClass() {
        return superClass;
    }

    public void setSuperClass(ClassInfo superClass) {
        this.superClass = superClass;
    }

    public List<ClassInfo> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<ClassInfo> interfaces) {
        this.interfaces = interfaces;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public Boolean getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Boolean interfaceType) {
        this.interfaceType = interfaceType;
    }

    private void checkInterface() {
        if (interfaces == null) {
            throw new RuntimeException("当前interfaceList为null");
        }
    }

    private void checkMethod() {
        if (methods == null) {
            throw new RuntimeException("当前methodList为null");
        }
    }

    private void checkImports() {
        if (imports == null) {
            throw new RuntimeException("当前importList为null");
        }
    }

    private void checkField() {
        if (fields == null) {
            throw new RuntimeException("当前fieldList为null");
        }
    }
}
