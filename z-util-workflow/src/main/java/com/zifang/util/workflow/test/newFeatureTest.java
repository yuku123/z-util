package com.zifang.util.workflow.test;

import com.zifang.util.workflow.conponents.WorkFlowApplicationContext;

/**
 * 新功能测试类。
 * <p>
 * 包含工作流引擎的各项功能测试方法，
 * 用于验证工作流配置加载、节点执行、状态流转等核心功能。
 * <p>
 * 测试资源文件路径：/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/feature/
 *
 * @see WorkFlowApplicationContext
 */
/**
 * newFeatureTest类。
 */
public class newFeatureTest {

    /**
     * 测试完整工作流执行功能。
     * <p>
     * 加载工作流配置文件，执行所有节点，验证整体流程是否正常。
     */
    /**
     * testAll方法。
     */
    public void testAll() {

        String filePath = "/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/feature/workflow_all.json";

        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext();
        workFlowApplicationContext.initialByLocalFilePath(filePath);
        workFlowApplicationContext.executeTask();
    }

//    public void test1All(){
//
//        String filePath = "/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/feature/workflow_all.json";
//
//        WorkFlowApplication workFlowApplication = new WorkFlowApplicationBuilder().
//
//
//
//
//        WorkFlowApplication workFlowApplication = new WorkFlowApplication();
//
//        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext();
//        workFlowApplicationContext.initialByLocalFilePath(filePath);
//        workFlowApplicationContext.executeTask();
//    }

    /**
     * main方法。
     *      * @param args String[]类型参数
     * @return static void类型返回值
     */
    public static void main(String[] args) {
        new newFeatureTest().testAll();
    }
}
