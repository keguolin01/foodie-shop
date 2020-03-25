package com.ikgl.service.impl;

import com.ikgl.enums.YesOrNo;
import com.ikgl.mapper.UserAddressMapper;
import com.ikgl.pojo.UserAddress;
import com.ikgl.pojo.bo.AddressBO;
import com.ikgl.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Override
    public List<UserAddress> queryAll(String userId) {
        Example example = new Example(UserAddress.class);
        example.createCriteria().andEqualTo("userId",userId);
        List<UserAddress> userAddresses = userAddressMapper.selectByExample(example);
        return userAddresses;
    }

    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        int isDefault = 0;
        List<UserAddress> userAddresses = queryAll(addressBO.getUserId());
        if(CollectionUtils.isEmpty(userAddresses)){
            isDefault = 1;
        }
        String addressId = sid.nextShort();
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setId(addressId);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddress.setIsDefault(isDefault);
        userAddressMapper.insert(userAddress);
    }

    @Override
    public void updateUserAddress(AddressBO addressBO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setUpdatedTime(new Date());
        userAddress.setId(addressBO.getAddressId());
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Override
    public void deleteUserAddress(String userId, String addressId) {
        Example example = new Example(UserAddress.class);
        example.createCriteria().andEqualTo("userId",userId).andEqualTo("id",addressId);
        userAddressMapper.deleteByExample(example);
    }

    @Transactional
    @Override
    public void setDefalutAddress(String userId, String addressId) {
        //把原来设置的默认 变为非默认
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setIsDefault(YesOrNo.YES.type);
        UserAddress ua = userAddressMapper.selectOne(userAddress);
        ua.setIsDefault(YesOrNo.NO.type);
        ua.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(ua);

        //再把现在的设置成默认的
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);
        return userAddressMapper.selectOne(userAddress);
    }
}
