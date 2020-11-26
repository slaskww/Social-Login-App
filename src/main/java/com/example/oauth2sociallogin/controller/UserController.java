package com.example.oauth2sociallogin.controller;

import com.example.oauth2sociallogin.domain.User;
import com.example.oauth2sociallogin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User oAuth2User, @AuthenticationPrincipal UserDetails userDetails){


        String username;
        if(userDetails == null){
           // oAuth2User.getAttributes().entrySet().stream().forEach(stringObjectEntry -> log.info(stringObjectEntry.getKey() + ":" + stringObjectEntry.getValue().toString()));
           User savedUser = userService.saveUser(oAuth2User);
           username = savedUser.getUsername();
        } else
            username = userDetails.getUsername();
        return Collections.singletonMap("username", username);
    }
}
