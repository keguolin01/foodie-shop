package com.ikgl.exception;

import com.ikgl.utils.ResponseJSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

//@ControllerAdvice
@RestControllerAdvice
public class CustomExceptionHandler {
        //对MaxUploadSizeExceededException 捕捉全局异常 对所有requestMapping有效
        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public ResponseJSONResult handlerMaxUploadException(MaxUploadSizeExceededException e){
            return ResponseJSONResult.errorMsg("文件大小超过限制200kb，请压缩图片在上传");
        }
}
