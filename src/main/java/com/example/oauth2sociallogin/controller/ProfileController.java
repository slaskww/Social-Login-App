package com.example.oauth2sociallogin.controller;

import com.example.oauth2sociallogin.domain.User;
import com.example.oauth2sociallogin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

        User user = userService.findUserById(14L);
        model.addAttribute("user", user);
        model.addAttribute("disabled", true);
        model.addAttribute("pdisabled", true);
        return "profile";
    }

    @PostMapping(params = {"edit"})
    public String getEditableProfileForm(Model model, @ModelAttribute("user") User usert){
        log.info(usert.toString());
        User user = userService.findUserById(14L);
        model.addAttribute("user", user);
        model.addAttribute("disabled", false);
        model.addAttribute("pdisabled", true);

        return "profile";
    }

    @PostMapping(params = {"cancel"})
    public String cancelEditableForm(){
        return "redirect:/profile";
    }

    @PostMapping(params = {"save"})
    public String updateUser(@Valid @ModelAttribute("user") User userToSave, BindingResult errors, Model model){

        if(errors.hasErrors()){
            log.info("Errors " + errors.getFieldError().getField());
            model.addAttribute("disabled", false);
            model.addAttribute("pdisabled", true);
            return "profile";
        }
        log.info(userToSave.toString());
        userService.updateUser(userToSave);
        return "redirect:/profile";
    }

    @PostMapping(params = {"pedit"})
    public String getEditablePasswordForm(Model model){
        User user = userService.findUserById(14L);
        model.addAttribute("user", user);
        model.addAttribute("disabled", true);
        model.addAttribute("pdisabled", false);
        return "profile";
    }

    @PostMapping(params = {"psave"})
    public String updatePassword(@RequestParam("pass") String pass, @RequestParam("repass") String repass){
        log.info("haslo {}, re-hasło {}", pass, repass);
        return "redirect:/profile";
    }

}
