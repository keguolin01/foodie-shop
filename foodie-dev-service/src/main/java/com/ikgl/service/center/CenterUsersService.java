package com.ikgl.service.center;

import com.ikgl.pojo.Users;
import com.ikgl.pojo.bo.center.CenterUserBO;

public interface CenterUsersService {

    /**
     * 查询用户信息
     */
    public Users queryUserInfo(String userId);

    /**
     * 更新用户信息
     * @param userId
     * @param centerUserBO
     * @return
     */
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 更新用户头像url信息
     * @param userId
     * @param faceUrl
     * @return
     */
    public Users updateUserFaceUrl(String userId,String faceUrl);
}
