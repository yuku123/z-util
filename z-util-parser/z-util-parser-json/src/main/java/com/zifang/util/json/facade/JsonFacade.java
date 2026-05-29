package com.zifang.util.json.facade;

/**
 * JSON 序列化/反序列化统一门面接口。
 * <p>
 * 支持切换不同后端实现（Gson / Jackson / FastJSON）。
 * <p>
 * 使用示例：
 * <pre>
 * JsonFacade facade = JsonFacadeFactory.getDefault();
 * String json = facade.toJson(user);
 * User u = facade.fromJson(json, User.class);
 * </pre>
 */
public interface JsonFacade {

    // ==================== 序列化 ====================

    /**
     * 将任意对象序列化为 JSON 字符串。
     */
    String toJson(Object obj);

    /**
     * 将任意对象序列化为格式化的（带缩进）JSON 字符串。
     */
    String toPrettyJson(Object obj);

    // ==================== 反序列化 ====================

    /**
     * 将 JSON 字符串反序列化为指定类型。
     */
    <T> T fromJson(String json, Class<T> clazz);

    /**
     * 将 JSON 字符串反序列化为泛型类型（支持复杂类型）。
     *
     * @param json        JSON 字符串
     * @param typeBinding 类型引用，如 {@code new TypeReference<List<User>>() {}}
     * @return 反序列化后的对象
     */
    <T> T fromJson(String json, TypeBinding<T> typeBinding);

    // ==================== JSON 结构操作 ====================

    /**
     * 将 JSON 字符串解析为 JsonObject（DOM 方式）。
     */
    JsonNode parseObject(String json);

    /**
     * 将 JSON 字符串解析为 JsonArray（DOM 方式）。
     */
    JsonNode parseArray(String json);

    // ==================== JSONPath 查询 ====================

    /**
     * 使用 JsonPath 表达式查询 JSON 数据。
     *
     * @param json JSON 字符串
     * @param path JsonPath 表达式，如 {@code "$.store.book[*].author"}
     * @return 查询结果列表
     */
    default Object query(String json, String path) {
        throw new UnsupportedOperationException("JSONPath 查询暂未实现，请使用支持该功能的实现类");
    }

    // ==================== 辅助 ====================

    /**
     * 当前使用的后端引擎名称。
     */
    String engine();

    /**
     * 判断是否为有效的 JSON 字符串。
     */
    default boolean isValidJson(String json) {
        if (json == null || json.trim().isEmpty()) return false;
        try {
            fromJson(json, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}