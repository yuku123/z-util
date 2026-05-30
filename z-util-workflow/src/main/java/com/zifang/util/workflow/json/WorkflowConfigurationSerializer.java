package com.zifang.util.workflow.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zifang.util.workflow.config.WorkflowConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 工作流配置JSON序列化器。
 * <p>
 * 使用Jackson库实现WorkflowConfiguration与JSON格式之间的相互转换。
 * 支持将配置序列化为JSON字符串或文件，以及从JSON字符串或文件反序列化。
 *
 * @see WorkflowConfiguration
 * @see ObjectMapper
 */
/**
 * WorkflowConfigurationSerializer类。
 */
public class WorkflowConfigurationSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Serialize WorkflowConfiguration to JSON string
     *
     * @param config the workflow configuration to serialize
     * @return JSON string representation
     */
    /**
     * toJson方法。
     *      * @param config WorkflowConfiguration类型参数
     * @return String类型返回值
     */
    public String toJson(WorkflowConfiguration config) {
        try {
            return objectMapper.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize WorkflowConfiguration to JSON", e);
        }
    }

    /**
     * Deserialize WorkflowConfiguration from JSON string
     *
     * @param json JSON string
     * @return WorkflowConfiguration instance
     */
    /**
     * fromJson方法。
     *      * @param json String类型参数
     * @return WorkflowConfiguration类型返回值
     */
    public WorkflowConfiguration fromJson(String json) {
        try {
            return objectMapper.readValue(json, WorkflowConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize WorkflowConfiguration from JSON", e);
        }
    }

    /**
     * Write WorkflowConfiguration to file
     *
     * @param config the workflow configuration to write
     * @param file   the target file
     */
    /**
     * toJsonFile方法。
     *      * @param config WorkflowConfiguration类型参数
     * @param file File类型参数
     */
    public void toJsonFile(WorkflowConfiguration config, File file) {
        try {
            Path path = Paths.get(file.toURI());
            Files.createDirectories(path.getParent());
            objectMapper.writeValue(file, config);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write WorkflowConfiguration to file: " + file.getAbsolutePath(), e);
        }
    }

    /**
     * Read WorkflowConfiguration from file
     *
     * @param file the source file
     * @return WorkflowConfiguration instance
     */
    /**
     * fromJsonFile方法。
     *      * @param file File类型参数
     * @return WorkflowConfiguration类型返回值
     */
    public WorkflowConfiguration fromJsonFile(File file) {
        try {
            return objectMapper.readValue(file, WorkflowConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read WorkflowConfiguration from file: " + file.getAbsolutePath(), e);
        }
    }
}