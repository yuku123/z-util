package com.zifang.util.http.server;

import com.zifang.util.http.base.define.*;
import org.junit.Test;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class HttpServerBuilderTest {

    /**
     * 用户服务实现
     */
    @RestController("/user1")
    public static class UserController1  {

        @GetMapping("/users")
        public String listUsers() {
            return "Listing all users";
        }

        @GetMapping("/users/{id}")
        public String getUser(@PathVariable("id") Long id) {
            return "User: " + id;
        }

        @PostMapping(values = "/users")
        public String createUser(String body) {
            return "Created user with body: " + body;
        }

        @PutMapping("/users/{id}")
        public String updateUser(@PathVariable("id")Long id, @RequestBody String body) {
            return "Updated user " + id + " with body: " + body;
        }

        @DeleteMapping("/users/{id}")
        public String deleteUser(@PathVariable("id") Long id) {
            return "Deleted user: " + id;
        }

        @GetMapping("/users/search")
        public String searchUsers(@RequestParam("keyword") String keyword, @RequestParam("page") int page) {
            return "Searching users with keyword: " + keyword + ", page: " + page;
        }
    }

    @RestController("/user2")
    public static class UserController2  {

        @GetMapping("/users")
        public String listUsers() {
            return "Listing all users";
        }

        @GetMapping("/users/{id}")
        public String getUser(@PathVariable("id") Long id) {
            return "User: " + id;
        }

        @PostMapping(values = "/users")
        public String createUser(String body) {
            return "Created user with body: " + body;
        }

        @PutMapping("/users/{id}")
        public String updateUser(@PathVariable("id")Long id, @RequestBody String body) {
            return "Updated user " + id + " with body: " + body;
        }

        @DeleteMapping("/users/{id}")
        public String deleteUser(@PathVariable("id") Long id) {
            return "Deleted user: " + id;
        }

        @GetMapping("/users/search")
        public String searchUsers(@RequestParam("keyword") String keyword, @RequestParam("page") int page) {
            return "Searching users with keyword: " + keyword + ", page: " + page;
        }
    }


    @Test
    public void testHttpServerBuilder() throws ScriptException, InterruptedException {



        HttpServerBuilder httpServerBuilder = HttpServerBuilder
                .bindPort(8080)
                .proxy(new UserController1())
                .proxy(new UserController2())
                .start();

        // 给服务器一点时间启动
        Thread.sleep(1000);

        System.out.println("Server started. You can test with curl:");
        System.out.println("  curl http://localhost:8080/user1/users");
        System.out.println("  curl http://localhost:8080/user1/users/123");
        System.out.println("  curl -X POST -d 'test data' http://localhost:8080/user1/users");
        System.out.println("  curl -X PUT -d 'update data' http://localhost:8080/user1/users/123");
        System.out.println("  curl -X DELETE http://localhost:8080/user1/users/123");
        System.out.println("  curl 'http://localhost:8080/user1/users/search?keyword=test&page=1'");

        // 停止服务器
        httpServerBuilder.stop();

    }


}
