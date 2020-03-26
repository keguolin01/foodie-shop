package com.ikgl.service.center;

import com.ikgl.pojo.Users;
import com.ikgl.pojo.bo.center.CenterUserBO;

public interface CenterUsersService {

    /**
     * 查询用户信息
     */
    public Users queryUserInfo(String userId);

    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);
}
