package com.zifang.util.db.context;

import com.zifang.util.db.transation.TranslationManager;
import org.junit.Test;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

/**
 * DataSourceContext 类测试
 */
public class DataSourceContextTest {

    @Test
    public void testDefaultConstructor() {
        DataSourceContext context = new DataSourceContext();
        assertNotNull(context);
        assertNull(context.getDatasourceFactory());
        assertNull(context.getScanPackageName());
        assertNull(context.getTransactionManager());
    }

    @Test
    public void testChainMethods() {
        DataSourceContext context = new DataSourceContext()
                .scanPackage("com.example.repository")
                .transationManager(new TranslationManager());

        assertEquals("com.example.repository", context.getScanPackageName());
        assertNotNull(context.getTransactionManager());
    }

    @Test
    public void testScanPackageName() {
        DataSourceContext context = new DataSourceContext();
        context.setScanPackageName("com.test.pkg");

        assertEquals("com.test.pkg", context.getScanPackageName());
    }

    @Test
    public void testDataSourceFactory() {
        DataSourceContext context = new DataSourceContext();
        DatasourceFactory factory = new DatasourceFactory() {
            @Override
            public DataSource getDatasource() {
                return null;
            }
        };

        context.setDatasourceFactory(factory);
        assertNotNull(context.getDatasourceFactory());
        assertSame(factory, context.getDatasourceFactory());
    }

    @Test
    public void testTransactionManager() {
        DataSourceContext context = new DataSourceContext();
        TranslationManager tm = new TranslationManager();

        context.setTransactionManager(tm);
        assertNotNull(context.getTransactionManager());
        assertSame(tm, context.getTransactionManager());
    }

    @Test
    public void testEquals() {
        DataSourceContext ctx1 = new DataSourceContext();
        ctx1.setScanPackageName("com.test");

        DataSourceContext ctx2 = new DataSourceContext();
        ctx2.setScanPackageName("com.test");

        assertEquals(ctx1, ctx2);
    }

    @Test
    public void testEqualsWithDifferentPackages() {
        DataSourceContext ctx1 = new DataSourceContext();
        ctx1.setScanPackageName("com.test1");

        DataSourceContext ctx2 = new DataSourceContext();
        ctx2.setScanPackageName("com.test2");

        assertNotEquals(ctx1, ctx2);
    }

    @Test
    public void testEqualsWithNull() {
        DataSourceContext ctx = new DataSourceContext();
        assertNotEquals(ctx, null);
    }

    @Test
    public void testHashCode() {
        DataSourceContext ctx1 = new DataSourceContext();
        ctx1.setScanPackageName("com.test");

        DataSourceContext ctx2 = new DataSourceContext();
        ctx2.setScanPackageName("com.test");

        assertEquals(ctx1.hashCode(), ctx2.hashCode());
    }

    @Test
    public void testToString() {
        DataSourceContext ctx = new DataSourceContext();
        String str = ctx.toString();

        assertNotNull(str);
        assertTrue(str.contains("DataSourceContext"));
    }

    @Test
    public void testFullConfiguration() {
        DatasourceFactory factory = new DatasourceFactory() {
            @Override
            public DataSource getDatasource() {
                return null;
            }
        };
        TranslationManager tm = new TranslationManager();

        DataSourceContext ctx = new DataSourceContext()
                .scanPackage("com.example")
                .dataSourceFactory(factory)
                .transationManager(tm);

        assertEquals("com.example", ctx.getScanPackageName());
        assertNotNull(ctx.getDatasourceFactory());
        assertNotNull(ctx.getTransactionManager());
    }
}