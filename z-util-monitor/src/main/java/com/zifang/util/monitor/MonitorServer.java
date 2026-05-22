package com.zifang.util.monitor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.zifang.util.monitor.exporter.HtmlExporter;
import com.zifang.util.monitor.exporter.JsonExporter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

/**
 * 监控服务器
 * <p>
 * 内置 HTTP 服务器，提供监控指标的 Web UI 和 JSON API。
 * </p>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * // 创建并启动服务器
 * MonitorServer server = MonitorServer.builder()
 *     .port(8849)
 *     .enableJvm()      // 启用 JVM 指标
 *     .enableOs()       // 启用 OS 指标
 *     .enableThread()   // 启用线程指标
 *     .build()
 *     .start();
 *
 * // 注册自定义指标
 * server.register("myapp.counter", () -> MyCounter.getValue(), "自定义计数器");
 *
 * // 停止服务器
 * server.stop();
 * }</pre>
 */
public class MonitorServer {

    public static final int DEFAULT_PORT = 8849;

    private HttpServer httpServer;
    private final int port;
    private final MetricsCollector collector;
    private volatile boolean started = false;

    private MonitorServer(int port) {
        this.port = port;
        this.collector = new MetricsCollector();
    }

    /**
     * 创建构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 启动服务器
     */
    public MonitorServer start() throws IOException {
        if (started) {
            throw new IllegalStateException("Server already started");
        }

        httpServer = HttpServer.create(new InetSocketAddress(port), 0);

        // 注册路由
        httpServer.createContext("/", new IndexHandler(this));
        httpServer.createContext("/json", new JsonHandler(this));
        httpServer.createContext("/health", new HealthHandler(this));
        httpServer.createContext("/metrics", new MetricsHandler(this));

        // 设置线程池
        httpServer.setExecutor(Executors.newFixedThreadPool(4));

        // 启动服务器
        httpServer.start();
        started = true;

        System.out.println("MonitorServer started on http://localhost:" + port);
        System.out.println("  - Web UI: http://localhost:" + port + "/");
        System.out.println("  - JSON API: http://localhost:" + port + "/json");
        System.out.println("  - Health: http://localhost:" + port + "/health");

        return this;
    }

    /**
     * 停止服务器
     */
    public void stop() {
        if (!started || httpServer == null) {
            return;
        }
        httpServer.stop(0);
        started = false;
        System.out.println("MonitorServer stopped");
    }

    /**
     * 重新启动服务器
     */
    public MonitorServer restart() throws IOException {
        stop();
        Thread.yield();
        return start();
    }

    public int getPort() {
        return port;
    }

    public MetricsCollector getCollector() {
        return collector;
    }

    public MetricsRegistry getRegistry() {
        return collector.getRegistry();
    }

    public boolean isStarted() {
        return started;
    }

    // ========== 内置指标启用方法 ==========

    /**
     * 启用所有内置指标（JVM、OS、线程）
     */
    public MonitorServer enableAll() {
        collector.enableAll();
        return this;
    }

    /**
     * 启用 JVM 指标
     */
    public MonitorServer enableJvm() {
        collector.enableJvm();
        return this;
    }

    /**
     * 启用操作系统指标
     */
    public MonitorServer enableOs() {
        collector.enableOs();
        return this;
    }

    /**
     * 启用线程指标
     */
    public MonitorServer enableThread() {
        collector.enableThread();
        return this;
    }

    /**
     * 注册自定义指标
     */
    public MonitorServer register(String name, java.util.function.Supplier<Object> provider) {
        collector.getRegistry().register(name, MetricsSnapshot.Category.CUSTOM, provider);
        return this;
    }

    /**
     * 注册自定义指标（带描述）
     */
    public MonitorServer register(String name, java.util.function.Supplier<Object> provider, String description) {
        collector.getRegistry().register(name, MetricsSnapshot.Category.CUSTOM, provider, description, null);
        return this;
    }

    /**
     * 注册自定义指标（带描述和单位）
     */
    public MonitorServer register(String name, java.util.function.Supplier<Object> provider, String description, String unit) {
        collector.getRegistry().register(name, MetricsSnapshot.Category.CUSTOM, provider, description, unit);
        return this;
    }

    // ========== HTTP Handlers ==========

    static class IndexHandler implements HttpHandler {
        private final MonitorServer server;

        IndexHandler(MonitorServer server) {
            this.server = server;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = new HtmlExporter(server.getRegistry()).export();
            sendResponse(exchange, "text/html; charset=utf-8", response);
        }
    }

    static class JsonHandler implements HttpHandler {
        private final MonitorServer server;

        JsonHandler(MonitorServer server) {
            this.server = server;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = new JsonExporter(server.getRegistry()).export();
            sendResponse(exchange, "application/json; charset=utf-8", response);
        }
    }

    static class HealthHandler implements HttpHandler {
        private final MonitorServer server;

        HealthHandler(MonitorServer server) {
            this.server = server;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String status = server.getRegistry().isEnabled() ? "UP" : "DOWN";
            String response = "{\"status\":\"" + status + "\",\"timestamp\":" + System.currentTimeMillis() + "}";
            sendResponse(exchange, "application/json; charset=utf-8", response);
        }
    }

    static class MetricsHandler implements HttpHandler {
        private final MonitorServer server;

        MetricsHandler(MonitorServer server) {
            this.server = server;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 重定向到 /json
            exchange.getResponseHeaders().set("Location", "/json");
            exchange.sendResponseHeaders(302, -1);
        }
    }

    private static void sendResponse(HttpExchange exchange, String contentType, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // ========== Builder ==========

    public static class Builder {
        private int port = DEFAULT_PORT;
        private boolean enableJvm = false;
        private boolean enableOs = false;
        private boolean enableThread = false;

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder enableJvm() {
            this.enableJvm = true;
            return this;
        }

        public Builder enableOs() {
            this.enableOs = true;
            return this;
        }

        public Builder enableThread() {
            this.enableThread = true;
            return this;
        }

        public Builder enableAll() {
            return enableJvm().enableOs().enableThread();
        }

        public MonitorServer build() {
            MonitorServer server = new MonitorServer(port);
            if (enableJvm) {
                server.collector.enableJvm();
            }
            if (enableOs) {
                server.collector.enableOs();
            }
            if (enableThread) {
                server.collector.enableThread();
            }
            return server;
        }
    }
}
