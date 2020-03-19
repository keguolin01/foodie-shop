package com.ikgl.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class SSOController {
    static final Logger logger = LoggerFactory.getLogger(SSOController.class);
    @GetMapping("login")
    public String hello(){
        return "login";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("userInfo","new User");
        session.setMaxInactiveInterval(3600);
        return "ok";
    }
    @PostMapping("doLogin")
    public String doLogin(){
        return "login";
    }
}
