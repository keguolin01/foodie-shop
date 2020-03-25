package com.ikgl.controller;

import com.ikgl.enums.YesOrNo;
import com.ikgl.pojo.Carousel;
import com.ikgl.pojo.UserAddress;
import com.ikgl.pojo.bo.AddressBO;
import com.ikgl.service.AddressService;
import com.ikgl.utils.IMOOCJSONResult;
import com.ikgl.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Api(value = "地址相关",tags={"地址相关的api接口"})
@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    /**
     * 1.查询用户相对应的地址列表
     * 2.新增地址
     * 3.修改地址
     * 4.删除地址
     * 5.设置默认地址
     */
    @ApiOperation(value = "展示地址列表",notes = "展示地址列表",httpMethod = "GET")
    @PostMapping("list")
    public IMOOCJSONResult list(@RequestParam String userId){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("用户id参数没传入");
        }
        List<UserAddress> userAddresses = addressService.queryAll(userId);
        return IMOOCJSONResult.ok(userAddresses);
    }

    @ApiOperation(value = "用户新增地址",notes = "用户新增地址",httpMethod = "POST")
    @PostMapping("add")
    public IMOOCJSONResult add(@RequestBody AddressBO addressBO){
        IMOOCJSONResult result = checkAddress(addressBO);
        if(result.getStatus() != 200){
            return result;
        }
        addressService.addNewUserAddress(addressBO);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户修改地址",notes = "用户修改地址",httpMethod = "POST")
    @PostMapping("update")
    public IMOOCJSONResult update(@RequestBody AddressBO addressBO){
        if(StringUtils.isBlank(addressBO.getAddressId())){
            IMOOCJSONResult.errorMsg("修改地址错误AddressId为空");
        }
        IMOOCJSONResult result = checkAddress(addressBO);
        if(result.getStatus() != 200){
            return result;
        }
        addressService.updateUserAddress(addressBO);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户删除地址",notes = "用户删除地址",httpMethod = "POST")
    @PostMapping("delete")
    public IMOOCJSONResult delete(@RequestParam String userId,
                                  @RequestParam String addressId){
        if(StringUtils.isBlank(userId)){
            IMOOCJSONResult.errorMsg("用户id为空");
        }
        if(StringUtils.isBlank(addressId)){
            IMOOCJSONResult.errorMsg("用户地址id为空");
        }
        addressService.deleteUserAddress(userId,addressId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址",notes = "用户设置默认地址",httpMethod = "POST")
    @PostMapping("setDefalut")
    public IMOOCJSONResult setDefalut(@RequestParam String userId,
                                  @RequestParam String addressId){
        if(StringUtils.isBlank(userId)){
            IMOOCJSONResult.errorMsg("用户id为空");
        }
        if(StringUtils.isBlank(addressId)){
            IMOOCJSONResult.errorMsg("用户地址id为空");
        }
        addressService.setDefalutAddress(userId,addressId);
        return IMOOCJSONResult.ok();
    }

    private IMOOCJSONResult checkAddress(AddressBO addressBO){
        String receiver = addressBO.getReceiver();
        if(StringUtils.isBlank(receiver)){
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if(receiver.length()> 12){
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }
        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return IMOOCJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return IMOOCJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }

        return IMOOCJSONResult.ok();
    }
}
