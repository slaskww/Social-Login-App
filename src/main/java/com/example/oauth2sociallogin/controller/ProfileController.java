package com.example.oauth2sociallogin.controller;

import com.example.oauth2sociallogin.domain.File;
import com.example.oauth2sociallogin.domain.User;
import com.example.oauth2sociallogin.dto.PasswordDto;
import com.example.oauth2sociallogin.service.FileService;
import com.example.oauth2sociallogin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final FileService fileService;

    public ProfileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @ModelAttribute("passwordDto")
    public PasswordDto getPasswordDTO(){
        return new PasswordDto();
    }

    @ModelAttribute("hasFile")
    public Boolean hasUserFile(){
        return true;
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
                                 @Valid @ModelAttribute("passwordDto") PasswordDto passwordDTO,
                                 BindingResult errors,
                                 Model model){

        if(passwordDTO.hasErrors()){
            errors.addError( new ObjectError("passwordDto", passwordDTO.getAllErrors()));
        }

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
            Long savedFileId = fileService.uploadFile(file);
            log.info("File saved. Set id={}", savedFileId);

            File loadedFile = fileService.getFile(savedFileId);
            log.info("File loaded.");
            log.info("contentTypeSize={}", loadedFile.getContent().length);
            log.info("getName={}", loadedFile.getFileName());
            log.info("contentType={}",(loadedFile.getContentType()));
        } else {
            log.info("FILE CANNOT BE SAVED");
            log.info("contentType={}", file.getContentType());
            log.info("getName={}", file.getName());
            log.info("originalFilename={}",file.getOriginalFilename());
            log.info("size={}",(file.getSize()));
        }
        return "redirect:/profile";
    }

    @GetMapping("/avatar")
    public ResponseEntity<Resource> getAvatar(){

        log.info("in getAvatar method");
        File avatar = fileService.getFile(15L);
        ByteArrayResource content = new ByteArrayResource(avatar.getContent());
       return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(avatar.getContentType()))
                .header("Content-Disposition", String.format("filename=%s", avatar.getFileName()))
                .body(content);

    }

    private boolean isFileValid(MultipartFile file) throws IOException {

        if(file.isEmpty()) return false;
        if(file.getOriginalFilename() == null || file.getOriginalFilename().equals("")) return false;
        if(file.getContentType() == null) return false;
        if(file.getBytes() == null) return false;

        return true;
    }
}
