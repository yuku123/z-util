package com.zifang.util.crawler.cua;

import com.zifang.util.crawler.cua.steps.ExtractStep;
import com.zifang.util.crawler.cua.steps.InputStep;
import com.zifang.util.crawler.cua.steps.NavigateStep;
import com.zifang.util.crawler.cua.steps.Step;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * OperationRegistryTest类。
 */
public class OperationRegistryTest {

    private OperationRegistry registry;

    @Before
    /**
     * setUp方法。
     */
    public void setUp() {
        registry = new OperationRegistry();
    }

    @Test
    /**
     * testRegisterAndResolve方法。
     */
    public void testRegisterAndResolve() {
        registry.register("custom_step", NavigateStep.class);
        Step step = registry.resolve("custom_step");
        assertNotNull(step);
        assertEquals("navigate", step.getName());
    }

    @Test
    /**
     * testResolveBuiltInSteps方法。
     */
    public void testResolveBuiltInSteps() {
        assertNotNull(registry.resolve("navigate"));
        assertEquals("navigate", registry.resolve("navigate").getName());

        assertNotNull(registry.resolve("click"));
        assertEquals("click", registry.resolve("click").getName());

        assertNotNull(registry.resolve("input"));
        assertEquals("input", registry.resolve("input").getName());

        assertNotNull(registry.resolve("wait"));
        assertEquals("wait", registry.resolve("wait").getName());

        assertNotNull(registry.resolve("extract"));
        assertEquals("extract", registry.resolve("extract").getName());

        assertNotNull(registry.resolve("screenshot"));
        assertEquals("screenshot", registry.resolve("screenshot").getName());

        assertNotNull(registry.resolve("switch"));
        assertEquals("switch", registry.resolve("switch").getName());
    }

    @Test
    /**
     * testResolveCaseInsensitive方法。
     */
    public void testResolveCaseInsensitive() {
        Step step1 = registry.resolve("navigate");
        Step step2 = registry.resolve("Navigate");
        Step step3 = registry.resolve("NAVIGATE");

        assertEquals(step1.getClass(), step2.getClass());
        assertEquals(step2.getClass(), step3.getClass());
    }

    @Test
    /**
     * testResolveUnknownStep方法。
     */
    public void testResolveUnknownStep() {
        Step step = registry.resolve("unknown_step_xyz");
        assertNull(step);
    }

    @Test
    /**
     * testIsRegistered方法。
     */
    public void testIsRegistered() {
        assertTrue(registry.isRegistered("navigate"));
        assertTrue(registry.isRegistered("click"));
        assertFalse(registry.isRegistered("custom_unregistered"));
    }

    @Test
    /**
     * testRegisterCustomStep方法。
     */
    public void testRegisterCustomStep() {
        registry.register("my_step", ExtractStep.class);
        assertTrue(registry.isRegistered("my_step"));

        Step step = registry.resolve("my_step");
        assertNotNull(step);
        assertTrue(step instanceof ExtractStep);
    }

    @Test
    /**
     * testOverrideBuiltinStep方法。
     */
    public void testOverrideBuiltinStep() {
        registry.register("click", InputStep.class);
        Step step = registry.resolve("click");
        assertNotNull(step);
        assertTrue(step instanceof InputStep);
    }
}
