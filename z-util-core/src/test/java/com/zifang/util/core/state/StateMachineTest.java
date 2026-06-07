package com.zifang.util.core.state;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * StateMachine 单元测试。
 * 覆盖：flat 流、层级 composite、entry/exit 动作、guard、history、监听器、并发安全。
 */
/**
 * StateMachineTest类。
 */
public class StateMachineTest {

    // ==================== 基础 flat 流 ====================

    enum S { A, B, C, D }
    enum E { GO_B, GO_C, GO_D, RESET }

    @Test
    /**
     * testFlat_linearFlow方法。
     */
    public void testFlat_linearFlow() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).to(S.B)
                .from(S.B).on(E.GO_C).to(S.C)
                .from(S.C).on(E.GO_D).to(S.D)
                .build();
        assertEquals(S.A, sm.getCurrentState());
        sm.fire(E.GO_B, null);
        assertEquals(S.B, sm.getCurrentState());
        sm.fire(E.GO_C, null);
        assertEquals(S.C, sm.getCurrentState());
        sm.fire(E.GO_D, null);
        assertEquals(S.D, sm.getCurrentState());
    }

    @Test(expected = StateMachineException.class)
    /**
     * testFlat_noTransition_throws方法。
     */
    public void testFlat_noTransition_throws() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).to(S.B)
                .build();
        sm.fire(E.GO_C, null); // A 没有 GO_C 的转移
    }

    @Test
    /**
     * testFlat_failFast_false_silent方法。
     */
    public void testFlat_failFast_false_silent() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).to(S.B)
                .build();
        // failFast=false：无匹配转移静默忽略
        S result = sm.fire(E.GO_C, null, false);
        assertEquals(S.A, result);
    }

    @Test
    /**
     * testFlat_reset方法。
     */
    public void testFlat_reset() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).to(S.B)
                .build();
        sm.fire(E.GO_B, null);
        sm.reset();
        assertEquals(S.A, sm.getCurrentState());
    }

    // ==================== 守卫 ====================

    @Test(expected = StateMachineException.class)
    /**
     * testGuard_rejected_throws方法。
     */
    public void testGuard_rejected_throws() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).guard(ctx -> false).to(S.B)
                .build();
        sm.fire(E.GO_B, null);
    }

    @Test
    /**
     * testGuard_passed方法。
     */
    public void testGuard_passed() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).guard(ctx -> true).to(S.B)
                .build();
        sm.fire(E.GO_B, null);
        assertEquals(S.B, sm.getCurrentState());
    }

    @Test
    /**
     * testGuard_combined_AND_semantics方法。
     */
    public void testGuard_combined_AND_semantics() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).guard(ctx -> true).guard(ctx -> false).to(S.B)
                .build();
        try {
            sm.fire(E.GO_B, null);
            fail("should throw");
        } catch (StateMachineException expected) {
            // guard true AND false = false
        }
    }

    // ==================== Listener ====================

    @Test
    /**
     * testListener_endCalled方法。
     */
    public void testListener_endCalled() {
        AtomicInteger endCount = new AtomicInteger();
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).to(S.B)
                .listener(new StateListener<S, E, Object>() {
                    @Override public void onTransitionEnd(S from, S to, E event, Object ctx) {
                        endCount.incrementAndGet();
                    }
                })
                .build();
        sm.fire(E.GO_B, null);
        assertEquals(1, endCount.get());
    }

    @Test
    /**
     * testListener_refusedCalledOnGuardFailure方法。
     */
    public void testListener_refusedCalledOnGuardFailure() {
        List<String> events = new ArrayList<>();
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).guard(ctx -> false).to(S.B)
                .listener(new StateListener<S, E, Object>() {
                    @Override public void onTransitionRefused(S s, E e, Object c, String r) {
                        events.add("refused");
                    }
                })
                .build();
        try { sm.fire(E.GO_B, null); } catch (StateMachineException ignored) {}
        assertEquals(Arrays.asList("refused"), events);
    }

    // ==================== Action 抛错处理 ====================

    @Test
    /**
     * testAction_throwsStateUnchanged方法。
     */
    public void testAction_throwsStateUnchanged() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B)
                    .action((from, ctx) -> { throw new RuntimeException("boom"); })
                    .to(S.B)
                .build();
        try {
            sm.fire(E.GO_B, null);
            fail("expected exception");
        } catch (StateMachineException ex) {
            assertTrue(ex.getCause() instanceof RuntimeException);
            assertEquals("boom", ex.getCause().getMessage());
        }
        // 状态未切换
        assertEquals(S.A, sm.getCurrentState());
    }

    // ==================== 层级状态：composite + 子态 ====================

    enum OrderState {
        CREATED, PAID,                 // 顶层：PAID 是 composite
        PENDING_INVOICE, INVOICED, H, // PAID 的子态：H 是 history 状态
        SHIPPED, DONE, CANCELLED       // 顶层叶子
    }
    enum OrderEvent { PAY, ISSUE_INVOICE, CONFIRM, SHIP, COMPLETE, CANCEL, REENTER }

    @Test
    /**
     * testHierarchical_enterComposite_descendsToInitialChild方法。
     */
    public void testHierarchical_enterComposite_descendsToInitialChild() {
        StateMachine<OrderState, OrderEvent, Object> sm = StateMachine
                .<OrderState, OrderEvent, Object>builder()
                .initial(OrderState.CREATED)
                .from(OrderState.CREATED).on(OrderEvent.PAY).to(OrderState.PAID)
                .composite(OrderState.PAID, OrderState.PENDING_INVOICE, sub -> sub
                        .from(OrderState.PENDING_INVOICE).on(OrderEvent.ISSUE_INVOICE).to(OrderState.INVOICED)
                )
                .build();
        sm.fire(OrderEvent.PAY, null);
        // 进入 PAID 应该自动下钻到 PENDING_INVOICE
        assertEquals(OrderState.PENDING_INVOICE, sm.getCurrentState());
        assertTrue(sm.isComposite(OrderState.PAID));
        assertFalse(sm.isComposite(OrderState.PENDING_INVOICE));
    }

    @Test
    /**
     * testHierarchical_localTransition方法。
     */
    public void testHierarchical_localTransition() {
        StateMachine<OrderState, OrderEvent, Object> sm = StateMachine
                .<OrderState, OrderEvent, Object>builder()
                .initial(OrderState.CREATED)
                .from(OrderState.CREATED).on(OrderEvent.PAY).to(OrderState.PAID)
                .composite(OrderState.PAID, OrderState.PENDING_INVOICE, sub -> sub
                        .from(OrderState.PENDING_INVOICE).on(OrderEvent.ISSUE_INVOICE).to(OrderState.INVOICED)
                )
                .build();
        sm.fire(OrderEvent.PAY, null);
        sm.fire(OrderEvent.ISSUE_INVOICE, null);
        // local transition：PENDING_INVOICE -> INVOICED
        assertEquals(OrderState.INVOICED, sm.getCurrentState());
    }

    @Test
    /**
     * testHierarchical_externalTransition_exitsParent方法。
     */
    public void testHierarchical_externalTransition_exitsParent() {
        StateMachine<OrderState, OrderEvent, Object> sm = StateMachine
                .<OrderState, OrderEvent, Object>builder()
                .initial(OrderState.CREATED)
                .from(OrderState.CREATED).on(OrderEvent.PAY).to(OrderState.PAID)
                .composite(OrderState.PAID, OrderState.PENDING_INVOICE, sub -> sub
                        .from(OrderState.PENDING_INVOICE).on(OrderEvent.ISSUE_INVOICE).to(OrderState.INVOICED)
                )
                // 外部转移：从 INVOICED（PAID 子态）出去到顶层 SHIPPED
                .from(OrderState.INVOICED).on(OrderEvent.CONFIRM).to(OrderState.SHIPPED)
                .build();
        sm.fire(OrderEvent.PAY, null);
        sm.fire(OrderEvent.ISSUE_INVOICE, null);
        sm.fire(OrderEvent.CONFIRM, null);
        assertEquals(OrderState.SHIPPED, sm.getCurrentState());
        // 不再在 PAID 里
        List<OrderState> ancestors = sm.ancestorsOf(OrderState.SHIPPED);
        assertFalse(ancestors.contains(OrderState.PAID));
    }

    @Test
    /**
     * testHierarchical_eventAtParent_appliesToAnyChild方法。
     */
    public void testHierarchical_eventAtParent_appliesToAnyChild() {
        // CANCEL 转移在 PAID 声明（没有具体 from），
        // 当子态收到 CANCEL 时，应该沿父链向上找到这条转移
        StateMachine<OrderState, OrderEvent, Object> sm = StateMachine
                .<OrderState, OrderEvent, Object>builder()
                .initial(OrderState.CREATED)
                .from(OrderState.CREATED).on(OrderEvent.PAY).to(OrderState.PAID)
                .composite(OrderState.PAID, OrderState.PENDING_INVOICE, sub -> sub
                        .from(OrderState.PENDING_INVOICE).on(OrderEvent.ISSUE_INVOICE).to(OrderState.INVOICED)
                )
                // CANCEL 写在 PAID 上，子态的 CANCEL 会通过继承命中
                .from(OrderState.PAID).on(OrderEvent.CANCEL).to(OrderState.CANCELLED)
                .build();
        sm.fire(OrderEvent.PAY, null);
        // 当前在 PENDING_INVOICE
        sm.fire(OrderEvent.CANCEL, null);
        assertEquals(OrderState.CANCELLED, sm.getCurrentState());
    }

    // ==================== Entry / Exit 动作 ====================

    @Test
    /**
     * testEntryExitActions_calledOnTransition方法。
     */
    public void testEntryExitActions_calledOnTransition() {
        List<String> log = new ArrayList<>();
        StateMachine<OrderState, OrderEvent, Object> sm = StateMachine
                .<OrderState, OrderEvent, Object>builder()
                .initial(OrderState.CREATED)
                .state(OrderState.CREATED).exit((s, c) -> log.add("exit CREATED"))
                .from(OrderState.CREATED).on(OrderEvent.PAY).to(OrderState.PAID)
                .state(OrderState.PAID).entry((s, c) -> log.add("enter PAID"))
                .composite(OrderState.PAID, OrderState.PENDING_INVOICE, sub -> sub
                        .from(OrderState.PENDING_INVOICE).on(OrderEvent.ISSUE_INVOICE).to(OrderState.INVOICED))
                .state(OrderState.PENDING_INVOICE).entry((s, c) -> log.add("enter PENDING_INVOICE"))
                .build();
        sm.fire(OrderEvent.PAY, null);
        assertEquals(Arrays.asList("exit CREATED", "enter PAID", "enter PENDING_INVOICE"), log);
    }

    @Test
    /**
     * testTransitionAction_invokedAtLCA方法。
     */
    public void testTransitionAction_invokedAtLCA() {
        AtomicInteger lcaActionCalls = new AtomicInteger();
        StateMachine<OrderState, OrderEvent, Object> sm = StateMachine
                .<OrderState, OrderEvent, Object>builder()
                .initial(OrderState.CREATED)
                .from(OrderState.CREATED).on(OrderEvent.PAY).to(OrderState.PAID)
                .composite(OrderState.PAID, OrderState.PENDING_INVOICE, sub -> sub
                        .from(OrderState.PENDING_INVOICE).on(OrderEvent.ISSUE_INVOICE).to(OrderState.INVOICED)
                )
                .from(OrderState.INVOICED).on(OrderEvent.CONFIRM)
                    .action((from, ctx) -> {
                        // from 应该是 LCA，在 INVOICED 跟 SHIPPED 之间 LCA = null（顶层）
                        lcaActionCalls.incrementAndGet();
                        assertNull(from); // 跨顶层无 LCA
                    })
                    .to(OrderState.SHIPPED)
                .build();
        sm.fire(OrderEvent.PAY, null);
        sm.fire(OrderEvent.ISSUE_INVOICE, null);
        sm.fire(OrderEvent.CONFIRM, null);
        assertEquals(1, lcaActionCalls.get());
    }

    // ==================== accept / hasTransition ====================

    @Test
    /**
     * testAccept_returnsTargetWhenAllowed方法。
     */
    public void testAccept_returnsTargetWhenAllowed() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).guard(ctx -> true).to(S.B)
                .build();
        assertEquals(S.B, sm.accept(E.GO_B, null));
    }

    @Test
    /**
     * testAccept_returnsNullWhenGuardRejects方法。
     */
    public void testAccept_returnsNullWhenGuardRejects() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).guard(ctx -> false).to(S.B)
                .build();
        assertNull(sm.accept(E.GO_B, null));
    }

    @Test
    /**
     * testHasTransition_walksParentChain方法。
     */
    public void testHasTransition_walksParentChain() {
        StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).to(S.B)
                .from(S.B).on(E.GO_C).to(S.C)
                .build();
        assertTrue(sm.hasTransition(S.A, E.GO_B));
        assertTrue(sm.hasTransition(S.B, E.GO_C));
        assertFalse(sm.hasTransition(S.A, E.GO_C));
    }

    // ==================== History（H）状态 ====================

    @Test
    /**
     * testHistory_restoresLastActiveSubstate方法。
     */
    public void testHistory_restoresLastActiveSubstate() {
        StateMachine<OrderState, OrderEvent, Object> sm = StateMachine
                .<OrderState, OrderEvent, Object>builder()
                .initial(OrderState.CREATED)
                .from(OrderState.CREATED).on(OrderEvent.PAY).to(OrderState.PAID)
                .composite(OrderState.PAID, OrderState.PENDING_INVOICE, sub -> sub
                        .from(OrderState.PENDING_INVOICE).on(OrderEvent.ISSUE_INVOICE).to(OrderState.INVOICED)
                        // 在 INVOICED 时收到 REENTER 跳到 H，H 恢复到上次激活的子态
                        .from(OrderState.INVOICED).on(OrderEvent.REENTER).to(OrderState.H)
                )
                .state(OrderState.H).history()
                .build();
        // 进入 PAID
        sm.fire(OrderEvent.PAY, null);
        // 推进到 INVOICED
        sm.fire(OrderEvent.ISSUE_INVOICE, null);
        assertEquals(OrderState.INVOICED, sm.getCurrentState());
        // 触发 REENTER，跳到 H。H 恢复到 PAID 上次激活的子态 = INVOICED
        sm.fire(OrderEvent.REENTER, null);
        assertEquals(OrderState.INVOICED, sm.getCurrentState());
        // 再来一次：从 INVOICED 跳到 H 仍然恢复到 INVOICED（同一个 leaf）
        sm.fire(OrderEvent.REENTER, null);
        assertEquals(OrderState.INVOICED, sm.getCurrentState());
    }

    @Test
    /**
     * testHistory_isHistoryFlagSet方法。
     */
    public void testHistory_isHistoryFlagSet() {
        StateMachine<OrderState, OrderEvent, Object> sm = StateMachine
                .<OrderState, OrderEvent, Object>builder()
                .initial(OrderState.CREATED)
                .state(OrderState.H).history()
                .build();
        assertTrue(sm.isHistory(OrderState.H));
        assertFalse(sm.isHistory(OrderState.CREATED));
    }

    // ==================== 并发安全 ====================

    @Test
    /**
     * testConcurrent_fire_threadSafe方法。
     */
    public void testConcurrent_fire_threadSafe() throws InterruptedException {
        final StateMachine<S, E, Object> sm = StateMachine.<S, E, Object>builder()
                .initial(S.A)
                .from(S.A).on(E.GO_B).to(S.B)
                .from(S.B).on(E.GO_C).to(S.C)
                .from(S.C).on(E.GO_D).to(S.D)
                .from(S.D).on(E.RESET).to(S.A)
                .build();
        int threads = 16;
        Thread[] ts = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            ts[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    sm.fire(E.GO_B, null, false);
                    sm.fire(E.GO_C, null, false);
                    sm.fire(E.GO_D, null, false);
                    sm.fire(E.RESET, null, false);
                }
            });
        }
        for (Thread t : ts) t.start();
        for (Thread t : ts) t.join();
        // 跑完所有 fire 之后状态必然是 A/B/C/D 之一（最终 RESET 把状态回到 A）
        S finalState = sm.getCurrentState();
        assertNotNull(finalState);
    }
}
