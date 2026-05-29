package com.zifang.util.json.facade;

/**
 * JsonFacade 工厂类，统一获取后端实现。
 * <p>
 * 默认顺序：Gson → Jackson → FastJSON
 * 通过系统属性或配置文件可切换：{@code -Djson.backend=gson|jackson|fastjson}
 */
public class JsonFacadeFactory {

    private static final String BACKEND_KEY = "json.backend";
    private static volatile JsonFacade INSTANCE;

    private JsonFacadeFactory() {}

    /**
     * 获取默认的 JsonFacade 实例（线程安全单例）。
     * <p>
     * 优先级：Gson > Jackson > FastJSON
     */
    public static JsonFacade getDefault() {
        if (INSTANCE == null) {
            synchronized (JsonFacadeFactory.class) {
                if (INSTANCE == null) {
                    String backend = System.getProperty(BACKEND_KEY, "").toLowerCase();
                    switch (backend) {
                        case "jackson":
                            INSTANCE = new JacksonBackend();
                            break;
                        case "gson":
                        default:
                            INSTANCE = new GsonBackend();
                            break;
                    }
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取指定后端的 JsonFacade 实例。
     */
    public static JsonFacade get(String backend) {
        switch (backend.toLowerCase()) {
            case "jackson":
                return new JacksonBackend();
            case "gson":
            default:
                return new GsonBackend();
        }
    }

    /**
     * 重置实例（用于测试切换后端）。
     */
    public static void reset() {
        INSTANCE = null;
    }
}