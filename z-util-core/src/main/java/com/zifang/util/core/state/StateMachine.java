package com.zifang.util.core.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * 业务级状态机（支持 statechart 层级）。
 * <p>
 * 相较于 flat 状态机，本实现提供以下能力，覆盖更复杂的业务流程：
 * <ul>
 *   <li><b>层级状态（composite）</b>：一个状态可以拥有子状态机，子状态机的初始子态作为父态激活后的实际当前态</li>
 *   <li><b>外部 / 本地转移</b>：转移的 from/to 可以是任一层级的状态，引擎会沿父链向上找匹配（继承），
 *       并通过 LCA（最近公共祖先）计算退出与进入路径</li>
 *   <li><b>Entry / Exit 动作</b>：状态本身可挂 entry/exit 动作，在状态进出时触发</li>
 *   <li><b>History 状态（H）</b>：声明为 history 的状态，会记住父 composite 上次激活的子态，
 *       下次进入时恢复到该子态</li>
 *   <li><b>动作在 LCA 上触发</b>：转移动作的 from 参数是 LCA，符合 UML statechart 语义</li>
 *   <li><b>守卫 / 监听器 / 失败快速开关</b>：与简单版一致</li>
 *   <li><b>线程安全</b>：{@link #fire} 加锁，多业务线程并发触发安全</li>
 * </ul>
 *
 * <h2>用法示例</h2>
 * <pre>{@code
 * enum OrderState {
 *     CREATED, PAID,                  // 顶层
 *     PENDING_INVOICE, INVOICED,     // PAID 的子态
 *     SHIPPED, DONE, CANCELLED        // 顶层
 * }
 * enum OrderEvent { PAY, ISSUE_INVOICE, CONFIRM, SHIP, COMPLETE, CANCEL }
 *
 * StateMachine<OrderState, OrderEvent, OrderCtx> sm = StateMachine
 *     .<OrderState, OrderEvent, OrderCtx>builder()
 *     .initial(OrderState.CREATED)
 *
 *     // 顶层转移
 *     .from(OrderState.CREATED).on(OrderEvent.PAY).to(OrderState.PAID)
 *     .from(OrderState.CREATED).on(OrderEvent.CANCEL).to(OrderState.CANCELLED)
 *
 *     // PAID 是个 composite：进入 PAID 会自动下钻到 PENDING_INVOICE
 *     .composite(OrderState.PAID, OrderState.PENDING_INVOICE, sub -> sub
 *         .from(OrderState.PENDING_INVOICE).on(OrderEvent.ISSUE_INVOICE)
 *             .action((from, ctx) -> ctx.invoiceIssued = true)
 *             .to(OrderState.INVOICED)
 *     )
 *
 *     // 外部转移：从 INVOICED（PAID 的子态）出去，到顶层 SHIPPED
 *     .from(OrderState.INVOICED).on(OrderEvent.CONFIRM).to(OrderState.SHIPPED)
 *
 *     // 外部转移：PAID 的任一子态都能被取消
 *     .from(OrderState.PENDING_INVOICE).on(OrderEvent.CANCEL).to(OrderState.CANCELLED)
 *     .from(OrderState.INVOICED).on(OrderEvent.CANCEL).to(OrderState.CANCELLED)
 *
 *     // 顶层转移
 *     .from(OrderState.SHIPPED).on(OrderEvent.COMPLETE).to(OrderState.DONE)
 *
 *     .build();
 *
 * sm.fire(OrderEvent.PAY, ctx);    // CREATED -> (exit CREATED) -> PAID -> (enter PAID, descend) -> PENDING_INVOICE
 * sm.getCurrentState();             // PENDING_INVOICE
 * }</pre>
 *
 * @param <S> 状态类型（推荐 enum）
 * @param <E> 事件类型
 * @param <C> 业务上下文类型
 */
/**
 * StateMachine类。
 */
public class StateMachine<S, E, C> {

    private final S initialState;
    private final Map<S, Map<E, Transition<S, E, C>>> transitionTable;
    private final Map<S, S> parentMap = new HashMap<>();            // 子态 -> 父态（顶层为 null）
    private final Map<S, S> compositeInitialMap = new HashMap<>();  // composite -> 初始子态
    private final Map<S, BiConsumer<S, C>> entryActions = new HashMap<>();
    private final Map<S, BiConsumer<S, C>> exitActions = new HashMap<>();
    private final Set<S> historyStates = new HashSet<>();
    private final Map<S, S> lastActiveSubstate = new HashMap<>();   // composite -> 上次激活的子态（H 用）
    private final Map<S, S> historyParentMap = new HashMap<>();     // H 状态 -> 其父 composite

    private final CopyOnWriteArrayList<StateListener<S, E, C>> listeners = new CopyOnWriteArrayList<>();

    private volatile S currentState;

    private StateMachine(S initialState,
                         Map<S, Map<E, Transition<S, E, C>>> transitionTable) {
        this.initialState = Objects.requireNonNull(initialState, "initialState");
        this.currentState = initialState;
        this.transitionTable = transitionTable;
    }

    /**
     * builder方法。
     * @return static <S, E, C> StateMachineBuilder<S, E, C>类型返回值
     */
    public static <S, E, C> StateMachineBuilder<S, E, C> builder() {
        return new StateMachineBuilder<>();
    }

    // ==================== 公共 API ====================

    /**
     * getCurrentState方法。
     * @return S类型返回值
     */
    public S getCurrentState() {
        return currentState;
    }

    /**
     * getInitialState方法。
     * @return S类型返回值
     */
    public S getInitialState() {
        return initialState;
    }

    /**
     * 重置回初始状态（不触发 entry/exit）。
     */
    /**
     * reset方法。
     * @return synchronized void类型返回值
     */
    public synchronized void reset() {
        this.currentState = initialState;
    }

    /**
     * 强制设到目标状态（跳过守卫/动作/监听器）。用于持久化恢复。
     */
    /**
     * forceState方法。
     *      * @param state S类型参数
     * @return synchronized void类型返回值
     */
    public synchronized void forceState(S state) {
        Objects.requireNonNull(state, "state");
        this.currentState = state;
    }

    /**
     * addListener方法。
     *      * @param listener StateListenerS,类型参数
     */
    public void addListener(StateListener<S, E, C> listener) {
        if (listener != null) listeners.add(listener);
    }

    /**
     * removeListener方法。
     *      * @param listener StateListenerS,类型参数
     */
    public void removeListener(StateListener<S, E, C> listener) {
        listeners.remove(listener);
    }

    /**
     * 触发一个事件，找不到匹配或守卫拒绝时抛 {@link StateMachineException}。
     */
    /**
     * fire方法。
     *      * @param event E类型参数
     * @param context C类型参数
     * @return synchronized S类型返回值
     */
    public synchronized S fire(E event, C context) {
        return fire(event, context, true);
    }

    /**
     * 触发一个事件。
     *
     * @param failFast true 表示无匹配转移或守卫失败抛异常；false 表示静默忽略并保留当前态
     */
    /**
     * fire方法。
     *      * @param event E类型参数
     * @param context C类型参数
     * @param failFast boolean类型参数
     * @return synchronized S类型返回值
     */
    public synchronized S fire(E event, C context, boolean failFast) {
        S from = currentState;
        Transition<S, E, C> t = resolveTransition(from, event);
        if (t == null) {
            String reason = "No transition from state '" + from + "' on event '" + event + "'";
            notifyRefused(from, event, context, reason);
            if (failFast) {
                throw new StateMachineException(reason);
            }
            return from;
        }
        if (!t.isAllowed(context)) {
            String reason = "Guard rejected transition from '" + from + "' on event '" + event + "'";
            notifyRefused(from, event, context, reason);
            if (failFast) {
                throw new StateMachineException(reason);
            }
            return from;
        }

        S transitionFrom = t.getFrom();
        S to = t.getTo();
        S lca = findLCA(from, to);

        // 进入转移的"begin"通知（在 exit 之前，转移尚未发生）
        notifyBegin(transitionFrom, to, event, context);

        // 1. 退出：从 currentState 向上退出，直到 (但不包括) lca
        //    如果 lca 为 null（顶层状态间转换），exit 链就是 from 自身
        List<S> exitChain = lca != null
                ? collectExitChain(from, lca)
                : Collections.singletonList(from);
        for (S s : exitChain) {
            if (compositeInitialMap.containsKey(s)) {
                lastActiveSubstate.put(s, currentState);
            }
            BiConsumer<S, C> exit = exitActions.get(s);
            if (exit != null) {
                exit.accept(s, context);
            }
        }

        // 2. 转移动作：传入 LCA；跨顶层转移时 LCA 为 null（UML 语义）
        try {
            t.executeAction(lca, context);
        } catch (RuntimeException ex) {
            notifyError(from, event, context, ex);
            throw new StateMachineException("Action failed during transition from '" + from
                    + "' on event '" + event + "'", ex);
        }

        // 3. 切换 currentState 到 target
        currentState = to;

        // 4. 进入：从 lca 向下到 target。lca 为 null 时直接 enter target
        if (lca != null) {
            List<S> enterChain = collectEnterChain(lca, to);
            for (S s : enterChain) {
                BiConsumer<S, C> entry = entryActions.get(s);
                if (entry != null) {
                    entry.accept(s, context);
                }
            }
        }
        BiConsumer<S, C> targetEntry = entryActions.get(to);
        if (targetEntry != null) {
            targetEntry.accept(to, context);
        }

        // 5. 如果 target 是 composite，下钻到其 initial 子态
        S resolved = descendIntoInitial(to, context);

        currentState = resolved;
        // 6. 记录每个祖先 composite 的 last active substate（H 恢复用）
        S ancestor = resolved;
        while (true) {
            S parent = parentMap.get(ancestor);
            if (parent == null) break;
            if (compositeInitialMap.containsKey(parent)) {
                lastActiveSubstate.put(parent, ancestor);
            }
            ancestor = parent;
        }
        notifyEnd(from, resolved, event, context);
        return resolved;
    }

    /**
     * 在不修改状态的前提下试探事件是否允许触发。
     */
    /**
     * accept方法。
     *      * @param event E类型参数
     * @param context C类型参数
     * @return S类型返回值
     */
    public S accept(E event, C context) {
        Transition<S, E, C> t = resolveTransition(currentState, event);
        if (t != null && t.isAllowed(context)) {
            return resolveTargetAfterEntry(t.getTo());
        }
        return null;
    }

    /**
     * 查询在指定状态、给定事件，是否存在注册转移（不评估守卫）。
     */
    /**
     * hasTransition方法。
     *      * @param state S类型参数
     * @param event E类型参数
     * @return boolean类型返回值
     */
    public boolean hasTransition(S state, E event) {
        return resolveTransition(state, event) != null;
    }

    /**
     * 判断某状态是否为 composite（有子状态）。
     */
    /**
     * isComposite方法。
     *      * @param state S类型参数
     * @return boolean类型返回值
     */
    public boolean isComposite(S state) {
        return compositeInitialMap.containsKey(state);
    }

    /**
     * 判断某状态是否为 history（H）状态。
     */
    /**
     * isHistory方法。
     *      * @param state S类型参数
     * @return boolean类型返回值
     */
    public boolean isHistory(S state) {
        return historyStates.contains(state);
    }

    /**
     * 取得指定状态的所有祖先（从自身到根）。
     */
    /**
     * ancestorsOf方法。
     *      * @param state S类型参数
     * @return List<S>类型返回值
     */
    public List<S> ancestorsOf(S state) {
        List<S> ancestors = new ArrayList<>();
        S s = state;
        while (s != null) {
            ancestors.add(s);
            s = parentMap.get(s);
        }
        return ancestors;
    }

    // ==================== 内部 ====================

    /**
     * 沿父链向上查找第一个匹配 (state, event) 的转移。
     */
    private Transition<S, E, C> resolveTransition(S from, E event) {
        S s = from;
        while (s != null) {
            Map<E, Transition<S, E, C>> row = transitionTable.get(s);
            if (row != null) {
                Transition<S, E, C> t = row.get(event);
                if (t != null) return t;
            }
            s = parentMap.get(s);
        }
        return null;
    }

    /**
     * 找两个状态在 parentMap 中的最近公共祖先。
     */
    private S findLCA(S a, S b) {
        Set<S> ancestorsA = new HashSet<>();
        S s = a;
        while (s != null) {
            ancestorsA.add(s);
            s = parentMap.get(s);
        }
        s = b;
        while (s != null) {
            if (ancestorsA.contains(s)) return s;
            s = parentMap.get(s);
        }
        return null;
    }

    /**
     * 从 currentState 向上到（不包括）lca，组成退出链。返回 [currentState, ..., lca.child]
     */
    private List<S> collectExitChain(S from, S lca) {
        List<S> chain = new ArrayList<>();
        S s = from;
        while (s != null && !s.equals(lca)) {
            chain.add(s);
            s = parentMap.get(s);
        }
        return chain;
    }

    /**
     * 从 lca 向下到 to，组成进入链（包含 lca 的下一层和 to 自身）。
     * 注意 lca 自身不在此链中（lca 已经在场，无需重新进入）。
     */
    private List<S> collectEnterChain(S lca, S to) {
        List<S> toAncestors = ancestorsOf(to);
        int lcaIdx = toAncestors.indexOf(lca);
        if (lcaIdx < 0) {
            return Collections.emptyList();
        }
        // 取 toAncestors[0..lcaIdx)（不包含 lca）
        List<S> chain = new ArrayList<>(toAncestors.subList(0, lcaIdx));
        Collections.reverse(chain);
        return chain;
    }

    /**
     * 如果 state 是 composite，递归下钻到其 initial 子态（也包括 history 状态）。
     */
    private S descendIntoInitial(S state, C context) {
        S s = state;
        // 防死循环：visited 集合
        java.util.Set<S> visited = new java.util.HashSet<>();
        while (visited.add(s)) {
            if (historyStates.contains(s)) {
                // History 状态：恢复到其父 composite 上次激活的子态
                S parent = historyParentMap.get(s);
                S last = parent != null ? lastActiveSubstate.get(parent) : null;
                if (last != null) {
                    s = last;
                } else if (parent != null) {
                    // 没有上次记录，下钻到 parent 的 initial
                    s = parent;
                } else {
                    // 没有父（孤立 H），保持 H
                    break;
                }
                continue;
            }
            S init = compositeInitialMap.get(s);
            if (init == null) break;
            BiConsumer<S, C> entry = entryActions.get(init);
            if (entry != null) entry.accept(init, context);
            s = init;
        }
        return s;
    }

    /**
     * 用于 accept()：解析一个 transition 的最终目标态（处理 composite 下钻，不触发 entry）。
     */
    private S resolveTargetAfterEntry(S target) {
        S s = target;
        while (compositeInitialMap.containsKey(s) && !historyStates.contains(s)) {
            s = compositeInitialMap.get(s);
        }
        return s;
    }

    private void notifyBegin(S from, S to, E event, C context) {
        for (StateListener<S, E, C> l : listeners) {
            try { l.onTransitionBegin(from, to, event, context); } catch (RuntimeException ignored) {}
        }
    }

    private void notifyEnd(S from, S to, E event, C context) {
        for (StateListener<S, E, C> l : listeners) {
            try {
                l.onAction(from, to, event, context);
                l.onTransitionEnd(from, to, event, context);
            } catch (RuntimeException ignored) {}
        }
    }

    private void notifyRefused(S state, E event, C context, String reason) {
        for (StateListener<S, E, C> l : listeners) {
            try { l.onTransitionRefused(state, event, context, reason); } catch (RuntimeException ignored) {}
        }
    }

    private void notifyError(S state, E event, C context, Throwable error) {
        for (StateListener<S, E, C> l : listeners) {
            try { l.onError(state, event, context, error); } catch (RuntimeException ignored) {}
        }
    }

    // ==================== Builder ====================

    public static class StateMachineBuilder<S, E, C> {

        private S initialState;
        private final Map<S, Map<E, Transition<S, E, C>>> table = new HashMap<>();
        private final java.util.List<StateListener<S, E, C>> listeners = new java.util.ArrayList<>();
        // 层级结构相关字段（与 StateMachine 同步，在 build() 时拷贝过去）
        private final Map<S, S> parentMap = new HashMap<>();
        private final Map<S, S> compositeInitialMap = new HashMap<>();
        private final Map<S, BiConsumer<S, C>> entryActions = new HashMap<>();
        private final Map<S, BiConsumer<S, C>> exitActions = new HashMap<>();
        private final Set<S> historyStates = new HashSet<>();
        // H 状态 -> 其父 composite 映射
        private final Map<S, S> historyParentMap = new HashMap<>();
        // 最近一次 composite 声明的 state（用于把后续 state(s).history() 关联到这个 composite）
        private S lastComposite = null;

        private StateMachineBuilder() {}

    /**
     * initial方法。
     *      * @param state S类型参数
     * @return StateMachineBuilder<S, E, C>类型返回值
     */
        public StateMachineBuilder<S, E, C> initial(S state) {
            this.initialState = state;
            return this;
        }

        /**
         * 启动 from 配置，链上下一步是 {@link #on(Object)}。
         */
    /**
     * from方法。
     *      * @param state S类型参数
     * @return FromStateStep<S, E, C>类型返回值
     */
        public FromStateStep<S, E, C> from(S state) {
            return new FromStateStep<>(this, state);
        }

    /**
     * listener方法。
     *      * @param listener StateListenerS,类型参数
     * @return StateMachineBuilder<S, E, C>类型返回值
     */
        public StateMachineBuilder<S, E, C> listener(StateListener<S, E, C> listener) {
            if (listener != null) listeners.add(listener);
            return this;
        }

        /**
         * 声明一个状态（可用于挂 entry/exit 动作或标记 composite）。
         * 用法：
         * <pre>{@code
         * .state(OrderState.PAID).composite(OrderState.PENDING_INVOICE, sub -> ...)
         * .state(OrderState.SHIPPED).entry(ctx -> log.info("shipped")).exit(ctx -> log.info("leaving shipped"))
         * }</pre>
         */
    /**
     * state方法。
     *      * @param state S类型参数
     * @return StateConfigStep<S, E, C>类型返回值
     */
        public StateConfigStep<S, E, C> state(S state) {
            return new StateConfigStep<>(this, state);
        }

        /**
         * 便捷方法：声明 state 为 composite，并指定初始子态与子状态机配置。
         */
    /**
     * composite方法。
     *      * @param state S类型参数
     * @param initialChild S类型参数
     * @param subConfig java.util.function.ConsumerSubMachineBuilderS,类型参数
     * @return StateMachineBuilder<S, E, C>类型返回值
     */
        public StateMachineBuilder<S, E, C> composite(S state, S initialChild,
                                                       java.util.function.Consumer<SubMachineBuilder<S, E, C>> subConfig) {
            SubMachineBuilder<S, E, C> sub = new SubMachineBuilder<>(this, state, initialChild);
            this.lastComposite = state;
            subConfig.accept(sub);
            // 消费完子机 lambda 后，自动把 composite 关系注册到 root
            sub.end();
            return this;
        }

        /**
         * 声明一个 history（H）状态 — 属于其父 composite，激活时恢复到父 composite 上次激活的子态。
         */
    /**
     * history方法。
     *      * @param state S类型参数
     * @return StateMachineBuilder<S, E, C>类型返回值
     */
        public StateMachineBuilder<S, E, C> history(S state) {
            this.historyStates.add(state);
            // H 状态视为 composite（拥有 default 子态），以便下钻
            // 实际恢复逻辑在 descendIntoInitial 中处理
            return this;
        }

        StateMachineBuilder<S, E, C> register(S from, E event, S to,
                                             Predicate<C> guard,
                                             BiConsumer<S, C> action) {
            table.computeIfAbsent(from, k -> new HashMap<>())
                 .put(event, new Transition<>(from, event, to, guard, action));
            return this;
        }

        void registerParent(S child, S parent) {
            this.parentMap.put(child, parent);
        }

        void registerComposite(S composite, S initialChild) {
            this.compositeInitialMap.put(composite, initialChild);
        }

        void registerEntry(S state, BiConsumer<S, C> action) {
            if (action != null) this.entryActions.put(state, action);
        }

        void registerExit(S state, BiConsumer<S, C> action) {
            if (action != null) this.exitActions.put(state, action);
        }

        void registerHistory(S state) {
            this.historyStates.add(state);
            if (lastComposite != null) {
                this.historyParentMap.put(state, lastComposite);
                this.parentMap.put(state, lastComposite);
            }
            // 消费完 lastComposite 后清掉，避免污染后续 state 声明
            this.lastComposite = null;
        }

    /**
     * build方法。
     * @return StateMachine<S, E, C>类型返回值
     */
        public StateMachine<S, E, C> build() {
            if (initialState == null) {
                throw new StateMachineException("Initial state is required");
            }
            // 复制为不可变视图
            Map<S, Map<E, Transition<S, E, C>>> copy = new HashMap<>();
            for (Map.Entry<S, Map<E, Transition<S, E, C>>> e : table.entrySet()) {
                copy.put(e.getKey(), new HashMap<>(e.getValue()));
            }
            StateMachine<S, E, C> sm = new StateMachine<>(initialState, copy);
            sm.parentMap.putAll(this.parentMap);
            sm.compositeInitialMap.putAll(this.compositeInitialMap);
            sm.entryActions.putAll(this.entryActions);
            sm.exitActions.putAll(this.exitActions);
            sm.historyStates.addAll(this.historyStates);
            sm.historyParentMap.putAll(this.historyParentMap);
            for (StateListener<S, E, C> l : listeners) {
                if (l != null) sm.listeners.add(l);
            }
            return sm;
        }
    }

    /**
     * from 配置入口。必须链 {@link OnEventStep#on(Object)}。
     */
    public static class FromStateStep<S, E, C> {

        private final StateMachineBuilder<S, E, C> parent;
        private final S from;
        private final S inComposite; // 非 null 表示这条 from 在 sub 上下文内（应把 from 注册为该 composite 的子态）

        FromStateStep(StateMachineBuilder<S, E, C> parent, S from) {
            this(parent, from, null);
        }

        FromStateStep(StateMachineBuilder<S, E, C> parent, S from, S inComposite) {
            this.parent = parent;
            this.from = from;
            this.inComposite = inComposite;
            if (inComposite != null) {
                parent.registerParent(from, inComposite);
            }
        }

    /**
     * on方法。
     *      * @param event E类型参数
     * @return OnEventStep<S, E, C>类型返回值
     */
        public OnEventStep<S, E, C> on(E event) {
            return new OnEventStep<>(parent, from, event, inComposite);
        }
    }

    public static class OnEventStep<S, E, C> {

        private final StateMachineBuilder<S, E, C> parent;
        private final S from;
        private final E event;
        private final S inComposite;
        private Predicate<C> guard;
        private BiConsumer<S, C> action;

        OnEventStep(StateMachineBuilder<S, E, C> parent, S from, E event) {
            this(parent, from, event, null);
        }

        OnEventStep(StateMachineBuilder<S, E, C> parent, S from, E event, S inComposite) {
            this.parent = parent;
            this.from = from;
            this.event = event;
            this.inComposite = inComposite;
        }

    /**
     * guard方法。
     *      * @param guard PredicateC类型参数
     * @return OnEventStep<S, E, C>类型返回值
     */
        public OnEventStep<S, E, C> guard(Predicate<C> guard) {
            Predicate<C> existing = this.guard;
            this.guard = existing == null ? guard : existing.and(guard);
            return this;
        }

    /**
     * action方法。
     *      * @param action BiConsumerS,类型参数
     * @return OnEventStep<S, E, C>类型返回值
     */
        public OnEventStep<S, E, C> action(BiConsumer<S, C> action) {
            this.action = action;
            return this;
        }

        /**
         * 结束 on().to() 链。
         * <p>
         * 如果本步在 composite 子机构建上下文内（{@code composite(...)} 的 lambda 内），
         * 则把 to 自动注册为该 composite 的子态。
         */
    /**
     * to方法。
     *      * @param to S类型参数
     * @return StateMachineBuilder<S, E, C>类型返回值
     */
        public StateMachineBuilder<S, E, C> to(S to) {
            parent.register(from, event, to, guard, action);
            if (inComposite != null) {
                parent.registerParent(to, inComposite);
            }
            return parent;
        }
    }

    /**
     * state 配置入口。提供 entry/exit 动作与 composite 声明。
     */
    public static class StateConfigStep<S, E, C> {

        private final StateMachineBuilder<S, E, C> parent;
        private final S state;

        StateConfigStep(StateMachineBuilder<S, E, C> parent, S state) {
            this.parent = parent;
            this.state = state;
        }

    /**
     * entry方法。
     *      * @param action BiConsumerS,类型参数
     * @return StateMachineBuilder<S, E, C>类型返回值
     */
        public StateMachineBuilder<S, E, C> entry(BiConsumer<S, C> action) {
            parent.registerEntry(state, action);
            return parent;
        }

    /**
     * exit方法。
     *      * @param action BiConsumerS,类型参数
     * @return StateMachineBuilder<S, E, C>类型返回值
     */
        public StateMachineBuilder<S, E, C> exit(BiConsumer<S, C> action) {
            parent.registerExit(state, action);
            return parent;
        }

    /**
     * history方法。
     * @return StateMachineBuilder<S, E, C>类型返回值
     */
        public StateMachineBuilder<S, E, C> history() {
            parent.registerHistory(state);
            return parent;
        }

        /**
         * 链式回到 transfer 配置入口（{@link #on(Object)} 之后）。
         */
    /**
     * from方法。
     *      * @param from S类型参数
     * @return FromStateStep<S, E, C>类型返回值
     */
        public FromStateStep<S, E, C> from(S from) {
            return new FromStateStep<>(parent, from);
        }

        /**
         * 把 state 声明为 composite，指定初始子态并通过 sub 配置子状态机。
         */
    /**
     * composite方法。
     *      * @param initialChild S类型参数
     * @param subConfig java.util.function.ConsumerSubMachineBuilderS,类型参数
     * @return StateMachineBuilder<S, E, C>类型返回值
     */
        public StateMachineBuilder<S, E, C> composite(S initialChild,
                                                       java.util.function.Consumer<SubMachineBuilder<S, E, C>> subConfig) {
            SubMachineBuilder<S, E, C> sub = new SubMachineBuilder<>(parent, state, initialChild);
            subConfig.accept(sub);
            return parent;
        }
    }

    /**
     * 子状态机构建器：在 composite 配置中，用于声明哪些 state 是当前 composite 的子态、配置子态内部转移。
     * <p>
     * 调用 {@link #child(S)} 显式声明哪些 state 属于本 composite（可选 — 引擎也能从 transfer 推断），
     * 然后通过 {@link #from(S)} 像顶层一样声明子态间的转移。
     */
    public static class SubMachineBuilder<S, E, C> {

        private final StateMachineBuilder<S, E, C> root;
        private final S composite;
        private final S initialChild;

        SubMachineBuilder(StateMachineBuilder<S, E, C> root, S composite, S initialChild) {
            this.root = root;
            this.composite = composite;
            this.initialChild = initialChild;
        }

    /**
     * child方法。
     *      * @param state S类型参数
     * @return SubMachineBuilder<S, E, C>类型返回值
     */
        public SubMachineBuilder<S, E, C> child(S state) {
            root.registerParent(state, composite);
            return this;
        }

    /**
     * from方法。
     *      * @param state S类型参数
     * @return FromStateStep<S, E, C>类型返回值
     */
        public FromStateStep<S, E, C> from(S state) {
            return new FromStateStep<>(root, state, composite);
        }

        /**
         * 显式结束子机构建（通常不必调用，链式 API 会自动回到 root）。
         */
    /**
     * end方法。
     * @return StateMachineBuilder<S, E, C>类型返回值
     */
        public StateMachineBuilder<S, E, C> end() {
            // 标记 composite 本身 — 在 end 时一次性注册 composite 关系
            root.registerComposite(composite, initialChild);
            return root;
        }
    }
}
