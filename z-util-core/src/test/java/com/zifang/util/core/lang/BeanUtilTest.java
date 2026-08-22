package com.zifang.util.core.lang;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * BeanUtilTest类。
 */
public class BeanUtilTest {

    /**
     * 测试用bean。
     */
    public static class SampleBean {
        private String name;
        private Integer age;
        private String city;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

    /**
     * 测试用子类bean（验证父类字段参与合并）。
     */
    public static class ChildBean extends SampleBean {
        private String extra;

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }
    }

    @Test
    /**
     * testCombineObject方法。
     */
    public void testCombineObject() {
        SampleBean source = new SampleBean();
        source.setName("alice");
        SampleBean target = new SampleBean();
        target.setName("bob");
        target.setAge(18);
        target.setCity("shanghai");

        SampleBean combined = BeanUtil.combineObject(source, target);
        // 返回同一实例
        assertSame(target, combined);
        // source非空属性覆盖target
        assertEquals("alice", combined.getName());
        // source为null的属性保持target原值
        assertEquals(Integer.valueOf(18), combined.getAge());
        assertEquals("shanghai", combined.getCity());
    }

    @Test
    /**
     * testCombineObject_NullInput方法。
     */
    public void testCombineObject_NullInput() {
        SampleBean target = new SampleBean();
        target.setName("bob");
        // source为null时直接返回target且不修改
        assertSame(target, BeanUtil.combineObject(null, target));
        assertEquals("bob", target.getName());
        // target为null时返回null
        assertNull(BeanUtil.combineObject(new SampleBean(), null));
        // 两者均为null返回null
        assertNull(BeanUtil.combineObject(null, null));
    }

    @Test
    /**
     * testCombineObject_InheritField方法。
     */
    public void testCombineObject_InheritField() {
        ChildBean source = new ChildBean();
        source.setExtra("ex");
        ChildBean target = new ChildBean();
        target.setName("parent");

        ChildBean combined = BeanUtil.combineObject(source, target);
        // 父类字段同样参与非空覆盖
        assertEquals("parent", combined.getName());
        assertEquals("ex", combined.getExtra());
    }

    @Test
    /**
     * testCombineObject_StaticFieldSkipped方法。
     */
    public void testCombineObject_StaticFieldSkipped() {
        SampleBean source = new SampleBean();
        source.setName("alice");
        SampleBean target = new SampleBean();
        // 静态字段不参与合并，不抛异常
        SampleBean combined = BeanUtil.combineObject(source, target);
        assertEquals("alice", combined.getName());
    }

    @Test
    /**
     * testIsAllFieldValueNull方法。
     */
    public void testIsAllFieldValueNull() {
        // 全部字段为null
        assertTrue(BeanUtil.isAllFieldValueNull(new SampleBean()));
        // 任一字段非null即返回false
        SampleBean bean = new SampleBean();
        bean.setAge(18);
        assertFalse(BeanUtil.isAllFieldValueNull(bean));
        // 对象本身为null
        assertTrue(BeanUtil.isAllFieldValueNull(null));
    }

    @Test
    /**
     * testIsAllFieldValueNull_InheritField方法。
     */
    public void testIsAllFieldValueNull_InheritField() {
        // 父类字段参与判断
        ChildBean child = new ChildBean();
        child.setName("parent");
        assertFalse(BeanUtil.isAllFieldValueNull(child));
        // 父类与子类字段全部为null
        assertTrue(BeanUtil.isAllFieldValueNull(new ChildBean()));
    }
}
