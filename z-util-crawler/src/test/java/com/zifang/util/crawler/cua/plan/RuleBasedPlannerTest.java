package com.zifang.util.crawler.cua.plan;

import com.zifang.util.crawler.cua.CuResult;
import com.zifang.util.crawler.cua.OperationRegistry;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RuleBasedPlannerTest {

    private OperationRegistry registry;
    private RuleBasedPlanner planner;

    @Before
    public void setUp() {
        registry = new OperationRegistry();
        planner = new RuleBasedPlanner(registry);
    }

    @Test
    public void testPlanWithMatchingRule() {
        PipelineContext ctx = new PipelineContext();
        ctx.putParameter("browserClient", null); // Will cause step to fail but that's ok for this test

        CuResult result = planner.plan("click the button", ctx);
        
        // The predefined rule for "click.*button" should match
        assertFalse(result.isSuccess()); // Will fail due to null browserClient
        assertNotNull(result.getStepResults());
    }

    @Test
    public void testPlanWithNoMatchingRule() {
        PipelineContext ctx = new PipelineContext();
        
        CuResult result = planner.plan("do something completely unknown xyz123", ctx);
        
        assertFalse(result.isSuccess());
        assertEquals("No matching rule found for task: do something completely unknown xyz123", result.getErrorMessage());
        assertTrue(result.getStepResults().isEmpty());
    }

    @Test
    public void testPlanExtractData() {
        PipelineContext ctx = new PipelineContext();
        ctx.putParameter("browserClient", null);
        
        CuResult result = planner.plan("extract data from page", ctx);
        
        // Rule "extract.*data" should match with ["wait_element", "extract"]
        assertNotNull(result.getContext());
    }

    @Test
    public void testPlanNavigate() {
        PipelineContext ctx = new PipelineContext();
        ctx.putParameter("browserClient", null);
        
        CuResult result1 = planner.plan("open url http://example.com", ctx);
        CuResult result2 = planner.plan("go to http://test.com", ctx);
        
        assertNotNull(result1);
        assertNotNull(result2);
    }

    @Test
    public void testAddRule() {
        planner.addRule("custom.*task", Arrays.asList("navigate", "click"));
        
        PipelineContext ctx = new PipelineContext();
        ctx.putParameter("browserClient", null);
        
        CuResult result = planner.plan("custom task for testing", ctx);
        assertNotNull(result);
    }

    @Test
    public void testRuleMatches() {
        RuleBasedPlanner.Rule rule = new RuleBasedPlanner.Rule("click.*button", Arrays.asList("test"));
        
        assertTrue(rule.matches("click the button"));
        assertTrue(rule.matches("CLICK THE BUTTON"));
        assertTrue(rule.matches("Click a button now"));
        assertFalse(rule.matches("submit form"));
    }

    @Test
    public void testRuleGetStepSequence() {
        List<String> steps = Arrays.asList("navigate", "click", "extract");
        RuleBasedPlanner.Rule rule = new RuleBasedPlanner.Rule("test.*pattern", steps);
        
        assertEquals(steps, rule.getStepSequence());
    }

    @Test
    public void testExecutePlanWithUnknownStep() {
        registry.register("unknown_step", com.zifang.util.crawler.cua.steps.NavigateStep.class);
        
        PipelineContext ctx = new PipelineContext();
        ctx.putParameter("browserClient", null);
        
        // Add a rule with an unknown step
        planner.addRule("test.*unknown", Arrays.asList("unknown_step_does_not_exist"));
        
        CuResult result = planner.plan("test unknown step", ctx);
        
        assertFalse(result.isSuccess());
        assertEquals(1, result.getStepResults().size());
        assertFalse(result.getStepResults().get(0).isSuccess());
        assertTrue(result.getStepResults().get(0).getErrorMessage().contains("Unknown step"));
    }

    @Test
    public void testRuleCaseInsensitive() {
        RuleBasedPlanner.Rule rule = new RuleBasedPlanner.Rule("TEST.*PATTERN", Arrays.asList("step"));
        
        assertTrue(rule.matches("test pattern"));
        assertTrue(rule.matches("TEST PATTERN"));
        assertTrue(rule.matches("Test Pattern"));
    }
}
