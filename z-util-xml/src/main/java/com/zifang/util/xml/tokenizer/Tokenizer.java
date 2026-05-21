package com.zifang.util.xml.tokenizer;

import com.zifang.util.xml.exception.XmlParseException;

import java.io.IOException;

/**
 * XML 分词器，将 XML 字符串分解为 Token 序列。
 */
public class Tokenizer {

    private CharReader cr;
    private TokenList tokens;

    public TokenList tokenize(CharReader cr) throws IOException {
        this.cr = cr;
        this.tokens = new TokenList();
        while (cr.hasMore()) {
            char ch = cr.peek();
            if (ch == '<') {
                readMarkup();
            } else {
                readTextContent();
            }
        }
        tokens.add(new Token(TokenType.END_DOCUMENT, null));
        return tokens;
    }

    // ===== 读到 '<' =====
    private void readMarkup() throws IOException {
        cr.next(); // consume '<'
        char ch = cr.next();
        if (ch == (char) -1) throw new XmlParseException("Unclosed <");

        if (ch == '!') {
            ch = cr.peek();
            if (ch == '[') {
                cr.next(); // consume '['
                readCdataSection();
            } else if (ch == '-') {
                readComment();
            } else {
                skipUntil('>');
            }
        } else if (ch == '?') {
            readProcessingInstruction();
        } else if (ch == '/') {
            readEndTag();
        } else {
            readStartTag(ch);
        }
    }

    // ===== 开始标签 =====
    private void readStartTag(char firstChar) throws IOException {
        StringBuilder name = new StringBuilder();
        if (!isNameStart(firstChar)) throw new XmlParseException("Invalid tag start: " + firstChar);
        name.append(firstChar);

        char ch = cr.peek();
        while (ch != (char) -1 && isNameChar(ch)) {
            name.append(cr.next());
            ch = cr.peek();
        }

        tokens.add(new Token(TokenType.TAG_OPEN, name.toString()));
        readTagTail();
    }

    // ===== 标签尾部（属性、自闭合） =====
    private void readTagTail() throws IOException {
        while (true) {
            char ch = cr.peek();
            System.err.println("DBG readTagTail peek ch='" + (ch == (char)-1 ? "EOF" : ch) + "' pos?");
            if (ch == '>') {
                cr.next();
                tokens.add(new Token(TokenType.TAG_CLOSE, ">"));
                System.err.println("DBG -> TAG_CLOSE");
                return;
            }
            if (ch == '/') {
                cr.next(); // consume '/'
                ch = cr.next(); // consume '>'
                System.err.println("DBG self-close check ch='" + (ch == (char)-1 ? "EOF" : ch) + "'");
                if (ch != '>') throw new XmlParseException("Expected > after /");
                tokens.add(new Token(TokenType.TAG_SELF_CLOSE, "/>"));
                System.err.println("DBG -> TAG_SELF_CLOSE");
                return;
            }
            if (ch == '<') {
                // end of tag, let main loop handle '<'
                System.err.println("DBG -> '<' return");
                return;
            }
            if (isWhiteSpace(ch)) {
                cr.next();
                continue;
            }
            readAttribute();
        }
    }

    // ===== 属性 =====
    private void readAttribute() throws IOException {
        char ch = cr.peek();
        System.err.println("DBG readAttribute peek ch='" + (ch == (char)-1 ? "EOF" : ch) + "'");
        if (ch == '<' || ch == '>' || ch == '/') return; // tail handles or markup start

        // attribute name
        StringBuilder name = new StringBuilder();
        while (ch != (char) -1 && !isWhiteSpace(ch) && ch != '=' && ch != '>' && ch != '/' && ch != '<') {
            name.append(cr.next());
            ch = cr.peek();
        }
        String attrName = name.toString();

        // skip ws to '='
        ch = skipWs(ch);
        if (ch != '=') {
            // valueless attribute: e.g., disabled
            // consume one char (whitespace or '/') before returning
            if (ch != (char) -1 && ch != '>' && ch != '<' && ch != '/') cr.next();
            tokens.add(new Token(TokenType.ATTRIBUTE, attrName + "="));
            return;
        }
        cr.next(); // consume '='
        ch = skipWs(cr.peek());

        char quote = ch;
        if (quote != '\'' && quote != '"') throw new XmlParseException("Unquoted attr value: " + quote);
        System.err.println("DBG consuming opening quote, then reading value until '" + quote + "'");
        cr.next(); // consume opening quote
        StringBuilder val = new StringBuilder();
        ch = cr.next();
        System.err.println("DBG value loop start: ch='" + (ch == (char)-1 ? "EOF" : ch) + "' quote='" + quote + "'");
        while (ch != quote && ch != (char) -1) {
            if (ch == '&') val.append(readEntity());
            else val.append(ch);
            ch = cr.next();
            System.err.println("DBG value loop: ch='" + (ch == (char)-1 ? "EOF" : ch) + "'");
        }
        System.err.println("DBG value loop exit: ch='" + (ch == (char)-1 ? "EOF" : ch) + "'");
        if (ch == (char) -1) throw new XmlParseException("Unclosed attr value");
        System.err.println("DBG found closing quote '" + quote + "', pos before consume=" + getCharReaderPos() + ", char at that pos='" + getCharAtCurrentPos() + "'");
        cr.next(); // consume closing quote
        System.err.println("DBG found closing quote '" + quote + "', pos AFTER consume=" + getCharReaderPos() + ", char peek='" + (cr.peek() == (char)-1 ? "EOF" : cr.peek()) + "'");
        tokens.add(new Token(TokenType.ATTRIBUTE, attrName + "=" + val.toString()));
    }

    // ===== 结束标签 =====
    private void readEndTag() throws IOException {
        char ch = cr.peek();
        StringBuilder name = new StringBuilder();
        while (ch != (char) -1 && ch != '>') {
            if (isWhiteSpace(ch)) throw new XmlParseException("WS in end tag");
            name.append(cr.next());
            ch = cr.peek();
        }
        if (ch != '>') throw new XmlParseException("Unclosed end tag");
        cr.next(); // consume '>'
        tokens.add(new Token(TokenType.TAG_END, name.toString()));
        tokens.add(new Token(TokenType.TAG_CLOSE, ">"));
    }

    // ===== 处理指令 / XML 声明 =====
    private void readProcessingInstruction() throws IOException {
        StringBuilder sb = new StringBuilder("?");
        char ch = cr.next();
        while (ch != (char) -1) {
            if (ch == '?') {
                char nch = cr.peek();
                if (nch == '>') {
                    cr.next(); // consume '>'
                    sb.append('?');
                    break;
                }
            }
            sb.append(ch);
            ch = cr.next();
        }
        String s = sb.toString();
        String trimmed = s.trim();
        // Must be exactly "?xml" followed by space or '?' for it to be a DECLARATION
        // "?xml-stylesheet" starts with "?xml" but is a PI
        if (trimmed.startsWith("?xml") && (trimmed.length() == 4 || Character.isWhitespace(trimmed.charAt(4)))) {
            tokens.add(new Token(TokenType.DECLARATION, s));
        } else {
            tokens.add(new Token(TokenType.PROCESSING_INSTRUCTION, trimmed));
        }
    }

    // ===== CDATA =====
    private void readCdataSection() throws IOException {
        StringBuilder marker = new StringBuilder();
        char ch = cr.next();
        while (marker.length() < 6 && ch != '[') {
            marker.append(ch);
            ch = cr.next();
        }
        if (!marker.toString().equals("CDATA")) throw new XmlParseException("Not CDATA: " + marker);
        if (ch != '[') cr.next(); // consume '['

        StringBuilder data = new StringBuilder();
        ch = cr.next();
        while (ch != (char) -1) {
            if (ch == ']') {
                char nch = cr.next();
                if (nch == ']') {
                    char nnch = cr.peek();
                    if (nnch == '>') {
                        cr.next(); // consume '>'
                        break;
                    }
                    data.append(ch);
                    data.append(nch);
                    ch = nnch;
                    continue;
                }
                data.append(ch);
                ch = nch;
                continue;
            }
            data.append(ch);
            ch = cr.next();
        }
        tokens.add(new Token(TokenType.CDATA, data.toString()));
    }

    // ===== 注释 =====
    private void readComment() throws IOException {
        // At first '-' of '<!--' (already consumed '<!' in readMarkup, peek returned '-')
        // Use a state machine: 0=looking for first '-', 1=found first '-', looking for second '-'
        StringBuilder sb = new StringBuilder();
        int state = 0; // 0=normal, 1=saw first '-', 2=saw '--'
        char ch = cr.next(); // consume first '-'
        while (ch != (char) -1) {
            if (state == 0) {
                if (ch == '-') {
                    state = 1;
                } else {
                    sb.append(ch);
                }
            } else if (state == 1) {
                if (ch == '-') {
                    state = 2;
                } else {
                    sb.append('-');
                    sb.append(ch);
                    state = 0;
                }
            } else { // state == 2
                if (ch == '>') {
                    break;
                } else if (ch == '-') {
                    sb.append('-');
                } else {
                    sb.append('-');
                    sb.append('-');
                    sb.append(ch);
                    state = 0;
                }
            }
            ch = cr.next();
        }
        tokens.add(new Token(TokenType.COMMENT, sb.toString()));
    }

    // ===== 文本内容 =====
    private void readTextContent() throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch = cr.next();
        while (ch != (char) -1 && ch != '<') {
            if (ch == '&') sb.append(readEntity());
            else sb.append(ch);
            ch = cr.next();
        }
        if (ch == '<') cr.back();
        if (sb.length() > 0) tokens.add(new Token(TokenType.TEXT, sb.toString()));
    }

    // ===== 实体引用 =====
    private String readEntity() throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch = cr.next();
        while (ch != ';' && ch != (char) -1) {
            sb.append(ch);
            ch = cr.next();
        }
        if (ch == (char) -1) throw new XmlParseException("Unclosed entity");
        String e = sb.toString();
        switch (e) {
            case "amp": return "&";
            case "lt": return "<";
            case "gt": return ">";
            case "quot": return "\"";
            case "apos": return "'";
            default:
                if (e.startsWith("#x") || e.startsWith("#X")) {
                    return new String(Character.toChars(Integer.parseInt(e.substring(2), 16)));
                } else if (e.startsWith("#")) {
                    return new String(Character.toChars(Integer.parseInt(e.substring(1), 10)));
                }
                throw new XmlParseException("Unknown entity: &" + e + ";");
        }
    }

    // ===== 辅助 =====
    private char skipWs(char ch) throws IOException {
        while (isWhiteSpace(ch)) ch = cr.next();
        return ch;
    }

    private void skipUntil(char target) throws IOException {
        char ch = cr.next();
        while (ch != target && ch != (char) -1) ch = cr.next();
    }

    private boolean isWhiteSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    private boolean isNameStart(char ch) {
        return Character.isLetter(ch) || ch == '_' || ch == ':';
    }

    private boolean isNameChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_' || ch == ':' || ch == '-' || ch == '.';
    }
}
