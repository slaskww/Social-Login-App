package com.example.oauth2sociallogin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@SpringBootApplication
public class Oauth2SocialLoginApplication  {


	public static void main(String[] args) {
		SpringApplication.run(Oauth2SocialLoginApplication.class, args);
	}


@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User oauth2User, @AuthenticationPrincipal UserDetails userDetails){

	   // oauth2User.getAttributes().entrySet().stream().forEach(stringObjectEntry -> System.out.println(stringObjectEntry.getKey() + ":" + stringObjectEntry.getValue().toString()));
    Map<String, Object> data = new HashMap<>();

if(userDetails == null){
	data.put("username", oauth2User.getAttribute("name"));
} else{
	data.put("username", userDetails.getUsername());
}
    return data;
}

@GetMapping("/error")
    public String getError(HttpServletRequest request){
	    String message = (String) request.getSession().getAttribute("errorMsg");

	    request.getSession().removeAttribute("errorMsg");
	    return message;
}
}
