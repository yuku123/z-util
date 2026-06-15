package com.zifang.util.parser.ini;

import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.core.TokenReader;
import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.g4.DynamicParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

/**
 * 基于 G4 DSL 的 INI 解析器。
 * <p>
 * 使用 DynamicLexer + DynamicParser 加载 IniLexer.g4 + IniParser.g4，
 * 无任何第三方依赖。
 * <p>
 * 支持：section、key=value、注释（; 或 #）、空行。
 * 注：续行（行尾 \）和 inline 注释（值中间的 ; 或 #）由本类在 AST 转换时处理。
 */
/**
 * IniG4Parser类。
 */
public class IniG4Parser {

    private static final String LEXER_G4 = "IniLexer.g4";
    private static final String PARSER_G4 = "IniParser.g4";

    /**
     * 解析 INI 字符串。
     */
    /**
     * parse方法。
     *      * @param content String类型参数
     * @return IniFile类型返回值
     */
    public IniFile parse(String content) {
        return parse(new StringReader(content));
    }

    /**
     * 解析 INI Reader。
     */
    /**
     * parse方法。
     *      * @param reader Reader类型参数
     * @return IniFile类型返回值
     */
    public IniFile parse(Reader reader) {
        try {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int n;
            while ((n = reader.read(buf)) != -1) {
                sb.append(buf, 0, n);
            }
            return parseString(sb.toString());
        } catch (IOException e) {
            throw new IniException("读取 INI 失败", e);
        }
    }

    private IniFile parseString(String content) {
        if (content == null || content.isEmpty()) {
            return new IniFile();
        }
        try {
            String lexerG4 = loadG4(LEXER_G4);
            String parserG4 = loadG4(PARSER_G4);

            DynamicLexer lexer = new DynamicLexer();
            lexer.loadG4(lexerG4);
            lexer.setInput(content);
            TokenReader tokenReader = lexer.getTokenReader();

            DynamicParser parser = new DynamicParser();
            parser.loadG4(parserG4);
            parser.setTokenReader(tokenReader);
            ASTNode ast = parser.parse("file");

            return astToIniFile(ast);
        } catch (IniException e) {
            throw e;
        } catch (Exception e) {
            throw new IniException("G4 INI解析失败: " + e.getMessage(), e);
        }
    }

    // ==================== G4 加载 ====================

    private String loadG4(String name) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(name);
            if (is == null) throw new IniException("G4文件未找到: " + name);
            byte[] bytes = is.readAllBytes();
            is.close();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IniException e) {
            throw e;
        } catch (Exception e) {
            throw new IniException("读取G4文件失败: " + name, e);
        }
    }

    // ==================== AST → IniFile ====================

    private IniFile astToIniFile(ASTNode node) {
        IniFile iniFile = new IniFile();
        IniSection currentSection = null;

        for (ASTNode line : node.getChildren()) {
            if (!"LINE".equals(line.getType())) continue;
            String text = line.getText();
            if (text == null) continue;
            String trimmed = text.trim();
            if (trimmed.isEmpty()) continue;
            // 注释（理论上 COMMENT 已被 lexer 隐藏，但保险起见）
            if (trimmed.startsWith(";") || trimmed.startsWith("#")) continue;
            // section: [name]
            if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                String name = trimmed.substring(1, trimmed.length() - 1).trim();
                currentSection = new IniSection(name);
                iniFile.addSection(currentSection);
                continue;
            }
            // entry: key = value
            int eqIdx = findUnquotedEquals(trimmed);
            if (eqIdx > 0) {
                String key = trimmed.substring(0, eqIdx).trim();
                String value = trimmed.substring(eqIdx + 1).trim();
                value = stripInlineComment(value);
                if (currentSection == null) {
                    currentSection = new IniSection();
                    iniFile.addSection(currentSection);
                }
                currentSection.put(key, value);
            }
        }
        return iniFile;
    }

    private int findUnquotedEquals(String s) {
        // 找到第一个不在引号内的 =
        boolean inDq = false, inSq = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' && !inSq) inDq = !inDq;
            else if (c == '\'' && !inDq) inSq = !inSq;
            else if (c == '=' && !inDq && !inSq) return i;
        }
        return -1;
    }

    private String stripInlineComment(String value) {
        if (value == null) return "";
        int semi = value.indexOf(';');
        int hash = value.indexOf('#');
        int idx = -1;
        if (semi >= 0 && hash >= 0) idx = Math.min(semi, hash);
        else if (semi >= 0) idx = semi;
        else if (hash >= 0) idx = hash;
        if (idx >= 0) value = value.substring(0, idx);
        return value.trim();
    }
}
