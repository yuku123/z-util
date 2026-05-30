package com.zifang.util.db.define;

/**
 * 删除操作注解
 * <p>
 * 用于标识需要进行数据库删除操作的方法，通常与MyBatis等ORM框架配合使用。
 * 标记在Mapper接口的方法上，表示该方法执行删除数据库操作。
 *
 * <p>使用示例：
 * <pre>
 * public interface UserMapper {
 *     &#64;Delete("DELETE FROM user WHERE id = #{id}")
 *     int deleteUserById(long id);
 * }
 * </pre>
 *
 * <p>该注解可以与&#64;Param注解配合使用，为方法参数指定名称，
 * 以便在SQL语句中通过#{paramName}方式引用。
 *
 * @see Insert
 * @see Update
 * @see Select
 * @see Param
 */
/**
 * Delete注解。
 */
public @interface Delete {
}
