package com.zifang.util.devops.docker;

import java.util.ArrayList;
import java.util.List;

/**
 * Docker 命令执行结果封装
 * <p>
 * 泛型封装 Docker 命令执行的成功/失败状态、标准输出、标准错误和附加数据
 *
 * @param <T> 结果数据类型
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

    /**
     * 创建包含数据的成功结果
     *
     * @param data 结果数据
     * @param <T>  数据类型
     * @return 成功结果对象
     */
    public static <T> DockerCommandResult<T> success(T data) {
        DockerCommandResult<T> result = new DockerCommandResult<>();
        result.success = true;
        result.data = data;
        return result;
    }

    /**
     * 创建包含标准输出的成功结果
     *
     * @param stdout 标准输出内容
     * @param <T>    数据类型
     * @return 成功结果对象
     */
    public static <T> DockerCommandResult<T> success(String stdout) {
        DockerCommandResult<T> result = new DockerCommandResult<>();
        result.success = true;
        result.stdout = stdout;
        return result;
    }

    /**
     * 创建包含退出码和错误信息的失败结果
     *
     * @param exitCode 退出码
     * @param stderr   标准错误内容
     * @param <T>      数据类型
     * @return 失败结果对象
     */
    public static <T> DockerCommandResult<T> fail(int exitCode, String stderr) {
        DockerCommandResult<T> result = new DockerCommandResult<>();
        result.success = false;
        result.exitCode = exitCode;
        result.stderr = stderr;
        return result;
    }

    /**
     * 创建包含错误消息的失败结果
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 失败结果对象
     */
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
