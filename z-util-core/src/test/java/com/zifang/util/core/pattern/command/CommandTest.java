package com.zifang.util.core.pattern.command;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * 命令模式测试
 */
public class CommandTest {

    @Test
    public void testCommandContext() {
        CommandContext context = new CommandContext();
        context.put("name", "test");
        context.put("value", 123);

        assertEquals("test", context.get("name"));
        assertEquals(123, context.get("value"));
        assertFalse(context.isInterrupted());
        assertTrue(context.getExecutedCommands().isEmpty());
    }

    @Test
    public void testOperandStack() {
        OperandStack stack = new OperandStack();

        stack.push(1);
        stack.push("hello");
        stack.push(true);

        assertEquals(3, stack.size());
        assertEquals(true, stack.peek());

        // dup
        stack.dup();
        assertEquals(4, stack.size());

        // pop and check
        Object popped = stack.pop();
        assertEquals(true, popped);

        // clear
        stack.clear();
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testOperandStackSwap() {
        OperandStack stack = new OperandStack();
        stack.push(1);
        stack.push(2);

        stack.swap();
        assertEquals(Integer.valueOf(1), stack.pop());
        assertEquals(Integer.valueOf(2), stack.pop());
    }

    @Test
    public void testOperandStackContext() {
        OperandStackContext context = new OperandStackContext();

        context.put("name", "test");
        context.push(100);
        context.push("hello");

        assertEquals("test", context.get("name"));
        assertEquals("hello", context.pop());
        assertEquals(Integer.valueOf(100), context.pop());
    }

    @Test
    public void testSimpleCommand() {
        AtomicBoolean executed = new AtomicBoolean(false);

        SimpleCommand<CommandContext> cmd = SimpleCommand.of("test", ctx -> {
            executed.set(true);
            ctx.put("done", true);
        });

        CommandContext context = new CommandContext();
        cmd.execute(context);

        assertTrue(executed.get());
        assertTrue((Boolean) context.get("done"));
        assertEquals("test", cmd.getName());
    }

    @Test
    public void testSimpleCommandWithUndo() {
        AtomicInteger value = new AtomicInteger(0);

        SimpleCommand<CommandContext> cmd = SimpleCommand.of(
                "increment",
                ctx -> value.incrementAndGet(),
                ctx -> value.decrementAndGet()
        );

        CommandContext context = new CommandContext();
        cmd.execute(context);
        assertEquals(1, value.get());

        cmd.undo(context);
        assertEquals(0, value.get());
        assertTrue(cmd.supportsUndo());
    }

    @Test
    public void testCommandRegistry() {
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();

        Command<CommandContext> cmd1 = SimpleCommand.of("cmd1", ctx -> {});
        Command<CommandContext> cmd2 = SimpleCommand.of("cmd2", ctx -> {});

        registry.register("command1", cmd1);
        registry.register("command2", "categoryA", cmd2);

        assertTrue(registry.contains("command1"));
        assertTrue(registry.contains("command2"));
        assertEquals(2, registry.size());

        assertEquals("categoryA", registry.getCategory("command2"));
        assertSame(cmd1, registry.get("command1"));
    }

    @Test
    public void testCommandRegistryByCategory() {
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();

        registry.register("cmd1", "groupA", SimpleCommand.of("cmd1", ctx -> {}));
        registry.register("cmd2", "groupA", SimpleCommand.of("cmd2", ctx -> {}));
        registry.register("cmd3", "groupB", SimpleCommand.of("cmd3", ctx -> {}));

        Map<String, Command<CommandContext>> groupA = registry.getCommandsByCategory("groupA");
        assertEquals(2, groupA.size());
        assertTrue(groupA.containsKey("cmd1"));
        assertTrue(groupA.containsKey("cmd2"));

        assertEquals(2, registry.getCategories().size());
    }

    @Test
    public void testCommandExecutor() {
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();
        registry.register("step1", SimpleCommand.of("step1", ctx -> ctx.put("step1", 1)));
        registry.register("step2", SimpleCommand.of("step2", ctx -> ctx.put("step2", 2)));

        CommandExecutor<CommandContext> executor = registry.executor();

        CommandContext context = new CommandContext();
        executor.executeChain(context, "step1", "step2");

        assertEquals(1, context.get("step1"));
        assertEquals(2, context.get("step2"));
        assertEquals(2, context.getExecutedCommands().size());
    }

    @Test
    public void testCommandExecutorWithListener() {
        AtomicBoolean beforeCalled = new AtomicBoolean(false);
        AtomicBoolean afterCalled = new AtomicBoolean(false);

        CommandRegistry<CommandContext> registry = new CommandRegistry<>();
        registry.register("test", SimpleCommand.of("test", ctx -> {}));

        CommandExecutor<CommandContext> executor = registry.executor();
        executor.addListener(new CommandExecutor.CommandListener<CommandContext>() {
            @Override
            public void onBeforeExecute(Command<CommandContext> command, CommandContext context) {
                beforeCalled.set(true);
            }

            @Override
            public void onAfterExecute(Command<CommandContext> command, CommandContext context, long duration, Exception error) {
                afterCalled.set(true);
            }
        });

        CommandContext context = new CommandContext();
        executor.execute(context, "test");

        assertTrue(beforeCalled.get());
        assertTrue(afterCalled.get());
    }

    @Test
    public void testCommandMacro() {
        AtomicInteger counter = new AtomicInteger(0);

        CommandMacro<CommandContext> macro = CommandMacro.of("batch");
        macro.add(ctx -> counter.incrementAndGet());
        macro.add(ctx -> counter.incrementAndGet());
        macro.add(ctx -> counter.incrementAndGet());

        CommandContext context = new CommandContext();
        macro.execute(context);

        assertEquals(3, counter.get());
        assertEquals("batch", macro.getName());
        assertEquals(3, macro.size());
    }

    @Test
    public void testCommandMacroWithUndo() {
        AtomicInteger value = new AtomicInteger(0);

        // Commands that properly support undo
        Command<CommandContext> cmd1 = new SimpleCommand<>("set10",
            ctx -> value.set(10), ctx -> value.set(0));
        Command<CommandContext> cmd2 = new SimpleCommand<>("set20",
            ctx -> value.set(20), ctx -> value.set(0));

        CommandMacro<CommandContext> macro = CommandMacro.of("batch");
        macro.add(cmd1);
        macro.add(cmd2);

        CommandContext context = new CommandContext();
        macro.execute(context);
        assertEquals(20, value.get());

        macro.undo(context);
        assertEquals(0, value.get()); // 逆序撤销，set20先撤销设为0
    }

    @Test
    public void testCommandParserBasic() {
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();

        registry.register("init", SimpleCommand.of("init", ctx -> ctx.put("initialized", true)));
        registry.register("process", SimpleCommand.of("process", ctx -> ctx.put("processed", true)));

        CommandParser<CommandContext> parser = new CommandParser<>(registry);

        CommandContext context = new CommandContext();
        parser.parseAndExecute("init; process", context);

        assertEquals(true, context.get("initialized"));
        assertEquals(true, context.get("processed"));
    }

    @Test
    public void testCommandParserWithParameters() {
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();

        registry.register("set", SimpleCommand.of("set", ctx -> {
            ctx.put("value", ctx.get("val"));
        }));

        CommandParser<CommandContext> parser = new CommandParser<>(registry);

        CommandContext context = new CommandContext();
        parser.parseAndExecute("set(val=hello)", context);

        assertEquals("hello", context.get("value"));
    }

    @Test
    public void testCommandParserWithList() {
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();

        AtomicInteger counter = new AtomicInteger(0);
        registry.register("inc", SimpleCommand.of("inc", ctx -> counter.incrementAndGet()));

        CommandParser<CommandContext> parser = new CommandParser<>(registry);

        List<String> commands = Arrays.asList(
                "# 这是注释",
                "",
                "inc",
                "inc"
        );

        CommandContext context = new CommandContext();
        parser.parseAndExecute(commands, context);

        assertEquals(2, counter.get());
    }

    @Test
    public void testInterrupt() {
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();

        registry.register("task1", SimpleCommand.of("task1", ctx -> ctx.put("task", 1)));
        registry.register("interrupt", SimpleCommand.of("interrupt", ctx -> ctx.interrupt()));
        registry.register("task2", SimpleCommand.of("task2", ctx -> ctx.put("task", 2)));

        CommandExecutor<CommandContext> executor = registry.executor();

        CommandContext context = new CommandContext();
        executor.executeChain(context, "task1", "interrupt", "task2");

        assertEquals(1, context.get("task"));
        assertTrue(context.isInterrupted());
    }

    @Test
    public void testCommandRegistryRemove() {
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();

        registry.register("cmd1", SimpleCommand.of("cmd1", ctx -> {}));
        registry.register("cmd2", SimpleCommand.of("cmd2", ctx -> {}));

        assertEquals(2, registry.size());

        Command<?> removed = registry.remove("cmd1");
        assertNotNull(removed);
        assertEquals(1, registry.size());
        assertFalse(registry.contains("cmd1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommandRegistryNullName() {
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();
        registry.register(null, SimpleCommand.of("test", ctx -> {}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommandRegistryNullCommand() {
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();
        registry.register("test", null);
    }

    @Test
    public void testCommandParserSupportsParameters() {
        // Test parameter parsing without conditions
        CommandRegistry<CommandContext> registry = new CommandRegistry<>();

        registry.register("greet", SimpleCommand.of("greet", ctx -> {
            String name = (String) ctx.get("name");
            ctx.put("greeting", "Hello, " + name);
        }));

        CommandParser<CommandContext> parser = new CommandParser<>(registry);
        CommandContext ctx = new CommandContext();

        parser.parseAndExecute("greet(name=World)", ctx);
        assertEquals("Hello, World", ctx.get("greeting"));
    }

    @Test
    public void testOperandStackPushPop() {
        OperandStackContext context = new OperandStackContext();

        context.push(10);
        context.push(20);
        context.push(30);

        assertEquals(Integer.valueOf(30), context.pop());
        assertEquals(Integer.valueOf(20), context.pop());
        assertEquals(Integer.valueOf(10), context.pop());
        assertTrue(context.getOperandStack().isEmpty());
    }
}
