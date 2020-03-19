package com.ikgl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ApiIgnore
@RestController
public class HelloController {
    static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    @GetMapping("hello")
    public Object hello(){
        logger.info("111");
        logger.debug("222");
        return "hello world";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("userInfo","new User");
        session.setMaxInactiveInterval(3600);
        return "ok";
    }
}
