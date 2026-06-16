package com.zifang.util.parser.proto;

import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.token.Token;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProtoG4DebugTest {

    @Test
    public void debugTokens() throws Exception {
        String content = "message User {\n  int32 id = 1;\n  string name = 2;\n}";
        InputStream is = getClass().getClassLoader().getResourceAsStream("ProtoLexer.g4");
        String lexerG4 = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput(content);
        List<Token> tokens = lexer.tokenize();
        System.out.println("Tokens (" + tokens.size() + "):");
        for (Token t : tokens) {
            System.out.println("  " + t.getTokenName() + " : '" + t.getText() + "'");
        }
    }
}
