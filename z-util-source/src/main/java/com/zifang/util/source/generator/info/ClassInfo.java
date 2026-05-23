package com.zifang.util.source.generator.info;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
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

    private Boolean interfaceType;
    private String simpleClassName;
    private String packageName;
    private ClassInfo superClass;
    private List<String> imports = new ArrayList<>();
    private List<String> comments = new ArrayList<>();
    private List<ClassInfo> interfaces = new ArrayList<>();
    private List<FieldInfo> fields = new ArrayList<>();
    private List<MethodInfo> methods = new ArrayList<>();
    private int modifiers;
    private List<ClassInfo> innerClasses = new ArrayList<>();
    private List<AnnotationInfo> annotations = new ArrayList<>();

    public ClassInfo() {
    }

    /**
     * 获取全类路径名称
     *
     * @return 包名.类名的格式
     */
    public String getName() {
        if (packageName == null || packageName.isEmpty()) {
            return simpleClassName;
        }
        return packageName + "." + simpleClassName;
    }

    /**
     * 获取全限定类名（包含包名）
     */
    public String getFullName() {
        return getName();
    }

    /**
     * 批量添加字段
     */
    public void appendFields(List<FieldInfo> fieldInfos) {
        if (fieldInfos == null) {
            return;
        }
        fields.addAll(fieldInfos);
    }

    /**
     * 添加单个字段
     */
    public void appendField(FieldInfo fieldInfo) {
        if (fieldInfo != null) {
            fields.add(fieldInfo);
        }
    }

    /**
     * 批量添加方法
     */
    public void appendMethods(List<MethodInfo> methodInfos) {
        if (methodInfos == null) {
            return;
        }
        methods.addAll(methodInfos);
    }

    /**
     * 添加单个方法
     */
    public void appendMethod(MethodInfo methodInfo) {
        if (methodInfo != null) {
            methods.add(methodInfo);
        }
    }

    /**
     * 批量添加接口
     */
    public void appendInterfaces(List<ClassInfo> interfaceClassInfos) {
        if (interfaceClassInfos == null) {
            return;
        }
        interfaces.addAll(interfaceClassInfos);
    }

    /**
     * 添加单个接口
     */
    public void appendInterface(ClassInfo interfaceClassInfo) {
        if (interfaceClassInfo != null) {
            interfaces.add(interfaceClassInfo);
        }
    }

    /**
     * 将一个运行态 class 直接解析转化为 ClassInfo
     *
     * @param clazz 要解析的 Class 对象
     * @return 解析后的 ClassInfo 对象
     */
    public static ClassInfo parser(Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz 不能为 null");
        }

        ClassInfo classInfo = new ClassInfo();
        classInfo.setInterfaceType(clazz.isInterface());
        classInfo.setSimpleClassName(clazz.getSimpleName());
        classInfo.setModifiers(clazz.getModifiers());

        Package pkg = clazz.getPackage();
        classInfo.setPackageName(pkg != null ? pkg.getName() : "");

        // 解析父类
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            classInfo.setSuperClass(parser(superClass));
        }

        // 解析接口
        for (Class<?> iface : clazz.getInterfaces()) {
            classInfo.appendInterface(parser(iface));
        }

        // 解析字段
        for (Field field : clazz.getDeclaredFields()) {
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setType(field.getType().getName());
            fieldInfo.setName(field.getName());
            fieldInfo.setModifiers(new int[]{field.getModifiers()});
            classInfo.appendField(fieldInfo);
        }

        // 解析方法
        for (Method method : clazz.getDeclaredMethods()) {
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setMethodName(method.getName());
            methodInfo.setReturnType(method.getReturnType().getName());
            methodInfo.setModifier(method.getModifiers());

            List<MethodParameterPair> params = new ArrayList<>();
            for (Parameter param : method.getParameters()) {
                params.add(new MethodParameterPair(param.getType().getName(), param.getName()));
            }
            methodInfo.setMethodParameterPairs(params);

            classInfo.appendMethod(methodInfo);
        }

        // 解析内部类
        for (Class<?> inner : clazz.getDeclaredClasses()) {
            classInfo.getInnerClasses().add(parser(inner));
        }

        return classInfo;
    }

    /**
     * 最小闭环构造器
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
     * @return 构建完成的 ClassInfo 对象
     */
    public static ClassInfo build(
            Boolean interfaceType,
            int modifiers,
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

    // ==================== Getters / Setters ====================

    public Boolean getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Boolean interfaceType) {
        this.interfaceType = interfaceType;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments != null ? comments : new ArrayList<>();
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

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports != null ? imports : new ArrayList<>();
    }

    public List<ClassInfo> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<ClassInfo> interfaces) {
        this.interfaces = interfaces != null ? interfaces : new ArrayList<>();
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields != null ? fields : new ArrayList<>();
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods != null ? methods : new ArrayList<>();
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * @deprecated Use {@link #setModifiers(int)} instead
     */
    @Deprecated
    public void setModifiers(Integer modifiers) {
        this.modifiers = modifiers != null ? modifiers : 0;
    }

    public List<ClassInfo> getInnerClasses() {
        return innerClasses;
    }

    public void setInnerClasses(List<ClassInfo> innerClasses) {
        this.innerClasses = innerClasses != null ? innerClasses : new ArrayList<>();
    }

    public List<AnnotationInfo> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationInfo> annotations) {
        this.annotations = annotations != null ? annotations : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "name=" + getName() +
                ", interfaceType=" + interfaceType +
                ", modifiers=" + Modifier.toString(modifiers) +
                '}';
    }
}
