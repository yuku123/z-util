package com.zifang.util.core.lang.annoations;


import java.lang.annotation.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 注解工具类
 * 快速获取注解对象、注解值等工具封装
 *
 * @author zifang
 */
/**
 * AnnotationUtil类。
 */
/**
 * AnnotationUtil类。
 */
public class AnnotationUtil {

    public static ElementType[] defaultElementTypes = new ElementType[]{
            ElementType.TYPE,
            ElementType.FIELD,
            ElementType.METHOD,
            ElementType.PARAMETER,
            ElementType.CONSTRUCTOR,
            ElementType.LOCAL_VARIABLE,
            ElementType.ANNOTATION_TYPE,
            ElementType.PACKAGE
    };


    /**
     * 是否会保存到 Javadoc 文档中
     *
     * @param annotationType 注解类
     * @return 是否会保存到 Javadoc 文档中
     */
    /**
     * isDocumented方法。
     *      * @param annotationType Class?类型参数
     * @return static boolean类型返回值
     */
    /**
     * isDocumented方法。
     *      * @param annotationType Class?类型参数
     * @return static boolean类型返回值
     */
    public static boolean isDocumented(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Documented.class);
    }

    /**
     * 是否可以被继承，默认为 false
     *
     * @param annotationType 注解类
     * @return 是否会保存到 Javadoc 文档中
     */
    /**
     * isInherited方法。
     *      * @param annotationType Class?类型参数
     * @return static boolean类型返回值
     */
    /**
     * isInherited方法。
     *      * @param annotationType Class?类型参数
     * @return static boolean类型返回值
     */
    public static boolean isInherited(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Inherited.class);
    }

    /**
     * 检查是否实现了这个注解
     *
     * @param annotationClass  注解类型
     * @param annotatedElement 需要判断的元素
     */
    /**
     * hasAnnotationOn方法。
     *      * @param annotationClass Class?类型参数
     * @param annotatedElement AnnotatedElement类型参数
     * @return static boolean类型返回值
     */
    /**
     * hasAnnotationOn方法。
     *      * @param annotationClass Class?类型参数
     * @param annotatedElement AnnotatedElement类型参数
     * @return static boolean类型返回值
     */
    public static boolean hasAnnotationOn(Class<? extends Annotation> annotationClass, AnnotatedElement annotatedElement) {
        return annotatedElement.isAnnotationPresent(annotationClass);
    }

    /**
     * 将指定的被注解的元素转换为组合注解元素
     *
     * @param annotationEle 注解元素
     * @return 组合注解元素
     */
    /**
     * toCombination方法。
     *      * @param annotationEle AnnotatedElement类型参数
     * @return static CombinationAnnotationElement类型返回值
     */
    /**
     * toCombination方法。
     *      * @param annotationEle AnnotatedElement类型参数
     * @return static CombinationAnnotationElement类型返回值
     */
    public static CombinationAnnotationElement toCombination(AnnotatedElement annotationEle) {
        if (annotationEle instanceof CombinationAnnotationElement) {
            return (CombinationAnnotationElement) annotationEle;
        }
        return new CombinationAnnotationElement(annotationEle);
    }

    /**
     * 获取指定注解
     *
     * @param annotationEle   {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
     * @param isToCombination 是否为转换为组合注解
     * @return 注解对象
     */
    /**
     * getAnnotations方法。
     *      * @param annotationEle AnnotatedElement类型参数
     * @param isToCombination boolean类型参数
     * @return static Annotation[]类型返回值
     */
    /**
     * getAnnotations方法。
     *      * @param annotationEle AnnotatedElement类型参数
     * @param isToCombination boolean类型参数
     * @return static Annotation[]类型返回值
     */
    public static Annotation[] getAnnotations(AnnotatedElement annotationEle, boolean isToCombination) {
        return (null == annotationEle) ? null : (isToCombination ? toCombination(annotationEle) : annotationEle).getAnnotations();
    }

    /**
     * 获取指定注解
     *
     * @param <A>            注解类型
     * @param annotationEle  {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
     * @param annotationType 注解类型
     * @return 注解对象
     */
    /**
     * getAnnotation方法。
     *      * @param annotationEle AnnotatedElement类型参数
     * @param annotationType ClassA类型参数
     * @return static <A extends Annotation> A类型返回值
     */
    /**
     * getAnnotation方法。
     *      * @param annotationEle AnnotatedElement类型参数
     * @param annotationType ClassA类型参数
     * @return static <A extends Annotation> A类型返回值
     */
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotationEle, Class<A> annotationType) {
        return (null == annotationEle) ? null : toCombination(annotationEle).getAnnotation(annotationType);
    }

    /**
     * 获取指定注解默认值<br>
     * 如果无指定的属性方法返回null
     *
     * @param <T>            注解值类型
     * @param annotationEle  {@link AccessibleObject}，可以是Class、Method、Field、Constructor、ReflectPermission
     * @param annotationType 注解类型
     * @return 注解对象
     */
    /**
     * getAnnotationValue方法。
     *      * @param annotationEle AnnotatedElement类型参数
     * @param annotationType Class?类型参数
     * @return static <T> T类型返回值
     */
    /**
     * getAnnotationValue方法。
     *      * @param annotationEle AnnotatedElement类型参数
     * @param annotationType Class?类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T getAnnotationValue(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType) {
        return getAnnotationValue(annotationEle, annotationType, "value");
    }

    /**
     * 获取指定注解属性的值<br>
     * 如果无指定的属性方法返回null
     *
     * @param <T>            注解值类型
     * @param annotationEle  {@link AccessibleObject}，可以是Class、Method、Field、Constructor、ReflectPermission
     * @param annotationType 注解类型
     * @param propertyName   属性名，例如注解中定义了name()方法，则 此处传入name
     * @return 注解对象
     */
    /**
     * getAnnotationValue方法。
     *      * @param annotationEle AnnotatedElement类型参数
     * @param annotationType Class?类型参数
     * @param propertyName String类型参数
     * @return static <T> T类型返回值
     */
    /**
     * getAnnotationValue方法。
     *      * @param annotationEle AnnotatedElement类型参数
     * @param annotationType Class?类型参数
     * @param propertyName String类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T getAnnotationValue(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType, String propertyName) {
        Annotation annotation = getAnnotation(annotationEle, annotationType);
        if (null == annotation) {
            return null;
        }

        try {
            Method method = annotation.getClass().getMethod(propertyName);
            method.setAccessible(true);
            return (T) method.invoke(annotation);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取注解类的保留时间，可选值 SOURCE（源码时），CLASS（编译时），RUNTIME（运行时），默认为 CLASS
     *
     * @param annotationType 注解类
     * @return 保留时间枚举
     */
    /**
     * getRetentionPolicy方法。
     *      * @param annotationType Class?类型参数
     * @return static RetentionPolicy类型返回值
     */
    /**
     * getRetentionPolicy方法。
     *      * @param annotationType Class?类型参数
     * @return static RetentionPolicy类型返回值
     */
    public static RetentionPolicy getRetentionPolicy(Class<? extends Annotation> annotationType) {
        final Retention retention = annotationType.getAnnotation(Retention.class);
        if (null == retention) {
            return RetentionPolicy.CLASS;
        }
        return retention.value();
    }

    /**
     * 获取注解类可以用来修饰哪些程序元素，如 TYPE, METHOD, CONSTRUCTOR, FIELD, PARAMETER 等
     * <p>
     * 当没有标记target的场合，默认都修饰
     *
     * @param annotationType 注解类
     * @return 注解修饰的程序元素数组
     */
    /**
     * getTargetType方法。
     *      * @param annotationType Class?类型参数
     * @return static ElementType[]类型返回值
     */
    /**
     * getTargetType方法。
     *      * @param annotationType Class?类型参数
     * @return static ElementType[]类型返回值
     */
    public static ElementType[] getTargetType(Class<? extends Annotation> annotationType) {
        final Target target = annotationType.getAnnotation(Target.class);
        if (null == target) {
            return defaultElementTypes;
        }
        return target.value();
    }


}
