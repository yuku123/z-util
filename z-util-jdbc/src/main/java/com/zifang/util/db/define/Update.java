package com.zifang.util.db.define;

/**
 * 更新操作注解
 * <p>
 * 用于标识需要进行数据库更新操作的方法，通常与MyBatis等ORM框架配合使用。
 * 标记在Mapper接口的方法上，表示该方法执行更新（修改）数据库操作。
 *
 * <p>使用示例：
 * <pre>
 * public interface UserMapper {
 *     &#64;Update("UPDATE user SET name = #{name}, age = #{age} WHERE id = #{id}")
 *     int updateUser(User user);
 * }
 * </pre>
 *
 * <p>该注解可以与&#64;Param注解配合使用，为方法参数指定名称，
 * 以便在SQL语句中通过#{paramName}方式引用。
 *
 * @see Insert
 * @see Delete
 * @see Select
 * @see Param
 */
/**
 * Update注解。
 */
public @interface Update {
}
