package com.zifang.util.crawler.cua;

import org.junit.Test;

import static org.junit.Assert.*;

public class StepResultTest {

    @Test
    public void testDefaultConstructor() {
        StepResult result = new StepResult();
        assertNull(result.getStepName());
        assertFalse(result.isSuccess());
        assertNull(result.getOutput());
        assertNull(result.getErrorMessage());
        assertEquals(0, result.getDurationMs());
    }

    @Test
    public void testTwoArgConstructor() {
        StepResult result = new StepResult("click", true);
        assertEquals("click", result.getStepName());
        assertTrue(result.isSuccess());
    }

    @Test
    public void testBuilder() {
        StepResult result = StepResult.builder()
                .stepName("navigate")
                .success(true)
                .output("http://example.com")
                .errorMessage(null)
                .durationMs(100)
                .build();

        assertEquals("navigate", result.getStepName());
        assertTrue(result.isSuccess());
        assertEquals("http://example.com", result.getOutput());
        assertNull(result.getErrorMessage());
        assertEquals(100, result.getDurationMs());
    }

    @Test
    public void testSettersAndGetters() {
        StepResult result = new StepResult();

        result.setStepName("input");
        assertEquals("input", result.getStepName());

        result.setSuccess(true);
        assertTrue(result.isSuccess());

        result.setOutput("some text");
        assertEquals("some text", result.getOutput());

        result.setErrorMessage("error occurred");
        assertEquals("error occurred", result.getErrorMessage());

        result.setDurationMs(250);
        assertEquals(250, result.getDurationMs());
    }

    @Test
    public void testBuilderWithAllFields() {
        StepResult result = StepResult.builder()
                .stepName("extract")
                .success(true)
                .output("extracted data")
                .errorMessage(null)
                .durationMs(50)
                .build();

        assertEquals("extract", result.getStepName());
        assertTrue(result.isSuccess());
        assertEquals("extracted data", result.getOutput());
        assertEquals(50, result.getDurationMs());
    }

    @Test
    public void testBuilderWithFailedResult() {
        StepResult result = StepResult.builder()
                .stepName("click")
                .success(false)
                .errorMessage("Element not found")
                .durationMs(10)
                .build();

        assertEquals("click", result.getStepName());
        assertFalse(result.isSuccess());
        assertEquals("Element not found", result.getErrorMessage());
        assertEquals(10, result.getDurationMs());
        assertNull(result.getOutput());
    }
}
