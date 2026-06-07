package com.zifang.util.db.test0;

import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.db.context.DataSourceContext;
import com.zifang.util.db.context.DatasourceContextManager;
import com.zifang.util.db.respository.RepositoryProxy;
import com.zifang.util.db.transation.TranslationManager;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * Test0类。
 */
public class Test0 {

    private static final Logger log = LoggerFactory.getLogger(Test0.class);

    @Before
    /**
     * init方法。
     */
    public void init() {

        // 数据库上下文
        DataSourceContext dataSourceContext = new DataSourceContext()
                .scanPackage("com.zifang.util.db")
                .transationManager(new TranslationManager())
                .dataSourceFactory(new MysqlDatasourceFactory());

        // 注册数据库信息
        DatasourceContextManager.register(DatasourceContextManager.DEFAULT, dataSourceContext);
    }

    @Test
    @org.junit.Ignore("需要 MySQL 数据库连接")
    /**
     * test方法。
     */
    public void test() {

        ResourceItemRepository resourceItemRepository = RepositoryProxy.proxy(ResourceItemRepository.class);

        List<ResourceItem> r1 = resourceItemRepository.findByNameList("5e8888e8be7fff746fb26b5a", 0);
        List<Map<String, Object>> r2 = resourceItemRepository.findByNameListMap("5e8888e8be7fff746fb26b5a", 0);
        ResourceItem r3 = resourceItemRepository.findByNameBean("5e8888e8be7fff746fb26b5a", 0);
        Map<String, Object> r4 = resourceItemRepository.findByNameMap("5e8888e8be7fff746fb26b5a", 0);

        log.info(GsonUtil.objectToJsonStr(r1));
        log.info(GsonUtil.objectToJsonStr(r2));
        log.info(GsonUtil.objectToJsonStr(r3));
        log.info(GsonUtil.objectToJsonStr(r4));

        log.info("结束");

    }

    @Test
    @org.junit.Ignore("需要 MySQL 数据库连接")
    /**
     * test1方法。
     */
    public void test1() {
        ResourceItemRepository resourceItemRepository = RepositoryProxy.proxy(ResourceItemRepository.class);

        ResourceItem resourceItem = new ResourceItem();
        resourceItem.setCmsId("xxxxx");
        resourceItem.setShared(false);
        resourceItemRepository.save(resourceItem);
        resourceItemRepository.findById(resourceItem.getId());
        resourceItemRepository.deleteById(resourceItem.getId());
    }
}
