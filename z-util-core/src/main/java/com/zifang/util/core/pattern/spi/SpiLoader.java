package com.zifang.util.core.pattern.spi;

/**
 * SPI（Service Provider Interface）加载器，用于动态加载实现了指定接口的实例。
 * 通过SPI机制，可以实现接口与实现类的解耦，支持插件化扩展。
 *
 * @param <T> 要加载的SPI接口类型
 * @author zifang
 */
public class SpiLoader<T> {

    private Class<T> clazz;

    /**
     * 获取一个SPI加载器实例。
     *
     * @param clazz SPI接口的Class对象，不能为null
     * @param <T>   SPI接口类型
     * @return SPI加载器实例
     * @throws IllegalArgumentException 如果clazz为null
     */
    public static <T> SpiLoader<T> getSpiLoader(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("SPI接口类型不能为null");
        }
        SpiLoader<T> spiLoader = new SpiLoader<>();
        spiLoader.setClazz(clazz);
        return spiLoader;
    }

    /**
     * 设置要加载的SPI接口类型。
     *
     * @param clazz SPI接口的Class对象
     */
    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

}
