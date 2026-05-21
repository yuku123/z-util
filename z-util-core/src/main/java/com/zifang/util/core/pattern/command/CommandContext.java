package com.zifang.util.core.pattern.command;

import java.util.*;

/**
 * 命令上下文
 * <p>
 * 携带命令执行所需的数据和状态
 *
 * @author zifang
 */
public class CommandContext implements Map<String, Object> {

    private final Map<String, Object> data;
    private final List<Command<?>> executedCommands;
    private boolean interrupted;

    public CommandContext() {
        this.data = new HashMap<>();
        this.executedCommands = new ArrayList<>();
        this.interrupted = false;
    }

    public CommandContext(Map<String, Object> initialData) {
        this.data = new HashMap<>(initialData);
        this.executedCommands = new ArrayList<>();
        this.interrupted = false;
    }

    /**
     * 添加已执行的命令
     */
    public void addExecutedCommand(Command<?> command) {
        this.executedCommands.add(command);
    }

    /**
     * 获取已执行的命令列表
     */
    public List<Command<?>> getExecutedCommands() {
        return Collections.unmodifiableList(executedCommands);
    }

    /**
     * 中断命令执行
     */
    public void interrupt() {
        this.interrupted = true;
    }

    /**
     * 是否被中断
     */
    public boolean isInterrupted() {
        return interrupted;
    }

    /**
     * 获取值并类型转换
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = data.get(key);
        if (value != null && !type.isInstance(value)) {
            throw new ClassCastException("Value for key " + key + " is not of type " + type);
        }
        return (T) value;
    }

    /**
     * 获取值，带默认值
     */
    public Object getOrDefault(String key, Object defaultValue) {
        Object value = data.get(key);
        return value != null ? value : defaultValue;
    }

    // Map接口实现

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return data.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return data.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return data.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        data.putAll(m);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public Set<String> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<Object> values() {
        return data.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return data.entrySet();
    }

    @Override
    public String toString() {
        return "CommandContext{" +
                "data=" + data +
                ", executedCommands=" + executedCommands.size() +
                ", interrupted=" + interrupted +
                '}';
    }
}
