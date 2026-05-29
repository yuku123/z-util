package com.zifang.util.source;

import org.junit.Test;

import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

/**
 * ModifierAdapter 测试
 */
public class ModifierAdapterTest {

    @Test
    public void testGetKeyWordPublic() {
        // 测试 PUBLIC 修饰符转换
        assertNotNull(
            com.zifang.util.source.generator.info.ModifierAdapter.getKeyWord(Modifier.PUBLIC)
        );
    }

    @Test
    public void testGetKeyWordPrivate() {
        // 测试 PRIVATE 修饰符转换
        assertNotNull(
            com.zifang.util.source.generator.info.ModifierAdapter.getKeyWord(Modifier.PRIVATE)
        );
    }

    @Test
    public void testGetKeyWordStatic() {
        // 测试 STATIC 修饰符转换
        assertNotNull(
            com.zifang.util.source.generator.info.ModifierAdapter.getKeyWord(Modifier.STATIC)
        );
    }

    @Test
    public void testKeywordValues() {
        // 验证返回的 Keyword 枚举值正确
        assertEquals(
            com.github.javaparser.ast.Modifier.Keyword.PUBLIC,
            com.zifang.util.source.generator.info.ModifierAdapter.getKeyWord(Modifier.PUBLIC)
        );
        assertEquals(
            com.github.javaparser.ast.Modifier.Keyword.PRIVATE,
            com.zifang.util.source.generator.info.ModifierAdapter.getKeyWord(Modifier.PRIVATE)
        );
        assertEquals(
            com.github.javaparser.ast.Modifier.Keyword.STATIC,
            com.zifang.util.source.generator.info.ModifierAdapter.getKeyWord(Modifier.STATIC)
        );
    }
}