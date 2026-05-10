package com.zifang.util.workflow.engine.interfaces;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.conponents.WorkFlowApplicationContext;

import java.util.Objects;

/**
 * 执行引擎服务的抽象类
 */
public abstract class AbstractEngineService {

    /**
     * 每个执行服务都要传入执行上下文
     */
    private WorkFlowApplicationContext workFlowApplicationContext;

    /**
     * 因为调用的方法的参数多种多样，因此参数不能是简单的json格式，因此都会转化为object，由工作的类对此进行改造
     */
    protected Object invokeParameter;

    /**
     * 一个service只会包裹一个dataset
     */
    //    protected Dataset<Row> dataset;

    /**
     * 执行引擎服务的执行
     */
    public abstract void exec(ExecutableWorkflowNode executableWorkflowNode);

    public AbstractEngineService() {
    }

    public WorkFlowApplicationContext getWorkFlowApplicationContext() {
        return workFlowApplicationContext;
    }

    public void setWorkFlowApplicationContext(WorkFlowApplicationContext workFlowApplicationContext) {
        this.workFlowApplicationContext = workFlowApplicationContext;
    }

    public Object getInvokeParameter() {
        return invokeParameter;
    }

    public void setInvokeParameter(Object invokeParameter) {
        this.invokeParameter = invokeParameter;
    }

    @Override
    public String toString() {
        return "AbstractEngineService{workFlowApplicationContext=" + workFlowApplicationContext + ", invokeParameter=" + invokeParameter + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEngineService that = (AbstractEngineService) o;
        return Objects.equals(workFlowApplicationContext, that.workFlowApplicationContext) &&
                Objects.equals(invokeParameter, that.invokeParameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workFlowApplicationContext, invokeParameter);
    }
}
