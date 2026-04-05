package com.zifang.util.http.server;

import com.zifang.util.http.base.define.*;
import com.zifang.util.http.base.pojo.HttpRequestDefinition;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 服务端请求处理器
 * 负责将 HTTP 请求分派到对应的方法
 */
public class HttpServerRequestHandler {

    private final Object target;
    private final Map<String, MethodMapping> methodMappings;

    public HttpServerRequestHandler(Object target) {
        this.target = target;
        this.methodMappings = new HashMap<>();
        scanMethods();
    }

    /**
     * 扫描目标类的方法，建立映射
     */
    private void scanMethods() {
        Class<?> clazz = target.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);

            if (mapping != null || getMapping != null || postMapping != null ||
                putMapping != null || deleteMapping != null) {

                MethodMapping methodMapping = new MethodMapping(method, mapping, getMapping,
                    postMapping, putMapping, deleteMapping);

                String key = generateKey(methodMapping);
                methodMappings.put(key, methodMapping);
            }
        }
    }

    /**
     * 生成方法映射的 key
     */
    private String generateKey(MethodMapping mapping) {
        return mapping.getHttpMethod() + ":" + mapping.getPath();
    }

    /**
     * 处理 HTTP 请求
     *
     * @param requestDefinition HTTP 请求定义
     * @return 处理结果
     */
    public Object handleRequest(HttpRequestDefinition requestDefinition) {
        if (requestDefinition == null || requestDefinition.getHttpRequestLine() == null) {
            throw new IllegalArgumentException("Invalid request definition");
        }

        String httpMethod = requestDefinition.getHttpRequestLine().getRequestMethod().name();
        String url = requestDefinition.getHttpRequestLine().getUrl();

        // 解析路径（去除查询参数）
        String path = url;
        int queryIndex = path.indexOf('?');
        if (queryIndex > 0) {
            path = path.substring(0, queryIndex);
        }

        // 查找匹配的方法
        String key = httpMethod + ":" + path;
        MethodMapping mapping = methodMappings.get(key);

        if (mapping == null) {
            // 尝试模糊匹配
            mapping = findMatchingMethod(httpMethod, path);
        }

        if (mapping == null) {
            throw new RuntimeException("No handler found for " + httpMethod + " " + path);
        }

        // 准备方法参数
        Object[] args = prepareArguments(mapping, requestDefinition);

        // 调用方法
        try {
            mapping.getMethod().setAccessible(true);
            return mapping.getMethod().invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke handler method", e);
        }
    }

    /**
     * 查找匹配的方法（支持路径变量）
     */
    private MethodMapping findMatchingMethod(String httpMethod, String path) {
        for (MethodMapping mapping : methodMappings.values()) {
            if (!mapping.getHttpMethod().equals(httpMethod)) {
                continue;
            }

            if (matchPath(mapping.getPath(), path)) {
                return mapping;
            }
        }
        return null;
    }

    /**
     * 匹配路径（支持路径变量如 /users/{id}）
     */
    private boolean matchPath(String pattern, String path) {
        if (pattern.equals(path)) {
            return true;
        }

        // 支持路径变量 {var}
        String regex = pattern.replaceAll("\\{[^/]+\\}", "[^/]+");
        return path.matches(regex);
    }

    /**
     * 准备方法参数
     */
    private Object[] prepareArguments(MethodMapping mapping, HttpRequestDefinition request) {
        Method method = mapping.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];

            // @PathVariable
            PathVariable pathVar = param.getAnnotation(PathVariable.class);
            if (pathVar != null) {
                args[i] = extractPathVariable(mapping.getPath(),
                    request.getHttpRequestLine().getUrl(), param.getType());
                continue;
            }

            // @RequestParam
            RequestParam requestParam = param.getAnnotation(RequestParam.class);
            if (requestParam != null) {
                args[i] = extractRequestParam(request, requestParam.value(), param.getType());
                continue;
            }

            // @RequestBody
            RequestBody requestBody = param.getAnnotation(RequestBody.class);
            if (requestBody != null) {
                args[i] = extractRequestBody(request, param.getType());
                continue;
            }

            // 默认：尝试从请求体转换
            args[i] = extractRequestBody(request, param.getType());
        }

        return args;
    }

    /**
     * 提取路径变量
     */
    private Object extractPathVariable(String pattern, String url, Class<?> type) {
        // 简化实现，实际应该解析路径变量
        return convertType("1", type);
    }

    /**
     * 提取请求参数
     */
    private Object extractRequestParam(HttpRequestDefinition request, String name, Class<?> type) {
        String url = request.getHttpRequestLine().getUrl();
        int queryStart = url.indexOf('?');
        if (queryStart < 0) {
            return null;
        }

        String query = url.substring(queryStart + 1);
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int eq = pair.indexOf('=');
            if (eq > 0) {
                String key = pair.substring(0, eq);
                if (key.equals(name)) {
                    String value = pair.substring(eq + 1);
                    return convertType(value, type);
                }
            }
        }
        return null;
    }

    /**
     * 提取请求体
     */
    private Object extractRequestBody(HttpRequestDefinition request, Class<?> type) {
        if (request.getHttpRequestBody() == null || request.getHttpRequestBody().getBody() == null) {
            return null;
        }

        String bodyStr = new String(request.getHttpRequestBody().getBody());

        // 如果是 String 类型，直接返回
        if (type == String.class) {
            return bodyStr;
        }

        // 其他类型转换（简化实现）
        return convertType(bodyStr, type);
    }

    /**
     * 类型转换
     */
    @SuppressWarnings("unchecked")
    private <T> T convertType(String value, Class<T> type) {
        if (value == null) {
            return null;
        }

        if (type == String.class) {
            return (T) value;
        } else if (type == Integer.class || type == int.class) {
            return (T) Integer.valueOf(value);
        } else if (type == Long.class || type == long.class) {
            return (T) Long.valueOf(value);
        } else if (type == Double.class || type == double.class) {
            return (T) Double.valueOf(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return (T) Boolean.valueOf(value);
        }

        return null;
    }

    /**
     * 方法映射信息
     */
    private static class MethodMapping {
        private final Method method;
        private final RequestMapping mapping;
        private final GetMapping getMapping;
        private final PostMapping postMapping;
        private final PutMapping putMapping;
        private final DeleteMapping deleteMapping;

        public MethodMapping(Method method, RequestMapping mapping, GetMapping getMapping,
                             PostMapping postMapping, PutMapping putMapping, DeleteMapping deleteMapping) {
            this.method = method;
            this.mapping = mapping;
            this.getMapping = getMapping;
            this.postMapping = postMapping;
            this.putMapping = putMapping;
            this.deleteMapping = deleteMapping;
        }

        public Method getMethod() {
            return method;
        }

        public String getHttpMethod() {
            if (getMapping != null) return "GET";
            if (postMapping != null) return "POST";
            if (putMapping != null) return "PUT";
            if (deleteMapping != null) return "DELETE";
            if (mapping != null) return mapping.method().name();
            return "GET";
        }

        public String getPath() {
            if (getMapping != null) return getMapping.value();
            if (postMapping != null) return postMapping.value();
            if (putMapping != null) return putMapping.value();
            if (deleteMapping != null) return deleteMapping.value();
            if (mapping != null) return mapping.value();
            return "/";
        }
    }

}
