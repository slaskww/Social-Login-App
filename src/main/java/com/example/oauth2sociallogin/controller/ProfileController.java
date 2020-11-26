package com.example.oauth2sociallogin.controller;

import com.example.oauth2sociallogin.domain.User;
import com.example.oauth2sociallogin.service.UserService;
import jdk.jfr.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getProfile(Model model){

        User user = userService.findUserById(11L);
        model.addAttribute("user", user);
        model.addAttribute("disabled", true);
        return "profile";
    }

    @PostMapping(params = {"edit"})
    public String getEditableProfile(Model model, @ModelAttribute("user") User usert){
        log.info(usert.toString());
        User user = userService.findUserById(11L);
        model.addAttribute("user", user);
        model.addAttribute("disabled", false);
        return "profile";
    }

    @PostMapping(params = {"cancel"})
    public String cancelEditableProfile(){
        return "redirect:/profile";
    }

    @PostMapping(params = {"save"})
    public String saveUser(@ModelAttribute("user") User userToSave){

        log.info(userToSave.toString());
        userService.updateUser(userToSave);
        return "redirect:/profile";
    }

}
