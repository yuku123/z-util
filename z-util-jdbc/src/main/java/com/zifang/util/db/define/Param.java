package com.zifang.util.db.define;

import java.lang.annotation.*;

/**
 * SQL参数注解
 * <p>
 * 用于标识方法参数在SQL语句中的绑定名称，配合MyBatis等ORM框架使用。
 * 当Mapper接口的方法有多个参数时，使用此注解为每个参数指定一个名称，
 * 以便在XML映射文件中通过#{paramName}或${paramName}来引用。
 *
 * <p>使用示例：
 * <pre>
 * public interface UserMapper {
 *     &#64;Select("SELECT * FROM user WHERE name = #{name} AND age = #{age}")
 *     User findByNameAndAge(&#64;Param("name") String name, &#64;Param("age") int age);
 * }
 * </pre>
 *
 * @see Insert
 * @see Update
 * @see Delete
 * @see Select
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * Param注解。
 */
public @interface Param {
    /**
     * 指定参数的名称
     *
     * @return 参数的名称，用于在SQL语句中引用该参数
     */
    String value();
}
