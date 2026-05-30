package com.zifang.util.devops.nexus;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Nexus组件管理器
 * <p>
 * 提供Nexus仓库组件的搜索、查询、删除等管理功能，
 * 支持GAV坐标查询和组件存在性检查。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * NexusComponentManager类。
 */
public class NexusComponentManager {

    /**
     * nexus访问地址
     */
    private static final String NEXUS_URL = "http://nexus.cfuture.cc";

    /**
     * nexus账号
     */
    private static final String NEXUS_USERNAME = "admin";

    /**
     * nexus密码
     */
    private static final String NEXUS_PASSWORD = "admin@123";


    public static final String respository = "maven-releases";

    /**
     * 成功删除的组件数量
     */
    private int successDeleteTotal;


    /**
     * 删除组件
     *
     * @param component 组件对象
     */
    /**
     * delete方法。
     *      * @param component Component类型参数
     */
    public void delete(Component component) {
        String url = String.format("%s/service/rest/v1/components/%s", NEXUS_URL, component.getId());
        HttpResponse response = null;
        try {
            response = Unirest.delete(url).basicAuth(NEXUS_USERNAME, NEXUS_PASSWORD).asBinary();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (response == null) {
            System.out.println(String.format("组件【%s-%s-%s】删除失败，HTTP 响应为空", component.getRepository(), component.getGroup(), component.getName(), component.getVersion()));
            return;
        }
        int status = response.getStatus();
        if (status == 204) {
            successDeleteTotal++;
        } else {
            System.out.println(String.format("组件【%s-%s-%s】删除失败，http响应代码：%d", component.getRepository(), component.getGroup(), component.getName(), component.getVersion(), status));
        }
    }

    /**
     * 搜索组件
     *
     * @param repository 仓库名称
     * @return
     */
    /**
     * search方法。
     *      * @param repository String类型参数
     * @return static List<Component>类型返回值
     */
    public static List<Component> search(String repository) {
        final List<Component> list = new ArrayList<>();
        search(repository, list, null);
        return list;
    }

    /**
     * 搜索组件
     * <p>
     * 搜索组件时API会对匹配结果进行分页，所以这里采用递归搜索
     *
     * @param repository 仓库名称
     * @param list       存放查询结果的集合
     * @param token      token，由上一页查询结果中获取，第一次查询传null
     */
    private static void search(String repository, List<Component> list, String token) {
        String url = String.format("%s/service/rest/v1/search?repository=%s", NEXUS_URL, repository);
        if (token != null) {
            url += "&continuationToken=" + token;
        }
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(url).basicAuth(NEXUS_USERNAME, NEXUS_PASSWORD).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (response == null) {
            System.out.println("组件搜索出错，HTTP 响应为空");
            return;
        }
        if (response.getStatus() == 200) {
            JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
            List<Component> temp = new Gson().fromJson(jsonObject.getAsJsonArray("items"), new TypeToken<List<Component>>() {
            }.getType());
            list.addAll(temp);
            if (!jsonObject.get("continuationToken").isJsonNull()) {
                token = jsonObject.get("continuationToken").getAsString();
                //search(repository, groupId, artifactId, list, token);
            }
        } else {
            System.out.println(String.format("组件搜索出错，http响应代码：%d，错误信息：%s", response.getStatus(), response.getStatusText()));
        }
    }

    /**
     * 根据 GAV 坐标查找组件
     *
     * @param groupId    组ID
     * @param artifactId  构件ID
     * @param version     版本号
     * @return 匹配的组件对象，未找到返回 null
     */
    /**
     * findByGav方法。
     *      * @param groupId String类型参数
     * @param artifactId String类型参数
     * @param version String类型参数
     * @return Component类型返回值
     */
    public Component findByGav(String groupId, String artifactId, String version) {

        List<Component> list = search(respository);

        List<Component> filted = list.stream()
                .filter(e -> e.getGroup().equals(groupId) && e.getName().equals(artifactId) && e.getVersion().equals(version))
                .collect(Collectors.toList());

        if (filted.size() == 1) {
            return filted.get(0);
        }
        return null;
    }

    /**
     * 检查指定 GAV 坐标的组件是否存在
     *
     * @param groupId    组ID
     * @param artifactId  构件ID
     * @param version     版本号
     * @return 存在返回 true，不存在返回 false
     */
    /**
     * checkExistGav方法。
     *      * @param groupId String类型参数
     * @param artifactId String类型参数
     * @param version String类型参数
     * @return boolean类型返回值
     */
    public Boolean checkExistGav(String groupId, String artifactId, String version) {

        List<Component> list = search(respository);

        System.out.print(list);

        List<Component> filted = list.stream()
                .filter(e -> e.getGroup().equals(groupId) && e.getName().equals(artifactId) && e.getVersion().equals(version))
                .collect(Collectors.toList());
        if (filted.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

}