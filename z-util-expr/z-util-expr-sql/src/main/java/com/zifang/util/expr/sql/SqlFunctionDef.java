package com.zifang.util.expr.sql;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * SQL 函数定义（元数据）。
 */
public class SqlFunctionDef {
    private final String name;
    private final Method method;
    private final boolean isVarargs;

    public SqlFunctionDef(String name, Method method) {
        this.name = name.toUpperCase();
        this.method = method;
        Class<?>[] params = method.getParameterTypes();
        this.isVarargs = params.length > 0
                && params[params.length - 1].isArray();
    }

    public String getName() { return name; }
    public Method getMethod() { return method; }

    public Object exec(java.util.Map<String, Object> row, Object... args) {
        try {
            if (isVarargs) {
                int paramCount = method.getParameterCount();
                // paramCount = 1(row) + M(fixed) + 1(varargs) = M+2
                // M = paramCount - 2 (number of fixed params after row, excluding varargs)
                int fixedCount = paramCount - 2;  // M = number of fixed params after row
                if (paramCount == 2) {
                    // (Map, Object...) — only row + varargs, no fixed params
                    // args go directly to the varargs array
                    return method.invoke(null, row, args);
                }
                // (Map, T1, ..., TM, Object...) — M fixed params + varargs
                // args[0..M-1] → fixed slots 1..M
                // args[M..] → varargs array (slot M+1)
                Object[] invokeArgs = new Object[paramCount];
                invokeArgs[0] = row;
                for (int i = 0; i < fixedCount; i++) {
                    invokeArgs[i + 1] = i < args.length ? args[i] : null;
                }
                Object[] varargs = new Object[Math.max(0, args.length - fixedCount)];
                for (int i = 0; i < varargs.length; i++) {
                    varargs[i] = args[fixedCount + i];
                }
                invokeArgs[paramCount - 1] = varargs;
                return method.invoke(null, invokeArgs);
            }
            switch (args.length) {
                case 0:  return method.invoke(null, row);
                case 1:  return method.invoke(null, row, args[0]);
                case 2:  return method.invoke(null, row, args[0], args[1]);
                case 3:  return method.invoke(null, row, args[0], args[1], args[2]);
                case 4:  return method.invoke(null, row, args[0], args[1], args[2], args[3]);
                default: return method.invoke(null, (Object) new Object[]{row, args});
            }
        } catch (Exception e) {
            throw new SqlException("Failed to execute function: " + name, e);
        }
    }

    @Override public boolean equals(Object o) { return o instanceof SqlFunctionDef && name.equals(((SqlFunctionDef) o).name) && method.equals(((SqlFunctionDef) o).method); }
    @Override public int hashCode() { return Objects.hash(name, method); }
    @Override public String toString() { return "SqlFunctionDef{name='" + name + "', method=" + method.getName() + "}"; }
}