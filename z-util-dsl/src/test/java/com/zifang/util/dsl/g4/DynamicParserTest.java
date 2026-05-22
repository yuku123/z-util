package com.zifang.util.dsl.g4;

import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.core.TokenReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicParserTest {

    @Test
    public void testLoadG4_BasicParserRule() {
        DynamicParser parser = new DynamicParser();
        String g4 = "lexer grammar Test;\n" +
                "ID : [a-z]+ ;\n" +
                "parser grammar TestParser;\n" +
                "prog : stat ;\n" +
                "stat : ID ;\n";

        parser.loadG4(g4);
        assertNotNull(parser);
    }

    @Test
    public void testParse_BasicRule() {
        DynamicLexer lexer = new DynamicLexer();
        String lexerG4 = "lexer grammar Test;\n" +
                "ID : [a-z]+ ;\n" +
                "WS : [ \\t\\n]+ -> channel(HIDDEN) ;\n";

        lexer.loadG4(lexerG4);
        lexer.setInput("hello");

        DynamicParser parser = new DynamicParser();
        String parserG4 = "lexer grammar Test;\n" +
                "ID : [a-z]+ ;\n" +
                "parser grammar TestParser;\n" +
                "prog : stat ;\n" +
                "stat : ID ;\n";

        parser.loadG4(parserG4);
        TokenReader tokenReader = lexer.getTokenReader();
        parser.setTokenReader(tokenReader);

        ASTNode node = parser.parse();
        assertNotNull(node);
    }

    @Test
    public void testParse_NoRulesLoaded() {
        DynamicParser parser = new DynamicParser();
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    public void testSetTokenReader() {
        DynamicParser parser = new DynamicParser();
        DynamicLexer lexer = new DynamicLexer();
        String g4 = "lexer grammar Test;\n" +
                "ID : [a-z]+ ;\n";

        lexer.loadG4(g4);
        lexer.setInput("test");
        lexer.tokenize();

        parser.setTokenReader(lexer.getTokenReader());
        assertNotNull(parser);
    }

    @Test
    public void testLoadG4_ParserAndLexerCombined() {
        DynamicLexer lexer = new DynamicLexer();
        DynamicParser parser = new DynamicParser();

        String combinedG4 = "lexer grammar ExprLexer;\n" +
                "ID : [a-z]+ ;\n" +
                "NUM : [0-9]+ ;\n" +
                "WS : [ \\t\\n]+ -> channel(HIDDEN) ;\n" +
                "parser grammar ExprParser;\n" +
                "prog : expr ;\n" +
                "expr : term (('+' | '-') term)* ;\n" +
                "term : factor (('*' | '/') factor)* ;\n" +
                "factor : NUM | '(' expr ')' ;\n";

        lexer.loadG4(combinedG4);
        parser.loadG4(combinedG4);

        lexer.setInput("a + b");
        lexer.tokenize();

        parser.setTokenReader(lexer.getTokenReader());
        ASTNode node = parser.parse();

        assertNotNull(node);
    }

    @Test
    public void testParse_SpecificStartRule() {
        DynamicLexer lexer = new DynamicLexer();
        String g4 = "lexer grammar Test;\n" +
                "ID : [a-z]+ ;\n" +
                "WS : [ \\t\\n]+ -> channel(HIDDEN) ;\n" +
                "parser grammar TestParser;\n" +
                "start : hello ;\n" +
                "hello : 'hello' ;\n";

        lexer.loadG4(g4);
        lexer.setInput("hello");

        DynamicParser parser = new DynamicParser();
        parser.loadG4(g4);
        parser.setTokenReader(lexer.getTokenReader());

        ASTNode node = parser.parse("start");
        assertNotNull(node);
    }

    @Test
    public void testParse_UnknownRule() {
        DynamicParser parser = new DynamicParser();
        String g4 = "lexer grammar Test;\n" +
                "ID : [a-z]+ ;\n" +
                "parser grammar TestParser;\n" +
                "start : hello ;\n" +
                "hello : 'hello' ;\n";

        parser.loadG4(g4);
        assertThrows(RuntimeException.class, () -> parser.parse("nonexistent"));
    }
}
