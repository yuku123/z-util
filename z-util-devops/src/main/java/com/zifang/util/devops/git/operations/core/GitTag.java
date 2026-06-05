package com.zifang.util.devops.git.operations.core;

/**
 * tag 信息
 *
 * @author zifang
 * @version 1.0.0
 */
public class GitTag {

    public enum Type {
        LIGHTWEIGHT, ANNOTATED
    }

    private String name;
    private String sha;
    private String message;
    private GitAuthor tagger;
    private Type type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GitAuthor getTagger() {
        return tagger;
    }

    public void setTagger(GitAuthor tagger) {
        this.tagger = tagger;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return (type == Type.ANNOTATED ? "[annotated] " : "") + name + " " + sha;
    }
}
