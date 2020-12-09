package com.example.oauth2sociallogin.controller;

import com.example.oauth2sociallogin.domain.User;
import com.example.oauth2sociallogin.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("passwordDto")
    public PasswordDTO getPasswordDTO(){
        return new PasswordDTO();
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
    public String updateUser(
            @Valid @ModelAttribute("user") User userToSave,
            BindingResult errors,
            Model model){

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
    public String updatePassword(
                                 @Valid @ModelAttribute("passwordDto") PasswordDTO passwordDTO,
                                 BindingResult errors,
                                 Model model){


        if(errors.hasErrors()){
            User user = userService.findUserById(14L);
            model.addAttribute("user", user);
            model.addAttribute("disabled", true);
            model.addAttribute("pdisabled", false);
            return "profile";
        }
        log.info("password={}, re-password={}", passwordDTO.getPassword(), passwordDTO.getRePassword());
        return "redirect:/profile";
    }

    @PostMapping(params = {"upload"})
    public String uploadFile(@RequestParam MultipartFile file) throws IOException {

        if(isFileValid(file)){
            log.info("FILE READY TO PERSIST");
            log.info("contentType={}", file.getContentType());
            log.info("getName={}", file.getName());
            log.info("originalFilename={}",file.getOriginalFilename());
            log.info("size={}",(file.getSize()));
        } else {
            log.info("FILE CANNOT BE SAVED");
            log.info("contentType={}", file.getContentType());
            log.info("getName={}", file.getName());
            log.info("originalFilename={}",file.getOriginalFilename());
            log.info("size={}",(file.getSize()));
        }
        return "redirect:/profile";
    }

    private boolean isFileValid(MultipartFile file) throws IOException {

        if(file.isEmpty()) return false;
        if(file.getName() == null) return false;
        if(file.getContentType() == null) return false;
        if(file.getBytes() == null) return false;

        return true;
    }

    @Data
    private static class PasswordDTO{

        @Size(min = 5, message = "Podane hasło jest za krótkie")
        private String password;

        @Size(min = 5, message = "Podane powtórne hasło jest za krótkie")
        private String rePassword;

    }

}
