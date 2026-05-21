package com.zifang.util.json.model;

import com.zifang.util.json.BeautifyJsonUtils;
import com.zifang.util.json.exception.JsonTypeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * JSON数组模型，表示一个JSON数组（有序的元素列表）。
 * <p>
 * 实现了Iterable接口，可以直接迭代遍历数组元素。
 *
 * @author zifang
 * @see JsonObject
 */
public class JsonArray implements Iterable {

    private List list = new ArrayList();

    /**
     * 向数组中添加一个元素。
     *
     * @param obj 元素，可以是基本类型、String、JsonObject、JsonArray或null
     */
    public void add(Object obj) {
        list.add(obj);
    }

    /**
     * 根据索引获取元素。
     *
     * @param index 索引位置
     * @return 对应的元素
     */
    public Object get(int index) {
        return list.get(index);
    }

    /**
     * 获取数组长度。
     *
     * @return 元素数量
     */
    public int size() {
        return list.size();
    }

    /**
     * 获取指定索引处的JsonObject元素。
     *
     * @param index 索引位置
     * @return JsonObject元素
     * @throws JsonTypeException 如果该位置不是JsonObject类型
     */
    public JsonObject getJsonObject(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JsonObject)) {
            throw new JsonTypeException("Type of value is not JsonObject");
        }

        return (JsonObject) obj;
    }

    /**
     * 获取指定索引处的JsonArray元素。
     *
     * @param index 索引位置
     * @return JsonArray元素
     * @throws JsonTypeException 如果该位置不是JsonArray类型
     */
    public JsonArray getJsonArray(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JsonArray)) {
            throw new JsonTypeException("Type of value is not JsonArray");
        }

        return (JsonArray) obj;
    }

    @Override
    public String toString() {
        return BeautifyJsonUtils.beautify(this);
    }

    /**
     * 返回数组的迭代器。
     *
     * @return 迭代器实例
     */
    public Iterator iterator() {
        return list.iterator();
    }
}
