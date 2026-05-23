package com.zifang.util.parser.proto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Proto 文档根模型，包含 package、import、message、service 等顶级定义。
 *
 * @author zifang
 */
public class ProtoDocument {

    private String syntax;
    private String packageName;
    private final List<String> imports;
    private final List<ProtoMessage> messages;
    private final List<ProtoService> services;

    public ProtoDocument() {
        this.imports = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.services = new ArrayList<>();
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImports() {
        return Collections.unmodifiableList(imports);
    }

    public void addImport(String importStmt) {
        imports.add(importStmt);
    }

    public List<ProtoMessage> getMessages() {
        return messages;
    }

    public void addMessage(ProtoMessage message) {
        messages.add(message);
    }

    public List<ProtoService> getServices() {
        return services;
    }

    public void addService(ProtoService service) {
        services.add(service);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtoDocument that = (ProtoDocument) o;
        return Objects.equals(syntax, that.syntax) &&
                Objects.equals(packageName, that.packageName) &&
                Objects.equals(imports, that.imports) &&
                Objects.equals(messages, that.messages) &&
                Objects.equals(services, that.services);
    }

    @Override
    public int hashCode() {
        return Objects.hash(syntax, packageName, imports, messages, services);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (syntax != null) {
            sb.append("syntax = \"").append(syntax).append("\";\n");
        }
        if (packageName != null) {
            sb.append("package ").append(packageName).append(";\n");
        }
        for (String imp : imports) {
            sb.append("import \"").append(imp).append("\";\n");
        }
        for (ProtoMessage msg : messages) {
            sb.append(msg.toString()).append("\n");
        }
        for (ProtoService svc : services) {
            sb.append(svc.toString()).append("\n");
        }
        return sb.toString();
    }
}
