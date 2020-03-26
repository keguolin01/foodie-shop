package com.ikgl.controller.center;

import com.ikgl.controller.BaseController;
import com.ikgl.pojo.Users;
import com.ikgl.pojo.bo.center.CenterUserBO;
import com.ikgl.service.center.CenterUsersService;
import com.ikgl.utils.CookieUtils;
import com.ikgl.utils.IMOOCJSONResult;
import com.ikgl.utils.JsonUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUsersService centerUsersService;

    @ApiOperation(value = "查询用户信息",notes = "查询用户信息",httpMethod = "POST")
    @PostMapping("update")
    public IMOOCJSONResult update(
            @ApiParam(value = "用户id",name = "userId",required = true)
            @RequestParam String userId,
            @Valid @RequestBody CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response){
        if(result.hasErrors()){
            Map<String,String> map = getErrors(result);
            return IMOOCJSONResult.errorMap(map);
        }
        Users users = centerUsersService.updateUserInfo(userId, centerUserBO);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "查询用户信息",notes = "查询用户信息",httpMethod = "POST")
    @PostMapping("uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(value = "用户id",name = "userId",required = true)
                    @RequestParam String userId,
                     MultipartFile file,
            HttpServletResponse response){
        FileOutputStream fos = null;
        if(file == null){
            return IMOOCJSONResult.errorMsg("文件不存在");
        }
        //1.文件名
        String fileName = file.getOriginalFilename();
        String filePath = IMAGE_USER_FACE_LOCATION;
        String uploadPathPrefix = File.separator + userId;
        //2.进行文件名称的重组
        if(StringUtils.isNotBlank(fileName)){
            try {
                String fileNameArr[] = fileName.split("\\.");
                String suffix = fileNameArr[fileNameArr.length -1];
                Long current = new Date().getTime();
                //3.新的文件名
                String newFileName = "face-" + userId + current + "." + suffix;

                //文件保存路径
                String outPath = filePath + uploadPathPrefix + File.separator
                        + newFileName;
                //判断文件夹是否存在
               File outFile = new File(outPath);
               System.out.println(outFile.getParentFile());
               if(!outFile.getParentFile().exists()){
                   //如果此文件夹中没有文件
                    outFile.getParentFile().mkdirs();
               }
                InputStream inputStream = file.getInputStream();
                fos = new FileOutputStream(outFile);
                IOUtils.copy(inputStream,fos);
//                int len = 0;
//                byte buffer[] = new byte[1024];
//                while ((len = inputStream.read(buffer))!= -1){
//                    fos.write(buffer,0,len);
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(fos!=null){
                        fos.flush();
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            return IMOOCJSONResult.errorMsg("文件名为空");
        }

        return IMOOCJSONResult.ok();
    }

    private Map<String,String> getErrors(BindingResult result){
        Map<String,String> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for(FieldError fieldError : fieldErrors){
            String field = fieldError.getField();
            String errorMsg = fieldError.getDefaultMessage();
            errorMap.put(field,errorMsg);
        }
        return errorMap;
    }

}