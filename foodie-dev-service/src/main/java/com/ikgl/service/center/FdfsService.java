package com.ikgl.service.center;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FdfsService {
    public String upload();
    public String uploadOss(MultipartFile file, String userId, String fileExtName) throws IOException;
}
