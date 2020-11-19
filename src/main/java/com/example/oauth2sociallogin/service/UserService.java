package com.example.oauth2sociallogin.service;

import com.example.oauth2sociallogin.domain.User;
import com.example.oauth2sociallogin.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

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

    public User findUserById(Long id){

       Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
}
