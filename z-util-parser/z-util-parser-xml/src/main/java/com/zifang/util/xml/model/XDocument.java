package com.zifang.util.xml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * XML 文档根节点。
 * <p>
 * 包含 XML 声明、根元素以及顶层注释/处理指令。
 *
 * @author zifang
 */
public class XDocument implements XNode {

    private XDeclaration declaration;

    private XElement root;

    private List<XNode> prependNodes = new ArrayList<>();

    public XDocument() {
    }

    public XDocument(XElement root) {
        this.root = root;
    }

    public XDeclaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(XDeclaration declaration) {
        this.declaration = declaration;
    }

    public XElement getRoot() {
        return root;
    }

    public void setRoot(XElement root) {
        this.root = root;
    }

    /**
     * 根元素之前的顶层节点（注释、处理指令）。
     */
    public List<XNode> getPrependNodes() {
        return prependNodes;
    }

    public void addPrependNode(XNode node) {
        prependNodes.add(node);
    }

    public String getRootElementName() {
        return root == null ? null : root.getName();
    }
}
