package com.example.oauth2sociallogin.service;

import com.example.oauth2sociallogin.domain.User;
import com.example.oauth2sociallogin.repository.UserRepository;
import jdk.jfr.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent())
            return user.get();
        else throw new UsernameNotFoundException("User " + username + " does not exist in database");
    }

    public User saveUser(User user){
       return userRepository.save(user);
    }

    public User saveUser(OAuth2User user){

        User userToPersist = new User();
        if(user.getAttribute("login") != null){ //true if OAuth2User authenticated via github
            userToPersist.setEmail("empty");
            userToPersist.setFullName(user.getAttribute("name"));
            Integer providerId = user.getAttribute("id");
            userToPersist.setOAuth2ProviderId(providerId.toString());
            userToPersist.setUsername(user.getAttribute("login"));
            userToPersist.setPassword(passwordEncoder.encode("123456"));
        } else{
            userToPersist.setEmail(user.getAttribute("email"));
            userToPersist.setFullName(user.getAttribute("name"));
            userToPersist.setOAuth2ProviderId(user.getAttribute("sub"));

            String email = user.getAttribute("email");
            int indexOfAt;

            String login = "user";
            if (email != null){
                indexOfAt = email.indexOf("@");
                login = email.substring(0,indexOfAt);
            }
            userToPersist.setUsername(login);
            userToPersist.setPassword(passwordEncoder.encode("123456"));
        }

        return userRepository.save(userToPersist);
    }

    public User findUserById(Long id){

       Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
}
