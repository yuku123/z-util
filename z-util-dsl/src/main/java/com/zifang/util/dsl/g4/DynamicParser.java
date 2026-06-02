package com.zifang.util.dsl.g4;

import com.zifang.util.dsl.core.ASTFactory;
import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.core.Parser;
import com.zifang.util.dsl.core.TokenReader;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.ast.SimpleASTNode;
import com.zifang.util.dsl.token.Token;

import java.util.*;

/**
 * 动态语法分析器
 * 根据.g4文件动态生成语法分析器
 */
/**
 * DynamicParser类。
 */
public class DynamicParser implements Parser {

    private TokenReader tokenReader;
    private Map<String, G4Rule> parserRules;
    private ASTFactory astFactory;

    // 预定义的Token类型（来自G4Lexer）
    private static final Set<String> TERMINALS = new HashSet<>(Arrays.asList(
        "Id", "StringLiteral", "Int", "Decimal"
    ));

    /**
     * DynamicParser方法。
     */
    public DynamicParser() {
        this.parserRules = new HashMap<>();
    }

    /**
     * 加载G4文件并初始化
     */
    /**
     * loadG4方法。
     *      * @param g4Content String类型参数
     */
    public void loadG4(String g4Content) {
        List<G4Rule> rules = G4FileParser.extractRules(g4Content);

        for (G4Rule rule : rules) {
            if (rule.getType() == G4Rule.RuleType.PARSER) {
                parserRules.put(rule.getName(), rule);
            }
        }
    }

    /**
     * 从文件加载G4
     */
    /**
     * loadG4File方法。
     *      * @param filePath String类型参数
     */
    public void loadG4File(String filePath) {
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
            loadG4(content);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load G4 file: " + filePath, e);
        }
    }

    @Override
    /**
     * setTokenReader方法。
     *      * @param tokenReader TokenReader类型参数
     */
    public void setTokenReader(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
    }

    @Override
    /**
     * parse方法。
     * @return ASTNode类型返回值
     */
    public ASTNode parse() {
        // 默认从第一个规则开始
        if (parserRules.isEmpty()) {
            throw new RuntimeException("No parser rules loaded");
        }
        String startRule = parserRules.keySet().iterator().next();
        return parse(startRule);
    }

    @Override
    /**
     * parse方法。
     *      * @param startRule String类型参数
     * @return ASTNode类型返回值
     */
    public ASTNode parse(String startRule) {
        G4Rule rule = parserRules.get(startRule);
        if (rule == null) {
            throw new RuntimeException("Unknown rule: " + startRule);
        }

        return parseRule(rule);
    }

    /**
     * 解析一条规则
     */
    private ASTNode parseRule(G4Rule rule) {
        String ruleName = rule.getName();
        String body = rule.getBody().trim();

        SimpleASTNode node = new SimpleASTNode();
        node.setType(ruleName);
        node.setLine(getCurrentLine());
        node.setColumn(getCurrentColumn());

        // 解析规则体
        List<ParseResult> results = parseAlternatives(body, node);

        // 返回第一个成功匹配的结果
        if (!results.isEmpty()) {
            return results.get(0).node;
        }

        return node;
    }

    /**
     * 解析规则体中的多个可选分支
     */
    private List<ParseResult> parseAlternatives(String body, ASTNode parent) {
        List<ParseResult> results = new ArrayList<>();

        // 按|分割，但需要注意括号内的|
        List<String> alternatives = splitAlternatives(body);

        for (String alt : alternatives) {
            SimpleASTNode altNode = new SimpleASTNode();
            altNode.setType("alt");
            altNode.setLine(getCurrentLine());
            altNode.setColumn(getCurrentColumn());

            try {
                parseSequence(alt.trim(), altNode);
                results.add(new ParseResult(altNode));
            } catch (ParseException e) {
                // 这个分支不匹配，尝试下一个
            }
        }

        return results;
    }

    /**
     * 解析连续的token序列
     */
    private void parseSequence(String sequence, ASTNode parent) {
        // 分割sequence中的各个元素
        List<String> elements = splitElements(sequence);

        for (String element : elements) {
            element = element.trim();
            if (element.isEmpty()) continue;

            ASTNode child = parseElement(element);
            if (child != null) {
                parent.addChild(child);
            }
        }
    }

    /**
     * 解析单个元素
     */
    private ASTNode parseElement(String element) {
        element = element.trim();

        // 可选元素 (...)
        if (element.startsWith("(") && element.endsWith(")")) {
            String inner = element.substring(1, element.length() - 1);
            SimpleASTNode node = new SimpleASTNode();
            node.setType("group");
            node.setLine(getCurrentLine());
            node.setColumn(getCurrentColumn());
            try {
                parseSequence(inner, node);
            } catch (ParseException e) {
                // 可选组，不匹配时不报错
            }
            return node;
        }

        // 可选元素 ?
        if (element.endsWith("?")) {
            String inner = element.substring(0, element.length() - 1).trim();
            SimpleASTNode node = new SimpleASTNode();
            node.setType("optional");
            node.setLine(getCurrentLine());
            node.setColumn(getCurrentColumn());
            try {
                parseSequence(inner, node);
            } catch (ParseException e) {
                // 可选，不匹配
            }
            return node;
        }

        // 闭包 * +
        if (element.endsWith("*") || element.endsWith("+")) {
            char op = element.charAt(element.length() - 1);
            String inner = element.substring(0, element.length() - 1).trim();
            SimpleASTNode node = new SimpleASTNode();
            node.setType(op == '*' ? "star" : "plus");
            node.setLine(getCurrentLine());
            node.setColumn(getCurrentColumn());

            int minCount = op == '+' ? 1 : 0;
            int count = 0;

            try {
                while (true) {
                    ASTNode child = parseElement(inner);
                    if (child != null) {
                        node.addChild(child);
                        count++;
                    } else {
                        break;
                    }
                }
            } catch (ParseException e) {
                // 停止匹配
            }

            if (count < minCount) {
                throw new ParseException("Expected at least " + minCount + " matches");
            }

            return node;
        }

        // 字符串字面量匹配
        if (element.startsWith("'") && element.endsWith("'")) {
            String expected = element.substring(1, element.length() - 1);
            return matchTerminal(expected);
        }

        // 字符字面量匹配
        if (element.startsWith("\"") && element.endsWith("\"")) {
            String expected = element.substring(1, element.length() - 1);
            return matchTerminal(expected);
        }

        // 规则引用（非终结符）
        if (isRuleRef(element)) {
            return matchNonTerminal(element);
        }

        // 大写开头：终结符（token 类型名）
        if (element.length() > 0 && element.charAt(0) >= 'A' && element.charAt(0) <= 'Z') {
            return matchTerminal(element);
        }

        // 忽略空白
        if (element.isEmpty()) {
            return null;
        }

        throw new ParseException("Unknown element: " + element);
    }

    /**
     * 匹配终结符
     */
    private ASTNode matchTerminal(String expected) {
        Token token = tokenReader.peek();
        if (token == null) {
            throw new ParseException("Expected: " + expected + ", got EOF");
        }
        // 如果 expected 是大写开头的 token 名称，按 token 类型匹配
        if (expected.length() > 0 && expected.charAt(0) >= 'A' && expected.charAt(0) <= 'Z') {
            if (expected.equals(token.getTokenName())) {
                tokenReader.advance();
                SimpleASTNode node = new SimpleASTNode();
                node.setType(token.getTokenName());
                node.setText(token.getText());
                node.setLine(token.getLine());
                node.setColumn(token.getColumn());
                node.setToken(token);
                return node;
            }
            throw new ParseException("Expected token: " + expected + ", got: " + token.getTokenName() + " (" + token.getText() + ")");
        }
        // 小写字母开头或普通字符，按文本精确匹配
        if (expected.equals(token.getText())) {
            tokenReader.advance();
            SimpleASTNode node = new SimpleASTNode();
            node.setType("terminal");
            node.setText(token.getText());
            node.setLine(token.getLine());
            node.setColumn(token.getColumn());
            node.setToken(token);
            return node;
        }
        throw new ParseException("Expected: " + expected + ", got: " + token.getText());
    }

    /**
     * 匹配非终结符
     */
    private ASTNode matchNonTerminal(String ruleName) {
        G4Rule rule = parserRules.get(ruleName);
        if (rule == null) {
            throw new ParseException("Unknown rule: " + ruleName);
        }
        return parseRule(rule);
    }

    /**
     * 判断是否是规则引用
     */
    private boolean isRuleRef(String name) {
        // 第一个字符是小写字母，且是已知的parser规则
        if (name.length() > 0 && name.charAt(0) >= 'a' && name.charAt(0) <= 'z') {
            return parserRules.containsKey(name);
        }
        return false;
    }

    /**
     * 按|分割（忽略括号内的|和字符串字面量内的|）
     */
    private List<String> splitAlternatives(String body) {
        List<String> alternatives = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int parenDepth = 0;
        boolean inQuote = false;
        char quoteChar = 0;

        for (int i = 0; i < body.length(); i++) {
            char ch = body.charAt(i);

            // 处理字符串字面量
            if (!inQuote && (ch == '\'' || ch == '"')) {
                inQuote = true;
                quoteChar = ch;
                sb.append(ch);
                continue;
            }
            if (inQuote && ch == '\\' && i + 1 < body.length()) {
                sb.append(ch);
                sb.append(body.charAt(++i));
                continue;
            }
            if (inQuote && ch == quoteChar) {
                inQuote = false;
                sb.append(ch);
                continue;
            }
            if (inQuote) {
                sb.append(ch);
                continue;
            }

            if (ch == '(' || ch == '[') parenDepth++;
            else if (ch == ')' || ch == ']') parenDepth = Math.max(0, parenDepth - 1);
            else if (ch == '|' && parenDepth == 0) {
                alternatives.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(ch);
            }
        }

        if (sb.length() > 0) {
            alternatives.add(sb.toString());
        }

        return alternatives;
    }

    /**
     * 分割元素序列（按空格分割，但保留括号内的空格）
     * G4元素分割：按空格分割，但忽略括号内的空格
     */
    private List<String> splitElements(String sequence) {
        List<String> elements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenDepth = 0;

        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);

            if (c == '(') {
                parenDepth++;
                current.append(c);
            } else if (c == ')') {
                parenDepth = Math.max(0, parenDepth - 1);
                current.append(c);
            } else if (c == ' ' && parenDepth == 0 && current.length() > 0) {
                elements.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            elements.add(current.toString());
        }

        return elements;
    }

    private int getCurrentLine() {
        Token peek = tokenReader.peek();
        return peek != null ? peek.getLine() : 0;
    }

    private int getCurrentColumn() {
        Token peek = tokenReader.peek();
        return peek != null ? peek.getColumn() : 0;
    }

    /**
     * 解析结果封装
     */
    private static class ParseResult {
        ASTNode node;
        ParseResult(ASTNode node) {
            this.node = node;
        }
    }

    /**
     * 解析异常
     */
    private static class ParseException extends RuntimeException {
        ParseException(String message) {
            super(message);
        }
    }
}