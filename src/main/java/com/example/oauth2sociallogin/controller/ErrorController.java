package com.example.oauth2sociallogin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ErrorController {

    @GetMapping("/error")
    public String error(HttpSession httpSession){

        String errorMsg = (String) httpSession.getAttribute("errorMsg");
        httpSession.removeAttribute("errorMsg");
        return errorMsg;
    }
}
