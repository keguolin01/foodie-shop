package com.ikgl.controller.center;

import com.ikgl.pojo.Users;
import com.ikgl.service.center.CenterUsersService;
import com.ikgl.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "门户相关",tags = "用门户相关的接口")
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUsersService centerUsersService;

    @ApiOperation(value = "查询用户信息",notes = "查询用户信息",httpMethod = "GET")
    @GetMapping("userInfo")
    public IMOOCJSONResult userInfo(
            @ApiParam(value = "用户id",name = "userId",required = true)
            @RequestParam String userId){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("用户id为空");
        }
        Users users = centerUsersService.queryUserInfo(userId);
        return IMOOCJSONResult.ok(users);
    }
}
