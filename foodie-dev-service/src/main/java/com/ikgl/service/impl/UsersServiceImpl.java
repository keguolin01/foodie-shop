package com.ikgl.service.impl;

import com.ikgl.enums.Sex;
import com.ikgl.mapper.UsersMapper;
import com.ikgl.pojo.Users;
import com.ikgl.pojo.bo.UserBO;
import com.ikgl.service.UsersService;
import com.ikgl.utils.DateUtil;
import com.ikgl.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    private static final String USER_FACE="http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users getById(String id) {
        return usersMapper.selectByPrimaryKey(id);
    }

    @Override
    public int saveUser(Users users) {
        Users u = new Users();
        u.setId("2");
        u.setNickname("aa");
        u.setUsername("aa");
        u.setPassword("22");
        u.setFace("2");
        u.setEmail("1");
        u.setUpdatedTime(new Date());
        u.setCreatedTime(new Date());
       return usersMapper.insert(u);
    }

    @Override
    public int updateUsers(Users users) {
        Users u = new Users();
        u.setId("2");
        u.setNickname("bb");
        u.setUsername("aa");
        u.setPassword("22");
        u.setFace("2");
        u.setEmail("1");
        return usersMapper.updateByPrimaryKey(users);
    }

    @Override
    public int deleteById(String id) {
        return usersMapper.deleteByPrimaryKey(id);
    }

    // @Transactional(propagation = Propagation.MANDATORY)
    public int saveUser1(Users users) {
        Users u = new Users();
        u.setId("2");
        u.setNickname("aa");
        u.setUsername("aa");
        u.setPassword("22");
        u.setFace("2");
        u.setEmail("1");
        u.setUpdatedTime(new Date());
        u.setCreatedTime(new Date());
        return usersMapper.insert(u);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public int saveUser2(Users users) {
        Users u = new Users();
        u.setId("3");
        u.setNickname("bb");
        u.setUsername("bb");
        u.setPassword("22");
        u.setFace("2");
        u.setEmail("1");
        u.setUpdatedTime(new Date());
        u.setCreatedTime(new Date());
        usersMapper.insert(u);
        int i = 1/0;
        return 1;
    }

    @Override
    public Boolean userNameIsExist(String userName) {
//        try {
//            Thread.sleep(3500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Example userExample = new Example(Users.class);
        userExample.createCriteria().andEqualTo("username",userName);
        Users users = usersMapper.selectOneByExample(userExample);
        return users == null? false:true;
    }

    //用户注册
    @Override
    public Users createUser(UserBO userBO) {
        Users user = new Users();
        String userId = sid.nextShort();
        user.setId(userId);
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setNickname(userBO.getUsername());
        user.setFace(USER_FACE);
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        usersMapper.insert(user);
        return user;
    }

    @Override
    public Users queryUserForLogin(String username, String password) {
        //
        Example example = new Example(Users.class);
        example.createCriteria().andEqualTo("username",username).andEqualTo("password",password);
        Users users = usersMapper.selectOneByExample(example);
        return users;
    }


}
