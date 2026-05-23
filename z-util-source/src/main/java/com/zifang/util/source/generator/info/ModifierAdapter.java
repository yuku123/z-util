package com.zifang.util.source.generator.info;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 修饰符适配器
 * <p>
 * 用于将java.lang.reflect.Modifier中的修饰符常量转换为字符串关键字。
 * 支持 public、private、protected、static、final、abstract 等常见修饰符的映射转换。
 *
 * @author zifang
 * @version 1.0.0
 */
public class ModifierAdapter {

    private static final Map<Integer, String> MODIFIER_KEYWORDS = new ConcurrentHashMap<>();

    static {
        MODIFIER_KEYWORDS.put(Modifier.PUBLIC, "public");
        MODIFIER_KEYWORDS.put(Modifier.PRIVATE, "private");
        MODIFIER_KEYWORDS.put(Modifier.PROTECTED, "protected");
        MODIFIER_KEYWORDS.put(Modifier.STATIC, "static");
        MODIFIER_KEYWORDS.put(Modifier.FINAL, "final");
        MODIFIER_KEYWORDS.put(Modifier.ABSTRACT, "abstract");
        MODIFIER_KEYWORDS.put(Modifier.SYNCHRONIZED, "synchronized");
        MODIFIER_KEYWORDS.put(Modifier.VOLATILE, "volatile");
        MODIFIER_KEYWORDS.put(Modifier.TRANSIENT, "transient");
        MODIFIER_KEYWORDS.put(Modifier.NATIVE, "native");
        MODIFIER_KEYWORDS.put(Modifier.STRICT, "strictfp");
    }

    private ModifierAdapter() {
    }

    /**
     * 获取修饰符关键字字符串
     *
     * @param modifier java.lang.reflect.Modifier 中的修饰符常量值
     * @return 对应的关键字字符串（如 "public"、"static"）
     * @throws IllegalArgumentException 如果修饰符值未知
     */
    public static String getKeyword(int modifier) {
        String keyword = MODIFIER_KEYWORDS.get(modifier);
        if (keyword == null) {
            throw new IllegalArgumentException("不支持的修饰符值: " + modifier);
        }
        return keyword;
    }

    /**
     * 将多个修饰符转换为关键字字符串列表
     *
     * @param modifiers 修饰符值（可以是多个修饰符的组合，如 Modifier.PUBLIC | Modifier.STATIC）
     * @return 关键字字符串列表
     */
    public static String toKeywords(int modifiers) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> entry : MODIFIER_KEYWORDS.entrySet()) {
            if ((modifiers & entry.getKey()) != 0) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(entry.getValue());
            }
        }
        return sb.toString();
    }

    /**
     * 检查是否为已知修饰符
     */
    public static boolean isKnown(int modifier) {
        return MODIFIER_KEYWORDS.containsKey(modifier);
    }
}
