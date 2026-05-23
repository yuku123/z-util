package com.zifang.util.json.tokenizer;

import com.zifang.util.json.exception.JsonParseException;

import java.io.IOException;

/**
 * JSON 词法分析器，将JSON字符串分解为Token序列。
 * <p>
 * 优化版本：直接操作 CharReader（char[]），消除 Reader 缓冲边界检查。
 * 采用状态机驱动，避免重复方法调用开销。
 *
 * @author zifang
 * @see Token
 * @see TokenList
 * @see CharReader
 */
public class Tokenizer {

    private static final int CT_WHITESPACE = 1;
    private static final int CT_DIGIT = 2;
    private static final int CT_QUOTE = 3;

    private CharReader charReader;

    /**
     * 对给定的字符流进行词法分析，生成Token列表。
     */
    public TokenList tokenize(CharReader charReader) throws IOException {
        this.charReader = charReader;
        TokenList tokens = new TokenList();
        tokenize(tokens);
        return tokens;
    }

    private void tokenize(TokenList tokens) throws IOException {
        Token token;
        do {
            token = nextToken();
            tokens.add(token);
        } while (token.getTokenType() != TokenType.END_DOCUMENT);
    }

    private Token nextToken() throws IOException {
        skipWhitespace();

        char ch = charReader.next();
        if (ch == (char) -1) {
            return Token.END_DOCUMENT;
        }

        switch (ch) {
            case '{':
                return Token.BEGIN_OBJECT;
            case '}':
                return Token.END_OBJECT;
            case '[':
                return Token.BEGIN_ARRAY;
            case ']':
                return Token.END_ARRAY;
            case ',':
                return Token.SEP_COMMA;
            case ':':
                return Token.SEP_COLON;
            case 'n':
                return readNull();
            case 't':
                if (charReader.next() == 'r' && charReader.next() == 'u' && charReader.next() == 'e') {
                    return Token.TRUE;
                }
                throw new JsonParseException("Invalid JSON: expected 'true'");
            case 'f':
                if (charReader.next() == 'a' && charReader.next() == 'l' && charReader.next() == 's' && charReader.next() == 'e') {
                    return Token.FALSE;
                }
                throw new JsonParseException("Invalid JSON: expected 'false'");
            case '"':
                return readString();
            case '-':
                return readNumber(true);
        }

        if (ch >= '0' && ch <= '9') {
            charReader.back();
            return readNumber(false);
        }

        throw new JsonParseException("Invalid character: " + ch);
    }

    private void skipWhitespace() throws IOException {
        char ch;
        while ((ch = charReader.next()) != (char) -1) {
            if (ch != ' ' && ch != '\t' && ch != '\r' && ch != '\n') {
                charReader.back();
                break;
            }
        }
    }

    private Token readString() throws IOException {
        StringBuilder sb = new StringBuilder(64);
        char ch;
        while ((ch = charReader.next()) != (char) -1) {
            if (ch == '\\') {
                char nc = charReader.next();
                switch (nc) {
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case 'u':
                        int val = 0;
                        for (int i = 0; i < 4; i++) {
                            char hc = charReader.next();
                            int digit = hexToInt(hc);
                            if (digit < 0) {
                                throw new JsonParseException("Invalid unicode escape");
                            }
                            val = (val << 4) | digit;
                        }
                        sb.append((char) val);
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'b':
                        sb.append('\b');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    default:
                        throw new JsonParseException("Invalid escape: \\" + nc);
                }
            } else if (ch == '"') {
                return new Token(TokenType.STRING, sb.toString());
            } else if (ch == '\r' || ch == '\n') {
                throw new JsonParseException("Unterminated string");
            } else {
                sb.append(ch);
            }
        }
        throw new JsonParseException("Unterminated string");
    }

    private int hexToInt(char ch) {
        if (ch >= '0' && ch <= '9') return ch - '0';
        if (ch >= 'a' && ch <= 'f') return ch - 'a' + 10;
        if (ch >= 'A' && ch <= 'F') return ch - 'A' + 10;
        return -1;
    }

    private Token readNumber(boolean negative) throws IOException {
        StringBuilder sb = new StringBuilder(16);
        if (negative) {
            sb.append('-');
        }
        char ch = charReader.next();
        if (ch == '0') {
            sb.append('0');
            appendFracAndExp(sb);
            return new Token(TokenType.NUMBER, sb.toString());
        }
        if (ch >= '1' && ch <= '9') {
            sb.append(ch);
            while ((ch = charReader.next()) >= '0' && ch <= '9') {
                sb.append(ch);
            }
            if (ch != (char) -1) {
                charReader.back();
            }
            appendFracAndExp(sb);
            return new Token(TokenType.NUMBER, sb.toString());
        }
        throw new JsonParseException("Invalid number");
    }

    private void appendFracAndExp(StringBuilder sb) throws IOException {
        char ch = charReader.next();
        if (ch == '.') {
            sb.append('.');
            ch = charReader.next();
            if (ch < '0' || ch > '9') {
                throw new JsonParseException("Invalid fraction");
            }
            sb.append(ch);
            while ((ch = charReader.next()) >= '0' && ch <= '9') {
                sb.append(ch);
            }
            if (ch != (char) -1) {
                charReader.back();
            }
            ch = charReader.peek();
        }
        if (ch == 'e' || ch == 'E') {
            sb.append(charReader.next());
            ch = charReader.next();
            if (ch == '+' || ch == '-') {
                sb.append(ch);
                ch = charReader.next();
            }
            if (ch < '0' || ch > '9') {
                throw new JsonParseException("Invalid exponent");
            }
            sb.append(ch);
            while ((ch = charReader.next()) >= '0' && ch <= '9') {
                sb.append(ch);
            }
            if (ch != (char) -1) {
                charReader.back();
            }
        } else if (ch != (char) -1) {
            charReader.back();
        }
    }

    private Token readNull() throws IOException {
        if (charReader.next() == 'u' && charReader.next() == 'l' && charReader.next() == 'l') {
            return Token.NULL;
        }
        throw new JsonParseException("Invalid JSON: expected 'null'");
    }
}
