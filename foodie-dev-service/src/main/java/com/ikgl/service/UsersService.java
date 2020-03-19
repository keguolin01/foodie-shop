package com.ikgl.service;

import com.ikgl.pojo.Users;
import com.ikgl.pojo.bo.UserBO;
import com.sun.org.apache.xpath.internal.operations.Bool;

public interface UsersService {

    public Users getById(String id);

    public int saveUser(Users users);

    public int updateUsers(Users users);

    public int deleteById(String id);

    Boolean userNameIsExist(String userName);
    /**
     * 判断用户名是否存在
     */
    public Users createUser(UserBO userBO);

    /**
     * 检索用户名和密码是否匹配，用于登录
     */
    public Users queryUserForLogin(String username, String password);
}
