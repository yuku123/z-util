package com.zifang.util.devops.git.operations.core;

import java.util.Date;

/**
 * 提交作者/提交人
 * <p>
 * 复用于 author 和 committer 两种身份。
 *
 * @author zifang
 * @version 1.0.0
 */
public class GitAuthor {

    private String name;
    private String email;
    private Date when;

    public GitAuthor() {
    }

    public GitAuthor(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public GitAuthor(String name, String email, Date when) {
        this.name = name;
        this.email = email;
        this.when = when;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    @Override
    public String toString() {
        return name + " <" + email + ">";
    }
}
