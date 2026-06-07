package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.*;
import com.zifang.util.dsl.ast.*;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

/**
 * JsonDebug17Test类。
 */
public class JsonDebug17Test {

    @Test
    /**
     * testAstToJava方法。
     */
    public void testAstToJava() throws Exception {
        // Directly test astToJava with a manually constructed AST
        JSONParser jsonParser = new JSONParser();

        // Create a SimpleASTNode representing Number "1"
        SimpleASTNode numberNode = new SimpleASTNode();
        numberNode.setType("Number");
        numberNode.setText("1");

        // Use reflection to call astToJava
        java.lang.reflect.Method m = JSONParser.class.getDeclaredMethod("astToJava", ASTNode.class);
        m.setAccessible(true);

        Object result = m.invoke(jsonParser, numberNode);
        System.out.println("astToJava(Number='1') = " + result + " type=" + (result != null ? result.getClass().getName() : "null"));
    }

    @Test
    /**
     * testParseScalar方法。
     */
    public void testParseScalar() throws Exception {
        JSONParser jsonParser = new JSONParser();

        java.lang.reflect.Method m = JSONParser.class.getDeclaredMethod("parseScalar", String.class, String.class);
        m.setAccessible(true);

        // Test number
        Object r1 = m.invoke(jsonParser, "number", "1");
        System.out.println("parseScalar(number, '1') = " + r1 + " type=" + (r1 != null ? r1.getClass().getName() : "null"));

        // Test string
        Object r2 = m.invoke(jsonParser, "string", "\"a\"");
        System.out.println("parseScalar(string, '\"a\"') = " + r2);

        // Test bool
        Object r3 = m.invoke(jsonParser, "bool", "true");
        System.out.println("parseScalar(bool, 'true') = " + r3);

        // Test null
        Object r4 = m.invoke(jsonParser, "Null", "null");
        System.out.println("parseScalar(Null, 'null') = " + r4);

        // Test terminal
        Object r5 = m.invoke(jsonParser, "terminal", "1");
        System.out.println("parseScalar(terminal, '1') = " + r5);
    }
}