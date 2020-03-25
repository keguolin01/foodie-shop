package com.ikgl.service;

import com.ikgl.pojo.UserAddress;
import com.ikgl.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {
    /**
     * 查询用户的地址列表
     * @param userId
     * @return
     */
    public List<UserAddress> queryAll(String userId);

    /**
     * 新增用户地址
     * @param addressBO
     */
    public void addNewUserAddress(AddressBO addressBO);

    /**
     * 修改用户地址
     * @param addressBO
     */
    public void updateUserAddress(AddressBO addressBO);

    /**
     * 删除用户地址
     * @param userId
     * @param addressId
     */
    void deleteUserAddress(String userId, String addressId);

    /**
     * 设置为默认地址
     * @param userId
     * @param addressId
     */
    void setDefalutAddress(String userId, String addressId);

    /**
     * 根据用户id和地址id查询地址信息
     * @param userId
     * @param addressId
     * @return
     */
    UserAddress queryUserAddress(String userId, String addressId);
}
