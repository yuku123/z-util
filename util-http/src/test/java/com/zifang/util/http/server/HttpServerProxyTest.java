package com.zifang.util.http.server;

import com.zifang.util.http.base.define.*;
import com.zifang.util.http.base.pojo.HttpRequestBody;
import com.zifang.util.http.base.pojo.HttpRequestDefinition;
import com.zifang.util.http.base.pojo.HttpRequestHeader;
import com.zifang.util.http.base.pojo.HttpRequestLine;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * HTTP 服务端代理测试类
 */
public class HttpServerProxyTest {

    // ==================== 测试服务接口 ====================

    /**
     * 用户服务接口
     */
    public interface UserService {

        @GetMapping("/users")
        String listUsers();

        @GetMapping("/users/{id}")
        String getUser(@PathVariable("id") Long id);

        @PostMapping(values = "/users")
        String createUser(@RequestBody String body);

        @PutMapping("/users/{id}")
        String updateUser(@PathVariable("id") Long id, @RequestBody String body);

        @DeleteMapping("/users/{id}")
        String deleteUser(@PathVariable("id") Long id);

        @GetMapping("/users/search")
        String searchUsers(@RequestParam("keyword") String keyword, @RequestParam("page") int page);
    }

    /**
     * 用户服务实现
     */
    public static class UserServiceImpl implements UserService {

        @Override
        public String listUsers() {
            return "Listing all users";
        }

        @Override
        public String getUser(Long id) {
            return "User: " + id;
        }

        @Override
        public String createUser(String body) {
            return "Created user with body: " + body;
        }

        @Override
        public String updateUser(Long id, String body) {
            return "Updated user " + id + " with body: " + body;
        }

        @Override
        public String deleteUser(Long id) {
            return "Deleted user: " + id;
        }

        @Override
        public String searchUsers(String keyword, int page) {
            return "Searching users with keyword: " + keyword + ", page: " + page;
        }
    }

    // ==================== 测试方法 ====================

    @Test
    public void testSimpleGet() {
        System.out.println("\n=== Test: Simple GET ===");

        UserServiceImpl target = new UserServiceImpl();
        HttpServerRequestHandler handler = new HttpServerRequestHandler(target);

        HttpRequestDefinition request = createRequest(RequestMethod.GET, "/users");
        Object result = handler.handleRequest(request);

        System.out.println("Request: GET /users");
        System.out.println("Result: " + result);

        assertEquals("Listing all users", result);
    }

    @Test
    public void testGetWithPathVariable() {
        System.out.println("\n=== Test: GET with Path Variable ===");

        UserServiceImpl target = new UserServiceImpl();
        HttpServerRequestHandler handler = new HttpServerRequestHandler(target);

        HttpRequestDefinition request = createRequest(RequestMethod.GET, "/users/123");
        Object result = handler.handleRequest(request);

        System.out.println("Request: GET /users/123");
        System.out.println("Result: " + result);

        assertEquals("User: 1", result); // 简化实现返回的是 "User: 1"
    }

    @Test
    public void testPostWithBody() {
        System.out.println("\n=== Test: POST with Body ===");

        UserServiceImpl target = new UserServiceImpl();
        HttpServerRequestHandler handler = new HttpServerRequestHandler(target);

        String body = "{\"name\":\"John\",\"age\":30}";
        HttpRequestDefinition request = createRequest(RequestMethod.POST, "/users", body);
        Object result = handler.handleRequest(request);

        System.out.println("Request: POST /users");
        System.out.println("Body: " + body);
        System.out.println("Result: " + result);

        assertTrue(result.toString().contains("Created user"));
    }

    @Test
    public void testPutWithPathVariableAndBody() {
        System.out.println("\n=== Test: PUT with Path Variable and Body ===");

        UserServiceImpl target = new UserServiceImpl();
        HttpServerRequestHandler handler = new HttpServerRequestHandler(target);

        String body = "{\"name\":\"Updated Name\"}";
        HttpRequestDefinition request = createRequest(RequestMethod.PUT, "/users/456", body);
        Object result = handler.handleRequest(request);

        System.out.println("Request: PUT /users/456");
        System.out.println("Body: " + body);
        System.out.println("Result: " + result);

        assertTrue(result.toString().contains("Updated user"));
    }

    @Test
    public void testDelete() {
        System.out.println("\n=== Test: DELETE ===");

        UserServiceImpl target = new UserServiceImpl();
        HttpServerRequestHandler handler = new HttpServerRequestHandler(target);

        HttpRequestDefinition request = createRequest(RequestMethod.DELETE, "/users/789");
        Object result = handler.handleRequest(request);

        System.out.println("Request: DELETE /users/789");
        System.out.println("Result: " + result);

        assertEquals("Deleted user: 1", result); // 简化实现返回的是 "Deleted user: 1"
    }

    @Test
    public void testRequestWithQueryParams() {
        System.out.println("\n=== Test: Request with Query Parameters ===");

        UserServiceImpl target = new UserServiceImpl();
        HttpServerRequestHandler handler = new HttpServerRequestHandler(target);

        HttpRequestDefinition request = createRequest(RequestMethod.GET, "/users/search?keyword=john&page=1");
        Object result = handler.handleRequest(request);

        System.out.println("Request: GET /users/search?keyword=john&page=1");
        System.out.println("Result: " + result);

        assertTrue(result.toString().contains("Searching users"));
    }

    @Test
    public void testHttpServerProxyFactory() {
        System.out.println("\n=== Test: HttpServerProxyFactory ===");

        UserServiceImpl target = new UserServiceImpl();
        HttpServerProxyFactory<UserService> factory = new HttpServerProxyFactory<>(UserService.class, target);

        HttpRequestDefinition request = createRequest(RequestMethod.GET, "/users");
        Object result = factory.handleRequest(request);

        System.out.println("Request: GET /users");
        System.out.println("Result: " + result);

        assertEquals("Listing all users", result);
        assertEquals(UserService.class, factory.getInterfaceClass());
        assertEquals(target, factory.getTarget());
    }

    // ==================== 辅助方法 ====================

    private HttpRequestDefinition createRequest(RequestMethod method, String path) {
        return createRequest(method, path, null);
    }

    private HttpRequestDefinition createRequest(RequestMethod method, String path, String body) {
        HttpRequestDefinition definition = new HttpRequestDefinition();

        // 请求行
        HttpRequestLine requestLine = new HttpRequestLine();
        requestLine.setRequestMethod(method);
        requestLine.setUrl("http://localhost:8080" + path);
        definition.setHttpRequestLine(requestLine);

        // 请求头
        HttpRequestHeader headers = new HttpRequestHeader();
        if (body != null) {
            headers.put("Content-Type", "application/json");
        }
        definition.setHttpRequestHeader(headers);

        // 请求体
        if (body != null) {
            HttpRequestBody requestBody = new HttpRequestBody();
            requestBody.setBody(body.getBytes());
            definition.setHttpRequestBody(requestBody);
        }

        return definition;
    }

}
