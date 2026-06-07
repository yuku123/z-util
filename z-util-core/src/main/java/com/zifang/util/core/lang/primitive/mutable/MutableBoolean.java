package com.zifang.util.core.lang.primitive.mutable;

import com.zifang.util.core.lang.primitive.BooleanUtil;

import java.io.Serializable;

/**
 * @author: zifang
 * @time: 2022-06-08 10:17:22
 * @description: A mutable {@code boolean} wrapper. Note that as MutableBoolean does not extend
 * Boolean, it is not treated by String.format() as a Boolean parameter.
 * @version: JDK 1.8
 */
/**
 * MutableBoolean类。
 */
/**
 * MutableBoolean类。
 */
public class MutableBoolean implements Mutable<Boolean>, Comparable<MutableBoolean>, Serializable,
        Cloneable {

    /**
     * Required for serialization support.
     *
     * @see Serializable
     */
    private static final long serialVersionUID = 8748555473950486620L;

    /**
     * The mutable value.
     */
    private boolean value;

    /**
     * Constructs a new MutableBoolean with the default value of false.
     */
    /**
     * MutableBoolean方法。
     */
    /**
     * MutableBoolean方法。
     */
    public MutableBoolean() {
    }

    /**
     * Constructs a new MutableBoolean with the specified value.
     *
     * @param value the initial value to store
     */
    /**
     * MutableBoolean方法。
     *      * @param value final类型参数
     */
    /**
     * MutableBoolean方法。
     *      * @param value final类型参数
     */
    public MutableBoolean(final boolean value) {
        this.value = value;
    }

    /**
     * Constructs a new MutableBoolean with the specified value.
     *
     * @param value the initial value to store, not null
     * @throws NullPointerException if the object is null
     */
    /**
     * MutableBoolean方法。
     *      * @param value final类型参数
     */
    /**
     * MutableBoolean方法。
     *      * @param value final类型参数
     */
    public MutableBoolean(final Boolean value) {
        this.value = value;
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the value as a Boolean instance.
     *
     * @return the value as a Boolean, never null
     */
    @Override
    /**
     * getValue方法。
     * @return boolean类型返回值
     */
    /**
     * getValue方法。
     * @return boolean类型返回值
     */
    public Boolean getValue() {
        return this.value;
    }

    /**
     * Sets the value.
     *
     * @param value the value to set
     */
    /**
     * setValue方法。
     *      * @param value final类型参数
     */
    /**
     * setValue方法。
     *      * @param value final类型参数
     */
    public void setValue(final boolean value) {
        this.value = value;
    }

    /**
     * Sets the value from any Boolean instance.
     *
     * @param value the value to set, not null
     * @throws NullPointerException if the object is null
     */
    @Override
    /**
     * setValue方法。
     *      * @param value final类型参数
     */
    /**
     * setValue方法。
     *      * @param value final类型参数
     */
    public void setValue(final Boolean value) {
        this.value = value;
    }

    /**
     * Sets the value to false.
     */
    /**
     * setFalse方法。
     */
    /**
     * setFalse方法。
     */
    public void setFalse() {
        this.value = false;
    }

    /**
     * Sets the value to true.
     */
    /**
     * setTrue方法。
     */
    /**
     * setTrue方法。
     */
    public void setTrue() {
        this.value = true;
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if the current value is {@code true}.
     *
     * @return {@code true} if the current value is {@code true}
     */
    /**
     * isTrue方法。
     * @return boolean类型返回值
     */
    /**
     * isTrue方法。
     * @return boolean类型返回值
     */
    public boolean isTrue() {
        return value;
    }

    /**
     * Checks if the current value is {@code false}.
     *
     * @return {@code true} if the current value is {@code false}
     */
    /**
     * isFalse方法。
     * @return boolean类型返回值
     */
    /**
     * isFalse方法。
     * @return boolean类型返回值
     */
    public boolean isFalse() {
        return !value;
    }

    //-----------------------------------------------------------------------

    /**
     * Returns the value of this MutableBoolean as a boolean.
     *
     * @return the boolean value represented by this object.
     */
    /**
     * booleanValue方法。
     * @return boolean类型返回值
     */
    /**
     * booleanValue方法。
     * @return boolean类型返回值
     */
    public boolean booleanValue() {
        return value;
    }

    //-----------------------------------------------------------------------

    /**
     * Gets this mutable as an instance of Boolean.
     *
     * @return a Boolean instance containing the value from this mutable, never null
     */
    /**
     * toBoolean方法。
     * @return boolean类型返回值
     */
    /**
     * toBoolean方法。
     * @return boolean类型返回值
     */
    public Boolean toBoolean() {
        return booleanValue();
    }

    //-----------------------------------------------------------------------

    /**
     * Compzifang this object to the specified object. The result is {@code true} if and only if the
     * argument is not {@code null} and is an {@code MutableBoolean} object that contains the same
     * {@code boolean} value as this object.
     *
     * @param obj the object to compare with, null returns false
     * @return {@code true} if the objects are the same; {@code false} otherwise.
     */
    @Override
    /**
     * equals方法。
     *      * @param obj final类型参数
     * @return boolean类型返回值
     */
    /**
     * equals方法。
     *      * @param obj final类型参数
     * @return boolean类型返回值
     */
    public boolean equals(final Object obj) {
        if (obj instanceof MutableBoolean) {
            return value == ((MutableBoolean) obj).booleanValue();
        }
        return false;
    }

    /**
     * Returns a suitable hash code for this mutable.
     *
     * @return the hash code returned by {@code Boolean.TRUE} or {@code Boolean.FALSE}
     */
    @Override
    /**
     * hashCode方法。
     * @return int类型返回值
     */
    /**
     * hashCode方法。
     * @return int类型返回值
     */
    public int hashCode() {
        return value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
    }

    //-----------------------------------------------------------------------

    /**
     * Compzifang this mutable to another in ascending order.
     *
     * @param other the other mutable to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater where false is less than
     * true
     */
    @Override
    /**
     * compareTo方法。
     *      * @param other final类型参数
     * @return int类型返回值
     */
    /**
     * compareTo方法。
     *      * @param other final类型参数
     * @return int类型返回值
     */
    public int compareTo(final MutableBoolean other) {
        return BooleanUtil.compare(this.value, other.value);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns the String value of this mutable.
     *
     * @return the mutable value as a string
     */
    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    /**
     * clone方法。
     * @return MutableBoolean类型返回值
     */
    /**
     * clone方法。
     * @return MutableBoolean类型返回值
     */
    public MutableBoolean clone() {
        return new MutableBoolean(this.value);
    }
}
