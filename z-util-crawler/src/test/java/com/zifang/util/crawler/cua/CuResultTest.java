package com.zifang.util.crawler.cua;

import com.zifang.util.crawler.pipeline.PipelineContext;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * CuResultTest类。
 */
public class CuResultTest {

    @Test
    /**
     * testDefaultConstructor方法。
     */
    public void testDefaultConstructor() {
        CuResult result = new CuResult();
        assertFalse(result.isSuccess());
        assertNotNull(result.getStepResults());
        assertTrue(result.getStepResults().isEmpty());
        assertNull(result.getContext());
        assertNull(result.getErrorMessage());
    }

    @Test
    /**
     * testBuilder方法。
     */
    public void testBuilder() {
        PipelineContext ctx = new PipelineContext();
        StepResult stepResult = StepResult.builder()
                .stepName("navigate")
                .success(true)
                .output("Navigated to http://example.com")
                .build();

        CuResult result = CuResult.builder()
                .success(true)
                .stepResults(Arrays.asList(stepResult))
                .context(ctx)
                .errorMessage(null)
                .build();

        assertTrue(result.isSuccess());
        assertEquals(1, result.getStepResults().size());
        assertEquals("navigate", result.getStepResults().get(0).getStepName());
        assertEquals(ctx, result.getContext());
        assertNull(result.getErrorMessage());
    }

    @Test
    /**
     * testSettersAndGetters方法。
     */
    public void testSettersAndGetters() {
        CuResult result = new CuResult();

        result.setSuccess(true);
        assertTrue(result.isSuccess());

        result.setErrorMessage("Test error");
        assertEquals("Test error", result.getErrorMessage());

        PipelineContext ctx = new PipelineContext();
        result.setContext(ctx);
        assertEquals(ctx, result.getContext());

        StepResult stepResult = StepResult.builder().stepName("test").success(true).build();
        result.addStepResult(stepResult);
        assertEquals(1, result.getStepResults().size());
    }

    @Test
    /**
     * testSetStepResults方法。
     */
    public void testSetStepResults() {
        CuResult result = new CuResult();
        StepResult step1 = StepResult.builder().stepName("step1").success(true).build();
        StepResult step2 = StepResult.builder().stepName("step2").success(false).build();

        result.setStepResults(Arrays.asList(step1, step2));

        assertEquals(2, result.getStepResults().size());
        assertEquals("step1", result.getStepResults().get(0).getStepName());
        assertEquals("step2", result.getStepResults().get(1).getStepName());
    }
}
