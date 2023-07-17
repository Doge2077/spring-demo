package com.example.mapper;

import com.example.entity.auth.Account;
import com.example.entity.user.AccountUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    // 根据用户名和邮箱查找指定账户，用户名和邮箱唯一且一一对应。
    @Select("SELECT * FROM db_account WHERE username = #{key} OR email = #{key}")
    Account getAccountByUsernameOrEmail(String key);

    @Select("SELECT * FROM db_account WHERE username = #{key} OR email = #{key}")
    AccountUser getAccountUserByUsernameOrEmail(String key);

    // 根据用户名查找指定账户
    @Select("SELECT * FROM db_account WHERE username = #{key}")
    Account getAccountByUsername(String key);

    // 根据邮箱查找指定账户
    @Select("SELECT * FROM db_account WHERE email = #{key}")
    Account getAccountByEmail(String key);

    // 注册新用户
    @Insert("INSERT INTO db_account (username, password, email) VALUES (#{username}, #{password}, #{email})")
    boolean createAccount(@Param("username") String username, @Param("password") String password,
                          @Param("email") String email);

    // 重置密码
    @Update("UPDATE db_account SET password = #{password} WHERE email = #{email}")
    boolean updatePassword(@Param("password") String psssword, @Param("email") String email);


}
