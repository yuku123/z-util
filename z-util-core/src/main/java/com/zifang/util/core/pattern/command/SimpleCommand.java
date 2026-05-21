package com.zifang.util.core.pattern.command;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 简单命令实现
 *
 * @param <C> 上下文类型
 * @author zifang
 */
public class SimpleCommand<C extends CommandContext> implements Command<C> {

    private final String name;
    private final String description;
    private final Consumer<C> action;
    private final Predicate<C> canExecute;
    private Consumer<C> undoAction;

    public SimpleCommand(String name, Consumer<C> action) {
        this(name, "", ctx -> true, action, null);
    }

    public SimpleCommand(String name, String description, Consumer<C> action) {
        this(name, description, ctx -> true, action, null);
    }

    public SimpleCommand(String name, Consumer<C> action, Consumer<C> undoAction) {
        this(name, "", ctx -> true, action, undoAction);
    }

    public SimpleCommand(String name, String description, Consumer<C> action, Consumer<C> undoAction) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.canExecute = ctx -> true;
        this.undoAction = undoAction;
    }

    public SimpleCommand(String name, String description, Predicate<C> canExecute, Consumer<C> action, Consumer<C> undoAction) {
        this.name = name;
        this.description = description;
        this.canExecute = canExecute;
        this.action = action;
        this.undoAction = undoAction;
    }

    @Override
    public void execute(C context) {
        if (!canExecute.test(context)) {
            return;
        }
        action.accept(context);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean supportsUndo() {
        return undoAction != null;
    }

    @Override
    public void undo(C context) {
        if (undoAction != null) {
            undoAction.accept(context);
        } else {
            throw new UnsupportedOperationException("Undo not configured");
        }
    }

    /**
     * 创建命令
     */
    public static <C extends CommandContext> SimpleCommand<C> of(String name, Consumer<C> action) {
        return new SimpleCommand<>(name, action);
    }

    /**
     * 创建带撤销的命令
     */
    public static <C extends CommandContext> SimpleCommand<C> of(String name, Consumer<C> action, Consumer<C> undoAction) {
        return new SimpleCommand<>(name, action, undoAction);
    }

    /**
     * 创建命名命令
     */
    public static <C extends CommandContext> SimpleCommand<C> named(String name, String description, Consumer<C> action) {
        return new SimpleCommand<>(name, description, action);
    }
}
