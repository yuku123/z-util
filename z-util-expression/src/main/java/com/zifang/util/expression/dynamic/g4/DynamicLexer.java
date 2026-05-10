package com.zifang.util.expression.dynamic.g4;

import com.zifang.util.expression.dynamic.core.Lexer;
import com.zifang.util.expression.dynamic.core.TokenReader;
import com.zifang.util.expression.dynamic.g4.model.G4Rule;
import com.zifang.util.expression.dynamic.g4.model.TokenDefinition;
import com.zifang.util.expression.dynamic.token.SimpleToken;
import com.zifang.util.expression.dynamic.token.Token;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 动态词法分析器
 * 根据.g4文件动态生成词法分析器
 */
public class DynamicLexer implements Lexer {

    private String input;
    private char[] chars;
    private int pos;
    private int line;
    private int column;
    
    private List<TokenDefinition> tokenDefinitions;
    private Map<String, Pattern> compiledPatterns;
    
    // Token类型映射
    private Map<String, Integer> tokenTypeMap;
    private int nextTypeId;
    
    // Fragment规则（不直接产生Token）
    private Map<String, String> fragmentRules;
    
    // 解析结果
    private List<Token> tokens;
    
    public DynamicLexer() {
        this.tokenDefinitions = new ArrayList<>();
        this.compiledPatterns = new HashMap<>();
        this.tokenTypeMap = new HashMap<>();
        this.fragmentRules = new HashMap<>();
        this.nextTypeId = 1;
    }
    
    /**
     * 加载G4文件并初始化
     */
    public void loadG4(String g4Content) {
        // 解析G4文件
        List<G4Rule> rules = G4FileParser.extractRules(g4Content);
        
        // 处理规则
        for (G4Rule rule : rules) {
            if (rule.isFragment()) {
                // Fragment规则不产生Token，但会被其他规则引用
                fragmentRules.put(rule.getName(), rule.getBody());
            } else {
                // 转换G4规则为正则表达式
                String pattern = convertToRegex(rule.getBody(), rule.getType());
                
                TokenDefinition def = new TokenDefinition(
                    rule.getName(),
                    pattern,
                    tokenDefinitions.size()
                );
                def.setFragment(false);
                tokenDefinitions.add(def);
                
                // 编译正则表达式
                try {
                    compiledPatterns.put(rule.getName(), Pattern.compile(pattern));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to compile pattern for rule: " + rule.getName() + ", pattern: " + pattern, e);
                }
                
                tokenTypeMap.put(rule.getName(), nextTypeId++);
            }
        }
    }
    
    /**
     * 从文件加载G4
     */
    public void loadG4File(String filePath) {
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
            loadG4(content);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load G4 file: " + filePath, e);
        }
    }

    /**
     * 将G4规则体转换为Java正则表达式
     * G4词法规则格式:
     * - 单引号包围的字面量: 'abc' 表示精确匹配 "abc"
     * - 字符类: [a-z] [^0-9]
     * - 取反: ~[a-z] 表示不在a-z中的字符
     * - 组合: 空格分隔表示连接, | 表示或
     * - 后缀: * + ? 表示重复
     */
    private String convertToRegex(String body, G4Rule.RuleType type) {
        String regex = body.trim();
        StringBuilder result = new StringBuilder();
        int i = 0;
        
        while (i < regex.length()) {
            char ch = regex.charAt(i);
            
            if (ch == '\'') {
                // 字面量: 'abc' 或 'a'
                int end = findMatchingQuote(regex, i + 1);
                if (end == -1) {
                    throw new RuntimeException("Unclosed string literal starting at " + i);
                }
                String literal = regex.substring(i + 1, end);
                result.append(escapeRegex(literal));
                i = end + 1;
            } else if (ch == '[') {
                // 字符类: [abc] 或 [^abc]
                int end = findMatchingBracket(regex, i);
                if (end == -1) {
                    throw new RuntimeException("Unclosed bracket starting at " + i);
                }
                String charClass = regex.substring(i, end + 1);
                // ~[xxx] 转换为 [^xxx]
                if (charClass.startsWith("~[")) {
                    result.append("([^"); // ~表示取反
                    result.append(charClass.substring(2, charClass.length() - 1));
                    result.append("]");
                } else {
                    result.append(charClass);
                }
                i = end + 1;
            } else if (ch == '(') {
                // 处理括号表达式
                int end = findMatchingParen(regex, i);
                if (end == -1) {
                    throw new RuntimeException("Unclosed parenthesis at " + i);
                }
                String inner = regex.substring(i + 1, end);
                // 检查是否有后缀操作符
                char suffix = 0;
                int j = end + 1;
                while (j < regex.length() && "?".indexOf(regex.charAt(j)) >= 0) {
                    suffix = regex.charAt(j);
                    j++;
                }
                result.append("(?:");
                result.append(convertToRegex(inner, type));
                result.append(")");
                if (suffix == '?') result.append("?");
                i = j;
            } else if (ch == '|') {
                result.append("|");
                i++;
            } else if (ch == '*' || ch == '+' || ch == '?') {
                // 后缀操作符
                result.append(ch);
                i++;
            } else if (ch == '~') {
                // 取反操作符，后面应该是 [...]
                i++;
                // 跳过空白
                while (i < regex.length() && regex.charAt(i) == ' ') i++;
                if (i < regex.length() && regex.charAt(i) == '[') {
                    int end = findMatchingBracket(regex, i);
                    String charClass = regex.substring(i, end + 1);
                    result.append(charClass); // 保持原样让DFA处理
                    i = end + 1;
                }
            } else if (Character.isLetterOrDigit(ch) || ch == '_') {
                // 规则引用（需要展开fragment）
                int start = i;
                while (i < regex.length() && (Character.isLetterOrDigit(regex.charAt(i)) || regex.charAt(i) == '_')) {
                    i++;
                }
                String ref = regex.substring(start, i);
                if (fragmentRules.containsKey(ref)) {
                    result.append("(?:");
                    result.append(convertToRegex(fragmentRules.get(ref), type));
                    result.append(")");
                } else {
                    result.append(ref);
                }
            } else if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') {
                // 忽略空白
                i++;
            } else {
                // 其他字符（运算符等），直接添加
                result.append(ch);
                i++;
            }
        }
        
        return result.toString();
    }
    
    private int findMatchingQuote(String s, int start) {
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == '\\' && i + 1 < s.length()) {
                i++; // skip escaped char
            } else if (s.charAt(i) == '\'') {
                return i;
            }
        }
        return -1;
    }
    
    private int findMatchingBracket(String s, int start) {
        int depth = 0;
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == '\\' && i + 1 < s.length()) {
                // 转义序列，跳过下一个字符
                i++;
                continue;
            }
            if (s.charAt(i) == '[') depth++;
            else if (s.charAt(i) == ']') {
                if (depth == 1) return i;  // found matching ]
                depth--;
            }
        }
        return -1;
    }
    
    private int findMatchingParen(String s, int start) {
        int depth = 0;
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == '(') depth++;
            else if (s.charAt(i) == ')') {
                if (depth == 0) return i;
                depth--;
            }
        }
        return -1;
    }
    
    private String escapeRegex(String literal) {
        // 转义正则特殊字符
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < literal.length(); i++) {
            char ch = literal.charAt(i);
            if ("\\^$.|?*+()[]{}".indexOf(ch) >= 0) {
                sb.append('\\');
            }
            sb.append(ch);
        }
        return sb.toString();
    }
    
    /**
     * 展开fragment引用
     */
    private String expandFragments(String regex) {
        // 查找所有规则引用（首字母大写的词）
        Pattern pattern = Pattern.compile("\\b([A-Z][a-zA-Z0-9_]*)\\b");
        Matcher matcher = pattern.matcher(regex);
        
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String ref = matcher.group(1);
            if (fragmentRules.containsKey(ref)) {
                String expanded = expandFragments(fragmentRules.get(ref));
                matcher.appendReplacement(sb, "(" + expanded + ")");
            }
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }

    @Override
    public void setInput(String input) {
        this.input = input;
        this.chars = input.toCharArray();
        this.pos = 0;
        this.line = 1;
        this.column = 1;
    }

    @Override
    public void setInput(char[] input) {
        this.chars = input;
        this.input = new String(input);
        this.pos = 0;
        this.line = 1;
        this.column = 1;
    }

    @Override
    public List<Token> tokenize() {
        tokens = new ArrayList<>();
        pos = 0;
        line = 1;
        column = 1;
        
        while (pos < chars.length) {
            Token token = nextToken();
            if (token != null) {
                tokens.add(token);
            }
        }
        
        return tokens;
    }

    /**
     * 获取下一个Token
     */
    private Token nextToken() {
        // 跳过空白字符
        while (pos < chars.length && isWhitespace(chars[pos])) {
            if (chars[pos] == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
            pos++;
        }
        
        if (pos >= chars.length) {
            return null;
        }
        
        // 尝试所有Token定义，找最长匹配
        String bestMatch = null;
        String bestTokenType = null;
        int bestEndPos = pos;
        int bestLine = line;
        int bestColumn = column;
        
        for (Map.Entry<String, Pattern> entry : compiledPatterns.entrySet()) {
            String tokenType = entry.getKey();
            Pattern pattern = entry.getValue();
            
            // 从当前位置匹配
            Matcher matcher = pattern.matcher(input.substring(pos));
            if (matcher.lookingAt()) {
                String matched = matcher.group();
                // 找最长匹配
                if (bestMatch == null || matched.length() > bestMatch.length()) {
                    bestMatch = matched;
                    bestTokenType = tokenType;
                    bestEndPos = pos + matched.length();
                }
            }
        }
        
        if (bestMatch != null) {
            // 创建Token
            SimpleToken token = new SimpleToken();
            token.setType(tokenTypeMap.get(bestTokenType));
            token.setText(bestMatch);
            token.setLine(bestLine);
            token.setColumn(bestColumn);
            token.setTokenName(bestTokenType);
            
            // 移动位置
            for (int i = 0; i < bestMatch.length(); i++) {
                if (bestMatch.charAt(i) == '\n') {
                    line++;
                    column = 1;
                } else {
                    column++;
                }
            }
            pos = bestEndPos;
            
            return token;
        }
        
        // 无法识别，报告错误
        throw new RuntimeException("Unexpected character: '" + chars[pos] + "' at line " + line + ", column " + column);
    }

    private boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    @Override
    public TokenReader getTokenReader() {
        if (tokens == null) {
            tokenize();
        }
        return new SimpleTokenReader(tokens);
    }

    /**
     * 简单的Token读取器实现
     */
    private static class SimpleTokenReader implements TokenReader {
        private final List<Token> tokens;
        private int index;
        
        public SimpleTokenReader(List<Token> tokens) {
            this.tokens = tokens;
            this.index = 0;
        }
        
        @Override
        public Token read() {
            if (index < tokens.size()) {
                return tokens.get(index++);
            }
            return null;
        }
        
        @Override
        public Token peek() {
            if (index < tokens.size()) {
                return tokens.get(index);
            }
            return null;
        }
        
        @Override
        public void advance() {
            if (index < tokens.size()) {
                index++;
            }
        }
        
        @Override
        public Token get(int offset) {
            int idx = index + offset;
            if (idx >= 0 && idx < tokens.size()) {
                return tokens.get(idx);
            }
            return null;
        }
        
        @Override
        public boolean hasNext() {
            return index < tokens.size();
        }
        
        @Override
        public void reset() {
            index = 0;
        }
        
        @Override
        public void close() {
            // nothing to close
        }
    }
}