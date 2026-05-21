package com.zifang.util.core.pattern.command;

import java.util.*;
import java.util.function.Predicate;

/**
 * 命令宏
 * <p>
 * 将多个命令组合成一个可重用的宏命令
 *
 * @param <C> 上下文类型
 * @author zifang
 */
public class CommandMacro<C extends CommandContext> implements Command<C> {

    private final String name;
    private final List<Command<C>> commands;
    private final Predicate<C> canExecute;

    public CommandMacro(String name) {
        this(name, new ArrayList<>());
    }

    public CommandMacro(String name, List<Command<C>> commands) {
        this.name = name;
        this.commands = new ArrayList<>(commands);
        this.canExecute = null;
    }

    public CommandMacro(String name, Predicate<C> canExecute) {
        this.name = name;
        this.commands = new ArrayList<>();
        this.canExecute = canExecute;
    }

    public CommandMacro(String name, Predicate<C> canExecute, List<Command<C>> commands) {
        this.name = name;
        this.commands = new ArrayList<>(commands);
        this.canExecute = canExecute;
    }

    /**
     * 添加命令
     */
    public CommandMacro<C> add(Command<C> command) {
        if (command != null) {
            commands.add(command);
        }
        return this;
    }

    /**
     * 添加多个命令
     */
    @SafeVarargs
    public final CommandMacro<C> addAll(Command<C>... commands) {
        for (Command<C> cmd : commands) {
            add(cmd);
        }
        return this;
    }

    /**
     * 在开头添加命令
     */
    public CommandMacro<C> addFirst(Command<C> command) {
        if (command != null) {
            commands.add(0, command);
        }
        return this;
    }

    /**
     * 移除命令
     */
    public CommandMacro<C> remove(Command<C> command) {
        commands.remove(command);
        return this;
    }

    /**
     * 清空命令
     */
    public CommandMacro<C> clear() {
        commands.clear();
        return this;
    }

    /**
     * 获取命令数量
     */
    public int size() {
        return commands.size();
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return commands.isEmpty();
    }

    /**
     * 获取所有命令
     */
    public List<Command<C>> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    @Override
    public void execute(C context) {
        if (canExecute != null && !canExecute.test(context)) {
            return;
        }

        for (Command<C> command : commands) {
            if (context.isInterrupted()) {
                break;
            }
            command.execute(context);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean supportsUndo() {
        // 支持撤销当所有子命令都支持
        for (Command<C> cmd : commands) {
            if (!cmd.supportsUndo()) {
                return false;
            }
        }
        return !commands.isEmpty();
    }

    @Override
    public void undo(C context) {
        // 逆序撤销所有命令
        List<Command<C>> reversed = new ArrayList<>(commands);
        Collections.reverse(reversed);

        for (Command<C> command : reversed) {
            if (command.supportsUndo()) {
                command.undo(context);
            }
        }
    }

    /**
     * 创建宏命令
     */
    public static <C extends CommandContext> CommandMacro<C> of(String name) {
        return new CommandMacro<>(name);
    }

    /**
     * 创建宏命令并添加初始命令
     */
    @SafeVarargs
    public static <C extends CommandContext> CommandMacro<C> of(String name, Command<C>... commands) {
        CommandMacro<C> macro = new CommandMacro<>(name);
        for (Command<C> cmd : commands) {
            macro.add(cmd);
        }
        return macro;
    }
}
