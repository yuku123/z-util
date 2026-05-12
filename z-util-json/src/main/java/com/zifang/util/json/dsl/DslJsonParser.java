package com.zifang.util.json.dsl;

import com.zifang.util.json.model.JsonObject;
import com.zifang.util.json.model.JsonArray;

/**
 * 手写 JSON 解析器，直接基于 token 流，无需 G4 引擎。
 * 支持: null/true/false, Number(含科学计数法), String(含转义), 嵌套对象/数组, 空白字符
 */
public class DslJsonParser {

    private String s;
    private int pos = -1;
    private int len;
    private char c;

    public Object parse(String json) {
        this.s = json;
        this.pos = -1;
        this.len = json.length();
        nextChar();
        return parseValue();
    }

    private void nextChar() {
        pos++;
        c = pos < len ? s.charAt(pos) : '\0';
    }

    // 跳过空白
    private void skipWs() {
        while (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
            nextChar();
        }
    }

    private Object parseValue() {
        skipWs();
        switch (c) {
            case '{': return parseObject();
            case '[': return parseArray();
            case '"': return parseString();
            case 't': return parseTrue();
            case 'f': return parseFalse();
            case 'n': return parseNull();
            case '-':
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                return parseNumber();
            case '\0': return null;
            default:
                throw new RuntimeException("Unexpected char at " + pos + ": " + c);
        }
    }

    private JsonObject parseObject() {
        nextChar(); // skip '{'
        skipWs();
        JsonObject obj = new JsonObject();
        if (c == '}') {
            nextChar();
            return obj;
        }
        while (true) {
            skipWs();
            String key = parseString();
            skipWs();
            if (c != ':') throw new RuntimeException("Expected ':' at " + pos);
            nextChar(); // skip ':'
            Object val = parseValue();
            obj.put(key, val);
            if (c == '}') {
                nextChar();
                return obj;
            } else if (c == ',') {
                nextChar(); // skip ','
            } else {
                throw new RuntimeException("Expected ',' or '}' at " + pos + ", got '" + c + "'");
            }
        }
    }

    private JsonArray parseArray() {
        nextChar(); // skip '['
        skipWs();
        JsonArray arr = new JsonArray();
        if (c == ']') {
            nextChar();
            return arr;
        }
        while (true) {
            Object val = parseValue();
            arr.add(val);
            skipWs();
            if (c == ']') {
                nextChar();
                return arr;
            } else if (c == ',') {
                nextChar(); // skip ','
            } else {
                throw new RuntimeException("Expected ',' or ']' at " + pos + ", got '" + c + "'");
            }
        }
    }

    private String parseString() {
        int start = pos; // current position is opening '"'
        nextChar(); // move into the string content
        while (c != '"') {
            if (c == '\\') {
                nextChar(); // skip escape char
                if (c == 'u') {
                    nextChar(); nextChar(); nextChar(); nextChar();
                }
            }
            if (c == '\0') throw new RuntimeException("Unterminated string");
            nextChar();
        }
        String raw = s.substring(start + 1, pos); // exclude opening quote (pos still at closing quote)
        nextChar(); // skip closing quote
        return raw;
    }

    private String decodeString(String raw) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < raw.length()) {
            char ch = raw.charAt(i);
            if (ch == '\\' && i + 1 < raw.length()) {
                char n = raw.charAt(i + 1);
                switch (n) {
                    case '"':  sb.append('"');  i += 2; break;
                    case '\\':  sb.append('\\'); i += 2; break;
                    case '/':   sb.append('/');  i += 2; break;
                    case 'n':   sb.append('\n'); i += 2; break;
                    case 'r':   sb.append('\r'); i += 2; break;
                    case 't':   sb.append('\t'); i += 2; break;
                    case 'b':   sb.append('\b'); i += 2; break;
                    case 'f':   sb.append('\f'); i += 2; break;
                    case 'u':
                        if (i + 5 < raw.length()) {
                            String hex = raw.substring(i + 2, i + 6);
                            sb.append((char) Integer.parseInt(hex, 16));
                            i += 6;
                        } else { sb.append(n); i += 2; }
                        break;
                    default: sb.append(n); i += 2; break;
                }
            } else {
                sb.append(ch);
                i++;
            }
        }
        return sb.toString();
    }

    private Number parseNumber() {
        int start = pos;
        if (c == '-') nextChar();
        while (c >= '0' && c <= '9') nextChar();
        boolean isDouble = false;
        if (c == '.') {
            isDouble = true;
            nextChar();
            while (c >= '0' && c <= '9') nextChar();
        }
        if (c == 'e' || c == 'E') {
            isDouble = true;
            nextChar();
            if (c == '+' || c == '-') nextChar();
            while (c >= '0' && c <= '9') nextChar();
        }
        String numStr = s.substring(start, pos);
        if (isDouble) {
            return Double.parseDouble(numStr);
        } else {
            try { return Long.parseLong(numStr); }
            catch (NumberFormatException e) { return Double.parseDouble(numStr); }
        }
    }

    private Boolean parseTrue() {
        if (!s.startsWith("true", pos)) throw new RuntimeException("Invalid 'true' at " + pos);
        pos += 4; c = pos < len ? s.charAt(pos) : '\0';
        return Boolean.TRUE;
    }

    private Boolean parseFalse() {
        if (!s.startsWith("false", pos)) throw new RuntimeException("Invalid 'false' at " + pos);
        pos += 5; c = pos < len ? s.charAt(pos) : '\0';
        return Boolean.FALSE;
    }

    private Object parseNull() {
        if (!s.startsWith("null", pos)) throw new RuntimeException("Invalid 'null' at " + pos);
        pos += 4; c = pos < len ? s.charAt(pos) : '\0';
        return null;
    }
}
