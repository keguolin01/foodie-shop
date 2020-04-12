package com.ikgl.controller.center;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ikgl.pojo.Users;
import com.ikgl.resource.FileResource;
import com.ikgl.service.center.CenterUsersService;
import com.ikgl.service.center.FdfsService;
import com.ikgl.utils.CookieUtils;
import com.ikgl.utils.JsonUtils;
import com.ikgl.utils.ResponseJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("fdfs")
public class FsController {

    @Autowired
    private CenterUsersService centerUsersService;

    @Autowired
    private FileResource fileResource;

    @PostMapping("uploadFace")
    @Transactional
    public ResponseJSONResult uploadFace(
            String userId,
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response){
        if(file == null){
            return ResponseJSONResult.errorMsg("文件不存在");
        }
        //1.文件名
        String fileName = file.getOriginalFilename();
        String outPath = "";
//        String uploadPathPrefix = File.separator + userId;
        //2.进行文件名称的重组
        if(StringUtils.isNotBlank(fileName)){
                String fileNameArr[] = fileName.split("\\.");
                String suffix = fileNameArr[fileNameArr.length -1];
                //对图片格式进行判断
                if(!suffix.equalsIgnoreCase("jpg")
                        && !suffix.equalsIgnoreCase("jpeg")
                        && !suffix.equalsIgnoreCase("png")){
                    return ResponseJSONResult.errorMsg("图片格式不正确");
                }
            String path = null;
            try {
                path = uploadOss(file,userId,suffix);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //文件保存路径
                outPath = fileResource.getOssHost()+path;

            //更新信息到数据库
            //1.更新用户信息faceUrl
            //由于浏览器会存在缓存
            //假如url相同的话，浏览器会从本地缓存先读取，那么这样就刷新及木有用处。
            //而加了随机数相当于每次url都不相同，会从服务器重新取得。
//            String faceUrl = fileUpLoad.getImageServerUrl() + uploadPathPrefix
//                    + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
            //2.更新用户头像信息接口
            Users users = centerUsersService.updateUserFaceUrl(userId, outPath);
            //TODO 后续会改，增加令牌token 整合redis
            CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);

        }else{
            return ResponseJSONResult.errorMsg("文件名为空");
        }

        return ResponseJSONResult.ok();
    }


    public String uploadOss(MultipartFile file,String userId, String fileExtName) throws IOException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = fileResource.getEndpoint();
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = fileResource.getAccessKeyId();
        String accessKeySecret = fileResource.getAccessKeySecret();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传网络流。
        InputStream inputStream = file.getInputStream();
        String myObjectName = fileResource.getObjectName() + "/" + userId + "/" + userId + "."+fileExtName;
        ossClient.putObject(fileResource.getBucketName(), myObjectName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        return myObjectName;
    }
}
