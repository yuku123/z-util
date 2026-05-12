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
 * JSON serializer for WorkflowConfiguration using Jackson
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
    public WorkflowConfiguration fromJsonFile(File file) {
        try {
            return objectMapper.readValue(file, WorkflowConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read WorkflowConfiguration from file: " + file.getAbsolutePath(), e);
        }
    }
}