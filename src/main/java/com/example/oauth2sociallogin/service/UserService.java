package com.example.oauth2sociallogin.service;

import com.example.oauth2sociallogin.domain.User;
import com.example.oauth2sociallogin.repository.UserRepository;
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
        if (user.isPresent())
            return user.get();
        else throw new UsernameNotFoundException("User " + username + " does not exist in database");
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User saveUser(OAuth2User user) {

        String username = getLoginFromOAuthUser(user);
        Optional<User> persistent  = userRepository.findByUsername(username);

        if(persistent.isPresent())
            return persistent.get();

        User userToPersist = new User();
        if (user.getAttribute("login") != null) //true if OAuth2User authenticated via github
            userToPersist = populateWithGithubData(user);
        else
            userToPersist = populateWithGoogleData(user);

        return userRepository.save(userToPersist);
    }

    public User findUserById(Long id) {

        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    private User populateWithGithubData(OAuth2User oAuth2User) {
        User user = new User();
        user.setEmail("empty");
        user.setFullName(oAuth2User.getAttribute("name"));
        Integer providerId = oAuth2User.getAttribute("id");
        user.setOAuth2ProviderId(providerId.toString());
        user.setUsername(oAuth2User.getAttribute("login"));
        user.setPassword(passwordEncoder.encode("123456"));
        return user;
    }

    private User populateWithGoogleData(OAuth2User oAuth2User) {
        User user = new User();
        user.setEmail(oAuth2User.getAttribute("email"));
        user.setFullName(oAuth2User.getAttribute("name"));
        user.setOAuth2ProviderId(oAuth2User.getAttribute("sub"));
        String email = oAuth2User.getAttribute("email");
        int indexOfAt;
        String login = "user";
        if (email != null) {
            indexOfAt = email.indexOf("@");
            login = email.substring(0, indexOfAt);
        }
        user.setUsername(login);
        user.setPassword(passwordEncoder.encode("123456"));
        return user;
    }

    private String getLoginFromOAuthUser(OAuth2User oAuth2User){
        String username = oAuth2User.getAttribute("login");
        if (username != null)
            return username;
        else{
            String email = oAuth2User.getAttribute("email");
            if (email != null) {
               int indexOfAt = email.indexOf("@");
               username = email.substring(0, indexOfAt);
            }
            return username;
        }
    }
}
