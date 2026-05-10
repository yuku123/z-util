package com.zifang.util.expression.dynamic.g4;

import com.zifang.util.expression.dynamic.g4.model.G4File;
import com.zifang.util.expression.dynamic.g4.model.G4Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * G4文件解析器
 * 解析.g4文件，提取词法和语法规则
 * 
 * G4格式参考:
 * - lexer grammar Name;
 * - parser grammar Name;
 * - ruleName : body ;
 * - fragment ruleName : body ;
 * - 支持 -> channel(HIDDEN) 动作语法
 */
public class G4FileParser {

    /**
     * 解析.g4文件内容
     */
    public static G4File parse(String g4Content) {
        G4File g4File = new G4File();
        
        // 提取grammar名称
        g4File.setGrammarName(extractGrammarName(g4Content));
        
        // 提取所有规则
        List<G4Rule> rules = extractRules(g4Content);
        
        return g4File;
    }

    /**
     * 解析.g4文件内容，提取所有规则
     * 使用逐字符解析而非正则，避免注释处理问题
     */
    public static List<G4Rule> extractRules(String g4Content) {
        List<G4Rule> rules = new ArrayList<>();
        
        // 首先提取 lexer grammar 和 parser grammar 的名称
        String lexerName = extractGrammarName(g4Content, "lexer");
        String parserName = extractGrammarName(g4Content, "parser");
        
        boolean inLexer = false;
        boolean inParser = false;
        
        // 逐行解析
        String[] lines = g4Content.split("\n");
        for (String line : lines) {
            line = line.trim();
            
            if (line.startsWith("lexer grammar")) {
                inLexer = true;
                inParser = false;
                continue;
            } else if (line.startsWith("parser grammar")) {
                inLexer = false;
                inParser = true;
                continue;
            }
            
            // 跳过空行和注释行
            if (line.isEmpty() || line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")) {
                continue;
            }
            
            // 解析规则行: ruleName : body ;
            G4Rule rule = parseRuleLine(line, inLexer ? G4Rule.RuleType.LEXER : G4Rule.RuleType.PARSER);
            if (rule != null) {
                rules.add(rule);
            }
        }
        
        return rules;
    }
    
    /**
     * 解析单行规则
     */
    private static G4Rule parseRuleLine(String line, G4Rule.RuleType defaultType) {
        // 跳过注释和空行
        line = line.trim();
        if (line.isEmpty() || line.startsWith("//") || line.startsWith("/*")) {
            return null;
        }
        
        // 移除行尾注释（但不在字符串内）
        line = removeLineComment(line);
        
        // 解析规则名称和body
        // 格式: [fragment] name : body ;
        // 或: [fragment] name ::= body ;
        
        boolean isFragment = false;
        String name = null;
        String body = null;
        
        if (line.startsWith("fragment")) {
            isFragment = true;
            line = line.substring(8).trim();
        }
        
        // 查找 : 或 ::= 分隔符
        int colonIdx = line.indexOf(':');
        int colon2Idx = line.indexOf("::=");
        
        int sepIdx = -1;
        if (colon2Idx >= 0 && (colonIdx < 0 || colon2Idx < colonIdx)) {
            sepIdx = colon2Idx;
        } else if (colonIdx >= 0) {
            sepIdx = colonIdx;
        }
        
        if (sepIdx < 0) {
            return null;
        }
        
        name = line.substring(0, sepIdx).trim();
        
        // 从分隔符位置往后找到 ; 结束
        int semiIdx = findSemicolon(line, sepIdx);
        if (semiIdx < 0) {
            return null;
        }
        
        body = line.substring(sepIdx + (colon2Idx >= 0 && colon2Idx == sepIdx ? 2 : 1), semiIdx).trim();
        
        // 剥离 -> channel(HIDDEN) 等动作
        int actionIdx = body.indexOf("->");
        if (actionIdx >= 0) {
            body = body.substring(0, actionIdx).trim();
        }
        
        // 判断类型
        G4Rule.RuleType type = G4Rule.RuleType.PARSER;
        if (name.length() > 0 && name.charAt(0) >= 'A' && name.charAt(0) <= 'Z') {
            type = G4Rule.RuleType.LEXER;
        }
        
        G4Rule rule = new G4Rule(name, type, body, isFragment);
        return rule;
    }
    
    /**
     * 移除行尾注释（不在字符串内）
     */
    private static String removeLineComment(String line) {
        boolean inString = false;
        char stringChar = 0;
        
        for (int i = 0; i < line.length() - 1; i++) {
            char ch = line.charAt(i);
            
            if (!inString && (ch == '\'' || ch == '"')) {
                inString = true;
                stringChar = ch;
            } else if (inString && ch == '\\' && i + 1 < line.length()) {
                i++; // skip escaped char
            } else if (inString && ch == stringChar) {
                inString = false;
            } else if (!inString && ch == '/' && line.charAt(i + 1) == '/') {
                // 行尾注释开始
                String before = line.substring(0, i);
                // 去掉末尾空白
                before = before.trim();
                return before.isEmpty() ? "" : before;
            }
        }
        
        return line;
    }
    
    /**
     * 从给定位置开始查找分号（考虑字符串、字符类和括号嵌套）
     */
    private static int findSemicolon(String s, int start) {
        int depth = 0;
        boolean inString = false;
        char stringChar = 0;
        boolean inCharClass = false;
        
        for (int i = start; i < s.length(); i++) {
            char ch = s.charAt(i);
            
            // String delimiters are only recognized outside char classes
            if (!inString && !inCharClass && (ch == '\'' || ch == '"')) {
                inString = true;
                stringChar = ch;
            } else if (inString && ch == '\\' && i + 1 < s.length()) {
                i++; // skip escaped
            } else if (inString && ch == stringChar) {
                inString = false;
            } else if (!inString && !inCharClass && ch == '[') {
                // 字符类开始
                inCharClass = true;
            } else if (!inString && inCharClass && ch == '\\' && i + 1 < s.length()) {
                i++; // skip escaped in char class
            } else if (!inString && inCharClass && ch == ']') {
                inCharClass = false;
            } else if (!inString && !inCharClass) {
                if (ch == '(' || ch == '{') {
                    depth++;
                } else if (ch == ')' || ch == '}') {
                    depth--;
                } else if (ch == ';' && depth == 0) {
                    return i;
                }
            }
        }
        
        return -1;
    }

    /**
     * 提取grammar名称
     */
    private static String extractGrammarName(String content, String type) {
        Pattern pattern = Pattern.compile(
            type + "\\s+grammar\\s+(\\w+)\\s*;",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown";
    }
    
    /**
     * 提取grammar名称（自动检测lexer或parser）
     */
    private static String extractGrammarName(String content) {
        Pattern pattern = Pattern.compile(
            "(?:lexer|parser)\\s+grammar\\s+(\\w+)\\s*;",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown";
    }

    /**
     * 从文件加载并解析
     */
    public static G4File parseFromFile(String filePath) {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            String content = new String(java.nio.file.Files.readAllBytes(path));
            return parse(content);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse G4 file: " + filePath, e);
        }
    }
}