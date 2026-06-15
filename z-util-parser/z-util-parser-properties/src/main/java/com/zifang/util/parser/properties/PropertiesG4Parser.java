package com.zifang.util.parser.properties;

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
 * 基于 G4 DSL 的 Properties 解析器。
 * <p>
 * 使用 DynamicLexer + DynamicParser 加载 PropertiesLexer.g4 + PropertiesParser.g4，
 * 无任何第三方依赖。
 * <p>
 * 支持：key=value、key:value、注释（# 或 !）、空行。
 * 转义序列（\\t \\n \\r \\" \\\\ \\uXXXX）和续行（行尾 \\）由本类在 AST 转换时处理。
 */
/**
 * PropertiesG4Parser类。
 */
public class PropertiesG4Parser {

    private static final String LEXER_G4 = "PropertiesLexer.g4";
    private static final String PARSER_G4 = "PropertiesParser.g4";

    /**
     * 解析 Properties 字符串。
     */
    /**
     * parse方法。
     *      * @param content String类型参数
     * @return PropertiesModel类型返回值
     */
    public PropertiesModel parse(String content) {
        return parse(new StringReader(content));
    }

    /**
     * 解析 Properties Reader。
     */
    /**
     * parse方法。
     *      * @param reader Reader类型参数
     * @return PropertiesModel类型返回值
     */
    public PropertiesModel parse(Reader reader) {
        try {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int n;
            while ((n = reader.read(buf)) != -1) {
                sb.append(buf, 0, n);
            }
            return parseString(sb.toString());
        } catch (IOException e) {
            throw new PropertiesException("读取 Properties 失败", e);
        }
    }

    private PropertiesModel parseString(String content) {
        PropertiesModel model = new PropertiesModel();
        if (content == null || content.isEmpty()) {
            return model;
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

            return astToModel(ast, model);
        } catch (PropertiesException e) {
            throw e;
        } catch (Exception e) {
            throw new PropertiesException("G4 Properties解析失败: " + e.getMessage(), e);
        }
    }

    // ==================== G4 加载 ====================

    private String loadG4(String name) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(name);
            if (is == null) throw new PropertiesException("G4文件未找到: " + name);
            byte[] bytes = is.readAllBytes();
            is.close();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (PropertiesException e) {
            throw e;
        } catch (Exception e) {
            throw new PropertiesException("读取G4文件失败: " + name, e);
        }
    }

    // ==================== AST → PropertiesModel ====================

    private PropertiesModel astToModel(ASTNode node, PropertiesModel model) {
        if (node == null) return model;
        for (ASTNode entry : node.getChildren()) {
            if (!"entry".equals(entry.getType())) continue;
            String key = null;
            String value = "";
            for (ASTNode sub : entry.getChildren()) {
                String st = sub.getType();
                if ("KEY".equals(st)) {
                    key = sub.getText();
                } else if ("VALUE".equals(st)) {
                    value = sub.getText() == null ? "" : sub.getText();
                }
            }
            if (key == null || key.isEmpty()) continue;
            // 去除 key 尾部空白
            key = key.trim();
            // 解码 value 中的转义序列
            value = decodeEscapeSequence(value);
            model.setProperty(key, value);
        }
        return model;
    }

    private String decodeEscapeSequence(String str) {
        if (str == null || str.isEmpty()) return str;
        StringBuilder sb = new StringBuilder();
        int len = str.length();
        int i = 0;
        while (i < len) {
            char c = str.charAt(i);
            if (c == '\\' && i + 1 < len) {
                char next = str.charAt(i + 1);
                switch (next) {
                    case 't': sb.append('\t'); i += 2; break;
                    case 'n': sb.append('\n'); i += 2; break;
                    case 'r': sb.append('\r'); i += 2; break;
                    case '"': sb.append('"'); i += 2; break;
                    case '\\': sb.append('\\'); i += 2; break;
                    case 'u':
                        if (i + 5 < len) {
                            try {
                                int cp = Integer.parseInt(str.substring(i + 2, i + 6), 16);
                                sb.appendCodePoint(cp);
                                i += 6;
                            } catch (NumberFormatException ex) {
                                sb.append(next);
                                i += 2;
                            }
                        } else {
                            sb.append(next);
                            i += 2;
                        }
                        break;
                    default:
                        sb.append(c).append(next);
                        i += 2;
                        break;
                }
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }
}
