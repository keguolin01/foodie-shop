package com.ikgl.service.impl.center;

import com.ikgl.mapper.UsersMapper;
import com.ikgl.pojo.Users;
import com.ikgl.pojo.bo.center.CenterUserBO;
import com.ikgl.service.center.CenterUsersService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CenterUsersServiceImpl implements CenterUsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;


    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;
    }

    @Transactional
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users user = new Users();
        BeanUtils.copyProperties(centerUserBO,user);
        user.setId(userId);
        user.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(user);
        return queryUserInfo(userId);
    }

}
