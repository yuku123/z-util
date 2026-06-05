package com.zifang.util.devops.git.operations.core;

/**
 * 远程仓库信息
 *
 * @author zifang
 * @version 1.0.0
 */
public class GitRemote {

    private String name;
    private String fetchUrl;
    private String pushUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFetchUrl() {
        return fetchUrl;
    }

    public void setFetchUrl(String fetchUrl) {
        this.fetchUrl = fetchUrl;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    @Override
    public String toString() {
        return "GitRemote{name='" + name + "', fetchUrl='" + fetchUrl + "'}";
    }
}
