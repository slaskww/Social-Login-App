package com.example.oauth2sociallogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@SpringBootApplication
public class Oauth2SocialLoginApplication  {


	public static void main(String[] args) {
		SpringApplication.run(Oauth2SocialLoginApplication.class, args);
	}


@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User oauth2User){

	    //oauth2User.getAttributes().entrySet().stream().forEach(stringObjectEntry -> System.out.println(stringObjectEntry.getKey() + ":" + stringObjectEntry.getValue().toString()));
    Map<String, Object> data = new HashMap<>();
    data.put("googleName", oauth2User.getAttribute("name"));
    data.put("githubLogin", oauth2User.getAttribute("login"));
    return data;
}

@GetMapping("/error")
    public String getError(HttpServletRequest request){
	    String message = (String) request.getSession().getAttribute("error.message");
	    request.getSession().removeAttribute("error.message");
	    return message;
}
}
