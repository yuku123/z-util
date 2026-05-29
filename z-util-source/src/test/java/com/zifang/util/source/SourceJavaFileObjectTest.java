package com.zifang.util.source;

import com.zifang.util.source.compiler.SourceJavaFileObject;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static org.junit.Assert.*;

/**
 * SourceJavaFileObject 测试
 */
public class SourceJavaFileObjectTest {

    @Test
    public void testConstructorWithSource() {
        String sourceCode = "public class Test {}";
        SourceJavaFileObject fileObject = new SourceJavaFileObject("Test", sourceCode);

        assertEquals(JavaFileObject.Kind.SOURCE, fileObject.getKind());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCharContent() throws Exception {
        String sourceCode = "package com.example;\npublic class Hello {}\n";
        SourceJavaFileObject fileObject = new SourceJavaFileObject("Hello", sourceCode);

        // SourceJavaFileObject 没有覆盖 getCharContent，调用父类方法会抛异常
        fileObject.getCharContent(false);
    }

    @Test
    public void testGetName() {
        SourceJavaFileObject fileObject = new SourceJavaFileObject("com.example.Test", "source code");
        assertTrue(fileObject.getName().contains("com.example.Test"));
    }
}