package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.*;
import com.zifang.util.dsl.ast.*;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

/**
 * JsonDebug18Test类。
 */
public class JsonDebug18Test {

    @Test
    /**
     * testAstToJavaDirectly方法。
     */
    public void testAstToJavaDirectly() throws Exception {
        // Manually build AST for {"a":1} and call astToJava
        JSONParser jsonParser = new JSONParser();

        // Build: object -> LBrace pair RBrace
        //         pair  -> string Colon value
        //         value -> number
        //         number -> Number "1"
        //         string -> StringLiteral "\"a\""

        SimpleASTNode objNode = new SimpleASTNode();
        objNode.setType("object");

        // LBrace child
        SimpleASTNode lbrace = new SimpleASTNode();
        lbrace.setType("LBrace");
        lbrace.setText("{");
        objNode.addChild(lbrace);

        // pair child
        SimpleASTNode pairNode = new SimpleASTNode();
        pairNode.setType("pair");

        // string child of pair
        SimpleASTNode strNode = new SimpleASTNode();
        strNode.setType("string");
        SimpleASTNode strLit = new SimpleASTNode();
        strLit.setType("StringLiteral");
        strLit.setText("\"a\"");
        strNode.addChild(strLit);
        pairNode.addChild(strNode);

        // Colon child
        SimpleASTNode colon = new SimpleASTNode();
        colon.setType("Colon");
        colon.setText(":");
        pairNode.addChild(colon);

        // value child of pair
        SimpleASTNode valNode = new SimpleASTNode();
        valNode.setType("value");
        SimpleASTNode numNode = new SimpleASTNode();
        numNode.setType("number");
        SimpleASTNode numLit = new SimpleASTNode();
        numLit.setType("Number");
        numLit.setText("1");
        numNode.addChild(numLit);
        valNode.addChild(numNode);
        pairNode.addChild(valNode);

        objNode.addChild(pairNode);

        // RBrace child
        SimpleASTNode rbrace = new SimpleASTNode();
        rbrace.setType("RBrace");
        rbrace.setText("}");
        objNode.addChild(rbrace);

        // Call astToJava via reflection
        java.lang.reflect.Method m = JSONParser.class.getDeclaredMethod("astToJava", ASTNode.class);
        m.setAccessible(true);

        try {
            Object result = m.invoke(jsonParser, objNode);
            System.out.println("astToJava(object) = " + result + " type=" + (result != null ? result.getClass().getName() : "null"));
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getCause().getClass().getName() + " " + e.getCause().getMessage());
        }
    }

    @Test
    /**
     * testExtractStringValue方法。
     */
    public void testExtractStringValue() throws Exception {
        JSONParser jsonParser = new JSONParser();

        // Build: string -> StringLiteral "\"a\""
        SimpleASTNode strNode = new SimpleASTNode();
        strNode.setType("string");
        SimpleASTNode strLit = new SimpleASTNode();
        strLit.setType("StringLiteral");
        strLit.setText("\"a\"");
        strNode.addChild(strLit);

        java.lang.reflect.Method m = JSONParser.class.getDeclaredMethod("extractStringValue", ASTNode.class);
        m.setAccessible(true);

        try {
            Object result = m.invoke(jsonParser, strNode);
            System.out.println("extractStringValue = " + result);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + (e.getCause() != null ? e.getCause().getClass().getName() + " " + e.getCause().getMessage() : e.getMessage()));
            e.getCause().printStackTrace();
        }
    }
}