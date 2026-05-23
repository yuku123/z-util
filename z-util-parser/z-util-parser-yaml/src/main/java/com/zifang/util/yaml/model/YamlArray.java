package com.zifang.util.yaml.model;

import com.zifang.util.yaml.define.YamlNodeType;

import java.util.*;

/**
 * YAML 序列节点，对应 YAML 中的序列（sequence）结构，
 * 即有序的元素列表，类似 JSON 中的数组。
 *
 * @author zifang
 */
public class YamlArray implements List<Object> {

    private final List<Object> delegate = new ArrayList<>();

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Object get(int index) {
        return delegate.get(index);
    }

    @Override
    public boolean add(Object element) {
        return delegate.add(element);
    }

    @Override
    public Object set(int index, Object element) {
        return delegate.set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        delegate.add(index, element);
    }

    @Override
    public Object remove(int index) {
        return delegate.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Iterator<Object> iterator() {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        return delegate.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        return delegate.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> c) {
        return delegate.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return delegate.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return delegate.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }

    public YamlNodeType getNodeType() {
        return YamlNodeType.SEQUENCE;
    }

    /**
     * 获取指定索引的字符串值。
     *
     * @param index 索引
     * @return 字符串值，若不存在或类型不匹配返回 null
     */
    public String getString(int index) {
        Object val = get(index);
        return val instanceof String ? (String) val : null;
    }

    /**
     * 获取指定索引的整数值。
     *
     * @param index 索引
     * @return 整数值，若不存在或类型不匹配返回 null
     */
    public Integer getInt(int index) {
        Object val = get(index);
        if (val instanceof Number) {
            return ((Number) val).intValue();
        }
        return null;
    }

    /**
     * 获取指定索引的嵌套映射。
     *
     * @param index 索引
     * @return YamlMap，若不存在或类型不匹配返回 null
     */
    public YamlMap getMap(int index) {
        Object val = get(index);
        return val instanceof YamlMap ? (YamlMap) val : null;
    }

    /**
     * 获取指定索引的嵌套列表。
     *
     * @param index 索引
     * @return YamlArray，若不存在或类型不匹配返回 null
     */
    public YamlArray getArray(int index) {
        Object val = get(index);
        return val instanceof YamlArray ? (YamlArray) val : null;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YamlArray)) return false;
        return delegate.equals(((YamlArray) o).delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
