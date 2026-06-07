package com.zifang.util.core.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zifang
 * @time: 2019-05-08 17:11:00
 * @description: class type util
 * @version: JDK 1.8
 */
/**
 * ClassUtil类。
 */
/**
 * ClassUtil类。
 */
public class ClassUtil {

    /**
     * base type wrapper list
     */
    private static final List<String> BASE_WRAP_TYPE_LIST = new ArrayList<>();
    /**
     * base type list
     */
    private static final List<String> BASE_TYPE_LIST = new ArrayList<>();

    static {
        BASE_TYPE_LIST.add("int");
        BASE_TYPE_LIST.add("double");
        BASE_TYPE_LIST.add("long");
        BASE_TYPE_LIST.add("short");
        BASE_TYPE_LIST.add("byte");
        BASE_TYPE_LIST.add("boolean");
        BASE_TYPE_LIST.add("char");
        BASE_TYPE_LIST.add("float");

        BASE_WRAP_TYPE_LIST.add("java.lang.Integer");
        BASE_WRAP_TYPE_LIST.add("java.lang.Double");
        BASE_WRAP_TYPE_LIST.add("java.lang.Float");
        BASE_WRAP_TYPE_LIST.add("java.lang.Long");
        BASE_WRAP_TYPE_LIST.add("java.lang.Short");
        BASE_WRAP_TYPE_LIST.add("java.lang.Byte");
        BASE_WRAP_TYPE_LIST.add("java.lang.Boolean");
        BASE_WRAP_TYPE_LIST.add("java.lang.Character");
    }

    /**
     * 判断类名是否为Java基本类型（如int、double等）
     *
     * @param className 类的全限定名
     * @return 如果是基本类型返回true，否则返回false
     */
    /**
     * isPrimitive方法。
     *      * @param className String类型参数
     * @return static boolean类型返回值
     */
    /**
     * isPrimitive方法。
     *      * @param className String类型参数
     * @return static boolean类型返回值
     */
    public static boolean isPrimitive(String className) {
        return BASE_TYPE_LIST.contains(className);
    }

    /**
     * 判断类名是否为基本类型的包装类（如java.lang.Integer、java.lang.Double等）
     *
     * @param className 类的全限定名
     * @return 如果是包装类返回true，否则返回false
     */
    /**
     * isBaseWrap方法。
     *      * @param className String类型参数
     * @return static boolean类型返回值
     */
    /**
     * isBaseWrap方法。
     *      * @param className String类型参数
     * @return static boolean类型返回值
     */
    public static boolean isBaseWrap(String className) {
        return BASE_WRAP_TYPE_LIST.contains(className);
    }

    /**
     * 判断Class对象是否为基本类型的包装类
     *
     * @param clazz Class对象
     * @return 如果是包装类返回true，否则返回false
     */
    /**
     * isBaseWrap方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    /**
     * isBaseWrap方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    public static boolean isBaseWrap(Class<?> clazz) {
        return isBaseWrap(clazz.getCanonicalName());
    }

    /**
     * 判断类名是否为基本类型或其包装类
     *
     * @param className 类的全限定名
     * @return 如果是基本类型或包装类返回true，否则返回false
     */
    /**
     * isBaseOrWrap方法。
     *      * @param className String类型参数
     * @return static boolean类型返回值
     */
    /**
     * isBaseOrWrap方法。
     *      * @param className String类型参数
     * @return static boolean类型返回值
     */
    public static boolean isBaseOrWrap(String className) {
        return isPrimitive(className) || isBaseWrap(className);
    }

    /**
     * 判断Class对象是否为基本类型或其包装类
     *
     * @param clazz Class对象
     * @return 如果是基本类型或包装类返回true，否则返回false
     */
    /**
     * isBaseOrWrap方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    /**
     * isBaseOrWrap方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    public static boolean isBaseOrWrap(Class<?> clazz) {
        return isBaseOrWrap(clazz.getCanonicalName());
    }

    /**
     * 判断Class对象是否为基本类型、包装类或String类型
     *
     * @param clazz Class对象
     * @return 如果是基本类型、包装类或String返回true，否则返回false
     */
    /**
     * isBaseOrWrapOrString方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    /**
     * isBaseOrWrapOrString方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    public static boolean isBaseOrWrapOrString(Class<?> clazz) {
        return isBaseOrWrap(clazz.getCanonicalName()) || isSameClass(clazz, String.class);
    }

    /**
     * 判断对象是否为基本类型或其包装类的实例
     *
     * @param object 待检查的对象
     * @return 如果是基本类型或包装类实例返回true，null返回false
     */
    /**
     * isBaseOrWrap方法。
     *      * @param object Object类型参数
     * @return static boolean类型返回值
     */
    /**
     * isBaseOrWrap方法。
     *      * @param object Object类型参数
     * @return static boolean类型返回值
     */
    public static boolean isBaseOrWrap(Object object) {
        return null != object && isBaseOrWrap(object.getClass());
    }

    /**
     * 判断两个Class对象是否相同，通过类加载器和规范类名进行比较
     *
     * @param clazz 第一个Class对象
     * @param clz   第二个Class对象
     * @return 如果两者相同返回true，否则返回false
     */
    /**
     * isSameClass方法。
     *      * @param clazz Class?类型参数
     * @param clz Class?类型参数
     * @return static boolean类型返回值
     */
    /**
     * isSameClass方法。
     *      * @param clazz Class?类型参数
     * @param clz Class?类型参数
     * @return static boolean类型返回值
     */
    public static boolean isSameClass(Class<?> clazz, Class<?> clz) {
        if (null == clazz && null == clz) {
            return true;
        }
        if (null == clazz || null == clz) {
            return false;
        }
        return clazz.isAssignableFrom(clz) && clz.isAssignableFrom(clazz)
                && clazz.getCanonicalName().equals(clz.getCanonicalName())
                && clazz.getClassLoader() == clz.getClassLoader();
    }

    /**
     * 判断两个Class对象的类名是否相同（不考虑类加载器差异）
     *
     * @param clazz 第一个Class对象
     * @param clz   第二个Class对象
     * @return 如果类名相同返回true，否则返回false
     */
    /**
     * isSameNameClass方法。
     *      * @param clazz Class?类型参数
     * @param clz Class?类型参数
     * @return static boolean类型返回值
     */
    /**
     * isSameNameClass方法。
     *      * @param clazz Class?类型参数
     * @param clz Class?类型参数
     * @return static boolean类型返回值
     */
    public static boolean isSameNameClass(Class<?> clazz, Class<?> clz) {
        if (null == clazz && null == clz) {
            return true;
        }
        if (null == clazz || null == clz) {
            return false;
        }
        return clazz.getCanonicalName().equals(clz.getCanonicalName());
    }

    /**
     * 获取简短的类名，将包名中的每个部分缩写为首字母加点的形式
     * 例如：com.example.TestClass -> c.e.TestClass
     *
     * @param className 类的全限定名
     * @return 简化后的类名，如果输入为null则返回null
     */
    /**
     * getShortClassName方法。
     *      * @param className String类型参数
     * @return static String类型返回值
     */
    /**
     * getShortClassName方法。
     *      * @param className String类型参数
     * @return static String类型返回值
     */
    public static String getShortClassName(String className) {
        if (className == null) {
            return null;
        } else {
            String[] ss = className.split("\\.");
            StringBuilder sb = new StringBuilder(className.length());

            for (int i = 0; i < ss.length; ++i) {
                String s = ss[i];
                if (i != ss.length - 1) {
                    sb.append(s.charAt(0)).append('.');
                } else {
                    sb.append(s);
                }
            }

            return sb.toString();
        }
    }

    /**
     * 判断Class对象是否为JDK原生类型（如List.class、Map.class等）
     *
     * @param clazz Class对象
     * @return 如果是JDK原生类型返回true（即Class.getClassLoader()返回null）
     */
    /**
     * isOriginJdkType方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    /**
     * isOriginJdkType方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    public static boolean isOriginJdkType(Class<?> clazz) {
        return null == clazz.getClassLoader();
    }

    /**
     * 判断Class对象是否为Java原始数值类型（long、int、short、byte）
     *
     * @param clazz Class对象
     * @return 如果是原始数值类型返回true，否则返回false
     */
    /**
     * isPrimitiveNumberType方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    /**
     * isPrimitiveNumberType方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    public static boolean isPrimitiveNumberType(Class<?> clazz) {
        return long.class.isAssignableFrom(clazz) || int.class.isAssignableFrom(clazz)
                || short.class.isAssignableFrom(clazz) || byte.class.isAssignableFrom(clazz);
    }

    /**
     * 判断Class对象是否为Java原始浮点数值类型（double、float）
     *
     * @param clazz Class对象
     * @return 如果是原始浮点数值类型返回true，否则返回false
     */
    /**
     * isPrimitiveFloatingPointNumberType方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    /**
     * isPrimitiveFloatingPointNumberType方法。
     *      * @param clazz Class?类型参数
     * @return static boolean类型返回值
     */
    public static boolean isPrimitiveFloatingPointNumberType(Class<?> clazz) {
        return double.class.isAssignableFrom(clazz) || float.class.isAssignableFrom(clazz);
    }

    /**
     * 将对象数组转换为Class对象数组
     *
     * <p>如果对象为null，则对应位置返回null。基本类型会被转换为对应的包装类。</p>
     *
     * @param array Object对象数组
     * @return Class对象数组，如果输入为null则返回null
     */
    /**
     * toClass方法。
     *      * @param array final类型参数
     * @return static Class<?>[]类型返回值
     */
    /**
     * toClass方法。
     *      * @param array final类型参数
     * @return static Class<?>[]类型返回值
     */
    public static Class<?>[] toClass(final Object... array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return ArraysUtil.EMPTY_CLASS_ARRAY;
        }
        final Class<?>[] classes = new Class[array.length];
        for (int i = 0; i < array.length; i++) {
            classes[i] = array[i] == null ? null : array[i].getClass();
        }
        return classes;
    }

    /**
     * 将Class对象数组转换为字符串表示形式，格式为：(Class1Name, Class2Name, ...)
     *
     * @param argTypes Class对象数组
     * @return 格式化后的参数字符串
     */
    /**
     * argumentTypesToString方法。
     *      * @param argTypes Class?[]类型参数
     * @return static String类型返回值
     */
    /**
     * argumentTypesToString方法。
     *      * @param argTypes Class?[]类型参数
     * @return static String类型返回值
     */
    public static String argumentTypesToString(Class<?>[] argTypes) {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        if (argTypes != null) {
            for (int i = 0; i < argTypes.length; i++) {
                if (i > 0) {
                    buf.append(", ");
                }
                Class<?> c = argTypes[i];
                buf.append((c == null) ? "null" : c.getName());
            }
        }
        buf.append(")");
        return buf.toString();
    }

}
