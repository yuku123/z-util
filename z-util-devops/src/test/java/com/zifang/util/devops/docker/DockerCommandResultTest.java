package com.zifang.util.devops.docker;

import org.junit.Test;

import static org.junit.Assert.*;

public class DockerCommandResultTest {

    @Test
    public void testSuccessWithData() {
        DockerCommandResult<String> result = DockerCommandResult.success("hello");
        assertTrue(result.isSuccess());
        assertEquals("hello", result.getStdout());
        assertNull(result.getStderr());
    }

    @Test
    public void testSuccessWithTypedData() {
        DockerCommandResult<Integer> result = DockerCommandResult.success(42);
        assertTrue(result.isSuccess());
        assertEquals(Integer.valueOf(42), result.getData());
        assertNull(result.getStdout());
    }

    @Test
    public void testFailWithExitCodeAndStderr() {
        DockerCommandResult<String> result = DockerCommandResult.fail(1, "error message");
        assertFalse(result.isSuccess());
        assertEquals(1, result.getExitCode());
        assertEquals("error message", result.getStderr());
    }

    @Test
    public void testFailWithMessage() {
        DockerCommandResult<String> result = DockerCommandResult.fail("connection timeout");
        assertFalse(result.isSuccess());
        assertEquals("connection timeout", result.getMessage());
    }

    @Test
    public void testToString() {
        DockerCommandResult<String> result = DockerCommandResult.fail(127, "not found");
        String str = result.toString();
        assertTrue(str.contains("success=false"));
        assertTrue(str.contains("exitCode=127"));
        assertTrue(str.contains("not found"));
    }

    @Test
    public void testSettersAndGetters() {
        DockerCommandResult<String> result = new DockerCommandResult<>();
        result.setSuccess(true);
        result.setExitCode(0);
        result.setStdout("output");
        result.setStderr("");
        result.setData("data");
        result.setMessage("msg");

        assertTrue(result.isSuccess());
        assertEquals(0, result.getExitCode());
        assertEquals("output", result.getStdout());
        assertEquals("", result.getStderr());
        assertEquals("data", result.getData());
        assertEquals("msg", result.getMessage());
    }
}
