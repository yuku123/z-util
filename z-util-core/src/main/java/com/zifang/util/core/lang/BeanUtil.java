package com.zifang.util.core.lang;

import com.zifang.util.core.lang.reflect.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 提供Bean的相关操作
 *
 * @author zifang
 */
public class BeanUtil {

    private static final Logger log = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 检测传入的Object是否为标准的Bean
     * 规则:
     * 1. Bean 应该具有默认构造函数（无参数）。
     * 2. Bean 应该提供 getter 和 setter 方法。
     * 3. 使用 getter 方法读取可读属性的值。
     * 4. Bean 应该实现 java.io.serializable
     */
    public static <T> boolean isBean(T bean) {
        // @todo
        if (ClassUtil.isNormalClass(bean.getClass())) {
            final Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
                    // 检测包含标准的setXXX方法即视为标准的JavaBean
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * t通过序列化的方式进行深复制
     */
    public static <T> T cloneBean(final T bean) throws IllegalAccessException, InstantiationException, InvocationTargetException, IntrospectionException {
        T t = (T) bean.getClass().newInstance();
        PropertyDescriptor[] pro = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : pro) {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            Method readMethod = propertyDescriptor.getReadMethod();
            writeMethod.invoke(t, readMethod.invoke(bean));
        }
        return t;
    }

    /**
     * 输入map，输出组装好了的bean
     */
    public static <T> T mapToBean(Class<T> clazz, Map<String, ? extends Object> map) throws IllegalAccessException, InstantiationException, IntrospectionException {
        T t = clazz.newInstance();
        PropertyDescriptor[] pro = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : pro) {
            String name = propertyDescriptor.getName();
            Method method = propertyDescriptor.getWriteMethod();
            if (map.keySet().contains(name)) {
                try {
                    method.invoke(t, map.get(name));
                } catch (Exception e) {
                    log.warn("mapToBean failed for property '{}' on {}", name, clazz, e);
                }
            }
        }
        return t;
    }

    /**
     * 输入bean 输出这个bean第一层value值
     */
    public static <T> Map<String, Object> beanToMap(final T t) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Map<String, Object> map = new LinkedHashMap<>();
        PropertyDescriptor[] pro = Introspector.getBeanInfo(t.getClass(), Object.class).getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : pro) {
            String key = propertyDescriptor.getName();
            Method readMethod = propertyDescriptor.getReadMethod();
            Object value = readMethod.invoke(t);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 对一个贫血对象设入参数
     *
     * @param obj   等待设入
     * @param name  设入的字段名
     * @param value 设入的字段值
     */
    public static void setProperty(Object obj, String name, Object value) {
        try {
            PropertyDescriptor[] pro = Introspector.getBeanInfo(obj.getClass(), Object.class).getPropertyDescriptors();
            PropertyDescriptor propertyDescriptor = findPropertyDescriptorByName(pro, name);
            if (propertyDescriptor != null) {
                propertyDescriptor.getWriteMethod().invoke(obj, value);
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            log.warn("setProperty failed for property '{}' on {}", name, obj == null ? null : obj.getClass(), e);
        }
    }

    /**
     * 得到一个Object的字段值
     *
     * @param obj  等待被摄取的实例
     * @param name 摄取的字段名
     * @return 字段值
     */
    public static Object getProperty(Object obj, String name) {
        try {
            PropertyDescriptor[] pro = Introspector.getBeanInfo(obj.getClass(), Object.class).getPropertyDescriptors();
            PropertyDescriptor propertyDescriptor = findPropertyDescriptorByName(pro, name);
            if (propertyDescriptor != null) {
                return propertyDescriptor.getReadMethod().invoke(obj);
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            log.warn("getProperty failed for property '{}' on {}", name, obj == null ? null : obj.getClass(), e);
        }
        return null;
    }

    /**
     * 将 source 对象中的非空属性值覆盖到 target 对象的同名属性上，
     * source 中为 null 的属性保持 target 原值不变，用于两个同类型对象的属性合并。
     * <p>
     * 遍历 source 类（含父类）的全部声明字段并跳过静态字段；
     * 仅当 source 侧字段取值非 null 时写入 target。
     *
     * @param sourceBean 提取属性值的对象；为 null 时不做任何修改
     * @param targetBean 被覆盖合并的对象；为 null 时直接返回 null
     * @param <T>        对象类型
     * @return 合并后的 targetBean
     */
    public static <T> T combineObject(T sourceBean, T targetBean) {
        if (sourceBean == null || targetBean == null) {
            return targetBean;
        }
        for (Class<?> clazz = sourceBean.getClass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Object value = field.get(sourceBean);
                    if (value != null) {
                        field.set(targetBean, value);
                    }
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    log.warn("combineObject skip field '{}' on {}", field.getName(), sourceBean.getClass(), e);
                }
            }
        }
        return targetBean;
    }

    /**
     * 判断对象的所有实例字段取值是否全部为 null。
     * <p>
     * 遍历对象类层次（含父类）的全部声明字段并跳过静态字段；
     * 只要存在一个非 null 取值即返回 false。常用于判断一个条件对象是否未携带任何条件。
     *
     * @param bean 待判断对象
     * @return 对象为 null 或全部字段为 null 时返回 true；存在任一非 null 字段返回 false
     */
    public static boolean isAllFieldValueNull(Object bean) {
        if (bean == null) {
            return true;
        }
        for (Class<?> clazz = bean.getClass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    if (field.get(bean) != null) {
                        return false;
                    }
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    log.warn("isAllFieldValueNull skip field '{}' on {}", field.getName(), bean.getClass(), e);
                }
            }
        }
        return true;
    }

    /**
     * 找到所需要的handle
     */
    private static PropertyDescriptor findPropertyDescriptorByName(PropertyDescriptor[] pro, String name) {
        for (PropertyDescriptor propertyDescriptor : pro) {
            if (name.equals(propertyDescriptor.getName())) {
                return propertyDescriptor;
            }
        }
        return null;
    }

    /**
     * 拆解bean 可以很方便得到bean的一些参数
     * */
}
