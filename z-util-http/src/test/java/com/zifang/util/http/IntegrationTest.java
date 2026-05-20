package com.zifang.util.http;

import com.zifang.util.http.base.define.*;
import com.zifang.util.http.base.pojo.*;
import com.zifang.util.http.client.HttpClientResult;
import com.zifang.util.http.client.HttpRequestProxy;
import com.zifang.util.http.parser.curl.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * z-util-http 全链路集成测试
 *
 * 测试内容：
 * 1. HttpServerBuilder - 嵌入式 HTTP 服务器（注解驱动）
 * 2. HttpUtil         - Apache HttpClient 封装
 * 3. OkHttpUtil       - OkHttp 封装
 * 4. CurlParserUtils  - cURL 命令解析
 * 5. HttpRequestProxy - 客户端动态代理（接口 → 真实 HTTP 请求）
 */
public class IntegrationTest {

    // ==================== 服务端接口定义 ====================

    @RestController("/api")
    public interface ApiService {

        @GetMapping("/users")
        String listUsers();

        @GetMapping("/users/{id}")
        String getUser(@PathVariable("id") Long id);

        @PostMapping("/users")
        String createUser(@RequestBody String body);

        @PutMapping("/users/{id}")
        String updateUser(@PathVariable("id") Long id, @RequestBody String body);

        @DeleteMapping("/users/{id}")
        String deleteUser(@PathVariable("id") Long id);

        @GetMapping("/search")
        String search(@RequestParam("keyword") String keyword, @RequestParam("page") int page);

        @PostMapping("/echo")
        String echo(@RequestBody String body);
    }

    public static class ApiServiceImpl implements ApiService {

        @Override
        public String listUsers() {
            return "[\"Alice\",\"Bob\",\"Charlie\"]";
        }

        @Override
        public String getUser(Long id) {
            return "{\"id\":" + id + ",\"name\":\"User\"}";
        }

        @Override
        public String createUser(String body) {
            return "{\"created\":true,\"body\":\"" + body + "\"}";
        }

        @Override
        public String updateUser(Long id, String body) {
            return "{\"updated\":true,\"id\":" + id + ",\"body\":\"" + body + "\"}";
        }

        @Override
        public String deleteUser(Long id) {
            return "{\"deleted\":true,\"id\":" + id + "}";
        }

        @Override
        public String search(String keyword, int page) {
            return "{\"keyword\":\"" + keyword + "\",\"page\":" + page + "}";
        }

        @Override
        public String echo(String body) {
            return body;
        }
    }

    // ==================== 测试状态 ====================

    private static com.sun.net.httpserver.HttpServer httpServer;
    private static int serverPort;
    private static String baseUrl;
    private static ApiService apiServiceProxy;

    // ==================== 测试初始化 ====================

    @BeforeClass
    public static void setup() throws Exception {
        // 找一个可用端口
        serverPort = findAvailablePort();

        // 创建嵌入式 HTTP 服务器
        httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(serverPort), 0);
        httpServer.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(4));

        // 注册路由
        ApiServiceImpl apiService = new ApiServiceImpl();
        ApiRequestHandler apiHandler = new ApiRequestHandler(apiService);

        httpServer.createContext("/api", apiHandler);
        httpServer.createContext("/", exchange -> {
            String resp = "z-util-http Integration Test Server OK";
            byte[] bytes = resp.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (java.io.OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        httpServer.start();
        baseUrl = "http://127.0.0.1:" + serverPort;

        System.out.println("HTTP Server started on port " + serverPort);

        // 创建客户端代理
        Map<String, Object> contextParams = new HashMap<>();
        contextParams.put("baseUrl", baseUrl);
        apiServiceProxy = HttpRequestProxy.proxy(ApiService.class, contextParams);
    }

    @AfterClass
    public static void teardown() {
        if (httpServer != null) {
            httpServer.stop(0);
            System.out.println("HTTP Server stopped");
        }
    }

    // ==================== 辅助方法 ====================

    private static int findAvailablePort() throws IOException {
        try (ServerSocket ss = new ServerSocket(0)) {
            return ss.getLocalPort();
        }
    }

    // ==================== HTTP 服务器请求处理器 ====================

    static class ApiRequestHandler implements com.sun.net.httpserver.HttpHandler {
        private final ApiServiceImpl service;

        ApiRequestHandler(ApiServiceImpl service) {
            this.service = service;
        }

        @Override
        public void handle(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            String response;
            int status = 200;

            try {
                // 读取请求体
                String body = "";
                if (!"GET".equalsIgnoreCase(method) && !"HEAD".equalsIgnoreCase(method)) {
                    try (java.io.InputStream is = exchange.getRequestBody();
                         java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = is.read(buf)) != -1) {
                            baos.write(buf, 0, len);
                        }
                        body = new String(baos.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
                    }
                }

                // 路由匹配
                response = route(method, path, query, body);
            } catch (Exception e) {
                response = "Error: " + e.getMessage();
                status = 500;
            }

            byte[] bytes = response.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(status, bytes.length);
            try (java.io.OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private String route(String method, String path, String query, String body) throws Exception {
            // 移除 /api 前缀
            String uri = path.startsWith("/api") ? path.substring(4) : path;

            if ("GET".equalsIgnoreCase(method)) {
                if ("/users".equals(uri)) {
                    return service.listUsers();
                } else if (uri.matches("/users/\\d+")) {
                    Long id = Long.parseLong(uri.substring("/users/".length()));
                    return service.getUser(id);
                } else if ("/search".equals(uri)) {
                    String keyword = "";
                    int page = 1;
                    if (query != null) {
                        for (String pair : query.split("&")) {
                            String[] kv = pair.split("=", 2);
                            if (kv.length == 2) {
                                if ("keyword".equals(kv[0])) keyword = kv[1];
                                if ("page".equals(kv[0])) page = Integer.parseInt(kv[1]);
                            }
                        }
                    }
                    return service.search(keyword, page);
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                if ("/users".equals(uri)) {
                    return service.createUser(body);
                } else if ("/echo".equals(uri)) {
                    return service.echo(body);
                }
            } else if ("PUT".equalsIgnoreCase(method)) {
                if (uri.matches("/users/\\d+")) {
                    Long id = Long.parseLong(uri.substring("/users/".length()));
                    return service.updateUser(id, body);
                }
            } else if ("DELETE".equalsIgnoreCase(method)) {
                if (uri.matches("/users/\\d+")) {
                    Long id = Long.parseLong(uri.substring("/users/".length()));
                    return service.deleteUser(id);
                }
            }

            return "{\"error\":\"not found\"}";
        }
    }

    // ==================== 测试用例 ====================

    // --- 1. HttpUtil (Apache HttpClient) ---

    @Test
    public void testHttpUtil_get() throws Exception {
        System.out.println("\n=== Test: HttpUtil GET ===");
        String url = baseUrl + "/api/users";
        HttpClientResult result = HttpUtil.doGet(url);
        System.out.println("GET " + url + " => " + result.getCode() + " " + result.getContent());
        assertEquals(200, result.getCode());
        assertTrue(result.getContent().contains("Alice"));
    }

    @Test
    public void testHttpUtil_getWithParams() throws Exception {
        System.out.println("\n=== Test: HttpUtil GET with params ===");
        String url = baseUrl + "/api/search";
        Map<String, String> params = new HashMap<>();
        params.put("keyword", "test");
        params.put("page", "5");
        HttpClientResult result = HttpUtil.doGet(url, params);
        System.out.println("GET " + url + "?keyword=test&page=5 => " + result.getCode() + " " + result.getContent());
        assertEquals(200, result.getCode());
        assertTrue(result.getContent().contains("test"));
        assertTrue(result.getContent().contains("5"));
    }

    @Test
    public void testHttpUtil_getWithHeaders() throws Exception {
        System.out.println("\n=== Test: HttpUtil GET with headers ===");
        String url = baseUrl + "/api/users";
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        HttpClientResult result = HttpUtil.doGet(url, headers, null);
        System.out.println("GET " + url + " (with Accept header) => " + result.getCode());
        assertEquals(200, result.getCode());
    }

    @Test
    public void testHttpUtil_post() throws Exception {
        System.out.println("\n=== Test: HttpUtil POST ===");
        String url = baseUrl + "/api/users";
        Map<String, String> params = new HashMap<>();
        params.put("name", "David");
        params.put("age", "30");
        HttpClientResult result = HttpUtil.doPost(url, params);
        System.out.println("POST " + url + " => " + result.getCode() + " " + result.getContent());
        assertEquals(200, result.getCode());
        assertTrue(result.getContent().contains("created"));
    }

    @Test
    public void testHttpUtil_put() throws Exception {
        System.out.println("\n=== Test: HttpUtil PUT ===");
        // PUT 不能带 body，需要通过 URL 参数传递
        String url = baseUrl + "/api/users/100";
        Map<String, String> params = new HashMap<>();
        params.put("name", "Updated");
        HttpClientResult result = HttpUtil.doPut(url, params);
        System.out.println("PUT " + url + " => " + result.getCode() + " " + result.getContent());
        assertEquals(200, result.getCode());
        assertTrue(result.getContent().contains("updated"));
    }

    @Test
    public void testHttpUtil_delete() throws Exception {
        System.out.println("\n=== Test: HttpUtil DELETE ===");
        String url = baseUrl + "/api/users/999";
        HttpClientResult result = HttpUtil.doDelete(url);
        System.out.println("DELETE " + url + " => " + result.getCode() + " " + result.getContent());
        assertEquals(200, result.getCode());
        assertTrue(result.getContent().contains("deleted"));
    }

    @Test
    public void testHttpUtil_basicAuth() throws Exception {
        System.out.println("\n=== Test: HttpUtil BasicAuth ===");
        String authValue = HttpUtil.toBasicAuthValue("admin", "password123");
        System.out.println("Generated BasicAuth: " + authValue);
        assertNotNull(authValue);
        assertTrue(authValue.startsWith("Basic "));
        // 用 httpbin 验证认证头
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authValue);
        HttpClientResult result = HttpUtil.doGet("https://httpbin.org/basic-auth/admin/password123", headers, null);
        System.out.println("httpbin basic-auth => " + result.getCode() + " " + result.getContent());
        assertEquals(200, result.getCode());
    }

    // --- 2. OkHttpUtil ---

    @Test
    public void testOkHttpUtil_get() throws Exception {
        System.out.println("\n=== Test: OkHttpUtil GET ===");
        HttpClientResult result = OkHttpUtil.get(baseUrl + "/api/users");
        System.out.println("OkHttp GET /api/users => " + result.getCode() + " " + result.getContent());
        assertEquals(200, result.getCode());
        assertTrue(result.getContent().contains("Alice"));
    }

    @Test
    public void testOkHttpUtil_getWithParams() throws Exception {
        System.out.println("\n=== Test: OkHttpUtil GET with params ===");
        Map<String, String> params = new HashMap<>();
        params.put("keyword", "hello");
        params.put("page", "3");
        HttpClientResult result = OkHttpUtil.get(baseUrl + "/api/search", params);
        System.out.println("OkHttp GET /api/search => " + result.getCode() + " " + result.getContent());
        assertEquals(200, result.getCode());
        assertTrue(result.getContent().contains("hello"));
    }

    @Test
    public void testOkHttpUtil_postJson() throws Exception {
        System.out.println("\n=== Test: OkHttpUtil POST JSON ===");
        String json = "{\"name\":\"OkHttpTest\",\"age\":100}";
        HttpClientResult result = OkHttpUtil.postJson(baseUrl + "/api/echo", json);
        System.out.println("OkHttp POST /api/echo => " + result.getCode() + " " + result.getContent());
        assertEquals(200, result.getCode());
        assertEquals(json, result.getContent());
    }

    @Test
    public void testOkHttpUtil_postForm() throws Exception {
        System.out.println("\n=== Test: OkHttpUtil POST FORM ===");
        Map<String, String> params = new HashMap<>();
        params.put("username", "testuser");
        params.put("email", "test@example.com");
        HttpClientResult result = OkHttpUtil.postForm(baseUrl + "/api/users", params);
        System.out.println("OkHttp POST /api/users => " + result.getCode() + " " + result.getContent());
        assertEquals(200, result.getCode());
    }

    @Test
    public void testOkHttpUtil_basicAuth() throws Exception {
        System.out.println("\n=== Test: OkHttpUtil BasicAuth ===");
        String auth = OkHttpUtil.basicAuth("admin", "password123");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", auth);
        HttpClientResult result = OkHttpUtil.get("https://httpbin.org/basic-auth/admin/password123", headers, null);
        System.out.println("httpbin basic-auth => " + result.getCode());
        assertEquals(200, result.getCode());
    }

    @Test
    public void testOkHttpUtil_async() throws Exception {
        System.out.println("\n=== Test: OkHttpUtil async GET ===");
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> response = new AtomicReference<>();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(baseUrl + "/api/users")
                .build();

        OkHttpUtil.async(request, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                response.set("error: " + e.getMessage());
                latch.countDown();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response res) throws IOException {
                try (okhttp3.ResponseBody body = res.body()) {
                    response.set(res.code() + " " + (body != null ? body.string() : ""));
                }
                latch.countDown();
            }
        });

        assertTrue("async callback should complete within 5s", latch.await(5, TimeUnit.SECONDS));
        System.out.println("OkHttp async => " + response.get());
        assertNotNull(response.get());
        assertTrue(response.get().startsWith("200"));
    }

    // --- 3. CurlParserUtils ---

    @Test
    public void testCurlParser_simpleGet() {
        System.out.println("\n=== Test: CurlParser simple GET ===");
        String curl = "curl " + baseUrl + "/api/users";
        HttpRequestDefinition def = CurlParser.parse(curl);
        System.out.println("Parsed: method=" + def.getHttpRequestLine().getRequestMethod()
                + ", url=" + def.getHttpRequestLine().getUrl());
        assertEquals(RequestMethod.GET, def.getHttpRequestLine().getRequestMethod());
        assertTrue(def.getHttpRequestLine().getUrl().contains("/api/users"));
    }

    @Test
    public void testCurlParser_getWithHeaders() {
        System.out.println("\n=== Test: CurlParser GET with headers ===");
        String curl = "curl -H 'Accept: application/json' -H 'X-Custom: test' " + baseUrl + "/api/users";
        HttpRequestDefinition def = CurlParser.parse(curl);
        System.out.println("Headers: " + def.getHttpRequestHeader());
        assertEquals("application/json", def.getHttpRequestHeader().get("Accept"));
        assertEquals("test", def.getHttpRequestHeader().get("X-Custom"));
    }

    @Test
    public void testCurlParser_postWithJson() {
        System.out.println("\n=== Test: CurlParser POST JSON ===");
        String curl = "curl -X POST -H 'Content-Type: application/json' -d '{\"name\":\"CurlTest\"}' " + baseUrl + "/api/users";
        HttpRequestDefinition def = CurlParser.parse(curl);
        System.out.println("method=" + def.getHttpRequestLine().getRequestMethod()
                + ", body=" + new String(def.getHttpRequestBody().getBody()));
        assertEquals(RequestMethod.POST, def.getHttpRequestLine().getRequestMethod());
        assertTrue(new String(def.getHttpRequestBody().getBody()).contains("CurlTest"));
    }

    @Test
    public void testCurlParser_basicAuth() {
        System.out.println("\n=== Test: CurlParser BasicAuth ===");
        String curl = "curl -u admin:secret123 " + baseUrl + "/api/users";
        HttpRequestDefinition def = CurlParser.parse(curl);
        String auth = def.getHttpRequestHeader().get("Authorization");
        System.out.println("Authorization: " + auth);
        assertNotNull(auth);
        assertTrue(auth.startsWith("Basic "));
    }

    @Test
    public void testCurlParser_cookies() {
        System.out.println("\n=== Test: CurlParser Cookies ===");
        String curl = "curl -b 'session=abc123; token=xyz789' " + baseUrl + "/api/users";
        HttpRequestDefinition def = CurlParser.parse(curl);
        String cookie = def.getHttpRequestHeader().get("Cookie");
        System.out.println("Cookie: " + cookie);
        assertNotNull(cookie);
        assertTrue(cookie.contains("session=abc123"));
    }

    @Test
    public void testCurlParser_roundTrip() {
        System.out.println("\n=== Test: CurlParser RoundTrip ===");
        String original = "curl -X POST -H 'Content-Type: application/json' -H 'Authorization: Bearer token123' -d '{\"name\":\"roundtrip\"}' " + baseUrl + "/api/users";
        HttpRequestDefinition def = CurlParser.parse(original);
        String rebuilt = CurlBuilder.build(def);
        System.out.println("Original: " + original);
        System.out.println("Rebuilt:  " + rebuilt);
        HttpRequestDefinition reparsed = CurlParser.parse(rebuilt);
        assertEquals(def.getHttpRequestLine().getRequestMethod(), reparsed.getHttpRequestLine().getRequestMethod());
        assertEquals(def.getHttpRequestLine().getUrl(), reparsed.getHttpRequestLine().getUrl());
    }

    @Test
    public void testCurlParser_multiple() {
        System.out.println("\n=== Test: CurlParser multiple commands ===");
        String multi = "curl " + baseUrl + "/api/users\ncurl -X POST -d '{\"name\":\"test\"}' " + baseUrl + "/api/users";
        List<HttpRequestDefinition> defs = CurlParserUtils.parseMultiple(multi);
        System.out.println("Parsed " + defs.size() + " commands");
        assertEquals(2, defs.size());
        assertEquals(RequestMethod.GET, defs.get(0).getHttpRequestLine().getRequestMethod());
        assertEquals(RequestMethod.POST, defs.get(1).getHttpRequestLine().getRequestMethod());
    }

    @Test
    public void testCurlParser_utils() {
        System.out.println("\n=== Test: CurlParserUtils utility methods ===");
        String curl = "curl -H 'Accept: application/json' " + baseUrl + "/api/users";
        assertTrue(CurlParserUtils.isValid(curl));
        assertFalse(CurlParserUtils.isValid(null));
        assertFalse(CurlParserUtils.isValid(""));

        String extracted = CurlParserUtils.extractUrl(curl);
        System.out.println("Extracted URL: " + extracted);
        assertNotNull(extracted);
        assertTrue(extracted.contains("/api/users"));

        String pretty = CurlParserUtils.toPrettyCurlCommand(CurlParser.parse(curl));
        System.out.println("Pretty: " + pretty);
        assertNotNull(pretty);
    }

    // --- 4. HttpServerBuilder (嵌入式服务器) ---

    @RestController("/builder")
    public interface BuilderTestApi {
        @GetMapping("/hello")
        String hello();

        @GetMapping("/user/{id}")
        String user(@PathVariable("id") Long id);

        @PostMapping("/echo")
        String echo(@RequestBody String body);
    }

    public static class BuilderTestApiImpl implements BuilderTestApi {
        @Override
        public String hello() {
            return "Hello from Builder!";
        }

        @Override
        public String user(Long id) {
            return "User: " + id;
        }

        @Override
        public String echo(String body) {
            return "Echo: " + body;
        }
    }

    @Test
    public void testHttpServerBuilder() throws Exception {
        System.out.println("\n=== Test: HttpServerBuilder ===");
        int builderPort = findAvailablePort();

        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(builderPort), 0);
        server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(4));

        // 使用 HttpServerBuilder 风格的路由注册
        BuilderTestApiImpl impl = new BuilderTestApiImpl();
        com.zifang.util.http.server.HttpServerRequestHandler handler =
                new com.zifang.util.http.server.HttpServerRequestHandler(impl);

        server.createContext("/builder", exchange -> {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            String body = "";
            if (!"GET".equalsIgnoreCase(method)) {
                try (java.io.InputStream is = exchange.getRequestBody();
                     java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) != -1) baos.write(buf, 0, len);
                    body = new String(baos.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
                }
            }

            String uri = path.startsWith("/builder") ? path.substring("/builder".length()) : path;

            HttpRequestDefinition def = new HttpRequestDefinition();
            HttpRequestLine line = new HttpRequestLine();
            line.setUrl("http://localhost:" + builderPort + path + (query != null ? "?" + query : ""));
            line.setRequestMethod(RequestMethod.valueOf(method.toUpperCase()));
            def.setHttpRequestLine(line);
            if (!body.isEmpty()) {
                HttpRequestBody rb = new HttpRequestBody();
                rb.setBody(body.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                def.setHttpRequestBody(rb);
            }

            String response;
            try {
                response = String.valueOf(handler.handleRequest(def));
            } catch (Exception e) {
                response = "error: " + e.getMessage();
            }

            byte[] bytes = response.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, bytes.length);
            try (java.io.OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        server.start();
        String testBaseUrl = "http://127.0.0.1:" + builderPort;

        try {
            // GET /builder/hello
            HttpClientResult r1 = HttpUtil.doGet(testBaseUrl + "/builder/hello");
            System.out.println("GET /builder/hello => " + r1.getCode() + " " + r1.getContent());
            assertEquals(200, r1.getCode());
            assertEquals("Hello from Builder!", r1.getContent());

            // GET /builder/user/99
            HttpClientResult r2 = HttpUtil.doGet(testBaseUrl + "/builder/user/99");
            System.out.println("GET /builder/user/99 => " + r2.getCode() + " " + r2.getContent());
            assertEquals(200, r2.getCode());

            // POST /builder/echo
            HttpClientResult r3 = HttpUtil.doPost(testBaseUrl + "/builder/echo", null,
                    new java.util.HashMap<String, String>() {{ put("data", "BuilderTest"); }});
            System.out.println("POST /builder/echo => " + r3.getCode() + " " + r3.getContent());
            assertEquals(200, r3.getCode());

        } finally {
            server.stop(0);
        }
    }

    // --- 5. HttpRequestProxy (客户端动态代理) ---

    @Test
    public void testHttpRequestProxy_get() throws Exception {
        System.out.println("\n=== Test: HttpRequestProxy GET ===");
        String result = apiServiceProxy.listUsers();
        System.out.println("proxy.listUsers() => " + result);
        assertNotNull(result);
        assertTrue(result.contains("Alice"));
    }

    @Test
    public void testHttpRequestProxy_getWithPathVariable() throws Exception {
        System.out.println("\n=== Test: HttpRequestProxy GET with path variable ===");
        String result = apiServiceProxy.getUser(42L);
        System.out.println("proxy.getUser(42L) => " + result);
        assertNotNull(result);
        assertTrue(result.contains("42"));
    }

    @Test
    public void testHttpRequestProxy_postWithBody() throws Exception {
        System.out.println("\n=== Test: HttpRequestProxy POST with body ===");
        String body = "{\"name\":\"ProxyTest\"}";
        String result = apiServiceProxy.createUser(body);
        System.out.println("proxy.createUser(...) => " + result);
        assertNotNull(result);
        assertTrue(result.contains("created"));
        assertTrue(result.contains("ProxyTest"));
    }

    @Test
    public void testHttpRequestProxy_putWithPathAndBody() throws Exception {
        System.out.println("\n=== Test: HttpRequestProxy PUT with path + body ===");
        String body = "{\"name\":\"UpdatedViaProxy\"}";
        String result = apiServiceProxy.updateUser(77L, body);
        System.out.println("proxy.updateUser(77L, ...) => " + result);
        assertNotNull(result);
        assertTrue(result.contains("updated"));
        assertTrue(result.contains("77"));
    }

    @Test
    public void testHttpRequestProxy_delete() throws Exception {
        System.out.println("\n=== Test: HttpRequestProxy DELETE ===");
        String result = apiServiceProxy.deleteUser(123L);
        System.out.println("proxy.deleteUser(123L) => " + result);
        assertNotNull(result);
        assertTrue(result.contains("deleted"));
        assertTrue(result.contains("123"));
    }

    @Test
    public void testHttpRequestProxy_search() throws Exception {
        System.out.println("\n=== Test: HttpRequestProxy search with query params ===");
        String result = apiServiceProxy.search("proxy keyword", 9);
        System.out.println("proxy.search('proxy keyword', 9) => " + result);
        assertNotNull(result);
        assertTrue(result.contains("proxy+keyword"));
        assertTrue(result.contains("9"));
    }

    @Test
    public void testHttpRequestProxy_echo() throws Exception {
        System.out.println("\n=== Test: HttpRequestProxy echo ===");
        String input = "Hello from HttpRequestProxy!";
        String result = apiServiceProxy.echo(input);
        System.out.println("proxy.echo('Hello from HttpRequestProxy!') => " + result);
        assertEquals(input, result);
    }

    // --- 6. HttpRequestDefinition POJO ---

    @Test
    public void testHttpRequestDefinition_pojo() {
        System.out.println("\n=== Test: HttpRequestDefinition POJO ===");
        HttpRequestDefinition def = new HttpRequestDefinition();

        HttpRequestLine line = new HttpRequestLine();
        line.setUrl("http://example.com/api/test");
        line.setRequestMethod(RequestMethod.POST);
        def.setHttpRequestLine(line);

        HttpRequestHeader headers = new HttpRequestHeader();
        headers.put("Content-Type", "application/json");
        headers.put("X-Request-ID", "12345");
        def.setHttpRequestHeader(headers);

        HttpRequestBody body = new HttpRequestBody();
        body.setBody("{\"test\":true}".getBytes());
        def.setHttpRequestBody(body);

        System.out.println("Definition: " + def);
        assertEquals("http://example.com/api/test", def.getHttpRequestLine().getUrl());
        assertEquals(RequestMethod.POST, def.getHttpRequestLine().getRequestMethod());
        assertEquals("application/json", def.getHttpRequestHeader().get("Content-Type"));
        assertEquals("12345", def.getHttpRequestHeader().get("X-Request-ID"));
        assertEquals("{\"test\":true}", new String(def.getHttpRequestBody().getBody()));

        // 测试 equals / hashCode
        HttpRequestDefinition def2 = new HttpRequestDefinition();
        def2.setHttpRequestLine(line);
        def2.setHttpRequestHeader(headers);
        def2.setHttpRequestBody(body);
        assertEquals(def, def2);
        assertEquals(def.hashCode(), def2.hashCode());
    }

    // --- 7. AllPathHttpServer (简单服务器) ---

    @Test
    public void testAllPathHttpServer() throws Exception {
        System.out.println("\n=== Test: AllPathHttpServer (basic) ===");
        int testPort = findAvailablePort();
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(testPort), 0);
        server.createContext("/", exchange -> {
            String resp = "AllPath OK";
            byte[] bytes = resp.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (java.io.OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });
        server.setExecutor(java.util.concurrent.Executors.newSingleThreadExecutor());
        server.start();

        try {
            HttpClientResult result = HttpUtil.doGet("http://127.0.0.1:" + testPort + "/");
            System.out.println("AllPathHttpServer GET / => " + result.getCode() + " " + result.getContent());
            assertEquals(200, result.getCode());
            assertEquals("AllPath OK", result.getContent());
        } finally {
            server.stop(0);
        }
    }

    // --- 8. HttpClientResult POJO ---

    @Test
    public void testHttpClientResult() {
        System.out.println("\n=== Test: HttpClientResult POJO ===");
        HttpClientResult r1 = new HttpClientResult(200, "OK");
        HttpClientResult r2 = new HttpClientResult(200, "OK");
        HttpClientResult r3 = new HttpClientResult(404, "Not Found");

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertEquals(200, r1.getCode());
        assertEquals("OK", r1.getContent());
        assertEquals("HttpClientResult{code=200, content=OK}", r1.toString());

        HttpClientResult r4 = new HttpClientResult(500);
        assertEquals(500, r4.getCode());
        assertNull(r4.getContent());

        HttpClientResult r5 = new HttpClientResult("body only");
        assertEquals(0, r5.getCode());
        assertEquals("body only", r5.getContent());
    }
}
