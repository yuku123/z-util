package com.zifang.util.devops.docker;

import java.util.ArrayList;
import java.util.List;

/**
 * Docker 命令执行结果封装
 */
public class DockerCommandResult<T> {

    private boolean success;
    private int exitCode;
    private String stdout;
    private String stderr;
    private T data;
    private String message;

    public DockerCommandResult() {
    }

    public static <T> DockerCommandResult<T> success(T data) {
        DockerCommandResult<T> result = new DockerCommandResult<>();
        result.success = true;
        result.data = data;
        return result;
    }

    public static <T> DockerCommandResult<T> success(String stdout) {
        DockerCommandResult<T> result = new DockerCommandResult<>();
        result.success = true;
        result.stdout = stdout;
        return result;
    }

    public static <T> DockerCommandResult<T> fail(int exitCode, String stderr) {
        DockerCommandResult<T> result = new DockerCommandResult<>();
        result.success = false;
        result.exitCode = exitCode;
        result.stderr = stderr;
        return result;
    }

    public static <T> DockerCommandResult<T> fail(String message) {
        DockerCommandResult<T> result = new DockerCommandResult<>();
        result.success = false;
        result.message = message;
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "DockerCommandResult{" +
                "success=" + success +
                ", exitCode=" + exitCode +
                ", stdout='" + stdout + '\'' +
                ", stderr='" + stderr + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
