//package com.ikgl.service.impl.center;
//
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
//import com.ikgl.service.center.FdfsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Service
//public class FdfsServiceImpl implements FdfsService {
//
//    @Autowired
//    private FileResource fileResource;
//
//    @Override
//    public String upload() {
//        return null;
//    }
//
//    @Override
//    public String uploadOss(MultipartFile file,String userId, String fileExtName) throws IOException {
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = fileResource.getEndpoint();
//        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
//        String accessKeyId = fileResource.getAccessKeyId();
//        String accessKeySecret = fileResource.getAccessKeySecret();
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        // 上传网络流。
//        InputStream inputStream = file.getInputStream();
//        String myObjectName = fileResource.getObjectName() + "/" + userId + "/" + userId + "."+fileExtName;
//        ossClient.putObject(fileResource.getBucketName(), myObjectName, inputStream);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//        return myObjectName;
//    }
//}
