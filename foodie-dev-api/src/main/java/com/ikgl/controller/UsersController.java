package com.ikgl.controller;

import com.ikgl.pojo.Users;
import com.ikgl.pojo.bo.ShopCartBO;
import com.ikgl.pojo.bo.UserBO;
import com.ikgl.service.UsersService;
import com.ikgl.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "用户注册",tags = "用于用户注册登录的接口")
@RestController
@RequestMapping("passport")
public class UsersController extends BaseController{

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private UsersService usersService;
    @ApiOperation(value = "用户名是否存在",notes = "用户名是否存在",httpMethod = "GET")
    @GetMapping("usernameIsExist")
    public IMOOCJSONResult userNameIsExist(@RequestParam String username){
        if(StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名为空");
        }
        Boolean isExist = usersService.userNameIsExist(username);
        if(isExist){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        return IMOOCJSONResult.ok();
    }

//    @GetMapping("getById")
//    public Users getById(String id){
//        Users byId = usersService.getById(id);
//        return byId;
//    }
//
//    @PostMapping("saveUser")
//    public String saveUser(){
//        int i = usersService.saveUser(new Users());
//        return i>0?"成功了":"失败了";
//    }
//    @PostMapping("updateUser")
//    public String updateUser(){
//        int i = usersService.updateUsers(new Users());
//        return i>0?"成功了":"失败了";
//    }
//    @PostMapping("deleteUser")
//    public String deleteUser(String id){
//        int i = usersService.deleteById(id);
//        return i>0?"成功了":"失败了";
//    }
    @ApiOperation(value = "用户注册",notes = "用户注册",httpMethod = "POST")
    @PostMapping("regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response){
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        //判断用户名 密码不能为空
        if(StringUtils.isBlank(username) ||StringUtils.isBlank(password) ||StringUtils.isBlank(confirmPassword)){
            IMOOCJSONResult.errorMsg("用户名密码不能为空");
        }
        //查看用户名是否存在
        Boolean isExist = usersService.userNameIsExist(username);
        if(isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        //判断密码强度
        if(password.length()<6){
            return IMOOCJSONResult.errorMsg("密码长度小于6");
        }
        //判断两次密码是否一致
        if(!password.equals(confirmPassword)){
            return IMOOCJSONResult.errorMsg("密码输入不一致");
        }
        //插入
        Users user = usersService.createUser(userBO);
        user = setNullproperty(user);
        //设置cookie
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(user),true);
        //同步购物车数据
        synchShopCartData(user.getId(),request,response);
        //注册完后
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        //判断用户名 密码不能为空
        if(StringUtils.isBlank(username) ||StringUtils.isBlank(password)){
            return IMOOCJSONResult.errorMsg("用户名密码不能为空");
        }
        Users user = usersService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if(user == null){
            return IMOOCJSONResult.errorMsg("用户名或密码出错");
        }
        //设置cookie
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(user),true);
        //TODO 生成用户token 存入redis会话
        //同步购物车数据
        synchShopCartData(user.getId(),request,response);
        return IMOOCJSONResult.ok(user);
    }

    private void synchShopCartData(String userId, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 1.redis无数据， 如果cookie为空 不作任何处理
         *               如果cookie不为空 以本地cookie覆盖redis
         * 2.redis有数据  如果cookie为空  以redis覆盖cookie
         *               如果cookie不为空 ，如果cookie中某个商品在redis中存在就以cookie，删除redis中的
         *               那商品就以cookie为主（参考京东）
         * 3.同步redis去了，覆盖本地cookie购物车的数据，保证购物车数据是同步的
         */
        String shopCartRedis = redisOperator.get(FOODIE_SHOPCART +":" + userId);
        String shopCartCookie = CookieUtils.getCookieValue(request,FOODIE_SHOPCART,true);
        if(StringUtils.isBlank(shopCartRedis) ||
                CollectionUtils.isEmpty(JsonUtils.jsonToList(shopCartRedis,ShopCartBO.class))){
            //redis为空 cookie不为空
            if(StringUtils.isNotBlank(shopCartCookie)){
                //以cookie覆盖redis
                redisOperator.set(FOODIE_SHOPCART +":" + userId,shopCartCookie);
            }
        }else{
            //redis不为空 cookie为空
            if(StringUtils.isBlank(shopCartCookie)){
                //redis覆盖cookie
                CookieUtils.setCookie(request,response,FOODIE_SHOPCART, shopCartRedis,true);
            }else{
                //redis不为空 cookie也不为空
                /**
                 * 1.已经存在的，把cookie的数量覆盖redis
                 * 2.标记该商品被删除
                 * 3.从cookie清理删除的list
                 * 4.合并redis和cookie的数据
                 * 5.更新redis和cookie数据
                 */
                List<ShopCartBO> pendingDelList = new ArrayList<>();
                List<ShopCartBO> shopCartRedisList = JsonUtils.jsonToList(shopCartRedis,ShopCartBO.class);
                List<ShopCartBO> shopCartCookieList = JsonUtils.jsonToList(shopCartCookie,ShopCartBO.class);
                for(ShopCartBO redis : shopCartRedisList){
                    for(ShopCartBO cookie : shopCartCookieList){
                        if(redis.getSpecId().equals(cookie.getSpecId())){
                            //把cookie数据移除 redis中的数量重新赋值
                            redis.setBuyCounts( cookie.getBuyCounts());
                            pendingDelList.add(cookie);
                        }
                    }
                }
                //处理好数据进行cookie移除 剩下的就是redis没有的 加入redis
                shopCartCookieList.removeAll(pendingDelList);
                //合并redis和cookie
                shopCartRedisList.addAll(shopCartCookieList);
                //更新redis和cookie数据
                CookieUtils.setCookie(request,response,FOODIE_SHOPCART, JsonUtils.objectToJson(shopCartRedisList),true);
                redisOperator.set(FOODIE_SHOPCART +":" + userId,JsonUtils.objectToJson(shopCartRedisList));
            }
        }
    }

    @ApiOperation(value = "用户退出",notes = "用户退出",httpMethod = "POST")
    @PostMapping("logout")
    public IMOOCJSONResult logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
       //1.清除cookie
        CookieUtils.deleteCookie(request,response,"user");

        //用户退出需要清空购物车 分布式会话中需要清除用户数据
        CookieUtils.deleteCookie(request,response,FOODIE_SHOPCART);

        return IMOOCJSONResult.ok();
    }

    public Users setNullproperty(Users users){
        users.setEmail(null);
        users.setMobile(null);
        users.setRealname(null);
        return users;
    }

}
